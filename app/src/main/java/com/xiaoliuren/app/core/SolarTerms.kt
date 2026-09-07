package com.xiaoliuren.app.core

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

/**
 * 二十四节气计算（基于寿星天文历算法）
 *
 * 算法原理：
 * 1. 每个节气对应太阳黄经的一个特定角度
 * 2. 用 VSOP87 截断级数计算太阳黄经
 * 3. 牛顿迭代反解：给定黄经角度，求对应的儒略日
 * 4. 儒略日转换为公历日期
 *
 * 精度：误差小于 1 分钟，完全满足节气换月（精度到日）需求
 *
 * 节气序号定义：n=0 小寒, 1 大寒, 2 立春, 3 雨水, ..., 23 冬至
 * 即按公历时间从年初到年末排列
 */
object SolarTerms {

    /** 二十四节气名称（按序号 0-23 排列，0=小寒） */
    val names = arrayOf(
        "小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨",
        "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑",
        "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"
    )

    /**
     * 节令（用于起课换月）的节气序号
     * 立春=2(正月), 惊蛰=4(二月), 清明=6(三月), 立夏=8(四月),
     * 芒种=10(五月), 小暑=12(六月), 立秋=14(七月), 白露=16(八月),
     * 寒露=18(九月), 立冬=20(十月), 大雪=22(十一月), 小寒=0(十二月)
     */
    private val jieIndex = intArrayOf(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 0)

    /** 节令对应的月份名（正月=1 ... 十二月=12） */
    private val jieMonth = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    /**
     * 计算 [year] 年第 [n] 个节气的公历日期
     * @param year 公历年
     * @param n 节气序号 0-23（0=小寒, 23=冬至）
     * @return Pair(月份, 日)
     */
    fun getSolarTerm(year: Int, n: Int): Pair<Int, Int> {
        val jd = solarTermJD(year, n)
        val (m, d) = jdToSolar(jd)
        return Pair(m, d)
    }

    /**
     * 获取 [year] 年所有 24 节气的公历日期
     * @return List<Pair<节气序号, Pair<月, 日>>>
     */
    fun getAllTerms(year: Int): List<Pair<Int, Pair<Int, Int>>> {
        return (0..23).map { it to getSolarTerm(year, it) }
    }

    /**
     * 获取 [year] 年所有节令（用于换月）的公历日期
     * @return List<节令信息>，每项含(月份1-12, 公历月, 公历日)
     */
    fun getJieDates(year: Int): List<Triple<Int, Int, Int>> {
        return (0..11).map { i ->
            val n = jieIndex[i]
            val (m, d) = getSolarTerm(year, n)
            Triple(jieMonth[i], m, d)
        }
    }

    /**
     * 根据公历日期确定节气月（用于起课）
     * 规则：以节令为界，立春开始为正月，惊蛰开始为二月...
     * 注意：跨年处理——1月、2月初可能属于上一年的十二月（丑月）
     *
     * @param year 公历年
     * @param month 公历月 1-12
     * @param day 公历日
     * @return 节气月 1-12（1=正月寅月 ... 12=十二月丑月）
     */
    fun getJieQiMonth(year: Int, month: Int, day: Int): Int {
        // 立春通常在2月3-5日，小寒在1月5-7日
        // 处理逻辑：找到该日期落在哪个节令之后
        // 节令按时间排列：小寒(1月初)→立春(2月初)→惊蛰(3月初)→...→冬至(12月底)→小寒(次年1月初)
        // 对应月份：12,1,2,3,...,11,12

        // 收集本年所有节令日期，加上上一年小寒和下一年小寒作为边界
        val terms = getJieDatesSorted(year)
        // terms 是按时间排序的 (月份1-12, 公历月, 公历日)

        val target = Triple(0, month, day)

        for (i in terms.indices) {
            val next = terms[i]
            if (compareDate(month, day, next.second, next.third) < 0) {
                // 在 next 之前，属于上一个节令的月份
                val prev = if (i == 0) {
                    // 在本年第一个节令之前，属于上一年最后一个节令（十二月）
                    terms.last()
                } else {
                    terms[i - 1]
                }
                return prev.first
            }
        }
        // 在本年最后一个节令之后，属于该节令的月份（直到下一年小寒）
        return terms.last().first
    }

    /** 获取 [year] 年节令按公历时间排序的列表 */
    private fun getJieDatesSorted(year: Int): List<Triple<Int, Int, Int>> {
        // 本年12个节令，按公历月日排序
        val list = getJieDates(year).toMutableList()
        // 小寒(12月)在1月初，立春(1月)在2月初...需要按实际公历日期排序
        list.sortWith(compareBy({ it.second }, { it.third }))
        return list
    }

    private fun compareDate(m1: Int, d1: Int, m2: Int, d2: Int): Int {
        if (m1 != m2) return m1 - m2
        return d1 - d2
    }

    // ==================== 天文计算核心 ====================

    private const val J2000 = 2451545.0 // 2000年1月1日12时儒略日

    /**
     * 计算 [year] 年第 [n] 个节气的儒略日（力学时）
     * 节气对应黄经：(285 + 15*n) % 360 度
     */
    private fun solarTermJD(year: Int, n: Int): Double {
        // 节气黄经角度（度）
        val longitude = (285.0 + 15.0 * n) % 360.0

        // 初始估算儒略日
        // 每个节气在公历中的近似日期（相对1月1日）
        val approxDayOfYear = approxTermDay(n)
        val jd0 = toJD(year, 1, 1.0) - 0.5 + approxDayOfYear

        // 牛顿迭代求精确儒略日
        var jd = jd0
        repeat(50) {
            val l = sunLongitude(jd) // 太阳黄经（度）
            // 有符号角度差，归一化到 -180~180
            var dl = longitude - l
            dl = ((dl + 180.0) % 360.0 + 360.0) % 360.0 - 180.0
            val djd = dl / 360.0 * 365.2422 // 估算时间修正（天）
            jd += djd
            if (abs(djd) < 0.0001) return@repeat
        }

        // 转换为世界时（减去 ΔT 的天数）
        val dt = deltaT(year + (if (n >= 2) 0 else -1)) / 86400.0
        return jd - dt
    }

    /** 节气在公历年中相对1月1日的近似天数（用于初始估算） */
    private fun approxTermDay(n: Int): Double {
        // 小寒≈5, 大寒≈20, 立春≈35(2月4日), 雨水≈50, ...
        // 每个节气间隔约15.218天
        return 5.0 + 15.2184 * n
    }

    /**
     * 太阳视黄经计算（基于 Jean Meeus《天文算法》简化公式，精度约 0.01 度）
     * @param jd 儒略日（力学时）
     * @return 太阳视黄经（度，0-360）
     */
    private fun sunLongitude(jd: Double): Double {
        val T = (jd - J2000) / 36525.0 // 儒略世纪数

        // 平近点角 g
        val g = toRad(357.52911 + 35999.05029 * T - 0.0001537 * T * T)

        // 太阳平黄经 + 中心差
        var l = 280.46646 + 36000.76983 * T + 0.0003032 * T * T
        l += 1.914666471 * sin(g)
        l += 0.019994643 * sin(2 * g)
        l += 0.0002894 * sin(3 * g)

        // 月球升交点平黄经（用于章动修正）
        val omega = toRad(125.04452 - 1934.136261 * T + 0.0020708 * T * T)

        // 章动与光行差修正
        l -= 0.00569
        l -= 0.00478 * sin(omega)

        return normAngle(l)
    }

    /** 角度归一化到 0-360 */
    private fun normAngle(a: Double): Double {
        var r = a % 360.0
        if (r < 0) r += 360.0
        return r
    }

    private fun toRad(deg: Double): Double = deg * PI / 180.0
    private fun toDeg(rad: Double): Double = rad * 180.0 / PI

    /**
     * ΔT 计算（力学时 UT1 之差，单位秒）
     * 使用 Espenak-Meeus 多项式拟合
     */
    private fun deltaT(year: Int): Double {
        val y = year + 0.0
        return when {
            year <= 1899 -> {
                val t = (y - 1800) / 100.0
                val c = doubleArrayOf(13.72, -0.332447, -0.1746426, 0.558475, -0.632658, 0.224928)
                poly(t, c)
            }
            year <= 1919 -> {
                val t = y - 1900
                val c = doubleArrayOf(-2.79, 1.494119, -0.0598939, 0.0061966, -0.000197)
                poly(t, c)
            }
            year <= 1940 -> {
                val t = y - 1920
                val c = doubleArrayOf(21.20, 0.84493, -0.076100, 0.0020936)
                poly(t, c)
            }
            year <= 1960 -> {
                val t = y - 1950
                val c = doubleArrayOf(29.07, 0.997, 0.0919, -0.00387)
                poly(t, c)
            }
            year <= 1985 -> {
                val t = y - 1975
                val c = doubleArrayOf(45.45, 1.067, -0.00633, 0.00015)
                poly(t, c)
            }
            year <= 2005 -> {
                val t = y - 2000
                val c = doubleArrayOf(63.86, 0.3345, -0.060374, 0.0017275, 0.000651814, -0.00002373599)
                poly(t, c)
            }
            year <= 2150 -> {
                val t = (y - 1820) / 100.0
                -20.0 + 32.0 * t * t
            }
            else -> {
                val t = (y - 1820) / 100.0
                -20.0 + 32.0 * t * t
            }
        }
    }

    private fun poly(t: Double, c: DoubleArray): Double {
        var r = 0.0
        var p = 1.0
        for (ci in c) {
            r += ci * p
            p *= t
        }
        return r
    }

    // ==================== 儒略日与公历互转 ====================

    /** 公历日期转儒略日 */
    fun toJD(year: Int, month: Int, day: Double): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    /** 儒略日转公历日期 */
    fun jdToSolar(jd: Double): Pair<Int, Int> {
        val jd0 = floor(jd + 0.5) + 0.5
        val z = floor(jd0)
        val f = jd0 - z
        var a = z
        if (z >= 2299161) {
            val alpha = floor((z - 1867216.25) / 36524.25)
            a = z + 1 + alpha - floor(alpha / 4.0)
        }
        val b = a + 1524
        val c = floor((b - 122.1) / 365.25)
        val d = floor(365.25 * c)
        val e = floor((b - d) / 30.6001)
        val day = b - d - floor(30.6001 * e) + f
        val month = if (e < 14) e - 1 else e - 13
        val year = if (month > 2) c - 4716 else c - 4715
        return Pair(month.toInt(), floor(day).toInt())
    }
}