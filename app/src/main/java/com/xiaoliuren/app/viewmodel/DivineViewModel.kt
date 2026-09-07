package com.xiaoliuren.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xiaoliuren.app.core.DivineEngine
import com.xiaoliuren.app.core.DivineResult
import com.xiaoliuren.app.core.LunarCalendar
import com.xiaoliuren.app.core.Shichen
import com.xiaoliuren.app.data.CurrentTimeInfo
import com.xiaoliuren.app.data.FullJudgment
import com.xiaoliuren.app.data.HistoryRepository
import com.xiaoliuren.app.data.JudgmentGenerator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

/**
 * 起课 ViewModel
 */
class DivineViewModel : ViewModel() {

    private val _currentTimeInfo = MutableStateFlow<CurrentTimeInfo?>(null)
    val currentTimeInfo: StateFlow<CurrentTimeInfo?> = _currentTimeInfo.asStateFlow()

    private val _isCalculating = MutableStateFlow(false)
    val isCalculating: StateFlow<Boolean> = _isCalculating.asStateFlow()

    private val _lastResult = MutableStateFlow<DivineResult?>(null)
    val lastResult: StateFlow<DivineResult?> = _lastResult.asStateFlow()

    private val _lastJudgment = MutableStateFlow<FullJudgment?>(null)
    val lastJudgment: StateFlow<FullJudgment?> = _lastJudgment.asStateFlow()

    /** 刷新当前时间信息 */
    fun refreshCurrentTime() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"))
        val info = buildTimeInfo(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE)
        )
        _currentTimeInfo.value = info
    }

    /** 根据指定时间构建时间信息（用于自定义时间预览） */
    fun buildTimeInfo(year: Int, month: Int, day: Int, hour: Int, minute: Int): CurrentTimeInfo {
        val shichenResult = Shichen.fromTime(hour, minute)

        var sy = year
        var sm = month
        var sd = day
        if (shichenResult.crossNextDay) {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).apply {
                set(year, month - 1, day, hour, minute)
                add(Calendar.DAY_OF_MONTH, 1)
            }
            sy = cal.get(Calendar.YEAR)
            sm = cal.get(Calendar.MONTH) + 1
            sd = cal.get(Calendar.DAY_OF_MONTH)
        }

        val supported = LunarCalendar.isSupported(sy)
        val lunarDate = if (supported) LunarCalendar.solarToLunar(sy, sm, sd) else null
        val jieqiMonth = if (supported && lunarDate != null) {
            com.xiaoliuren.app.core.SolarTerms.getJieQiMonth(sy, sm, sd)
        } else 0

        return CurrentTimeInfo(
            solarYear = sy,
            solarMonth = sm,
            solarDay = sd,
            solarHour = hour,
            solarMinute = minute,
            lunarDate = lunarDate,
            jieqiMonth = jieqiMonth,
            jieqiMonthName = if (jieqiMonth > 0) DivineEngine.jieqiMonthFullName(jieqiMonth) else "暂不支持",
            shichenIndex = shichenResult.index,
            shichenName = Shichen.nameWithZhi(shichenResult.index),
            shichenTimeRange = Shichen.timeRange(shichenResult.index),
            supported = supported && lunarDate != null
        )
    }

    /**
     * 一键起课（当前时间）
     * @param onResult 起课完成回调（返回结果与解读）
     */
    fun divineNow(onResult: (DivineResult, FullJudgment) -> Unit) {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"))
        divine(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            onResult
        )
    }

    /**
     * 自定义时间起课
     */
    fun divine(
        year: Int, month: Int, day: Int, hour: Int, minute: Int,
        onResult: (DivineResult, FullJudgment) -> Unit
    ) {
        viewModelScope.launch {
            _isCalculating.value = true
            // 模拟"掐指推算"仪式感
            delay(800)
            val result = DivineEngine.divine(year, month, day, hour, minute)
            val judgment = if (result.supported) {
                JudgmentGenerator.buildFullJudgment(result)
            } else {
                FullJudgment(
                    monthPalace = com.xiaoliuren.app.data.LiuRenPalace.DA_AN,
                    dayPalace = com.xiaoliuren.app.data.LiuRenPalace.DA_AN,
                    timePalace = com.xiaoliuren.app.data.LiuRenPalace.DA_AN,
                    comprehensive = "暂不支持该时间范围（仅支持1900-2100年）",
                    timing = "",
                    wuxingRelation = ""
                )
            }
            _lastResult.value = result
            _lastJudgment.value = judgment
            _isCalculating.value = false
            onResult(result, judgment)
        }
    }

    /** 保存到历史记录 */
    fun saveToHistory(repository: HistoryRepository, result: DivineResult, judgment: FullJudgment) {
        viewModelScope.launch {
            repository.insert(result, judgment, System.currentTimeMillis())
        }
    }
}