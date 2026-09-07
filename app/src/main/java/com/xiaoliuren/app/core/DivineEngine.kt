package com.xiaoliuren.app.core

import java.util.Calendar
import java.util.TimeZone

/**
 * 统一起课引擎
 *
 * 完整流程：
 * 1. 输入公历年月日时分
 * 2. 计算时辰（23:00 子时换日规则）
 * 3. 若跨日，公历日期顺延 1 天
 * 4. 公历转农历
 * 5. 计算节气月（以节令为界）
 * 6. 调用三步起课算法
 * 7. 返回完整结果
 */
object DivineEngine {

    /** 节气月名称（1=正月 ... 12=十二月） */
    private val jieqiMonthNames = arrayOf(
        "正月", "二月", "三月", "四月", "五月", "六月",
        "七月", "八月", "九月", "十月", "十一月", "十二月"
    )

    /** 节气月对应地支 */
    private val jieqiMonthZhi = arrayOf(
        "寅", "卯", "辰", "巳", "午", "未",
        "申", "酉", "戌", "亥", "子", "丑"
    )

    /**
     * 以公历时间起课
     *
     * @param year 公历年
     * @param month 公历月 1-12
     * @param day 公历日
     * @param hour 小时 0-23
     * @param minute 分钟 0-59
     * @return DivineResult
     */
    fun divine(year: Int, month: Int, day: Int, hour: Int, minute: Int): DivineResult {
        // 1. 计算时辰（含 23 点换日判断）
        val shichenResult = Shichen.fromTime(hour, minute)

        // 2. 处理子时换日：23:00 后日期顺延 1 天
        var solarYear = year
        var solarMonth = month
        var solarDay = day
        if (shichenResult.crossNextDay) {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).apply {
                set(year, month - 1, day, hour, minute)
                add(Calendar.DAY_OF_MONTH, 1)
            }
            solarYear = cal.get(Calendar.YEAR)
            solarMonth = cal.get(Calendar.MONTH) + 1
            solarDay = cal.get(Calendar.DAY_OF_MONTH)
        }

        // 3. 检查农历支持范围
        if (!LunarCalendar.isSupported(solarYear)) {
            return DivineResult(
                supported = false,
                solarYear = solarYear,
                solarMonth = solarMonth,
                solarDay = solarDay,
                solarHour = hour,
                solarMinute = minute,
                crossNextDay = shichenResult.crossNextDay,
                shichenIndex = shichenResult.index,
                lunarDate = null,
                jieqiMonth = 0,
                monthPalaceIndex = 0,
                dayPalaceIndex = 0,
                timePalaceIndex = 0
            )
        }

        // 4. 公历转农历
        val lunarDate = LunarCalendar.solarToLunar(solarYear, solarMonth, solarDay)
            ?: return DivineResult(
                supported = false,
                solarYear = solarYear,
                solarMonth = solarMonth,
                solarDay = solarDay,
                solarHour = hour,
                solarMinute = minute,
                crossNextDay = shichenResult.crossNextDay,
                shichenIndex = shichenResult.index,
                lunarDate = null,
                jieqiMonth = 0,
                monthPalaceIndex = 0,
                dayPalaceIndex = 0,
                timePalaceIndex = 0
            )

        // 5. 计算节气月（以节令为界）
        val jieqiMonth = SolarTerms.getJieQiMonth(solarYear, solarMonth, solarDay)

        // 6. 三步起课
        val (monthPalace, dayPalace, timePalace) = XiaoLiuRen.divine(
            month = jieqiMonth,
            day = lunarDate.day,
            hourIndex = shichenResult.index
        )

        return DivineResult(
            supported = true,
            solarYear = solarYear,
            solarMonth = solarMonth,
            solarDay = solarDay,
            solarHour = hour,
            solarMinute = minute,
            crossNextDay = shichenResult.crossNextDay,
            shichenIndex = shichenResult.index,
            lunarDate = lunarDate,
            jieqiMonth = jieqiMonth,
            monthPalaceIndex = monthPalace,
            dayPalaceIndex = dayPalace,
            timePalaceIndex = timePalace
        )
    }

    /** 以当前系统时间起课 */
    fun divineNow(): DivineResult {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"))
        return divine(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE)
        )
    }

    /** 节气月名称 */
    fun jieqiMonthName(month: Int): String = jieqiMonthNames[month - 1]

    /** 节气月地支 */
    fun jieqiMonthZhi(month: Int): String = jieqiMonthZhi[month - 1]

    /** 节气月完整名（如"三月辰月"） */
    fun jieqiMonthFullName(month: Int): String = "${jieqiMonthNames[month - 1]}${jieqiMonthZhi[month - 1]}月"
}

/**
 * 起课完整结果
 *
 * @property supported 是否支持（农历范围 1900-2099）
 * @property solarYear/Month/Day/Hour/Minute 实际用于起课的公历时间（已处理换日）
 * @property crossNextDay 是否因 23 点子时而顺延日期
 * @property shichenIndex 时辰序号 1-12
 * @property lunarDate 农历日期（不支持时为 null）
 * @property jieqiMonth 节气月 1-12
 * @property monthPalaceIndex 月宫索引 0-5
 * @property dayPalaceIndex 日宫索引 0-5
 * @property timePalaceIndex 时宫索引 0-5
 */
data class DivineResult(
    val supported: Boolean,
    val solarYear: Int,
    val solarMonth: Int,
    val solarDay: Int,
    val solarHour: Int,
    val solarMinute: Int,
    val crossNextDay: Boolean,
    val shichenIndex: Int,
    val lunarDate: LunarDate?,
    val jieqiMonth: Int,
    val monthPalaceIndex: Int,
    val dayPalaceIndex: Int,
    val timePalaceIndex: Int
)