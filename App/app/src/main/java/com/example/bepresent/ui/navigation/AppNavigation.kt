package com.example.bepresent.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bepresent.database.dbManager.DatabaseManager
import com.example.bepresent.ui.screens.*
import com.example.bepresent.viewmodel.AuthViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    database: DatabaseManager,
    authViewModel: AuthViewModel,
    navController: NavHostController = rememberNavController()
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "dashboard" else "login",
        modifier = modifier
    ) {
        // Auth Screens
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    authViewModel.login()
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate("signup")
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                onSignUpSuccess = {
                    authViewModel.login()
                    navController.navigate("dashboard") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Main App Screens
        composable("dashboard") {
            DashboardScreen(
                database = database,
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToBoardSettings = { navController.navigate("board_settings") },
                onNavigateToGiftBoard = { boardId ->
                    navController.navigate("gift_board/$boardId")
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("board_settings") {
            BoardSettingsScreen(
                database = database,
                onNavigateBack = { navController.popBackStack() },
                onBoardCreated = { navController.popBackStack() }
            )
        }

        composable("gift_board/{boardId}") { backStackEntry ->
            val boardId = backStackEntry.arguments?.getString("boardId")?.toIntOrNull() ?: 0
            GiftBoardScreen(
                database = database,
                boardId = boardId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddGift = { navController.navigate("add_gift/$boardId") }
            )
        }

        composable("add_gift/{boardId}") { backStackEntry ->
            val boardId = backStackEntry.arguments?.getString("boardId")?.toIntOrNull() ?: 0
            AddGiftScreen(
                database = database,
                boardId = boardId,
                onNavigateBack = { navController.popBackStack() },
                onGiftAdded = { navController.popBackStack() }
            )
        }
    }
}