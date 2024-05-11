package dev.alejo.triqui.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alejo.triqui.data.network.FirebaseService
import dev.alejo.triqui.data.network.model.GameModel
import dev.alejo.triqui.data.network.model.PlayerData
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseService: FirebaseService
) : ViewModel() {

    fun onCreateGame() {
        firebaseService.createGame(createNewGame())
    }

    private fun createNewGame(): GameModel {
        val currentPlayer = PlayerData(playerType = 1)
        return GameModel(
            board = List(9) { 0 },
            player1 = currentPlayer,
            playerTurn = currentPlayer,
            player2 = null
        )
    }

    fun onJoinGame(gameId: String) {

    }

}