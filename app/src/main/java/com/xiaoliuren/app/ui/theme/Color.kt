package com.xiaoliuren.app.ui.theme

import androidx.compose.ui.graphics.Color

// ==================== 墨色系（Ink） ====================
val InkBlack = Color(0xFF1A1A1A)
val InkDark = Color(0xFF2B2B2B)
val InkMedium = Color(0xFF3D3D3D)
val InkLight = Color(0xFF5C5C5C)
val InkFade = Color(0xFF8A8A8A)

// ==================== 朱砂系（Cinnabar） ====================
val Cinnabar = Color(0xFFB83A3A)
val CinnabarDark = Color(0xFF8B2A2A)
val CinnabarLight = Color(0xFFD45E5E)
val CinnabarBright = Color(0xFFE07070)
val CinnabarDeep = Color(0xFF6B1E1E)

// ==================== 米白/纸色系（Rice/Paper） ====================
val RiceWhite = Color(0xFFF5F0E6)
val RiceLight = Color(0xFFFAF6EE)
val RiceBright = Color(0xFFFFFBF4)
val Paper = Color(0xFFEFE8D8)
val PaperDark = Color(0xFFE2D9C5)

// ==================== 装饰金/铜色系（Gold/Bronze） ====================
val AntiqueGold = Color(0xFFB8956A)
val AntiqueGoldLight = Color(0xFFD4B888)
val AntiqueGoldDeep = Color(0xFF8A6D45)
val BronzeLine = Color(0xFFA89070)
val BronzeLineLight = Color(0xFFC4AC8A)

// ==================== 吉凶颜色 ====================
val JiGreen = Color(0xFF3A7D44)
val JiGreenLight = Color(0xFF4E8F58)
val HalfYellow = Color(0xFFC9A227)
val HalfYellowLight = Color(0xFFE9C247)
val XiongRed = Color(0xFFB83A3A)
val XiongRedLight = Color(0xFFD45E5E)
val XiongRedDeep = Color(0xFF8B2A2A)

// ==================== 功能色（浅色主题） ====================
val Surface = Color(0xFFFAF6EE)
val SurfaceVariant = Color(0xFFEFE8D8)
val SurfaceElevated = Color(0xFFFFFBF4)
val OnSurface = Color(0xFF1A1A1A)
val OnSurfaceVariant = Color(0xFF5C5C5C)
val Outline = Color(0xFFBDB5A3)
val OutlineLight = Color(0xFFD4CCBA)
val Background = Color(0xFFF5F0E6)
val BackgroundGradientTop = Color(0xFFF8F3E9)
val BackgroundGradientBottom = Color(0xFFEFE7D5)

// ==================== 暗色主题色 ====================
val DarkInkBlack = Color(0xFF0D0D0D)
val DarkInkDark = Color(0xFF1A1A1A)
val DarkInkMedium = Color(0xFF252525)
val DarkInkSurface = Color(0xFF1E1E1E)
val DarkInkSurfaceVariant = Color(0xFF2A2A2A)
val DarkInkElevated = Color(0xFF2D2D2D)
val DarkRiceWhite = Color(0xFFE8E0D0)
val DarkRiceLight = Color(0xFFD0C8B8)
val DarkOnSurface = Color(0xFFE8E0D0)
val DarkOnSurfaceVariant = Color(0xFFA89E8C)
val DarkOutline = Color(0xFF4A4438)
val DarkBackground = Color(0xFF0D0D0D)
val DarkBackgroundGradientTop = Color(0xFF151515)
val DarkBackgroundGradientBottom = Color(0xFF0A0A0A)
val DarkCinnabar = Color(0xFFD45E5E)
val DarkCinnabarDark = Color(0xFFB83A3A)
val DarkAntiqueGold = Color(0xFFC4A878)

/** 根据吉凶等级获取颜色（亮色主题基准色） */
fun levelColor(level: Int): Color = when (level) {
    0 -> JiGreen
    1 -> JiGreenLight
    2 -> HalfYellow
    3 -> XiongRed
    4 -> XiongRedDeep
    else -> InkLight
}

/** 根据吉凶等级获取浅色底（亮色主题用） */
fun levelColorLight(level: Int): Color = when (level) {
    0 -> Color(0xFFE8F5E9)
    1 -> Color(0xFFEFF7F0)
    2 -> Color(0xFFFFF8E1)
    3 -> Color(0xFFFDECEA)
    4 -> Color(0xFFF8E0E0)
    else -> SurfaceVariant
}

/** 根据吉凶等级获取深色底（暗色主题用） */
fun levelColorDark(level: Int): Color = when (level) {
    0 -> Color(0xFF1B3022)
    1 -> Color(0xFF1E2D24)
    2 -> Color(0xFF2A2818)
    3 -> Color(0xFF2E1A1A)
    4 -> Color(0xFF260E0E)
    else -> DarkInkSurfaceVariant
}
