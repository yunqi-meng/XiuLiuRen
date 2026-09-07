package com.xiaoliuren.app.data

import com.xiaoliuren.app.core.DivineResult

/**
 * 综合断语生成器
 *
 * 结合月宫（起因）+ 日宫（过程）+ 时宫（结果）
 * 生成连贯自然的整体解读与应期判断
 */
object JudgmentGenerator {

    /** 各宫位在综合断语中的特质描述 */
    private val traitText = mapOf(
        0 to "安稳停滞、守成平顺",
        1 to "拖延纠缠、悬而未决",
        2 to "快速喜庆、消息立至",
        3 to "口舌争执、破损损耗",
        4 to "人际和合、得人相助",
        5 to "虚空落空、徒劳无功"
    )

    /** 各宫位的结果走向描述 */
    private val resultText = mapOf(
        0 to "事情趋于平稳，宜静守等待，不宜主动变动",
        1 to "事情仍会反复拖延，短期内难有明确结果",
        2 to "事情很快会有喜讯，进展迅速，宜趁热打铁",
        3 to "事情易生口舌争执，需防小人阻碍与破损",
        4 to "事情可借他人之力小范围促成，宜沟通求助",
        5 to "事情恐有名无实，落空难成，宜及时止损"
    )

    /** 各宫位的应对建议 */
    private val adviceText = mapOf(
        0 to "建议保持现状、耐心等待，主动冒进反而损耗。",
        1 to "建议缓处理、勿催促，放一放反而更容易有转机。",
        2 to "建议立即行动、趁热打铁，错过时机则吉气消退。",
        3 to "建议低调忍让、避免争执，防小人、防破财。",
        4 to "建议多沟通协商、借助他人之力，外出办事更顺。",
        5 to "建议及时止损、放弃执念，切勿继续投入。"
    )

    /**
     * 生成综合断语
     * @param monthPalace 月宫
     * @param dayPalace 日宫
     * @param timePalace 时宫
     * @return 综合断语文本
     */
    fun generate(
        monthPalace: LiuRenPalace,
        dayPalace: LiuRenPalace,
        timePalace: LiuRenPalace
    ): String {
        val cause = traitText[monthPalace.index]!!
        val process = traitText[dayPalace.index]!!
        val result = resultText[timePalace.index]!!
        val advice = adviceText[timePalace.index]!!

        return buildString {
            append("此事起因带有「${cause}」的特质，")
            append("发展过程中容易出现「${process}」的情况，")
            append("最终结果：${result}。")
            append(advice)
        }
    }

    /**
     * 生成应期判断
     * @param timePalace 时宫
     * @return 应期描述
     */
    fun generateTiming(timePalace: LiuRenPalace): String {
        return when (timePalace.index) {
            0 -> "大安主慢，应期多以天数、周数计，对应数字 ${timePalace.numbers.joinToString("、")}。"
            1 -> "留连主慢且反复，应期多以天数、周数计，对应数字 ${timePalace.numbers.joinToString("、")}。"
            2 -> "速喜主快，应期多以时辰、天数计，对应数字 ${timePalace.numbers.joinToString("、")}。"
            3 -> "赤口主速变，应期多以时辰、天数计，对应数字 ${timePalace.numbers.joinToString("、")}。"
            4 -> "小吉主较快，应期多以时辰、天数计，对应数字 ${timePalace.numbers.joinToString("、")}。"
            5 -> "空亡主落空，应期不定，对应数字 ${timePalace.numbers.joinToString("、")}。"
            else -> ""
        }
    }

    /**
     * 生成五行生克分析（进阶）
     * @param dayPalace 日宫
     * @param timePalace 时宫
     * @return 五行关系描述（可空）
     */
    fun generateWuxingRelation(dayPalace: LiuRenPalace, timePalace: LiuRenPalace): String {
        val dayWx = wuxingElement(dayPalace.wuxing)
        val timeWx = wuxingElement(timePalace.wuxing)
        if (dayWx < 0 || timeWx < 0) return ""

        // 五行相生：木生火、火生土、土生金、金生水、水生木
        val sheng = intArrayOf(1, 2, 3, 4, 0) // 0木 1火 2土 3金 4水 -> 所生
        // 五行相克：木克土、火克金、土克水、金克木、水克火
        val ke = intArrayOf(2, 3, 4, 0, 1) // 0木 1火 2土 3金 4水 -> 所克

        return when {
            sheng[dayWx] == timeWx -> "日宫生时宫（${dayPalace.wuxing}生${timePalace.wuxing}），过程顺利，结果得助力，吉上加吉。"
            ke[dayWx] == timeWx -> "日宫克时宫（${dayPalace.wuxing}克${timePalace.wuxing}），过程阻碍大，结果打折扣，吉变弱、凶加重。"
            sheng[timeWx] == dayWx -> "时宫生日宫（${timePalace.wuxing}生${dayPalace.wuxing}），结果耗费自身力量，成事但有损耗。"
            ke[timeWx] == dayWx -> "时宫克日宫（${timePalace.wuxing}克${dayPalace.wuxing}），结局能突破阻碍，虽过程难但最终可控。"
            dayWx == timeWx -> "日时同属${dayPalace.wuxing.takeLast(1)}行，力量持平，按本宫象义直断。"
            else -> ""
        }
    }

    /** 五行转元素索引：0木 1火 2土 3金 4水 */
    private fun wuxingElement(wuxing: String): Int {
        val ch = wuxing.takeLast(1)
        return when (ch) {
            "木" -> 0
            "火" -> 1
            "土" -> 2
            "金" -> 3
            "水" -> 4
            else -> -1
        }
    }

    /**
     * 生成完整解读结果
     */
    fun buildFullJudgment(result: DivineResult): FullJudgment {
        val monthPalace = LiuRenPalace.fromIndex(result.monthPalaceIndex)
        val dayPalace = LiuRenPalace.fromIndex(result.dayPalaceIndex)
        val timePalace = LiuRenPalace.fromIndex(result.timePalaceIndex)

        return FullJudgment(
            monthPalace = monthPalace,
            dayPalace = dayPalace,
            timePalace = timePalace,
            comprehensive = generate(monthPalace, dayPalace, timePalace),
            timing = generateTiming(timePalace),
            wuxingRelation = generateWuxingRelation(dayPalace, timePalace)
        )
    }
}

/** 完整解读结果 */
data class FullJudgment(
    val monthPalace: LiuRenPalace,
    val dayPalace: LiuRenPalace,
    val timePalace: LiuRenPalace,
    val comprehensive: String,      // 综合断语
    val timing: String,             // 应期判断
    val wuxingRelation: String      // 五行生克分析
)