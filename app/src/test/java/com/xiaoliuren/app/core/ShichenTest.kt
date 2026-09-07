package com.xiaoliuren.app.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 时辰计算单元测试
 */
class ShichenTest {

    @Test
    fun `子时23点 应跨日`() {
        val r = Shichen.fromTime(23, 0)
        assertEquals(1, r.index)
        assertTrue(r.crossNextDay)
    }

    @Test
    fun `子时0点 不跨日`() {
        val r = Shichen.fromTime(0, 30)
        assertEquals(1, r.index)
        assertTrue(!r.crossNextDay)
    }

    @Test
    fun `申时15点`() {
        val r = Shichen.fromTime(15, 30)
        assertEquals(9, r.index)
        assertTrue(!r.crossNextDay)
    }

    @Test
    fun `亥时21点`() {
        val r = Shichen.fromTime(21, 0)
        assertEquals(12, r.index)
    }

    @Test
    fun `亥时22点`() {
        val r = Shichen.fromTime(22, 59)
        assertEquals(12, r.index)
    }

    @Test
    fun `各时辰边界验证`() {
        // 丑时01:00
        assertEquals(2, Shichen.fromTime(1, 0).index)
        // 寅时03:00
        assertEquals(3, Shichen.fromTime(3, 0).index)
        // 卯时05:00
        assertEquals(4, Shichen.fromTime(5, 0).index)
        // 辰时07:00
        assertEquals(5, Shichen.fromTime(7, 0).index)
        // 巳时09:00
        assertEquals(6, Shichen.fromTime(9, 0).index)
        // 午时11:00
        assertEquals(7, Shichen.fromTime(11, 0).index)
        // 未时13:00
        assertEquals(8, Shichen.fromTime(13, 0).index)
        // 酉时17:00
        assertEquals(10, Shichen.fromTime(17, 0).index)
        // 戌时19:00
        assertEquals(11, Shichen.fromTime(19, 0).index)
    }
}