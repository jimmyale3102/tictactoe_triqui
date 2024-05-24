package dev.alejo.triqui.ui.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.alejo.triqui.ui.game.GameScreen
import dev.alejo.triqui.ui.home.HomeScreen
import dev.alejo.triqui.ui.core.Routes.*

@Composable
fun ContentWrapper(navigationController: NavHostController) {
    NavHost(navController = navigationController, startDestination = Home.route) {
        composable(Home.route) {
            HomeScreen(navigateToGame = { gameId, playerId, isOwner ->
                navigationController.navigate(
                    Game.createRoute(gameId, playerId, isOwner)
                )
            })
        }
        composable(
            Game.route,
            arguments = listOf(
                navArgument("gameId") { type = NavType.StringType },
                navArgument("playerId") { type = NavType.StringType },
                navArgument("isOwner") { type = NavType.BoolType }
            )
        ) {
            GameScreen(
                gameId = it.arguments?.getString("gameId").orEmpty(),
                playerId = it.arguments?.getString("playerId").orEmpty(),
                isOwner = it.arguments?.getBoolean("isOwner") ?: false
            ) {
                navigationController.popBackStack()
            }
        }
    }
}