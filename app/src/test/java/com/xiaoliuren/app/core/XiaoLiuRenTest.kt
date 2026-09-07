package com.xiaoliuren.app.core

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 小六壬起课算法单元测试
 */
class XiaoLiuRenTest {

    @Test
    fun `三月初五申时 应得 速喜`() {
        // 设计方案验证案例：农历三月初五申时
        val (monthPalace, dayPalace, timePalace) = XiaoLiuRen.divine(month = 3, day = 5, hourIndex = 9)
        assertEquals(2, monthPalace) // 速喜
        assertEquals(0, dayPalace)   // 大安
        assertEquals(2, timePalace)  // 速喜
    }

    @Test
    fun `速算法与三步法结果一致`() {
        for (month in 1..12) {
            for (day in 1..30) {
                for (hour in 1..12) {
                    val (_, _, timePalace) = XiaoLiuRen.divine(month, day, hour)
                    val quick = XiaoLiuRen.quickCheck(month, day, hour)
                    assertEquals("月=$month 日=$day 时=$hour", timePalace, quick)
                }
            }
        }
    }

    @Test
    fun `正月初一子时 应得 大安`() {
        val (m, d, t) = XiaoLiuRen.divine(1, 1, 1)
        assertEquals(0, m) // 大安
        assertEquals(0, d) // 大安
        assertEquals(0, t) // 大安
    }

    @Test
    fun `六宫循环验证`() {
        // 正月走6步回到大安
        val (m, _, _) = XiaoLiuRen.divine(7, 1, 1)
        assertEquals(0, m) // 大安（7月=正月+6）
    }

    @Test
    fun `宫位名称正确`() {
        assertEquals("大安", XiaoLiuRen.palaceName(0))
        assertEquals("留连", XiaoLiuRen.palaceName(1))
        assertEquals("速喜", XiaoLiuRen.palaceName(2))
        assertEquals("赤口", XiaoLiuRen.palaceName(3))
        assertEquals("小吉", XiaoLiuRen.palaceName(4))
        assertEquals("空亡", XiaoLiuRen.palaceName(5))
    }
}