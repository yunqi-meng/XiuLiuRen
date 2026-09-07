package com.xiaoliuren.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaoliuren.app.data.db.HistoryRecord
import com.xiaoliuren.app.ui.components.DecorativeDivider
import com.xiaoliuren.app.ui.components.LevelTag
import com.xiaoliuren.app.ui.components.SealStamp
import com.xiaoliuren.app.ui.components.SectionTitle
import com.xiaoliuren.app.ui.theme.*
import com.xiaoliuren.app.viewmodel.HistoryViewModel
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
    ) {
        TopAppBar(
            title = {
                Text(
                    "历史记录",
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                }
            },
            actions = {
                if (records.isNotEmpty()) {
                    IconButton(onClick = { showClearDialog = true }) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "清空", tint = cinnabarColor())
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = inkDarkColor(),
                navigationIconContentColor = inkDarkColor()
            )
        )

        if (records.isEmpty()) {
            // 空状态美化
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SealStamp(text = "空", size = 72, color = AntiqueGold.copy(alpha = 0.6f))
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                records.forEach { record ->
                    HistoryItemCard(
                        record = record,
                        onDelete = { viewModel.deleteById(record.id) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
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
                }) { Text("确认", color = XiongRed, fontWeight = FontWeight.Medium) }
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

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
                spotColor = Color.Black.copy(alpha = 0.08f)
            ),
        shape = RoundedCornerShape(14.dp),
        color = surfaceColor(),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, outlineColor().copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .drawBehind {
                    // 左侧装饰条 — 根据吉凶等级
                    val accentColor = levelColor(record.resultLevel)
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(accentColor.copy(alpha = 0.6f), accentColor.copy(alpha = 0.2f))
                        ),
                        topLeft = androidx.compose.ui.geometry.Offset(0f, 0f),
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
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "删除",
                            tint = onSurfaceVariantColor().copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
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
    val containerColor = if (highlight) cinnabarColor() else surfaceVariantColor()
    val textColor = if (highlight) RiceWhite else onSurfaceColor()
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$label:$name",
            fontSize = 11.sp,
            color = textColor,
            fontWeight = if (highlight) FontWeight.Medium else FontWeight.Normal,
            letterSpacing = 0.3.sp
        )
    }
}
