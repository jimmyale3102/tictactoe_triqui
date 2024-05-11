package dev.alejo.triqui.ui.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.alejo.triqui.ui.game.GameScreen
import dev.alejo.triqui.ui.home.HomeScreen
import dev.alejo.triqui.ui.core.Routes.*

@Composable
fun ContentWrapper(navigationController: NavHostController) {
    NavHost(navController = navigationController, startDestination = Home.route ) {
        composable(Home.route) {
            HomeScreen(navigateToGame = { navigationController.navigate(Game.route) })
        }
        composable(Game.route) {
            GameScreen()
        }
    }
}