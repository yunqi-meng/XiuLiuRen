package com.xiaoliuren.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.xiaoliuren.app.ui.theme.*

/** 免责提示条（常驻顶部）— 带渐变背景和装饰线 */
@Composable
fun DisclaimerBar(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = RiceWhite
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF6B1E1E),
                                Color(0xFF8B2A2A)
                            )
                        )
                    )
                }
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 7.dp),
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp,
                color = RiceWhite,
                letterSpacing = 0.5.sp
            )
        }
    }
}

/** 信息卡片 — 带渐变背景、装饰顶部线和细腻阴影 */
@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val currentSurface = surfaceColor()
    val currentOutline = outlineColor()
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
                spotColor = Color.Black.copy(alpha = 0.08f)
            ),
        shape = RoundedCornerShape(14.dp),
        color = currentSurface,
        border = BorderStroke(0.5.dp, currentOutline.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .drawBehind {
                    // 顶部装饰线 — 朱砂金渐变
                    val brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Cinnabar.copy(alpha = 0.5f),
                            AntiqueGold.copy(alpha = 0.7f),
                            Cinnabar.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    )
                    drawRect(
                        brush = brush,
                        topLeft = androidx.compose.ui.geometry.Offset(0f, 0f),
                        size = androidx.compose.ui.geometry.Size(size.width, 1.5f)
                    )
                }
                .padding(18.dp),
            content = content
        )
    }
}

/** 区块标题 — 带古风装饰条和括号 */
@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 装饰条 — 渐变竖线
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Cinnabar, AntiqueGold)
                    )
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = inkDarkColor(),
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        // 右侧装饰小点
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .padding(start = 3.dp)
                    .size(3.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (index == 1) AntiqueGold else BronzeLine.copy(alpha = 0.5f)
                    )
            )
        }
    }
}

/** 标签（吉凶等级）— 带渐变和细腻描边 */
@Composable
fun LevelTag(level: Int, modifier: Modifier = Modifier) {
    val text = when (level) {
        0 -> "大吉"; 1 -> "小吉"; 2 -> "半吉半凶"; 3 -> "凶"; 4 -> "大凶"; else -> "未知"
    }
    val baseColor = levelColor(level)
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(baseColor, baseColor.copy(alpha = 0.85f))
                    )
                )
                .border(
                    width = 0.5.dp,
                    color = baseColor.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = text,
                color = RiceWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
        }
    }
}

/** 键值文本行 — 带装饰点 */
@Composable
fun KeyValueRow(key: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            // 装饰小点
            Box(
                modifier = Modifier
                    .size(3.dp)
                    .clip(RoundedCornerShape(50))
                    .background(BronzeLine.copy(alpha = 0.6f))
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = key,
                style = MaterialTheme.typography.bodyMedium,
                color = onSurfaceVariantColor()
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = onSurfaceColor(),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}

/** 底部娱乐提示 — 带装饰边框 */
@Composable
fun EntertainmentFooter(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(surfaceVariantColor())
                .border(
                    width = 0.5.dp,
                    color = BronzeLine.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(14.dp)
        ) {
            Text(
                text = "以上结果仅供民俗文化娱乐参考，请勿过度迷信，切勿作为现实决策依据。",
                style = MaterialTheme.typography.bodySmall,
                color = onSurfaceVariantColor(),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                letterSpacing = 0.3.sp
            )
        }
    }
}

/** 渐变按钮 — 带按压动画和阴影 */
@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    height: Int = 56,
    fontSize: Int = 18
) {
    val interactionSource = remember { MutableInteractionSource() }
    val buttonAlpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.5f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "buttonAlpha"
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .shadow(
                elevation = if (enabled) 8.dp else 0.dp,
                shape = RoundedCornerShape(height.dp),
                ambientColor = Cinnabar.copy(alpha = 0.3f),
                spotColor = Cinnabar.copy(alpha = 0.5f)
            ),
        shape = RoundedCornerShape(height.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = RiceWhite,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = RiceWhite.copy(alpha = 0.5f)
        ),
        enabled = enabled && !isLoading,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = if (enabled) {
                            listOf(Cinnabar, CinnabarDark)
                        } else {
                            listOf(Cinnabar.copy(alpha = 0.4f), CinnabarDark.copy(alpha = 0.4f))
                        }
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = RiceWhite,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "正在推算…",
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
            } else {
                Text(
                    text = text,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.Bold,
                    color = RiceWhite,
                    letterSpacing = 4.sp
                )
            }
        }
    }
}

/** 装饰性分隔线 — 朱砂金渐变 */
@Composable
fun DecorativeDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .drawBehind {
                val brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        BronzeLine.copy(alpha = 0.3f),
                        AntiqueGold.copy(alpha = 0.5f),
                        Cinnabar.copy(alpha = 0.4f),
                        AntiqueGold.copy(alpha = 0.5f),
                        BronzeLine.copy(alpha = 0.3f),
                        Color.Transparent
                    )
                )
                drawRect(brush = brush)
            }
    )
}

/** 印章风格圆形标记 */
@Composable
fun SealStamp(
    text: String,
    modifier: Modifier = Modifier,
    size: Int = 48,
    color: Color = Cinnabar
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape(50))
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = 0.12f),
                        color.copy(alpha = 0.04f)
                    )
                )
            )
            .border(
                width = 1.5.dp,
                color = color.copy(alpha = 0.5f),
                shape = RoundedCornerShape(50)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = (size * 0.35).sp,
            color = color,
            letterSpacing = 0.sp
        )
    }
}

/** 古风边框装饰卡片 — 带四角装饰 */
@Composable
fun OrnateCard(
    modifier: Modifier = Modifier,
    accentColor: Color = Cinnabar,
    content: @Composable ColumnScope.() -> Unit
) {
    val currentSurface = surfaceColor()
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(14.dp),
        color = currentSurface,
        border = BorderStroke(0.5.dp, outlineColor().copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier
                .drawBehind {
                    val w = size.width
                    val h = size.height
                    val cornerLen = 24f
                    val accent = accentColor.copy(alpha = 0.6f)
                    // 四角装饰线
                    // 左上角
                    drawLine(accent, start = androidx.compose.ui.geometry.Offset(0f, 0f), end = androidx.compose.ui.geometry.Offset(cornerLen, 0f), strokeWidth = 2f)
                    drawLine(accent, start = androidx.compose.ui.geometry.Offset(0f, 0f), end = androidx.compose.ui.geometry.Offset(0f, cornerLen), strokeWidth = 2f)
                    // 右上角
                    drawLine(accent, start = androidx.compose.ui.geometry.Offset(w - cornerLen, 0f), end = androidx.compose.ui.geometry.Offset(w, 0f), strokeWidth = 2f)
                    drawLine(accent, start = androidx.compose.ui.geometry.Offset(w, 0f), end = androidx.compose.ui.geometry.Offset(w, cornerLen), strokeWidth = 2f)
                    // 左下角
                    drawLine(accent, start = androidx.compose.ui.geometry.Offset(0f, h - cornerLen), end = androidx.compose.ui.geometry.Offset(0f, h), strokeWidth = 2f)
                    drawLine(accent, start = androidx.compose.ui.geometry.Offset(0f, h), end = androidx.compose.ui.geometry.Offset(cornerLen, h), strokeWidth = 2f)
                    // 右下角
                    drawLine(accent, start = androidx.compose.ui.geometry.Offset(w - cornerLen, h), end = androidx.compose.ui.geometry.Offset(w, h), strokeWidth = 2f)
                    drawLine(accent, start = androidx.compose.ui.geometry.Offset(w, h - cornerLen), end = androidx.compose.ui.geometry.Offset(w, h), strokeWidth = 2f)
                }
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                content = content
            )
        }
    }
}
