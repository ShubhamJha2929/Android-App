package com.aksharadeepa.tutor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aksharadeepa.tutor.di.AppContainer
import com.aksharadeepa.tutor.ui.screens.DashboardScreen
import com.aksharadeepa.tutor.ui.screens.GapAnalysisScreen
import com.aksharadeepa.tutor.ui.screens.QuizListScreen
import com.aksharadeepa.tutor.ui.screens.QuizScreen
import com.aksharadeepa.tutor.ui.screens.SplashScreen
import com.aksharadeepa.tutor.ui.screens.StrengthScreen
import com.aksharadeepa.tutor.ui.screens.SyllabusScreen
import com.aksharadeepa.tutor.ui.viewmodel.DashboardViewModel
import com.aksharadeepa.tutor.ui.viewmodel.QuizListViewModel
import com.aksharadeepa.tutor.ui.viewmodel.QuizViewModel
import com.aksharadeepa.tutor.ui.viewmodel.StrengthViewModel
import com.aksharadeepa.tutor.ui.viewmodel.SyllabusViewModel

@Composable
fun AppNavGraph(appContainer: AppContainer) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoutes.SPLASH) {
        composable(NavRoutes.SPLASH) {
            SplashScreen(
                onFinished = {
                    navController.navigate(NavRoutes.DASHBOARD) {
                        popUpTo(NavRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.DASHBOARD) {
            val viewModel: DashboardViewModel = viewModel(
                factory = DashboardViewModel.Factory(
                    appContainer.syllabusRepository,
                    appContainer.dailyGoalRepository
                )
            )
            DashboardScreen(
                viewModel = viewModel,
                onNavigateSyllabus = { subjectId ->
                    navController.navigate(NavRoutes.syllabus(subjectId))
                },
                onNavigateQuizList = { navController.navigate(NavRoutes.QUIZ_LIST) },
                onNavigateStrength = { navController.navigate(NavRoutes.STRENGTH) },
                onNavigateGapAnalysis = { navController.navigate(NavRoutes.GAP_ANALYSIS) }
            )
        }

        composable(
            route = NavRoutes.SYLLABUS,
            arguments = listOf(navArgument("subjectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val subjectId = backStackEntry.arguments?.getLong("subjectId") ?: 1L
            val viewModel: SyllabusViewModel = viewModel(
                factory = SyllabusViewModel.Factory(appContainer.syllabusRepository, subjectId)
            )
            SyllabusScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onStartQuiz = { chapterId, subId ->
                    navController.navigate(NavRoutes.quiz(chapterId, subId))
                }
            )
        }

        composable(NavRoutes.QUIZ_LIST) {
            val viewModel: QuizListViewModel = viewModel(
                factory = QuizListViewModel.Factory(appContainer.syllabusRepository)
            )
            QuizListScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onStartQuiz = { chapterId, subjectId ->
                    navController.navigate(NavRoutes.quiz(chapterId, subjectId))
                }
            )
        }

        composable(
            route = NavRoutes.QUIZ,
            arguments = listOf(
                navArgument("chapterId") { type = NavType.LongType },
                navArgument("subjectId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getLong("chapterId") ?: 0L
            val subjectId = backStackEntry.arguments?.getLong("subjectId") ?: 1L
            val viewModel: QuizViewModel = viewModel(
                factory = QuizViewModel.Factory(
                    appContainer.quizRepository,
                    chapterId,
                    subjectId
                )
            )
            QuizScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onQuizFinished = {
                    navController.navigate(NavRoutes.DASHBOARD) {
                        popUpTo(NavRoutes.DASHBOARD) { inclusive = false }
                    }
                }
            )
        }

        composable(NavRoutes.STRENGTH) {
            val viewModel: StrengthViewModel = viewModel(
                factory = StrengthViewModel.Factory(appContainer.strengthRepository)
            )
            StrengthScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.GAP_ANALYSIS) {
            val viewModel: StrengthViewModel = viewModel(
                factory = StrengthViewModel.Factory(appContainer.strengthRepository)
            )
            GapAnalysisScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
