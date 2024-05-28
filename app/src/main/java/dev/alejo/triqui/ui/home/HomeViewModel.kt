package dev.alejo.triqui.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alejo.triqui.data.network.FirebaseService
import dev.alejo.triqui.data.network.model.GameData
import dev.alejo.triqui.data.network.model.PlayerData
import dev.alejo.triqui.ui.model.PlayerType
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseService: FirebaseService
) : ViewModel() {

    fun onCreateGame(navigateToGame: (String, String, Boolean) -> Unit) {
        val game = createNewGame()
        val gameId = firebaseService.createGame(game)
        val userId = game.mainPlayer?.userId.orEmpty()
        val isOwner = true
        navigateToGame(gameId, userId, isOwner)
    }

    fun onJoinGame(gameId: String, navigateToGame: (String, String, Boolean) -> Unit) {
        val isOwner = false
        navigateToGame(gameId, createPlayerId(), isOwner)
    }

    private fun createPlayerId(): String = Calendar.getInstance().timeInMillis.hashCode().toString()

    private fun createNewGame(): GameData {
        val currentPlayer = PlayerData(playerType = PlayerType.Main.id)
        return GameData(
            board = List(9) { 0 },
            mainPlayer = currentPlayer,
            playerTurn = currentPlayer,
            secondPlayer = null
        )
    }

}