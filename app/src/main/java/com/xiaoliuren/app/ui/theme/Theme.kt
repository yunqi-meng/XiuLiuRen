package com.xiaoliuren.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
private val AppTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 32.sp, fontWeight = FontWeight.Bold, color = InkBlack,
        letterSpacing = 2.sp, lineHeight = 40.sp
    ),
    displayMedium = TextStyle(
        fontSize = 26.sp, fontWeight = FontWeight.Bold, color = InkBlack,
        letterSpacing = 1.5.sp, lineHeight = 34.sp
    ),
    headlineLarge = TextStyle(
        fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = InkDark,
        letterSpacing = 1.sp, lineHeight = 30.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = InkDark,
        letterSpacing = 0.8.sp, lineHeight = 28.sp
    ),
    titleLarge = TextStyle(
        fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = InkDark,
        letterSpacing = 0.5.sp, lineHeight = 26.sp
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp, fontWeight = FontWeight.Medium, color = InkDark,
        letterSpacing = 0.3.sp, lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 15.sp, color = OnSurface,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp, color = OnSurface,
        lineHeight = 22.sp
    ),
    bodySmall = TextStyle(
        fontSize = 13.sp, color = OnSurfaceVariant,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp, fontWeight = FontWeight.Medium, color = OnSurface,
        letterSpacing = 0.3.sp, lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontSize = 12.sp, fontWeight = FontWeight.Medium, color = OnSurfaceVariant,
        letterSpacing = 0.2.sp, lineHeight = 18.sp
    ),
    labelSmall = TextStyle(
        fontSize = 11.sp, color = OnSurfaceVariant,
        lineHeight = 16.sp
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

// ==================== 渐变画笔工具 ====================

/** 背景渐变（浅色主题） */
val BackgroundBrushLight = Brush.verticalGradient(
    colors = listOf(BackgroundGradientTop, BackgroundGradientBottom)
)

/** 背景渐变（暗色主题） */
val BackgroundBrushDark = Brush.verticalGradient(
    colors = listOf(DarkBackgroundGradientTop, DarkBackgroundGradientBottom)
)

/** 朱砂渐变（按钮用） */
val CinnabarGradient = Brush.horizontalGradient(
    colors = listOf(Cinnabar, CinnabarDark)
)

/** 卡片顶部装饰线渐变 */
val DecorativeLineBrush = Brush.horizontalGradient(
    colors = listOf(Color.Transparent, Cinnabar, AntiqueGold, Cinnabar, Color.Transparent)
)

/** 金色装饰线渐变 */
val GoldLineBrush = Brush.horizontalGradient(
    colors = listOf(Color.Transparent, BronzeLine, AntiqueGold, BronzeLine, Color.Transparent)
)

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
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            window.statusBarColor = android.graphics.Color.TRANSPARENT
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}

/** 获取当前主题的背景渐变画笔 */
@Composable
fun backgroundGradient(): Brush {
    return if (isSystemInDarkTheme()) BackgroundBrushDark else BackgroundBrushLight
}

/** 获取当前主题的背景色 */
@Composable
fun backgroundColor(): Color {
    return if (isSystemInDarkTheme()) DarkBackground else Background
}

/** 获取当前主题的表面色 */
@Composable
fun surfaceColor(): Color {
    return if (isSystemInDarkTheme()) DarkInkSurface else SurfaceElevated
}

/** 获取当前主题的表面变体色 */
@Composable
fun surfaceVariantColor(): Color {
    return if (isSystemInDarkTheme()) DarkInkSurfaceVariant else SurfaceVariant
}

/** 获取当前主题的文字主色 */
@Composable
fun onSurfaceColor(): Color {
    return if (isSystemInDarkTheme()) DarkOnSurface else OnSurface
}

/** 获取当前主题的文字次色 */
@Composable
fun onSurfaceVariantColor(): Color {
    return if (isSystemInDarkTheme()) DarkOnSurfaceVariant else OnSurfaceVariant
}

/** 获取当前主题的描边色 */
@Composable
fun outlineColor(): Color {
    return if (isSystemInDarkTheme()) DarkOutline else Outline
}

/** 获取当前主题的朱砂色 */
@Composable
fun cinnabarColor(): Color {
    return if (isSystemInDarkTheme()) DarkCinnabar else Cinnabar
}

/** 获取当前主题的墨色 */
@Composable
fun inkDarkColor(): Color {
    return if (isSystemInDarkTheme()) DarkRiceWhite else InkDark
}
