package com.xiaoliuren.app.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 农历转换与节气月单元测试
 */
class LunarCalendarTest {

    @Test
    fun `支持范围检查`() {
        assertTrue(LunarCalendar.isSupported(1900))
        assertTrue(LunarCalendar.isSupported(2099))
        assertTrue(!LunarCalendar.isSupported(1899))
        assertTrue(!LunarCalendar.isSupported(2100))
    }

    @Test
    fun `2024年元旦 转农历`() {
        // 2024年1月1日 = 癸卯年十一月二十
        val lunar = LunarCalendar.solarToLunar(2024, 1, 1)
        assertNotNull(lunar)
        assertEquals(2023, lunar!!.year)
        assertEquals(11, lunar.month)
        assertEquals(20, lunar.day)
    }

    @Test
    fun `2024年春节 转农历`() {
        // 2024年2月10日 = 甲辰年正月初一
        val lunar = LunarCalendar.solarToLunar(2024, 2, 10)
        assertNotNull(lunar)
        assertEquals(2024, lunar!!.year)
        assertEquals(1, lunar.month)
        assertEquals(1, lunar.day)
    }

    @Test
    fun `干支年验证`() {
        // 2024年 = 甲辰年
        assertEquals("甲辰", LunarCalendar.ganZhiYear(2024))
        // 1900年 = 庚子年
        assertEquals("庚子", LunarCalendar.ganZhiYear(1900))
    }

    @Test
    fun `生肖验证`() {
        assertEquals("龙", LunarCalendar.shengXiao(2024))
        assertEquals("鼠", LunarCalendar.shengXiao(1900))
        assertEquals("兔", LunarCalendar.shengXiao(2023))
    }

    @Test
    fun `闰月验证`() {
        // 2023年有闰二月
        assertEquals(2, LunarCalendar.leapMonth(2023))
        // 2024年无闰月
        assertEquals(0, LunarCalendar.leapMonth(2024))
        // 2025年有闰六月
        assertEquals(6, LunarCalendar.leapMonth(2025))
    }

    @Test
    fun `农历格式化`() {
        val lunar = LunarCalendar.solarToLunar(2024, 2, 10)
        assertNotNull(lunar)
        // 正月初一
        val formatted = LunarCalendar.formatLunar(lunar!!)
        assertTrue(formatted.contains("正月"))
        assertTrue(formatted.contains("初一"))
    }
}