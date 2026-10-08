package com.xiaoliuren.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.xiaoliuren.app.data.HistoryRepository
import com.xiaoliuren.app.data.db.AppDatabase
import com.xiaoliuren.app.ui.navigation.AppNavigation
import com.xiaoliuren.app.ui.theme.XiaoLiuRenTheme
import com.xiaoliuren.app.viewmodel.DivineViewModel
import com.xiaoliuren.app.viewmodel.HistoryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 无参 enableEdgeToEdge() 默认就是 SystemBarStyle.auto(透明, 透明)，
        // 会自动根据系统深浅色切换状态栏/导航栏图标明暗。
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(this)
        val repository = HistoryRepository.fromDatabase(database)

        setContent {
            XiaoLiuRenTheme {
                val divineViewModel: DivineViewModel = viewModel()
                val historyViewModel: HistoryViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer { HistoryViewModel(repository) }
                    }
                )

                // 启动时刷新当前时间
                LaunchedEffect(Unit) {
                    divineViewModel.refreshCurrentTime()
                }

                AppNavigation(
                    divineViewModel = divineViewModel,
                    historyViewModel = historyViewModel,
                    repository = repository
                )
            }
        }
    }
}
