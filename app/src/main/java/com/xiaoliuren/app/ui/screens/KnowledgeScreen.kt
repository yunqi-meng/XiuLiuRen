package com.xiaoliuren.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.LocalIndication
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaoliuren.app.data.LiuRenPalace
import com.xiaoliuren.app.ui.components.*
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
        AppTopBar(title = "六宫知识库", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsInset()
                .padding(horizontal = 18.dp)
        ) {
            // ===== 掌诀口诀 =====
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedEntry(index = 0) {
                Column {
                    SectionTitle("掌诀记忆口诀")
                    Spacer(modifier = Modifier.height(10.dp))
                    OrnateCard(accentColor = AntiqueGold) {
                        Text(
                            "掌位口诀",
                            color = cinnabarColor(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        )
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
                        Text(
                            "顺序口诀",
                            color = cinnabarColor(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        )
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
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== 六宫列表 =====
            AnimatedEntry(index = 1) {
                SectionTitle("六宫详解")
            }
            Spacer(modifier = Modifier.height(10.dp))

            LiuRenPalace.entries.forEachIndexed { index, palace ->
                AnimatedEntry(index = index + 2) {
                    PalaceListItem(palace = palace, onClick = { onPalaceClick(palace.index) })
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun PalaceListItem(palace: LiuRenPalace, onClick: () -> Unit) {
    val accentColor = levelColorThemed(palace.jiXiongLevel)
    val surfaceTint = levelSurfaceColor(palace.jiXiongLevel)

    ClickableCard(
        onClick = onClick,
        pressedScale = 0.97f,
        borderColor = outlineColor().copy(alpha = 0.3f),
        elevation = 2.dp,
        onClickLabel = "查看${palace.palaceName}详解"
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRect(
                        brush = Brush.verticalGradient(
                            listOf(accentColor.copy(alpha = 0.5f), accentColor.copy(alpha = 0.15f))
                        ),
                        size = androidx.compose.ui.geometry.Size(4f, size.height)
                    )
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.radialGradient(
                            listOf(surfaceTint, surfaceTint.copy(alpha = 0.5f))
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = accentColor.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    palace.palaceName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = accentColor,
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
