package com.xiaoliuren.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.xiaoliuren.app.core.DivineResult
import com.xiaoliuren.app.data.FullJudgment
import com.xiaoliuren.app.data.HistoryRepository
import com.xiaoliuren.app.ui.screens.*
import com.xiaoliuren.app.viewmodel.DivineViewModel
import com.xiaoliuren.app.viewmodel.HistoryViewModel

object Routes {
    const val HOME = "home"
    const val CUSTOM = "custom"
    const val RESULT = "result"
    const val KNOWLEDGE = "knowledge"
    const val KNOWLEDGE_DETAIL = "knowledge_detail/{index}"
    const val HISTORY = "history"
    const val ABOUT = "about"

    fun knowledgeDetail(index: Int) = "knowledge_detail/$index"
}

@Composable
fun AppNavigation(
    divineViewModel: DivineViewModel,
    historyViewModel: HistoryViewModel,
    repository: HistoryRepository
) {
    val navController: NavHostController = rememberNavController()
    val isCalculating by divineViewModel.isCalculating.collectAsState()
    val lastResult by divineViewModel.lastResult.collectAsState()
    val lastJudgment by divineViewModel.lastJudgment.collectAsState()

    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                viewModel = divineViewModel,
                isCalculating = isCalculating,
                onDivineNow = {
                    divineViewModel.divineNow { result, judgment ->
                        divineViewModel.saveToHistory(repository, result, judgment)
                        navController.navigate(Routes.RESULT)
                    }
                },
                onCustomTime = { navController.navigate(Routes.CUSTOM) },
                onKnowledge = { navController.navigate(Routes.KNOWLEDGE) },
                onHistory = { navController.navigate(Routes.HISTORY) },
                onAbout = { navController.navigate(Routes.ABOUT) }
            )
        }

        composable(Routes.CUSTOM) {
            CustomTimeScreen(
                viewModel = divineViewModel,
                isCalculating = isCalculating,
                onBack = { navController.popBackStack() },
                onDivine = { y, m, d, h, min ->
                    divineViewModel.divine(y, m, d, h, min) { result, judgment ->
                        divineViewModel.saveToHistory(repository, result, judgment)
                        navController.navigate(Routes.RESULT)
                    }
                }
            )
        }

        composable(Routes.RESULT) {
            val result = lastResult
            val judgment = lastJudgment
            if (result != null && judgment != null) {
                ResultScreen(
                    result = result,
                    judgment = judgment,
                    onBack = { navController.popBackStack() }
                )
            } else {
                // 无数据时返回首页
                navController.popBackStack()
            }
        }

        composable(Routes.KNOWLEDGE) {
            KnowledgeScreen(
                onBack = { navController.popBackStack() },
                onPalaceClick = { index ->
                    navController.navigate(Routes.knowledgeDetail(index))
                }
            )
        }

        composable(
            route = Routes.KNOWLEDGE_DETAIL,
            arguments = listOf(navArgument("index") { type = NavType.StringType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
            KnowledgeDetailScreen(
                palaceIndex = index,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.HISTORY) {
            HistoryScreen(
                viewModel = historyViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ABOUT) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
    }
}