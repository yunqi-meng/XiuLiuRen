package com.xiaoliuren.app.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 节气月计算单元测试
 */
class SolarTermsTest {

    @Test
    fun `立春后属于正月`() {
        // 2024年立春约2月4日，2月10日应在立春后
        val month = SolarTerms.getJieQiMonth(2024, 2, 10)
        assertEquals(1, month) // 正月
    }

    @Test
    fun `小寒前属于十一月`() {
        // 2024年1月3日，小寒约1月6日，应属大雪后的子月(十一月)
        val month = SolarTerms.getJieQiMonth(2024, 1, 3)
        assertEquals(11, month)
    }

    @Test
    fun `小寒后属于十二月`() {
        // 2024年1月10日，小寒约1月6日，应属丑月(十二月)
        val month = SolarTerms.getJieQiMonth(2024, 1, 10)
        assertEquals(12, month)
    }

    @Test
    fun `惊蛰后属于二月`() {
        // 2024年惊蛰约3月5日，3月10日应属二月
        val month = SolarTerms.getJieQiMonth(2024, 3, 10)
        assertEquals(2, month)
    }

    @Test
    fun `冬至后属于十一月`() {
        // 2024年冬至约12月21日，12月25日应属十一月(子月)
        val month = SolarTerms.getJieQiMonth(2024, 12, 25)
        assertEquals(11, month)
    }

    @Test
    fun `节气日期合理范围`() {
        // 立春应在2月3-5日
        val (m, d) = SolarTerms.getSolarTerm(2024, 2) // 立春
        assertEquals(2, m)
        assertTrue("立春日期: $d", d in 3..5)
    }

    @Test
    fun `小寒日期合理范围`() {
        // 小寒应在1月5-7日
        val (m, d) = SolarTerms.getSolarTerm(2024, 0) // 小寒
        assertEquals(1, m)
        assertTrue("小寒日期: $d", d in 5..7)
    }

    @Test
    fun `冬至日期合理范围`() {
        // 冬至应在12月21-23日
        val (m, d) = SolarTerms.getSolarTerm(2024, 23) // 冬至
        assertEquals(12, m)
        assertTrue("冬至日期: $d", d in 21..23)
    }
}