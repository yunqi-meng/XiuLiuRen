package com.xiaoliuren.app.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.LocalIndication
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaoliuren.app.data.CurrentTimeInfo
import com.xiaoliuren.app.ui.components.*
import com.xiaoliuren.app.ui.theme.*
import com.xiaoliuren.app.viewmodel.DivineViewModel
import java.util.Calendar
import java.util.TimeZone

@Composable
fun CustomTimeScreen(
    viewModel: DivineViewModel,
    isCalculating: Boolean,
    onBack: () -> Unit,
    onDivine: (year: Int, month: Int, day: Int, hour: Int, minute: Int) -> Unit
) {
    val context = LocalContext.current
    val cal = remember { Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")) }

    var year by remember { mutableIntStateOf(cal.get(Calendar.YEAR)) }
    var month by remember { mutableIntStateOf(cal.get(Calendar.MONTH) + 1) }
    var day by remember { mutableIntStateOf(cal.get(Calendar.DAY_OF_MONTH)) }
    var hour by remember { mutableIntStateOf(cal.get(Calendar.HOUR_OF_DAY)) }
    var minute by remember { mutableIntStateOf(cal.get(Calendar.MINUTE)) }

    val timeInfo = remember(year, month, day, hour, minute) {
        viewModel.buildTimeInfo(year, month, day, hour, minute)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
    ) {
        AppTopBar(title = "自定义时间起课", onBack = onBack)

        // 免责条挪到顶栏正下方并全出血，和首页保持一致（之前夹在横向内边距里，两侧被切）
        DisclaimerBar(text = "本工具仅供民俗文化娱乐，不构成任何决策建议")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsInset()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // ===== 日期选择（整卡可点） =====
            AnimatedEntry(index = 0) {
                PickerCard(
                    icon = Icons.Default.DateRange,
                    label = "选择日期",
                    value = "$year 年 $month 月 $day 日",
                    onSelect = {
                        DatePickerDialog(
                            context,
                            { _, y, m, d -> year = y; month = m + 1; day = d },
                            year, month - 1, day
                        ).show()
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ===== 时间选择（整卡可点） =====
            AnimatedEntry(index = 1) {
                PickerCard(
                    icon = Icons.Default.Schedule,
                    label = "选择时间",
                    value = "${"%02d".format(hour)}:${"%02d".format(minute)}",
                    onSelect = {
                        TimePickerDialog(
                            context,
                            { _, h, m -> hour = h; minute = m },
                            hour, minute, true
                        ).show()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ===== 时间预览 =====
            AnimatedEntry(index = 2) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "时间预览",
                            style = MaterialTheme.typography.titleMedium,
                            color = inkDarkColor(),
                            fontWeight = FontWeight.SemiBold
                        )
                        SealStamp(text = "预", size = 32, color = AntiqueGold)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    AnimatedContent(
                        targetState = timeInfo,
                        transitionSpec = {
                            fadeIn(tween(220)) togetherWith fadeOut(tween(160))
                        },
                        label = "timePreview"
                    ) { info ->
                        TimePreviewCard(timeInfo = info)
                    }
                }
            }

            if (timeInfo.crossNextDayHint()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(cinnabarColor().copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        "※ 当前时间已过 23:00，按子时换日规则，日期已顺延至次日",
                        fontSize = 12.sp,
                        color = cinnabarColor()
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            AnimatedEntry(index = 3) {
                GradientButton(
                    text = "开始测算",
                    onClick = { onDivine(year, month, day, hour, minute) },
                    height = 52,
                    fontSize = 17,
                    enabled = !isCalculating && timeInfo.supported,
                    isLoading = isCalculating
                )
            }

            if (!timeInfo.supported) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "暂不支持该时间范围（仅支持 1900-2100 年）",
                    fontSize = 12.sp,
                    color = XiongRed,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun PickerCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    onSelect: () -> Unit
) {
    ClickableCard(
        onClick = onSelect,
        pressedScale = 0.98f,
        borderColor = outlineColor().copy(alpha = 0.3f),
        elevation = 2.dp,
        onClickLabel = "$label，当前 $value"
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.radialGradient(
                            listOf(
                                cinnabarColor().copy(alpha = 0.15f),
                                cinnabarColor().copy(alpha = 0.05f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = cinnabarColor(), modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    label,
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurfaceVariantColor(),
                    letterSpacing = 0.5.sp
                )
                Text(
                    value,
                    style = MaterialTheme.typography.titleMedium,
                    color = inkDarkColor(),
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            }
            Text("更改", color = cinnabarColor(), fontWeight = FontWeight.Medium, fontSize = 14.sp)
        }
    }
}

@Composable
private fun TimePreviewCard(timeInfo: CurrentTimeInfo) {
    InfoCard {
        KeyValueRow(key = "公历", value = "${timeInfo.solarYear}-${"%02d".format(timeInfo.solarMonth)}-${"%02d".format(timeInfo.solarDay)} ${"%02d".format(timeInfo.solarHour)}:${"%02d".format(timeInfo.solarMinute)}")
        Spacer(modifier = Modifier.height(10.dp))
        KeyValueRow(key = "农历", value = timeInfo.lunarString)
        Spacer(modifier = Modifier.height(10.dp))
        KeyValueRow(key = "节气月", value = timeInfo.jieqiMonthName)
        Spacer(modifier = Modifier.height(10.dp))
        KeyValueRow(key = "时辰", value = "${timeInfo.shichenName} ${timeInfo.shichenTimeRange}")
    }
}

private fun CurrentTimeInfo.crossNextDayHint(): Boolean {
    return solarHour == 23 || (solarHour == 0 && solarMinute == 0)
}
