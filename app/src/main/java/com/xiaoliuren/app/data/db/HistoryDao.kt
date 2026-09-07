package com.xiaoliuren.app.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * 历史记录数据访问对象
 */
@Dao
interface HistoryDao {

    @Query("SELECT * FROM history_records ORDER BY timestamp DESC")
    fun getAll(): Flow<List<HistoryRecord>>

    @Query("SELECT * FROM history_records WHERE id = :id")
    suspend fun getById(id: Long): HistoryRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: HistoryRecord): Long

    @Delete
    suspend fun delete(record: HistoryRecord)

    @Query("DELETE FROM history_records WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM history_records")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM history_records")
    suspend fun count(): Int
}