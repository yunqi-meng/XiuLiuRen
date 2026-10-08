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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaoliuren.app.ui.components.*
import com.xiaoliuren.app.ui.theme.*

@Composable
fun AboutScreen(onBack: () -> Unit) {
    var section = 0
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
    ) {
        AppTopBar(title = "关于本工具", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsInset()
                .padding(horizontal = 18.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // ===== 应用信息头部 =====
            AnimatedEntry(index = section++) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(24.dp),
                                ambientColor = Cinnabar.copy(alpha = 0.15f),
                                spotColor = Cinnabar.copy(alpha = 0.25f)
                            )
                            .background(Brush.radialGradient(listOf(InkDark, InkBlack)))
                            .border(
                                width = 1.dp,
                                color = AntiqueGold.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(24.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "六壬",
                                color = Cinnabar,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                letterSpacing = 2.sp
                            )
                            Text("時課", color = AntiqueGold, fontSize = 10.sp, letterSpacing = 3.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "小六壬时课",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = inkDarkColor(),
                        letterSpacing = 4.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .width(24.dp)
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
                            "版本 1.0.0",
                            color = onSurfaceVariantColor(),
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .width(24.dp)
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
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("民俗文化娱乐参考", color = cinnabarColor(), fontSize = 13.sp, letterSpacing = 1.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ===== 免责声明 =====
            AnimatedEntry(index = section++) {
                Column {
                    SectionTitle("免责声明")
                    Spacer(modifier = Modifier.height(10.dp))
                    InfoCard {
                        Text(
                            text = "本工具仅供中国传统民俗文化研究与娱乐参考，不构成任何人生决策、医疗、法律、财务建议，请勿过度迷信。\n\n小六壬属于民俗占卜文化范畴，结果仅供参考，切勿作为现实决策依据。不得用于重大决策、生死疾病等场景。",
                            style = MaterialTheme.typography.bodyMedium,
                            color = inkDarkColor(),
                            lineHeight = 24.sp,
                            letterSpacing = 0.3.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== 算法说明 =====
            AnimatedEntry(index = section++) {
                Column {
                    SectionTitle("算法说明")
                    Spacer(modifier = Modifier.height(10.dp))
                    InfoCard {
                        Text(
                            text = "本应用采用民间流传最广的基础版小六壬（六壬时课/马前课）算法，严格遵循「大安起正月、月上起日、日上起时」正统三步法。",
                            style = MaterialTheme.typography.bodyMedium,
                            color = inkDarkColor(),
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        DecorativeDivider()
                        Spacer(modifier = Modifier.height(10.dp))
                        listOf(
                            "月份以二十四节气划分（非农历初一）",
                            "日子以 23:00 子时为界，过子时日期顺延",
                            "农历算法有效范围：1900 年 - 2100 年"
                        ).forEach { BulletPoint(it, Cinnabar) }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "仅适合测算近期日常小事，流派众多，结果仅供参考。",
                            style = MaterialTheme.typography.bodySmall,
                            color = onSurfaceVariantColor(),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== 隐私说明 =====
            AnimatedEntry(index = section++) {
                Column {
                    SectionTitle("隐私说明")
                    Spacer(modifier = Modifier.height(10.dp))
                    InfoCard {
                        Text(
                            text = "本应用为纯离线工具，不联网、不收集任何用户个人信息，所有测算记录均存储于本地设备，不会上传至任何服务器。\n\n卸载应用后，所有本地数据将一并清除。",
                            style = MaterialTheme.typography.bodyMedium,
                            color = inkDarkColor(),
                            lineHeight = 24.sp,
                            letterSpacing = 0.3.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== 使用边界 =====
            AnimatedEntry(index = section++) {
                Column {
                    SectionTitle("使用边界")
                    Spacer(modifier = Modifier.height(10.dp))
                    InfoCard {
                        listOf(
                            "仅适用于近期（一般 3 个月内）的具体小事",
                            "不占违法悖德之事，不占他人隐私",
                            "重病、生死、重大人生决策请遵从现实规律与专业建议",
                            "同一事短期内不宜反复占问",
                            "娱乐为主，切勿沉迷"
                        ).forEach { BulletPoint(it, AntiqueGold, square = true) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ===== 底部签名 =====
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SealStamp(text = "印", size = 40, color = Cinnabar.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Powered by 民俗文化研究\nMade with Kotlin + Jetpack Compose",
                    fontSize = 11.sp,
                    color = onSurfaceVariantColor(),
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun BulletPoint(text: String, color: Color, square: Boolean = false) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(if (square) 5.dp else 4.dp)
                .clip(if (square) RoundedCornerShape(1.dp) else RoundedCornerShape(50))
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = onSurfaceColor(),
            lineHeight = 20.sp
        )
    }
}
