package com.xiaoliuren.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xiaoliuren.app.data.HistoryRepository
import com.xiaoliuren.app.data.db.HistoryRecord
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 历史记录 ViewModel
 */
class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {

    val allRecords: StateFlow<List<HistoryRecord>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteById(id: Long) {
        viewModelScope.launch { repository.deleteById(id) }
    }

    /** 撤销删除：把原记录按原 id 写回去 */
    fun restore(record: HistoryRecord) {
        viewModelScope.launch { repository.restore(record) }
    }

    fun clearAll() {
        viewModelScope.launch { repository.clearAll() }
    }
}