package com.xiaoliuren.app.core

/**
 * 十二时辰工具类
 *
 * 时辰对应表（序号用于起课）：
 *   子时 23:00-01:00 → 1
 *   丑时 01:00-03:00 → 2
 *   寅时 03:00-05:00 → 3
 *   卯时 05:00-07:00 → 4
 *   辰时 07:00-09:00 → 5
 *   巳时 09:00-11:00 → 6
 *   午时 11:00-13:00 → 7
 *   未时 13:00-15:00 → 8
 *   申时 15:00-17:00 → 9
 *   酉时 17:00-19:00 → 10
 *   戌时 19:00-21:00 → 11
 *   亥时 21:00-23:00 → 12
 */
object Shichen {

    /** 时辰名称（序号 1-12 对应索引 0-11） */
    val names = arrayOf(
        "子时", "丑时", "寅时", "卯时", "辰时", "巳时",
        "午时", "未时", "申时", "酉时", "戌时", "亥时"
    )

    /** 时辰对应的地支 */
    val diZhi = arrayOf(
        "子", "丑", "寅", "卯", "辰", "巳",
        "午", "未", "申", "酉", "戌", "亥"
    )

    /** 时辰对应的时间段（用于显示） */
    val timeRanges = arrayOf(
        "23:00-01:00", "01:00-03:00", "03:00-05:00", "05:00-07:00",
        "07:00-09:00", "09:00-11:00", "11:00-13:00", "13:00-15:00",
        "15:00-17:00", "17:00-19:00", "19:00-21:00", "21:00-23:00"
    )

    /**
     * 根据小时、分钟计算时辰
     *
     * 重要规则：23:00 即进入次日子时，需日期顺延
     *
     * @param hour 小时 0-23
     * @param minute 分钟 0-59
     * @return ShichenResult（含时辰序号 1-12、是否跨日）
     */
    fun fromTime(hour: Int, minute: Int): ShichenResult {
        // 23:00 之后（含 23:00 整）算次日子时
        if (hour == 23) {
            return ShichenResult(index = 1, crossNextDay = true)
        }
        // 0:00 - 0:59 仍属当日子时
        if (hour == 0) {
            return ShichenResult(index = 1, crossNextDay = false)
        }
        // 1:00 - 22:59
        val index = (hour + 1) / 2 + 1
        return ShichenResult(index = index, crossNextDay = false)
    }

    /** 时辰名称（序号 1-12） */
    fun name(index: Int): String = names[index - 1]

    /** 时辰地支（序号 1-12） */
    fun diZhi(index: Int): String = diZhi[index - 1]

    /** 时辰时间段（序号 1-12） */
    fun timeRange(index: Int): String = timeRanges[index - 1]

    /** 时辰名称带地支（如"申时(申)"） */
    fun nameWithZhi(index: Int): String = "${names[index - 1]}（${diZhi[index - 1]}）"
}

/** 时辰计算结果 */
data class ShichenResult(
    val index: Int,        // 时辰序号 1-12
    val crossNextDay: Boolean // 是否跨入次日（23点后）
)