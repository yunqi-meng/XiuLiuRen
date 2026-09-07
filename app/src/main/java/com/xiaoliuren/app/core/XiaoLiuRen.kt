package com.xiaoliuren.app.core

/**
 * 小六壬正统三步起课算法
 *
 * 核心口诀：大安起正月，月上起日，日上起时
 *
 * 六宫固定顺序（顺时针循环，索引 0-5）：
 *   0 = 大安  1 = 留连  2 = 速喜
 *   3 = 赤口  4 = 小吉  5 = 空亡
 *
 * 算法完全等价于左手掐指逻辑，数学公式精准对应传统起课结果
 */
object XiaoLiuRen {

    /** 六宫名称（索引 0-5） */
    val palaceNames = arrayOf("大安", "留连", "速喜", "赤口", "小吉", "空亡")

    /**
     * 三步起课核心算法
     *
     * @param month 节气月数 1-12（正月=1，以节令为界）
     * @param day 农历日数 1-30
     * @param hourIndex 时辰序号 1-12（子=1 ... 亥=12）
     * @return Triple(月宫索引, 日宫索引, 时宫索引)
     */
    fun divine(month: Int, day: Int, hourIndex: Int): Triple<Int, Int, Int> {
        // 步骤一：起月（定月宫）
        // 大安为正月，顺时针数到问事月份
        val monthPalaceIndex = (month - 1) % 6

        // 步骤二：起日（定日宫）
        // 月宫为初一，顺时针数到问事日期
        val dayPalaceIndex = (monthPalaceIndex + day - 1) % 6

        // 步骤三：起时（定时宫）
        // 日宫为子时，顺时针数到问事时辰
        val timePalaceIndex = (dayPalaceIndex + hourIndex - 1) % 6

        return Triple(monthPalaceIndex, dayPalaceIndex, timePalaceIndex)
    }

    /** 六宫名称（索引 0-5） */
    fun palaceName(index: Int): String = palaceNames[index]

    /**
     * 应急速算法（数学公式，与掐指结果完全一致）
     * 时宫索引 = (月 + 日 + 时 - 3) % 6（0-based，与 divine 返回一致）
     * 注：民间口诀“月+日+时-2 取余”为 1-based 余数（1=大安…0=空亡），此处转为 0-based 索引
     */
    fun quickCheck(month: Int, day: Int, hourIndex: Int): Int {
        return (month + day + hourIndex - 3) % 6
    }
}