package com.xiaoliuren.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaoliuren.app.data.LiuRenPalace
import com.xiaoliuren.app.ui.components.*
import com.xiaoliuren.app.ui.theme.*

@Composable
fun KnowledgeDetailScreen(
    palaceIndex: Int,
    onBack: () -> Unit
) {
    val palace = LiuRenPalace.fromIndex(palaceIndex)
    val accentColor = levelColorThemed(palace.jiXiongLevel)
    val surfaceTint = levelSurfaceColor(palace.jiXiongLevel)
    var section = 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
    ) {
        AppTopBar(
            title = "${palace.palaceName}·${palace.shensha}${palace.wuxing.takeLast(1)}",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsInset()
                .padding(horizontal = 18.dp)
        ) {
            // ===== 宫位标识头部 =====
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedEntry(index = section++) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.radialGradient(
                                    listOf(surfaceTint, surfaceTint.copy(alpha = 0.5f))
                                )
                            )
                            .border(
                                width = 1.5.dp,
                                color = accentColor.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            palace.palaceName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = accentColor,
                            letterSpacing = 2.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LevelTag(level = palace.jiXiongLevel)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== 基本属性 =====
            AnimatedEntry(index = section++) {
                Column {
                    SectionTitle("基本属性")
                    Spacer(modifier = Modifier.height(10.dp))
                    InfoCard {
                        KeyValueRow(key = "宫位", value = palace.palaceName)
                        Spacer(modifier = Modifier.height(10.dp))
                        KeyValueRow(key = "五行", value = palace.wuxing)
                        Spacer(modifier = Modifier.height(10.dp))
                        KeyValueRow(key = "对应神煞", value = palace.shensha)
                        Spacer(modifier = Modifier.height(10.dp))
                        KeyValueRow(key = "吉凶定性", value = palace.levelText)
                        Spacer(modifier = Modifier.height(10.dp))
                        KeyValueRow(key = "核心基调", value = palace.coreTone)
                        Spacer(modifier = Modifier.height(10.dp))
                        KeyValueRow(key = "手掌定位", value = palace.handPosition)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== 古诀 =====
            AnimatedEntry(index = section++) {
                Column {
                    SectionTitle("古诀原文")
                    Spacer(modifier = Modifier.height(10.dp))
                    OrnateCard(accentColor = AntiqueGold) {
                        Text(
                            palace.ancientFormula,
                            style = MaterialTheme.typography.bodyMedium,
                            color = inkDarkColor(),
                            lineHeight = 24.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== 核心象义 =====
            AnimatedEntry(index = section++) {
                Column {
                    SectionTitle("核心象义")
                    Spacer(modifier = Modifier.height(10.dp))
                    InfoCard {
                        Text(
                            palace.coreMeaning,
                            style = MaterialTheme.typography.bodyLarge,
                            color = inkDarkColor(),
                            lineHeight = 24.sp
                        )
                        if (palace.supplement.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            DecorativeDivider()
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "补充：${palace.supplement}",
                                style = MaterialTheme.typography.bodySmall,
                                color = onSurfaceVariantColor()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== 分类解读 =====
            AnimatedEntry(index = section++) {
                Column {
                    SectionTitle("分类解读")
                    Spacer(modifier = Modifier.height(10.dp))
                    CategoryFullReadings(palace = palace)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== 宜忌 =====
            AnimatedEntry(index = section++) {
                Column {
                    SectionTitle("宜忌参考")
                    Spacer(modifier = Modifier.height(10.dp))
                    InfoCard {
                        YiJiRow(suitable = palace.suitable, avoid = palace.avoid)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== 对应信息 =====
            AnimatedEntry(index = section++) {
                Column {
                    SectionTitle("对应信息")
                    Spacer(modifier = Modifier.height(10.dp))
                    InfoCard {
                        KeyValueRow(key = "对应数字", value = palace.numbers.joinToString("、"))
                        Spacer(modifier = Modifier.height(10.dp))
                        KeyValueRow(key = "参考方位", value = palace.direction)
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
private fun CategoryFullReadings(palace: LiuRenPalace) {
    val categories = listOf(
        "办事谋望" to palace.workMeaning,
        "求财交易" to palace.moneyMeaning,
        "失物找寻" to palace.lostMeaning,
        "行人音讯" to palace.travelerMeaning,
        "感情婚恋" to palace.loveMeaning,
        "健康疾病" to palace.healthMeaning,
        "官非口舌" to palace.officialMeaning
    )
    categories.forEach { (title, content) ->
        InfoCard(modifier = Modifier.padding(bottom = 10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(cinnabarColor())
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    color = cinnabarColor(),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                content,
                style = MaterialTheme.typography.bodyMedium,
                color = onSurfaceColor(),
                lineHeight = 22.sp,
                letterSpacing = 0.3.sp
            )
        }
    }
}
