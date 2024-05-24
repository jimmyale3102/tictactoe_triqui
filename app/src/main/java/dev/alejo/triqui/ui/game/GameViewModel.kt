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
    private val _winner = MutableStateFlow<PlayerType?>(null)
    val winner: StateFlow<PlayerType?> = _winner

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
                            playerType = PlayerType.Second
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
                val gameResult = game?.copy(
                    isGameReady = game.player2 != null,
                    isMyTurn = isMyTurn(game.playerTurn)
                )
                _game.value = gameResult
                gameResult?.let {
                    if (it.player1PlayAgain && it.player2PlayAgain) {
                        onRestartGame()
                    }
                    verifyWinner()
                }
            }
        }
    }

    private fun isMyTurn(playerTurn: PlayerModel) = playerTurn.id == playerId

    fun updateGame(position: Int) {
        val currentGame = _game.value ?: return
        if (currentGame.isGameReady
            && currentGame.board[position] == PlayerType.Empty && isMyTurn(currentGame.playerTurn)
        ) {
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

    private fun verifyWinner() {
        val board: List<PlayerType>? = game.value?.board
        board?.let { boardData ->
            val boardIsNotComplete = boardData.any { it.id == PlayerType.Empty.id }
            if (boardData.size == 9) {
                when {
                    isGameWon(board, PlayerType.Main) -> {
                        _winner.value = PlayerType.Main
                    }

                    isGameWon(board, PlayerType.Second) -> {
                        _winner.value = PlayerType.Second
                    }

                    !boardIsNotComplete -> _winner.value = PlayerType.Empty
                }
            }
        } ?: return

    }

    private fun isGameWon(board: List<PlayerType>, playerType: PlayerType): Boolean {
        return when {
            // Row
            (board[0] == playerType && board[1] == playerType && board[2] == playerType) -> true
            (board[3] == playerType && board[4] == playerType && board[5] == playerType) -> true
            (board[6] == playerType && board[7] == playerType && board[8] == playerType) -> true

            // Column
            (board[0] == playerType && board[3] == playerType && board[6] == playerType) -> true
            (board[1] == playerType && board[4] == playerType && board[7] == playerType) -> true
            (board[2] == playerType && board[5] == playerType && board[8] == playerType) -> true

            // Diagonal
            (board[0] == playerType && board[4] == playerType && board[8] == playerType) -> true
            (board[2] == playerType && board[4] == playerType && board[6] == playerType) -> true

            else -> false
        }
    }

    fun getPlayer(): PlayerType? = when {
        (game.value?.player1?.id == playerId) -> PlayerType.Main
        (game.value?.player2?.id == playerId) -> PlayerType.Second
        else -> null
    }

    private fun getOpponentPlayer(): PlayerModel? {
        return if (game.value?.player1?.id == playerId) game.value?.player2 else game.value?.player1
    }

    fun onPlayAgain() {
        viewModelScope.launch {
            val gameUpdated = if (getPlayer() == PlayerType.Main) {
                game.value!!.copy(
                    player1PlayAgain = true,
                )
            } else {
                game.value!!.copy(
                    player2PlayAgain = true,
                )
            }
            firebaseService.updateGame(gameUpdated.toData())
        }
    }

    private fun onRestartGame() {
        _winner.value = null
        val boardUpdated = _game.value!!.board.toMutableList().map { PlayerType.Empty }
        _game.value = _game.value!!.copy(
            board = boardUpdated,
            player1PlayAgain = false,
            player2PlayAgain = false
        )
    }

}