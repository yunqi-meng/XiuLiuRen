package com.xiaoliuren.app.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.LocalIndication
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.xiaoliuren.app.data.CurrentTimeInfo
import com.xiaoliuren.app.ui.components.*
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

    LaunchedEffect(Unit) {
        while (true) {
            viewModel.refreshCurrentTime()
            // 界面只显示到「分」，而刷新一次要重算农历 + 二十四节气。
            // 睡到下一个整分钟再刷新，省掉大量无意义的历法计算与重组。
            delay(60_000L - System.currentTimeMillis() % 60_000L + 50L)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
            .verticalScroll(rememberScrollState())
    ) {
        DisclaimerBar(text = "本工具仅供民俗文化娱乐，不构成任何决策建议")

        // ===== 标题区 =====
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SealStamp(text = "壬", size = 56, color = Cinnabar)
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "小六壬时课",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = inkDarkColor(),
                letterSpacing = 6.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(1.dp)
                        .drawBehind {
                            drawRect(
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, AntiqueGold)
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
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(1.dp)
                        .drawBehind {
                            drawRect(
                                Brush.horizontalGradient(
                                    listOf(AntiqueGold, Color.Transparent)
                                )
                            )
                        }
                )
            }
        }

        // ===== 当前时间卡片 =====
        AnimatedEntry(index = 1) {
            if (timeInfo != null) {
                TimeInfoCard(timeInfo = timeInfo!!)
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // ===== 一键起课 =====
        AnimatedEntry(index = 2) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                GradientButton(
                    text = "一键起课",
                    onClick = onDivineNow,
                    enabled = !isCalculating && (timeInfo?.supported != false),
                    isLoading = isCalculating,
                    height = 56,
                    fontSize = 18
                )
                if (timeInfo?.supported == false) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "当前时间超出 1900–2100 年有效范围，无法起课",
                        fontSize = 12.sp,
                        color = levelColorThemed(3),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ===== 自定义时间 =====
        AnimatedEntry(index = 3) {
            OutlinedButton(
                onClick = onCustomTime,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = inkDarkColor()),
                border = androidx.compose.foundation.BorderStroke(1.dp, outlineColor())
            ) {
                Icon(
                    Icons.Default.EditCalendar,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = cinnabarColor()
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("自定义时间起课", fontSize = 15.sp, letterSpacing = 1.sp)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // ===== 底部功能入口 =====
        AnimatedEntry(index = 4) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FeatureEntry(Icons.Default.AutoStories, "六宫知识库", onKnowledge)
                FeatureEntry(Icons.Default.History, "历史记录", onHistory)
                FeatureEntry(Icons.Default.Info, "关于", onAbout)
            }
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

        // 底部避让导航栏 / 手势条（背景渐变依然铺满）
        BottomInsetSpacer()
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
            LiveDot()
        }
        Spacer(modifier = Modifier.height(14.dp))
        KeyValueRow(
            key = "公历",
            value = "${timeInfo.solarYear}-${"%02d".format(timeInfo.solarMonth)}-${"%02d".format(timeInfo.solarDay)} ${"%02d".format(timeInfo.solarHour)}:${"%02d".format(timeInfo.solarMinute)}"
        )
        Spacer(modifier = Modifier.height(10.dp))
        KeyValueRow(key = "农历", value = timeInfo.lunarString)
        Spacer(modifier = Modifier.height(10.dp))
        KeyValueRow(key = "节气月", value = timeInfo.jieqiMonthName)
        Spacer(modifier = Modifier.height(10.dp))
        KeyValueRow(key = "时辰", value = "${timeInfo.shichenName} ${timeInfo.shichenTimeRange}")
    }
}

/** 呼吸实时指示灯 */
@Composable
private fun LiveDot() {
    val transition = rememberInfiniteTransition(label = "liveDot")
    val alpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100),
            repeatMode = RepeatMode.Reverse
        ),
        label = "liveDotAlpha"
    )
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(RoundedCornerShape(50))
            .background(JiGreen.copy(alpha = alpha))
    )
}

@Composable
private fun FeatureEntry(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val haptic = LocalHapticFeedback.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .pressScale(interactionSource, pressedScale = 0.9f)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClickLabel = label,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            )
            // 触摸目标至少 48dp：图标容器 54dp + padding，已满足
            .padding(10.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .size(54.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(18.dp),
                    ambientColor = Color.Black.copy(alpha = 0.03f),
                    spotColor = Color.Black.copy(alpha = 0.06f)
                ),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(surfaceVariantColor(), surfaceColor())
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    // 图标只是装饰，文字标签已经说明了用途；
                    // 两者都设描述会让 TalkBack 连着念两遍
                    contentDescription = null,
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
