package com.xiaoliuren.app.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaoliuren.app.ui.theme.*
import kotlinx.coroutines.delay

// ==================== 交互工具 ====================

/** 统一的轻触触觉反馈入口 */
@Composable
fun rememberTapHaptic(): HapticFeedback = LocalHapticFeedback.current

/** 装饰色上该配深字还是浅字 —— 按背景亮度自动选，保证 WCAG AA 对比度 */
fun onColorFor(background: Color): Color =
    if (background.luminance() > 0.179f) InkBlack else RiceWhite

/** 按压回弹缩放 — 所有可点卡片/入口的统一触感 */
@Composable
fun Modifier.pressScale(
    interactionSource: MutableInteractionSource,
    pressedScale: Float = 0.96f
): Modifier {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "pressScale"
    )
    return this.graphicsLayer { scaleX = scale; scaleY = scale }
}

/**
 * 交错渐进入场 — 页面内容按序浮现。
 *
 * 只操作渲染层（alpha + 位移），不切换可见性：
 * 用可见性做入场时，元素在动画开始前不占布局高度，页面会先「短一截」再撑开，
 * 首屏能看到明显跳动；只动渲染层还避免了动画期间触发重新布局。
 */
@Composable
fun AnimatedEntry(
    index: Int,
    modifier: Modifier = Modifier,
    delayStep: Int = Motion.STAGGER_STEP,
    content: @Composable () -> Unit
) {
    var started by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        val wait = (index.coerceAtLeast(0) * delayStep).toLong()
        if (wait > 0) delay(wait)
        started = true
    }
    val progress by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMillis = Motion.MEDIUM, easing = LinearOutSlowInEasing),
        label = "entryProgress"
    )
    Box(
        modifier = modifier.graphicsLayer {
            alpha = progress
            translationY = (1f - progress) * 20.dp.toPx()
        }
    ) {
        content()
    }
}

// ==================== 系统栏避让 ====================

/**
 * 让内容避让导航栏 / 手势条。
 * 背景渐变仍可全出血铺到屏幕边缘，只有内容被推上来。
 */
@Composable
fun Modifier.navigationBarsInset(): Modifier =
    windowInsetsPadding(WindowInsets.navigationBars)

/** 底部系统栏高度的占位（用于可滚动内容末尾留白） */
@Composable
fun BottomInsetSpacer() {
    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
}

// ==================== 通用结构 ====================

/**
 * 免责提示条。
 *
 * 只有整页最顶端的调用方（首页）才需要 [applyStatusBarInset]：
 * 状态栏区域留给页面底色，而不是让朱砂条铺到状态栏后面 ——
 * 状态栏图标的明暗只由「当前是否暗色主题」决定（浅色主题 → 深色图标），
 * 而朱砂条在两种主题下都是深红。若让朱砂条盖住状态栏，浅色主题下
 * 深色图标压在深红上几乎看不见。
 *
 * 顶栏下方的调用方要传 false：insets 的消费是沿着组合树向下的，
 * 顶栏和本组件是兄弟节点，顶栏吃掉的状态栏高度这里看不到，会重复加一次。
 */
@Composable
fun DisclaimerBar(
    text: String,
    modifier: Modifier = Modifier,
    applyStatusBarInset: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (applyStatusBarInset) {
                    Modifier.windowInsetsPadding(WindowInsets.statusBars)
                } else {
                    Modifier
                }
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(CinnabarDeep, CinnabarDark)
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

/** 统一页面顶栏（TopAppBar 自带顶部 systemBars inset，无需额外处理） */
@Composable
fun AppTopBar(
    title: String,
    onBack: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(title, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
        },
        navigationIcon = {
            IconButton(
                onClick = onBack,
                modifier = Modifier.semantics { } // 保持 48dp 最小触摸目标
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = inkDarkColor()
                )
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = inkDarkColor(),
            navigationIconContentColor = inkDarkColor(),
            actionIconContentColor = cinnabarColor()
        )
    )
}

/** 信息卡片 — 干净纸面 + 细描边 */
@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = Color.Black.copy(alpha = 0.03f),
                spotColor = Color.Black.copy(alpha = 0.06f)
            ),
        shape = RoundedCornerShape(14.dp),
        color = surfaceColor(),
        border = BorderStroke(0.5.dp, outlineColor().copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(18.dp), content = content)
    }
}

/**
 * 可点击卡片 — 统一「按压缩放 + 阴影 + 描边 + haptic」。
 * 之前每个页面各写一遍，参数很容易漂移，这里收敛成一处。
 */
@Composable
fun ClickableCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    pressedScale: Float = 0.98f,
    shape: Shape = RoundedCornerShape(14.dp),
    color: Color = surfaceColor(),
    borderColor: Color = outlineColor().copy(alpha = 0.3f),
    borderWidth: Dp = 0.5.dp,
    elevation: Dp = 2.dp,
    spotColor: Color = Color.Black.copy(alpha = 0.06f),
    haptic: Boolean = true,
    onClickLabel: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val haptics = LocalHapticFeedback.current
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .pressScale(interactionSource, pressedScale = pressedScale)
            .shadow(
                elevation = if (enabled) elevation else 0.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.03f),
                spotColor = spotColor
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClickLabel = onClickLabel
            ) {
                if (haptic) haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
        shape = shape,
        color = color,
        border = BorderStroke(borderWidth, borderColor)
    ) {
        Column(content = content)
    }
}

/** 区块标题 — 渐变竖条（带 heading 语义，方便读屏按标题跳转） */
@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics { heading() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(18.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Brush.verticalGradient(listOf(cinnabarColor(), antiqueGoldColor())))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = inkDarkColor(),
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )
    }
}

/** 吉凶等级标签（底色与文字色随主题自适应，并保证 AA 对比度） */
@Composable
fun LevelTag(level: Int, modifier: Modifier = Modifier) {
    val text = when (level) {
        0 -> "大吉"; 1 -> "小吉"; 2 -> "半吉半凶"; 3 -> "凶"; 4 -> "大凶"; else -> "未知"
    }
    val baseColor = levelColorThemed(level)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(baseColor, baseColor.copy(alpha = 0.88f))
                )
            )
            .border(0.5.dp, baseColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = onColorFor(baseColor),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp
        )
    }
}

/** 键值文本行 */
@Composable
fun KeyValueRow(key: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = key,
            style = MaterialTheme.typography.bodyMedium,
            color = onSurfaceVariantColor()
        )
        Spacer(modifier = Modifier.width(16.dp))
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

/** 宜 / 忌 双栏 */
@Composable
fun YiJiRow(suitable: String, avoid: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        YiJiColumn(mark = "宜", color = levelColorThemed(0), text = suitable, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(16.dp))
        YiJiColumn(mark = "忌", color = levelColorThemed(3), text = avoid, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun YiJiColumn(mark: String, color: Color, text: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(50))
                .background(color.copy(alpha = 0.12f))
                .border(0.5.dp, color.copy(alpha = 0.35f), RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Text(mark, color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = onSurfaceColor(),
            lineHeight = 20.sp
        )
    }
}

/** 底部娱乐提示 */
@Composable
fun EntertainmentFooter(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(surfaceVariantColor())
            .border(0.5.dp, BronzeLine.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Text(
            text = "以上结果仅供民俗文化娱乐参考，请勿过度迷信，切勿作为现实决策依据。",
            style = MaterialTheme.typography.bodySmall,
            color = onSurfaceVariantColor(),
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            letterSpacing = 0.3.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/** 渐变主按钮 — 按压回弹 + 触觉反馈 + loading */
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
    val haptic = LocalHapticFeedback.current
    val shape = RoundedCornerShape(height.dp)

    Button(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .pressScale(interactionSource, pressedScale = 0.97f)
            .shadow(
                elevation = if (enabled) 6.dp else 0.dp,
                shape = shape,
                ambientColor = cinnabarColor().copy(alpha = 0.28f),
                spotColor = cinnabarColor().copy(alpha = 0.45f)
            ),
        shape = shape,
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
                            listOf(CinnabarDeep, Cinnabar, CinnabarDark)
                        } else {
                            listOf(
                                Cinnabar.copy(alpha = 0.4f),
                                CinnabarDark.copy(alpha = 0.4f)
                            )
                        }
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
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
                    letterSpacing = 3.sp
                )
            }
        }
    }
}

/** 装饰性分隔线 — 铜金朱砂渐变 */
@Composable
fun DecorativeDivider(modifier: Modifier = Modifier) {
    // 主题色必须在 composable 作用域取好：drawBehind 的 lambda 不是 @Composable 上下文。
    // 顺便 remember 住画笔，避免每帧重建渐变。
    val gold = antiqueGoldColor()
    val cinnabar = cinnabarColor()
    val brush = remember(gold, cinnabar) {
        Brush.horizontalGradient(
            colors = listOf(
                Color.Transparent,
                BronzeLine.copy(alpha = 0.3f),
                gold.copy(alpha = 0.5f),
                cinnabar.copy(alpha = 0.4f),
                gold.copy(alpha = 0.5f),
                BronzeLine.copy(alpha = 0.3f),
                Color.Transparent
            )
        )
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .drawBehind { drawRect(brush = brush) }
    )
}

/**
 * 印章风格圆形标记。
 * 「印」是纯装饰，用 clearAndSetSemantics 把它从读屏朗读中摘掉，
 * 否则 TalkBack 会念出一个孤零零的「壬」字而用户不知道那是什么。
 */
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
            .clearAndSetSemantics { }
            .clip(RoundedCornerShape(50))
            .background(
                Brush.radialGradient(
                    listOf(color.copy(alpha = 0.12f), color.copy(alpha = 0.04f))
                )
            )
            .border(1.5.dp, color.copy(alpha = 0.5f), RoundedCornerShape(50)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = (size * 0.35).sp,
            color = color
        )
    }
}

/** 古风四角装饰卡片 — 仅用于古诀/断语等重点内容 */
@Composable
fun OrnateCard(
    modifier: Modifier = Modifier,
    accentColor: Color = Cinnabar,
    content: @Composable ColumnScope.() -> Unit
) {
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
        color = surfaceColor(),
        border = BorderStroke(0.5.dp, outlineColor().copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier.drawBehind {
                val w = size.width
                val h = size.height
                val cornerLen = 24f
                val accent = accentColor.copy(alpha = 0.6f)
                fun o(x: Float, y: Float) = androidx.compose.ui.geometry.Offset(x, y)
                drawLine(accent, o(0f, 0f), o(cornerLen, 0f), strokeWidth = 2f)
                drawLine(accent, o(0f, 0f), o(0f, cornerLen), strokeWidth = 2f)
                drawLine(accent, o(w - cornerLen, 0f), o(w, 0f), strokeWidth = 2f)
                drawLine(accent, o(w, 0f), o(w, cornerLen), strokeWidth = 2f)
                drawLine(accent, o(0f, h - cornerLen), o(0f, h), strokeWidth = 2f)
                drawLine(accent, o(0f, h), o(cornerLen, h), strokeWidth = 2f)
                drawLine(accent, o(w - cornerLen, h), o(w, h), strokeWidth = 2f)
                drawLine(accent, o(w, h - cornerLen), o(w, h), strokeWidth = 2f)
            }
        ) {
            Column(modifier = Modifier.padding(18.dp), content = content)
        }
    }
}
