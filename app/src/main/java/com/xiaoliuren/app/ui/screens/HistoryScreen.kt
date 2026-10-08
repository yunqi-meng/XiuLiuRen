package com.xiaoliuren.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaoliuren.app.data.db.HistoryRecord
import com.xiaoliuren.app.ui.components.*
import com.xiaoliuren.app.ui.theme.*
import com.xiaoliuren.app.viewmodel.HistoryViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBack: () -> Unit
) {
    val records by viewModel.allRecords.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(
                title = "历史记录",
                onBack = onBack,
                actions = {
                    if (records.isNotEmpty()) {
                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(
                                Icons.Default.DeleteSweep,
                                contentDescription = "清空全部记录",
                                tint = cinnabarColor()
                            )
                        }
                    }
                }
            )

            if (records.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsInset(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    SealStamp(text = "空", size = 72, color = antiqueGoldColor().copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        "暂无测算记录",
                        style = MaterialTheme.typography.titleMedium,
                        color = onSurfaceVariantColor(),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "起课后将自动保存记录",
                        fontSize = 13.sp,
                        color = onSurfaceVariantColor(),
                        letterSpacing = 0.5.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsInset(),
                    contentPadding = PaddingValues(
                        start = 18.dp, end = 18.dp, top = 12.dp, bottom = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(
                        items = records,
                        key = { _, record -> record.id }
                    ) { index, record ->
                        AnimatedEntry(index = index.coerceAtMost(8)) {
                            HistoryItemCard(
                                record = record,
                                onDelete = {
                                    // 先删再给后悔药：Snackbar 会被后续操作顶掉，所以配合 duration=Short 使用
                                    viewModel.deleteById(record.id)
                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "已删除 1 条记录",
                                            actionLabel = "撤销",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            viewModel.restore(record)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsInset()
        )
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("确认清空", fontWeight = FontWeight.SemiBold) },
            text = { Text("确认清空所有测算记录？此操作不可撤销。") },
            shape = RoundedCornerShape(16.dp),
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearAll()
                    showClearDialog = false
                }) { Text("确认清空", color = XiongRed, fontWeight = FontWeight.Medium) }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("取消") }
            }
        )
    }
}

@Composable
private fun HistoryItemCard(record: HistoryRecord, onDelete: () -> Unit) {
    val timeFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
    val timeStr = remember(record.timestamp) { timeFormat.format(Date(record.timestamp)) }
    val haptic = LocalHapticFeedback.current
    // 装饰条颜色随主题取，drawBehind 里不能调 @Composable
    val accentColor = levelColorThemed(record.resultLevel)
    val accentBrush = remember(accentColor) {
        Brush.verticalGradient(
            listOf(accentColor.copy(alpha = 0.6f), accentColor.copy(alpha = 0.2f))
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = Color.Black.copy(alpha = 0.03f),
                spotColor = Color.Black.copy(alpha = 0.06f)
            ),
        shape = RoundedCornerShape(14.dp),
        color = surfaceColor(),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, outlineColor().copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .drawBehind {
                    drawRect(
                        brush = accentBrush,
                        size = androidx.compose.ui.geometry.Size(4f, size.height)
                    )
                }
                .padding(start = 16.dp, top = 14.dp, end = 14.dp, bottom = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    timeStr,
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurfaceVariantColor(),
                    letterSpacing = 0.3.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LevelTag(level = record.resultLevel)
                    Spacer(modifier = Modifier.width(4.dp))
                    // 图标看着小，但触摸目标必须 ≥48dp（这里用 IconButton 默认尺寸，不额外缩）
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onDelete()
                        }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "删除这条记录",
                            tint = onSurfaceVariantColor().copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                PalaceChip("月宫", record.monthPalace)
                Spacer(modifier = Modifier.width(8.dp))
                PalaceChip("日宫", record.dayPalace)
                Spacer(modifier = Modifier.width(8.dp))
                PalaceChip("时宫", record.timePalace, highlight = true)
            }
            Spacer(modifier = Modifier.height(10.dp))
            DecorativeDivider()
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                record.comprehensive,
                style = MaterialTheme.typography.bodySmall,
                color = onSurfaceColor(),
                lineHeight = 19.sp,
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${record.lunarInfo} · ${record.jieqiMonth} · ${record.shichenName}",
                fontSize = 11.sp,
                color = onSurfaceVariantColor(),
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun PalaceChip(label: String, name: String, highlight: Boolean = false) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (highlight) cinnabarColor() else surfaceVariantColor())
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$label:$name",
            fontSize = 11.sp,
            color = if (highlight) RiceWhite else onSurfaceColor(),
            fontWeight = if (highlight) FontWeight.Medium else FontWeight.Normal,
            letterSpacing = 0.3.sp
        )
    }
}
