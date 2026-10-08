package com.xiaoliuren.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
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
    var section = 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
    ) {
        AppTopBar(title = "测算结果", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsInset()
                .padding(horizontal = 18.dp)
        ) {
            // 起课时间信息
            AnimatedEntry(index = section++) {
                TimeSummaryCard(result = result, lunar = lunar)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 三宫总览
            AnimatedEntry(index = section++) {
                Column {
                    SectionTitle("三宫总览")
                    Spacer(modifier = Modifier.height(10.dp))
                    ThreePalaceRow(judgment = judgment)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 古诀原文
            AnimatedEntry(index = section++) {
                Column {
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
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 核心象义
            AnimatedEntry(index = section++) {
                Column {
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
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 分类解读
            AnimatedEntry(index = section++) {
                Column {
                    SectionTitle("分类解读")
                    Spacer(modifier = Modifier.height(10.dp))
                    CategoryReadings(palace = timePalace)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 综合断语
            AnimatedEntry(index = section++) {
                Column {
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
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 应期
            AnimatedEntry(index = section++) {
                Column {
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
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 参考信息
            AnimatedEntry(index = section++) {
                Column {
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
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
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
        KeyValueRow(key = "公历", value = "${result.solarYear}年${result.solarMonth}月${result.solarDay}日 ${"%02d".format(result.solarHour)}:${"%02d".format(result.solarMinute)}")
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
        PalaceCard("月宫·起因", judgment.monthPalace, false, "一", Modifier.weight(1f))
        PalaceCard("日宫·过程", judgment.dayPalace, false, "二", Modifier.weight(1f))
        PalaceCard("时宫·结果", judgment.timePalace, true, "三", Modifier.weight(1f))
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
    val accentColor = if (highlight) levelColor(palace.jiXiongLevel) else AntiqueGold

    Surface(
        modifier = modifier.shadow(
            elevation = if (highlight) 5.dp else 2.dp,
            shape = RoundedCornerShape(14.dp),
            ambientColor = Color.Black.copy(alpha = 0.04f),
            spotColor = if (highlight) accentColor.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.07f)
        ),
        shape = RoundedCornerShape(14.dp),
        // 高亮卡片的浅色底必须随主题切换，否则暗色模式下会是一块刺眼的白斑
        color = if (highlight) levelSurfaceColor(palace.jiXiongLevel) else surfaceColor(),
        border = androidx.compose.foundation.BorderStroke(
            if (highlight) 1.5.dp else 0.5.dp,
            accentColor.copy(alpha = if (highlight) 0.55f else 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(50))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(step, fontSize = 10.sp, color = accentColor, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(title, fontSize = 11.sp, color = onSurfaceVariantColor(), letterSpacing = 0.5.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = palace.palaceName,
                fontSize = if (highlight) 24.sp else 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (highlight) accentColor else inkDarkColor(),
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(palace.wuxingShensha, fontSize = 11.sp, color = onSurfaceVariantColor())
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
                        "最终结果",
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
        val arrowRotation by animateFloatAsState(
            targetValue = if (isExpanded) 180f else 0f,
            animationSpec = tween(Motion.MICRO),
            label = "arrowRotation"
        )
        ClickableCard(
            onClick = { expandedIndex = if (isExpanded) -1 else index },
            modifier = Modifier.padding(bottom = 8.dp),
            pressedScale = 0.98f,
            shape = RoundedCornerShape(12.dp),
            borderColor = if (isExpanded) {
                cinnabarColor().copy(alpha = 0.35f)
            } else {
                outlineColor().copy(alpha = 0.2f)
            },
            elevation = if (isExpanded) 3.dp else 1.dp,
            onClickLabel = if (isExpanded) "收起$title" else "展开$title"
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium,
                        color = inkDarkColor(),
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "收起" else "展开",
                        tint = if (isExpanded) cinnabarColor() else onSurfaceVariantColor(),
                        modifier = Modifier
                            .size(20.dp)
                            .graphicsLayer { rotationZ = arrowRotation }
                    )
                }
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(tween(Motion.MEDIUM)) + fadeIn(tween(Motion.MEDIUM)),
                    exit = shrinkVertically(tween(Motion.SHORT)) + fadeOut(tween(Motion.SHORT))
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
private fun UnsupportedScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
            .navigationBarsInset(),
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
