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
import androidx.compose.material.icons.filled.ChevronRight
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

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaoliuren.app.data.LiuRenPalace
import com.xiaoliuren.app.ui.components.DecorativeDivider
import com.xiaoliuren.app.ui.components.InfoCard
import com.xiaoliuren.app.ui.components.LevelTag
import com.xiaoliuren.app.ui.components.SealStamp
import com.xiaoliuren.app.ui.components.SectionTitle
import com.xiaoliuren.app.ui.theme.*

@Composable
fun KnowledgeScreen(
    onBack: () -> Unit,
    onPalaceClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
    ) {
        TopAppBar(
            title = {
                Text(
                    "六宫知识库",
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
            // ===== 掌诀口诀 =====
            Spacer(modifier = Modifier.height(12.dp))
            SectionTitle("掌诀记忆口诀")
            Spacer(modifier = Modifier.height(10.dp))

            // 口诀卡片 — 古风装饰
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = Color.Black.copy(alpha = 0.05f),
                        spotColor = Color.Black.copy(alpha = 0.1f)
                    ),
                shape = RoundedCornerShape(14.dp),
                color = surfaceColor(),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, outlineColor().copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .drawBehind {
                            // 顶部装饰条
                            drawRect(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        AntiqueGold.copy(alpha = 0.5f),
                                        Cinnabar.copy(alpha = 0.4f),
                                        AntiqueGold.copy(alpha = 0.5f),
                                        Color.Transparent
                                    )
                                ),
                                topLeft = androidx.compose.ui.geometry.Offset(0f, 0f),
                                size = androidx.compose.ui.geometry.Size(size.width, 2f)
                            )
                        }
                        .padding(18.dp)
                ) {
                    // 掌位口诀
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Cinnabar)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "掌位口诀",
                            color = cinnabarColor(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "大安根，留连尖；速喜中指尖，赤口无名尖；小吉无名根，空亡中指根",
                        style = MaterialTheme.typography.bodyMedium,
                        color = inkDarkColor(),
                        lineHeight = 22.sp,
                        letterSpacing = 0.3.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    DecorativeDivider()
                    Spacer(modifier = Modifier.height(14.dp))
                    // 顺序口诀
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Cinnabar)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "顺序口诀",
                            color = cinnabarColor(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "大安留连速喜，赤口小吉空亡",
                        style = MaterialTheme.typography.bodyMedium,
                        color = inkDarkColor(),
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            DecorativeDivider(modifier = Modifier.padding(horizontal = 20.dp))
            Spacer(modifier = Modifier.height(20.dp))

            // ===== 六宫列表 =====
            SectionTitle("六宫详解")
            Spacer(modifier = Modifier.height(10.dp))

            LiuRenPalace.entries.forEach { palace ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .shadow(
                            elevation = 3.dp,
                            shape = RoundedCornerShape(14.dp),
                            ambientColor = Color.Black.copy(alpha = 0.04f),
                            spotColor = Color.Black.copy(alpha = 0.08f)
                        )
                        .clickable { onPalaceClick(palace.index) },
                    shape = RoundedCornerShape(14.dp),
                    color = surfaceColor(),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, outlineColor().copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                // 左侧装饰条
                                val accentColor = levelColor(palace.jiXiongLevel)
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(accentColor.copy(alpha = 0.5f), accentColor.copy(alpha = 0.15f))
                                    ),
                                    topLeft = androidx.compose.ui.geometry.Offset(0f, 0f),
                                    size = androidx.compose.ui.geometry.Size(3.5f, size.height)
                                )
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 宫位圆形标记
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            levelColorLight(palace.jiXiongLevel),
                                            levelColorLight(palace.jiXiongLevel).copy(alpha = 0.5f)
                                        )
                                    )
                                )
                                .border(
                                    width = 1.dp,
                                    color = levelColor(palace.jiXiongLevel).copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                palace.palaceName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = levelColor(palace.jiXiongLevel),
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "${palace.palaceName}·${palace.shensha}${palace.wuxing.takeLast(1)}",
                                style = MaterialTheme.typography.titleMedium,
                                color = inkDarkColor(),
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                palace.coreTone,
                                style = MaterialTheme.typography.bodySmall,
                                color = onSurfaceVariantColor(),
                                maxLines = 1
                            )
                        }
                        LevelTag(level = palace.jiXiongLevel)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = onSurfaceVariantColor().copy(alpha = 0.4f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
