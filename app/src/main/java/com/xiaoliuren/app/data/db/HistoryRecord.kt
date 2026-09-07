package com.xiaoliuren.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 历史记录实体
 */
@Entity(tableName = "history_records")
data class HistoryRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,          // 测算时间戳
    val solarTime: String,        // 测算用公历时间
    val lunarInfo: String,        // 对应农历信息
    val jieqiMonth: String,       // 节气月
    val shichenName: String,      // 时辰
    val monthPalace: String,      // 月宫名称
    val dayPalace: String,        // 日宫名称
    val timePalace: String,       // 时宫名称
    val resultLevel: Int,         // 吉凶等级 0-4
    val comprehensive: String     // 综合断语
)