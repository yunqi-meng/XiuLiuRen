package com.xiaoliuren.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import com.xiaoliuren.app.core.DivineEngine
import com.xiaoliuren.app.core.DivineResult
import com.xiaoliuren.app.core.LunarCalendar
import com.xiaoliuren.app.core.Shichen
import com.xiaoliuren.app.data.FullJudgment
import com.xiaoliuren.app.data.LiuRenPalace
import com.xiaoliuren.app.ui.components.*
import com.xiaoliuren.app.ui.theme.*

@Composable
fun ResultScreen(
    result: DivineResult,
    judgment: FullJudgment,
    onBack: () -> Unit
) {
    if (!result.supported) {
        UnsupportedScreen(onBack = onBack)
        return
    }

    val timePalace = judgment.timePalace
    val lunar = result.lunarDate!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
    ) {
        TopAppBar(
            title = {
                Text(
                    "测算结果",
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = inkDarkColor(),
                navigationIconContentColor = inkDarkColor()
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
        ) {
            // 起课时间信息
            TimeSummaryCard(result = result, lunar = lunar)

            Spacer(modifier = Modifier.height(16.dp))

            // 三宫总览
            SectionTitle("三宫总览")
            Spacer(modifier = Modifier.height(10.dp))
            ThreePalaceRow(judgment = judgment)

            Spacer(modifier = Modifier.height(20.dp))
            DecorativeDivider()
            Spacer(modifier = Modifier.height(20.dp))

            // 古诀原文
            SectionTitle("古诀原文")
            Spacer(modifier = Modifier.height(10.dp))
            OrnateCard(accentColor = AntiqueGold) {
                Text(
                    text = timePalace.ancientFormula,
                    style = MaterialTheme.typography.bodyMedium,
                    color = inkDarkColor(),
                    lineHeight = 24.sp,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 核心象义
            SectionTitle("核心象义")
            Spacer(modifier = Modifier.height(10.dp))
            InfoCard {
                Text(
                    text = timePalace.coreMeaning,
                    style = MaterialTheme.typography.bodyLarge,
                    color = inkDarkColor(),
                    lineHeight = 24.sp
                )
                if (timePalace.supplement.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    DecorativeDivider()
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "补充：${timePalace.supplement}",
                        style = MaterialTheme.typography.bodySmall,
                        color = onSurfaceVariantColor()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 分类解读
            SectionTitle("分类解读")
            Spacer(modifier = Modifier.height(10.dp))
            CategoryReadings(palace = timePalace)

            Spacer(modifier = Modifier.height(20.dp))
            DecorativeDivider()
            Spacer(modifier = Modifier.height(20.dp))

            // 综合断语
            SectionTitle("综合断语")
            Spacer(modifier = Modifier.height(10.dp))
            OrnateCard(accentColor = levelColor(timePalace.jiXiongLevel)) {
                Text(
                    text = judgment.comprehensive,
                    style = MaterialTheme.typography.bodyLarge,
                    color = inkDarkColor(),
                    lineHeight = 25.sp,
                    letterSpacing = 0.3.sp
                )
            }

            // 五行生克分析
            if (judgment.wuxingRelation.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                InfoCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(AntiqueGold)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "五行生克：${judgment.wuxingRelation}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = onSurfaceVariantColor(),
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 应期
            SectionTitle("应期参考")
            Spacer(modifier = Modifier.height(10.dp))
            InfoCard {
                Text(
                    text = judgment.timing,
                    style = MaterialTheme.typography.bodyMedium,
                    color = inkDarkColor(),
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 参考信息
            SectionTitle("参考信息")
            Spacer(modifier = Modifier.height(10.dp))
            InfoCard {
                YiJiRow(suitable = timePalace.suitable, avoid = timePalace.avoid)
                Spacer(modifier = Modifier.height(12.dp))
                DecorativeDivider()
                Spacer(modifier = Modifier.height(12.dp))
                KeyValueRow(key = "对应数字", value = timePalace.numbers.joinToString("、"))
                Spacer(modifier = Modifier.height(10.dp))
                KeyValueRow(key = "参考方位", value = timePalace.direction)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 底部娱乐提示
            EntertainmentFooter()

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun TimeSummaryCard(result: DivineResult, lunar: com.xiaoliuren.app.core.LunarDate) {
    InfoCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "起课时间",
                style = MaterialTheme.typography.titleMedium,
                color = inkDarkColor(),
                fontWeight = FontWeight.SemiBold
            )
            SealStamp(text = "課", size = 36, color = AntiqueGold)
        }
        Spacer(modifier = Modifier.height(14.dp))
        KeyValueRow(key = "公历", value = "${result.solarYear}年${result.solarMonth}月${result.solarDay}日 ${String.format("%02d", result.solarHour)}:${String.format("%02d", result.solarMinute)}")
        Spacer(modifier = Modifier.height(10.dp))
        KeyValueRow(key = "农历", value = LunarCalendar.formatLunar(lunar))
        Spacer(modifier = Modifier.height(10.dp))
        KeyValueRow(key = "节气月", value = DivineEngine.jieqiMonthFullName(result.jieqiMonth))
        Spacer(modifier = Modifier.height(10.dp))
        KeyValueRow(key = "时辰", value = Shichen.nameWithZhi(result.shichenIndex))
        if (result.crossNextDay) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Cinnabar.copy(alpha = 0.1f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("※ 已过子时，日期顺延", fontSize = 12.sp, color = cinnabarColor())
            }
        }
    }
}

@Composable
private fun ThreePalaceRow(judgment: FullJudgment) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PalaceCard(
            title = "月宫·起因",
            palace = judgment.monthPalace,
            highlight = false,
            step = "一",
            modifier = Modifier.weight(1f)
        )
        PalaceCard(
            title = "日宫·过程",
            palace = judgment.dayPalace,
            highlight = false,
            step = "二",
            modifier = Modifier.weight(1f)
        )
        PalaceCard(
            title = "时宫·结果",
            palace = judgment.timePalace,
            highlight = true,
            step = "三",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PalaceCard(
    title: String,
    palace: LiuRenPalace,
    highlight: Boolean,
    step: String,
    modifier: Modifier = Modifier
) {
    val borderColor = if (highlight) levelColor(palace.jiXiongLevel) else outlineColor()
    val borderWidth = if (highlight) 2.dp else 1.dp
    val accentColor = if (highlight) levelColor(palace.jiXiongLevel) else AntiqueGold

    Surface(
        modifier = modifier
            .shadow(
                elevation = if (highlight) 6.dp else 3.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
                spotColor = if (highlight) levelColor(palace.jiXiongLevel).copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.08f)
            ),
        shape = RoundedCornerShape(14.dp),
        color = if (highlight) levelColorLight(palace.jiXiongLevel) else surfaceColor(),
        border = androidx.compose.foundation.BorderStroke(borderWidth, borderColor.copy(alpha = if (highlight) 0.6f else 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    if (highlight) {
                        // 顶部装饰条
                        drawRect(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    accentColor.copy(alpha = 0.7f),
                                    Color.Transparent
                                )
                            ),
                            topLeft = androidx.compose.ui.geometry.Offset(0f, 0f),
                            size = androidx.compose.ui.geometry.Size(size.width, 3f)
                        )
                    }
                }
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 步骤序号
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(50))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = step,
                    fontSize = 10.sp,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                color = onSurfaceVariantColor(),
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = palace.palaceName,
                fontSize = if (highlight) 24.sp else 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (highlight) levelColor(palace.jiXiongLevel) else inkDarkColor(),
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = palace.wuxingShensha,
                fontSize = 11.sp,
                color = onSurfaceVariantColor()
            )
            Spacer(modifier = Modifier.height(8.dp))
            LevelTag(level = palace.jiXiongLevel)
            if (highlight) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(cinnabarColor().copy(alpha = 0.12f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "最终结果",
                        fontSize = 10.sp,
                        color = cinnabarColor(),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryReadings(palace: LiuRenPalace) {
    val categories = listOf(
        "办事谋望" to palace.workMeaning,
        "求财交易" to palace.moneyMeaning,
        "失物找寻" to palace.lostMeaning,
        "行人音讯" to palace.travelerMeaning,
        "感情婚恋" to palace.loveMeaning,
        "健康疾病" to palace.healthMeaning,
        "官非口舌" to palace.officialMeaning
    )
    var expandedIndex by remember { mutableStateOf(-1) }

    categories.forEachIndexed { index, (title, content) ->
        val isExpanded = expandedIndex == index
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .shadow(
                    elevation = if (isExpanded) 4.dp else 2.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = Color.Black.copy(alpha = 0.03f),
                    spotColor = Color.Black.copy(alpha = 0.06f)
                )
                .clickable { expandedIndex = if (isExpanded) -1 else index },
            shape = RoundedCornerShape(12.dp),
            color = surfaceColor(),
            border = androidx.compose.foundation.BorderStroke(
                0.5.dp,
                if (isExpanded) cinnabarColor().copy(alpha = 0.3f) else outlineColor().copy(alpha = 0.2f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // 装饰小方块
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(if (isExpanded) cinnabarColor() else AntiqueGold.copy(alpha = 0.6f))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            title,
                            style = MaterialTheme.typography.titleMedium,
                            color = inkDarkColor(),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Icon(
                        if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = onSurfaceVariantColor(),
                        modifier = Modifier.size(20.dp)
                    )
                }
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        DecorativeDivider()
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = content,
                            style = MaterialTheme.typography.bodyMedium,
                            color = onSurfaceColor(),
                            lineHeight = 22.sp,
                            letterSpacing = 0.3.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun YiJiRow(suitable: String, avoid: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // 宜
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(50))
                        .background(JiGreen.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("宜", color = JiGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text("宜", color = JiGreen, fontWeight = FontWeight.Medium, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                suitable,
                style = MaterialTheme.typography.bodyMedium,
                color = onSurfaceColor(),
                lineHeight = 20.sp
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        // 忌
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(50))
                        .background(XiongRed.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("忌", color = XiongRed, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text("忌", color = XiongRed, fontWeight = FontWeight.Medium, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                avoid,
                style = MaterialTheme.typography.bodyMedium,
                color = onSurfaceColor(),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun UnsupportedScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SealStamp(text = "限", size = 64, color = XiongRed)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "暂不支持该时间范围",
            style = MaterialTheme.typography.headlineMedium,
            color = inkDarkColor(),
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("仅支持 1900 年 - 2100 年", color = onSurfaceVariantColor())
        Spacer(modifier = Modifier.height(28.dp))
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = cinnabarColor()),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.height(48.dp)
        ) {
            Text("返回", letterSpacing = 2.sp)
        }
    }
}
