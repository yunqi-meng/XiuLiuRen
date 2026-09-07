package com.xiaoliuren.app.data

import com.xiaoliuren.app.core.LunarDate

/**
 * 当前时间信息（用于首页展示）
 */
data class CurrentTimeInfo(
    val solarYear: Int,
    val solarMonth: Int,
    val solarDay: Int,
    val solarHour: Int,
    val solarMinute: Int,
    val lunarDate: LunarDate?,
    val jieqiMonth: Int,
    val jieqiMonthName: String,
    val shichenIndex: Int,
    val shichenName: String,
    val shichenTimeRange: String,
    val supported: Boolean
) {
    /** 公历时间字符串 */
    val solarTimeString: String
        get() = "${solarYear}年${solarMonth}月${solarDay}日 ${String.format("%02d:%02d", solarHour, solarMinute)}"

    /** 农历时间字符串 */
    val lunarString: String
        get() = if (lunarDate != null) {
            com.xiaoliuren.app.core.LunarCalendar.formatLunar(lunarDate)
        } else "暂不支持"
}