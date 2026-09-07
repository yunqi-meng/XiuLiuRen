package com.xiaoliuren.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.xiaoliuren.app.data.CurrentTimeInfo
import com.xiaoliuren.app.ui.components.DisclaimerBar
import com.xiaoliuren.app.ui.components.DecorativeDivider
import com.xiaoliuren.app.ui.components.GradientButton
import com.xiaoliuren.app.ui.components.InfoCard
import com.xiaoliuren.app.ui.components.KeyValueRow
import com.xiaoliuren.app.ui.components.SealStamp
import com.xiaoliuren.app.ui.theme.*
import com.xiaoliuren.app.viewmodel.DivineViewModel

@Composable
fun HomeScreen(
    viewModel: DivineViewModel,
    isCalculating: Boolean,
    onDivineNow: () -> Unit,
    onCustomTime: () -> Unit,
    onKnowledge: () -> Unit,
    onHistory: () -> Unit,
    onAbout: () -> Unit
) {
    val timeInfo by viewModel.currentTimeInfo.collectAsState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        while (true) {
            viewModel.refreshCurrentTime()
            delay(1000L)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
            .verticalScroll(rememberScrollState())
    ) {
        DisclaimerBar(text = "本工具仅供民俗文化娱乐，不构成任何决策建议")

        // ===== 装饰性标题区 =====
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // 印章标记
                SealStamp(text = "壬", size = 56, color = Cinnabar)
                Spacer(modifier = Modifier.height(16.dp))
                // 主标题
                Text(
                    text = "小六壬时课",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = inkDarkColor(),
                    letterSpacing = 6.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                // 装饰线
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // 左装饰线
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(1.dp)
                            .drawBehind {
                                drawRect(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color.Transparent, AntiqueGold)
                                    )
                                )
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "民俗文化娱乐参考",
                        fontSize = 12.sp,
                        color = cinnabarColor(),
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // 右装饰线
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(1.dp)
                            .drawBehind {
                                drawRect(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(AntiqueGold, Color.Transparent)
                                    )
                                )
                            }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 }),
            exit = fadeOut() + slideOutVertically()
        ) {
            // ===== 当前时间卡片 =====
            if (timeInfo != null) {
                TimeInfoCard(timeInfo = timeInfo!!)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ===== 一键起课按钮 =====
        GradientButton(
            text = "一  键  起  课",
            onClick = onDivineNow,
            modifier = Modifier.padding(horizontal = 24.dp),
            enabled = !isCalculating && (timeInfo?.supported != false),
            isLoading = isCalculating,
            height = 56,
            fontSize = 18
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ===== 自定义时间按钮 =====
        OutlinedButton(
            onClick = onCustomTime,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = inkDarkColor()
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, outlineColor())
        ) {
            Icon(Icons.Default.EditCalendar, contentDescription = null, modifier = Modifier.size(20.dp), tint = cinnabarColor())
            Spacer(modifier = Modifier.width(10.dp))
            Text("自定义时间起课", fontSize = 15.sp, letterSpacing = 1.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ===== 装饰分隔线 =====
        DecorativeDivider(modifier = Modifier.padding(horizontal = 40.dp))

        Spacer(modifier = Modifier.height(24.dp))

        // ===== 底部功能入口 =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FeatureEntry(Icons.Default.MenuBook, "六宫知识库", onKnowledge)
            FeatureEntry(Icons.Default.History, "历史记录", onHistory)
            FeatureEntry(Icons.Default.Info, "关于", onAbout)
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ===== 底部免责 =====
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(surfaceVariantColor())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "本工具仅供中国传统民俗文化研究与娱乐参考，\n不构成任何人生决策、医疗、法律、财务建议，请勿过度迷信。",
                fontSize = 11.sp,
                color = onSurfaceVariantColor(),
                textAlign = TextAlign.Center,
                lineHeight = 17.sp,
                letterSpacing = 0.3.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun TimeInfoCard(timeInfo: CurrentTimeInfo) {
    InfoCard(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "当前时间",
                style = MaterialTheme.typography.titleMedium,
                color = inkDarkColor(),
                fontWeight = FontWeight.SemiBold
            )
            // 实时指示灯
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(JiGreen.copy(alpha = 0.8f))
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        KeyValueRow(key = "公历", value = "${timeInfo.solarYear}-${String.format("%02d", timeInfo.solarMonth)}-${String.format("%02d", timeInfo.solarDay)} ${String.format("%02d", timeInfo.solarHour)}:${String.format("%02d", timeInfo.solarMinute)}")
        Spacer(modifier = Modifier.height(10.dp))
        KeyValueRow(key = "农历", value = timeInfo.lunarString)
        Spacer(modifier = Modifier.height(10.dp))
        KeyValueRow(key = "节气月", value = timeInfo.jieqiMonthName)
        Spacer(modifier = Modifier.height(10.dp))
        KeyValueRow(key = "时辰", value = "${timeInfo.shichenName} ${timeInfo.shichenTimeRange}")
    }
}

@Composable
private fun FeatureEntry(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(10.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .size(54.dp)
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(18.dp),
                    ambientColor = Color.Black.copy(alpha = 0.04f),
                    spotColor = Color.Black.copy(alpha = 0.08f)
                ),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                surfaceVariantColor(),
                                surfaceColor()
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = label,
                    tint = cinnabarColor(),
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = onSurfaceVariantColor(),
            letterSpacing = 0.5.sp
        )
    }
}
