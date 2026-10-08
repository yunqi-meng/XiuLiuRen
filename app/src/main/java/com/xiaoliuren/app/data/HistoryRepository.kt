package com.xiaoliuren.app.data

import com.xiaoliuren.app.core.DivineResult
import com.xiaoliuren.app.core.DivineEngine
import com.xiaoliuren.app.core.LunarCalendar
import com.xiaoliuren.app.core.Shichen
import com.xiaoliuren.app.data.db.AppDatabase
import com.xiaoliuren.app.data.db.HistoryDao
import com.xiaoliuren.app.data.db.HistoryRecord
import kotlinx.coroutines.flow.Flow

/**
 * 历史记录仓库
 */
class HistoryRepository(private val dao: HistoryDao) {

    fun getAll(): Flow<List<HistoryRecord>> = dao.getAll()

    suspend fun getById(id: Long): HistoryRecord? = dao.getById(id)

    suspend fun insert(result: DivineResult, judgment: FullJudgment, divineTime: Long): Long {
        val record = HistoryRecord(
            timestamp = divineTime,
            solarTime = formatSolarTime(result),
            lunarInfo = formatLunarInfo(result),
            jieqiMonth = DivineEngine.jieqiMonthFullName(result.jieqiMonth),
            shichenName = Shichen.nameWithZhi(result.shichenIndex),
            monthPalace = judgment.monthPalace.palaceName,
            dayPalace = judgment.dayPalace.palaceName,
            timePalace = judgment.timePalace.palaceName,
            resultLevel = judgment.timePalace.jiXiongLevel,
            comprehensive = judgment.comprehensive
        )
        return dao.insert(record)
    }

    suspend fun deleteById(id: Long) = dao.deleteById(id)

    /**
     * 恢复一条被删除的记录（用于「撤销删除」）。
     * DAO 的 insert 是 REPLACE 策略，因此原 id 会原样写回。
     */
    suspend fun restore(record: HistoryRecord) = dao.insert(record)

    suspend fun clearAll() = dao.clearAll()

    private fun formatSolarTime(result: DivineResult): String {
        return "${result.solarYear}年${result.solarMonth}月${result.solarDay}日 ${String.format("%02d:%02d", result.solarHour, result.solarMinute)}"
    }

    private fun formatLunarInfo(result: DivineResult): String {
        val lunar = result.lunarDate ?: return "暂不支持"
        return LunarCalendar.formatLunar(lunar)
    }

    companion object {
        fun fromDatabase(db: AppDatabase): HistoryRepository {
            return HistoryRepository(db.historyDao())
        }
    }
}