package com.xiaoliuren.app.core

import kotlin.math.floor

/**
 * 农历转换工具类（公历 ⇄ 农历）
 *
 * 数据范围：1900 年 - 2099 年
 * 数据格式（lunarInfo[year - 1900]）：
 *   bits 0-3  : 闰月月份（0 = 无闰月）
 *   bits 4-15 : 12 个月大小，bit(4+i) = 第(i+1)月（1 = 大月 30 天，0 = 小月 29 天）
 *   bit  16   : 闰月大小（1 = 大月 30 天，0 = 小月 29 天）
 *
 * 农历 1900 年正月初一 = 公历 1900 年 1 月 31 日
 */
object LunarCalendar {

    /** 农历 1900-2099 年信息表 */
    private val lunarInfo = intArrayOf(
        0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2, // 1900-1909
        0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977, // 1910-1919
        0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, // 1920-1929
        0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950, // 1930-1939
        0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, // 1940-1949
        0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5b0, 0x14573, 0x052b0, 0x0a9a8, 0x0e950, 0x06aa0, // 1950-1959
        0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b53, 0x056a0, // 1960-1969
        0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b6a0, 0x195a6, // 1970-1979
        0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570, // 1980-1989
        0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, // 1990-1999
        0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5, // 2000-2009
        0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, // 2010-2019
        0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530, // 2020-2029
        0x05aa0, 0x076a3, 0x096d0, 0x04afb, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, // 2030-2039
        0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0, // 2040-2049
        0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, // 2050-2059
        0x096d5, 0x092e0, 0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, // 2060-2069
        0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, // 2070-2079
        0x052b0, 0x0a930, 0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, // 2080-2089
        0x0ea65, 0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04afb, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250  // 2090-2099
    )

    private const val MIN_YEAR = 1900
    private const val MAX_YEAR = 2099

    /** 农历月名 */
    private val monthNames = arrayOf(
        "正月", "二月", "三月", "四月", "五月", "六月",
        "七月", "八月", "九月", "十月", "冬月", "腊月"
    )

    /** 农历日名 */
    private val dayNames = arrayOf(
        "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
        "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
        "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"
    )

    /** 天干 */
    private val tianGan = arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
    /** 地支 */
    private val diZhi = arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")
    /** 生肖 */
    private val shengXiao = arrayOf("鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪")

    /** 是否支持该公历年份的农历转换 */
    fun isSupported(year: Int): Boolean = year in MIN_YEAR..MAX_YEAR

    /** 返回 [year] 年的闰月月份（0 = 无闰月） */
    fun leapMonth(year: Int): Int = lunarInfo[year - MIN_YEAR] and 0xF

    /** 返回 [year] 年 [month] 月（1-12）的天数 */
    fun monthDays(year: Int, month: Int): Int {
        val info = lunarInfo[year - MIN_YEAR]
        return if (((info shr (4 + month - 1)) and 0x1) == 1) 30 else 29
    }

    /** 返回 [year] 年闰月的天数（无闰月返回 0） */
    fun leapDays(year: Int): Int {
        val leap = leapMonth(year)
        if (leap == 0) return 0
        return if (((lunarInfo[year - MIN_YEAR] shr 16) and 0x1) == 1) 30 else 29
    }

    /** 返回 [year] 年农历总天数 */
    fun yearDays(year: Int): Int {
        var sum = 0
        for (i in 1..12) sum += monthDays(year, i)
        sum += leapDays(year)
        return sum
    }

    /**
     * 公历转农历
     * @param year 公历年
     * @param month 公历月 1-12
     * @param day 公历日
     * @return LunarDate 或 null（超出支持范围）
     */
    fun solarToLunar(year: Int, month: Int, day: Int): LunarDate? {
        if (!isSupported(year)) return null
        // 边界：早于 1900 年正月初一（公历 1900-1-31）
        if (year == 1900 && (month < 1 || (month == 1 && day < 31))) return null

        // 计算公历日期距 1900-1-31 的天数
        var offset = daysBetween(1900, 1, 31, year, month, day)

        // 确定农历年
        var lunarYear = MIN_YEAR
        var temp: Int
        while (lunarYear <= MAX_YEAR) {
            temp = yearDays(lunarYear)
            if (offset < temp) break
            offset -= temp
            lunarYear++
        }
        if (lunarYear > MAX_YEAR) return null

        // 确定农历月
        val leap = leapMonth(lunarYear)
        var isLeap = false
        var lunarMonth = 1
        while (lunarMonth <= 12) {
            // 闰月处理：当 month == leap 时，先算正常月，再算闰月
            temp = monthDays(lunarYear, lunarMonth)
            if (offset < temp) break
            offset -= temp

            // 检查是否进入闰月
            if (lunarMonth == leap && !isLeap) {
                isLeap = true
                temp = leapDays(lunarYear)
                if (offset < temp) break
                offset -= temp
                isLeap = false
            }
            lunarMonth++
        }

        val lunarDay = offset + 1
        return LunarDate(
            year = lunarYear,
            month = lunarMonth,
            day = lunarDay,
            isLeapMonth = isLeap && lunarMonth == leap
        )
    }

    /** 计算两个公历日期之间的天数差（date2 - date1） */
    private fun daysBetween(y1: Int, m1: Int, d1: Int, y2: Int, m2: Int, d2: Int): Int {
        val jd1 = toJulianDay(y1, m1, d1)
        val jd2 = toJulianDay(y2, m2, d2)
        return (jd2 - jd1).toInt()
    }

    /** 公历日期转儒略日（整数） */
    private fun toJulianDay(year: Int, month: Int, day: Int): Long {
        var y = year.toLong()
        var m = month.toLong()
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0).toLong()
        val b = 2 - a + floor(a / 4.0).toLong()
        return (floor(365.25 * (y + 4716)).toLong()
                + floor(30.6001 * (m + 1)).toLong()
                + day + b - 1524)
    }

    /** 农历日期格式化（如"二〇二三年癸卯年(兔)二月十五"） */
    fun formatLunar(date: LunarDate): String {
        return "${ganZhiYear(date.year)}年${monthName(date)}${dayName(date.day)}"
    }

    /** 农历年月日简短格式（如"农历二月十五"） */
    fun formatLunarShort(date: LunarDate): String {
        return "农历${monthName(date)}${dayName(date.day)}"
    }

    /** 月名（含闰月前缀） */
    fun monthName(date: LunarDate): String {
        val prefix = if (date.isLeapMonth) "闰" else ""
        return prefix + monthNames[date.month - 1]
    }

    /** 日名 */
    fun dayName(day: Int): String = dayNames[day - 1]

    /** 干支年（如"癸卯"） */
    fun ganZhiYear(lunarYear: Int): String {
        // 农历 1900 年为庚子年
        val ganIdx = (lunarYear - 1900 + 6) % 10 // 1900 庚=6
        val zhiIdx = (lunarYear - 1900 + 0) % 12 // 1900 子=0
        return tianGan[ganIdx] + diZhi[zhiIdx]
    }

    /** 生肖（如"兔"） */
    fun shengXiao(lunarYear: Int): String {
        // 1900 鼠=0
        return shengXiao[(lunarYear - 1900) % 12]
    }

    /** 干支年带生肖（如"癸卯兔年"） */
    fun ganZhiWithAnimal(lunarYear: Int): String {
        return "${ganZhiYear(lunarYear)}${shengXiao(lunarYear)}年"
    }
}

/** 农历日期数据类 */
data class LunarDate(
    val year: Int,       // 农历年
    val month: Int,      // 农历月 1-12
    val day: Int,        // 农历日 1-30
    val isLeapMonth: Boolean // 是否闰月
)