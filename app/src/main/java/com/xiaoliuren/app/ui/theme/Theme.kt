package com.xiaoliuren.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

// ==================== 浅色主题 ====================
private val LightColorScheme = lightColorScheme(
    primary = Cinnabar,
    onPrimary = RiceWhite,
    primaryContainer = CinnabarDark,
    onPrimaryContainer = RiceWhite,
    secondary = AntiqueGold,
    onSecondary = InkBlack,
    secondaryContainer = AntiqueGoldLight,
    onSecondaryContainer = InkDark,
    tertiary = HalfYellow,
    onTertiary = InkBlack,
    tertiaryContainer = HalfYellowLight,
    onTertiaryContainer = InkDark,
    background = Background,
    onBackground = OnSurface,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    surfaceTint = Cinnabar,
    inverseSurface = InkDark,
    inverseOnSurface = RiceWhite,
    outline = Outline,
    outlineVariant = OutlineLight,
    error = XiongRed,
    onError = RiceWhite
)

// ==================== 暗色主题 ====================
private val DarkColorScheme = darkColorScheme(
    primary = DarkCinnabar,
    onPrimary = DarkInkBlack,
    primaryContainer = CinnabarDark,
    onPrimaryContainer = RiceWhite,
    secondary = DarkAntiqueGold,
    onSecondary = DarkInkBlack,
    secondaryContainer = AntiqueGoldDeep,
    onSecondaryContainer = DarkRiceWhite,
    tertiary = HalfYellowLight,
    onTertiary = DarkInkBlack,
    tertiaryContainer = Color(0xFF5A4A20),
    onTertiaryContainer = HalfYellowLight,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkInkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkInkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceTint = DarkCinnabar,
    inverseSurface = RiceWhite,
    inverseOnSurface = InkDark,
    outline = DarkOutline,
    outlineVariant = Color(0xFF3A3528),
    error = XiongRedLight,
    onError = DarkInkBlack
)

// ==================== 排版系统 ====================
//
// 注意：这里 **绝对不能** 写 `color = ...`。
// Compose 的文本取色优先级是 `Text(color =)` > `style.color` > `LocalContentColor`，
// 一旦在 Typography 里写死颜色，MaterialTheme 的 contentColor（以及暗色主题）
// 就会被整体架空 —— 暗色模式下文字会变成近黑色，在深色背景上完全不可读。
// 颜色统一交给 LocalContentColor / 各处显式的主题色函数处理。
private val AppTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 32.sp, fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp, lineHeight = 40.sp
    ),
    displayMedium = TextStyle(
        fontSize = 26.sp, fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp, lineHeight = 34.sp
    ),
    displaySmall = TextStyle(
        fontSize = 24.sp, fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp, lineHeight = 32.sp
    ),
    headlineLarge = TextStyle(
        fontSize = 22.sp, fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.sp, lineHeight = 30.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 20.sp, fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.8.sp, lineHeight = 28.sp
    ),
    headlineSmall = TextStyle(
        fontSize = 18.sp, fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.6.sp, lineHeight = 26.sp
    ),
    titleLarge = TextStyle(
        fontSize = 18.sp, fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.5.sp, lineHeight = 26.sp
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp, fontWeight = FontWeight.Medium,
        letterSpacing = 0.3.sp, lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontSize = 14.sp, fontWeight = FontWeight.Medium,
        letterSpacing = 0.2.sp, lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 15.sp, lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp, lineHeight = 22.sp
    ),
    bodySmall = TextStyle(
        fontSize = 13.sp, lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp, fontWeight = FontWeight.Medium,
        letterSpacing = 0.3.sp, lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontSize = 12.sp, fontWeight = FontWeight.Medium,
        letterSpacing = 0.2.sp, lineHeight = 18.sp
    ),
    labelSmall = TextStyle(
        fontSize = 11.sp, fontWeight = FontWeight.Medium,
        letterSpacing = 0.2.sp, lineHeight = 16.sp
    )
)

// ==================== 形状系统 ====================
private val AppShapes = Shapes(
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
    small = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
)

// ==================== 动效令牌 ====================
//
// 统一节奏，避免各处散落的魔法数字。
// 参考 Material Design 3 时长规范：微交互 50-100ms / 短 100-200ms / 中 200-300ms / 长 300-500ms。
object Motion {
    /** 图标、箭头等状态切换 */
    const val MICRO = 120

    /** 按压回弹、开关 */
    const val SHORT = 180

    /** 展开/收起 */
    const val MEDIUM = 260

    /** 页面切换 */
    const val LONG = 320

    /** 列表交错入场步长（20-40ms 属于舒适区间） */
    const val STAGGER_STEP = 35
}

// ==================== 渐变画笔工具 ====================

/** 背景渐变（浅色主题） */
private val BackgroundBrushLight = Brush.verticalGradient(
    colors = listOf(BackgroundGradientTop, BackgroundGradientBottom)
)

/** 背景渐变（暗色主题） */
private val BackgroundBrushDark = Brush.verticalGradient(
    colors = listOf(DarkBackgroundGradientTop, DarkBackgroundGradientBottom)
)

// ==================== 主题状态 ====================
//
// 用 CompositionLocal 承载「当前是否暗色」，而不是各处自己去问 isSystemInDarkTheme()。
// 否则一旦上层显式指定 darkTheme（预览、截图、强制主题），
// 主题色助手函数会和 MaterialTheme 的实际配色对不上。
private val LocalIsDarkTheme = staticCompositionLocalOf { false }

@Composable
fun XiaoLiuRenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // 状态栏/导航栏图标明暗跟随主题；状态栏底色透明由 enableEdgeToEdge 统一处理
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalIsDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}

/** 当前是否处于暗色主题（读取的是 XiaoLiuRenTheme 的实际取值） */
@Composable
@ReadOnlyComposable
fun isDarkTheme(): Boolean = LocalIsDarkTheme.current

// ==================== 主题色助手 ====================

/** 获取当前主题的背景渐变画笔 */
@Composable
@ReadOnlyComposable
fun backgroundGradient(): Brush {
    return if (LocalIsDarkTheme.current) BackgroundBrushDark else BackgroundBrushLight
}

/** 获取当前主题的背景色 */
@Composable
@ReadOnlyComposable
fun backgroundColor(): Color {
    return if (LocalIsDarkTheme.current) DarkBackground else Background
}

/** 获取当前主题的表面色 */
@Composable
@ReadOnlyComposable
fun surfaceColor(): Color {
    return if (LocalIsDarkTheme.current) DarkInkSurface else SurfaceElevated
}

/** 获取当前主题的表面变体色 */
@Composable
@ReadOnlyComposable
fun surfaceVariantColor(): Color {
    return if (LocalIsDarkTheme.current) DarkInkSurfaceVariant else SurfaceVariant
}

/** 获取当前主题的文字主色 */
@Composable
@ReadOnlyComposable
fun onSurfaceColor(): Color {
    return if (LocalIsDarkTheme.current) DarkOnSurface else OnSurface
}

/** 获取当前主题的文字次色 */
@Composable
@ReadOnlyComposable
fun onSurfaceVariantColor(): Color {
    return if (LocalIsDarkTheme.current) DarkOnSurfaceVariant else OnSurfaceVariant
}

/** 获取当前主题的描边色 */
@Composable
@ReadOnlyComposable
fun outlineColor(): Color {
    return if (LocalIsDarkTheme.current) DarkOutline else Outline
}

/** 获取当前主题的朱砂色 */
@Composable
@ReadOnlyComposable
fun cinnabarColor(): Color {
    return if (LocalIsDarkTheme.current) DarkCinnabar else Cinnabar
}

/** 获取当前主题的墨色（正文主色） */
@Composable
@ReadOnlyComposable
fun inkDarkColor(): Color {
    return if (LocalIsDarkTheme.current) DarkRiceWhite else InkDark
}

/** 获取当前主题的装饰金 */
@Composable
@ReadOnlyComposable
fun antiqueGoldColor(): Color {
    return if (LocalIsDarkTheme.current) DarkAntiqueGold else AntiqueGold
}

/**
 * 吉凶色（随主题适配）。
 * 暗色主题下用提亮后的色值，保证在深色背景上依然有足够对比度。
 */
@Composable
@ReadOnlyComposable
fun levelColorThemed(level: Int): Color {
    return if (LocalIsDarkTheme.current) levelColorDarkInk(level) else levelColor(level)
}

/**
 * 吉凶色的浅色底（随主题适配）。
 * 浅色主题用浅底，暗色主题用深底 —— 直接把浅色底放在深色主题上会变成一块刺眼的白斑。
 */
@Composable
@ReadOnlyComposable
fun levelSurfaceColor(level: Int): Color {
    return if (LocalIsDarkTheme.current) levelColorDark(level) else levelColorLight(level)
}

/**
 * 吉凶标签上的文字色。
 * 「半吉半凶」的黄底配白字对比度仅约 2.4:1，远低于 WCAG AA 的 4.5:1，
 * 因此黄底改用深墨字（约 7.2:1）。
 */
fun levelTagContentColor(level: Int): Color = when (level) {
    2 -> InkBlack
    else -> RiceWhite
}

private fun levelColorDarkInk(level: Int): Color = when (level) {
    0 -> Color(0xFF6FBF7C)
    1 -> Color(0xFF8FD09A)
    2 -> Color(0xFFE9C247)
    3 -> Color(0xFFE08585)
    4 -> Color(0xFFEFA9A9)
    else -> DarkOnSurfaceVariant
}
