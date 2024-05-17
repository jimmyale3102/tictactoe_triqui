package dev.alejo.triqui.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alejo.triqui.data.network.FirebaseService
import dev.alejo.triqui.ui.model.GameModel
import dev.alejo.triqui.ui.model.PlayerModel
import dev.alejo.triqui.ui.model.PlayerType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private val firebaseService: FirebaseService) :
    ViewModel() {

    private lateinit var playerId: String
    private val _game = MutableStateFlow<GameModel?>(null)
    val game: StateFlow<GameModel?> = _game

    fun joinToGame(gameId: String, playerId: String, isOwner: Boolean) {
        this.playerId = playerId
        if (isOwner) {
            join(gameId)
        } else {
            joinToGameAsGuest(gameId)
        }
    }

    private fun joinToGameAsGuest(gameId: String) {
        viewModelScope.launch {

            firebaseService.joinToGame(gameId).take(1).collect { game ->
                game?.let {
                    val gameResult = it.copy(
                        player2 = PlayerModel(
                            id = playerId,
                            playerType = PlayerType.PlayerGuest
                        )
                    )
                    firebaseService.updateGame(gameResult.toData())
                }
            }

            join(gameId)
        }
    }

    private fun join(gameId: String) {
        viewModelScope.launch {
            firebaseService.joinToGame(gameId).collect { game ->
                val gameResult = game?.copy(isGameReady = game.player2 != null, isMyTurn = isMyTurn())
                _game.value = gameResult
            }
        }
    }

    private fun isMyTurn() = game.value?.playerTurn?.id == playerId

    fun updateGame(position: Int) {
        val currentGame = _game.value ?: return
        if (currentGame.isGameReady && currentGame.board[position] == PlayerType.Empty && isMyTurn()) {
            viewModelScope.launch {
                val boardUpdated = currentGame.board.toMutableList()
                boardUpdated[position] = getPlayer() ?: PlayerType.Empty

                firebaseService.updateGame(
                    currentGame.copy(
                        board = boardUpdated,
                        playerTurn = getOpponentPlayer()!!
                    ).toData()
                )
            }
        }
    }

    private fun getPlayer(): PlayerType? = when {
        (game.value?.player1?.id == playerId) -> PlayerType.PlayerOwner
        (game.value?.player2?.id == playerId) -> PlayerType.PlayerGuest
        else -> null
    }

    private fun getOpponentPlayer(): PlayerModel? {
        return if (game.value?.player1?.id == playerId) game.value?.player2 else game.value?.player1
    }

}