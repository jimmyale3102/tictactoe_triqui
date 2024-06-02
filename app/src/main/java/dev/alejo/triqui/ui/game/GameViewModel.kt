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
                        secondPlayer = PlayerModel(
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
                    isGameReady = isGameReady(game),
                    isMyTurn = isMyTurn(game.playerTurn)
                )
                _game.value = gameResult
                gameResult?.let {
                    when {
                        (it.mainPlayerPlayAgain && it.secondPlayerPlayAgain) -> resetGameData()
                        (!it.mainPlayerPlayAgain && !it.secondPlayerPlayAgain) -> verifyWinner()
                    }
                }
            }
        }
    }

    private fun isGameReady(game: GameModel) = game.secondPlayer != null || game.singlePlayer

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
                    if (currentGame.singlePlayer) {
                        currentGame.copy(board = boardUpdated).toData()
                    } else {
                        currentGame.copy(
                            board = boardUpdated,
                            playerTurn = getOpponentPlayer()!!
                        ).toData()
                    }
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
                        val mainVictories = _game.value!!.victories.mainPlayer
                        val victoriesUpdated = _game.value!!.victories.copy(
                            mainPlayer = mainVictories + 1
                        )
                        _game.value = _game.value!!.copy(victories = victoriesUpdated)
                        _winner.value = PlayerType.Main
                    }

                    isGameWon(board, PlayerType.Second) -> {
                        val secondVictories = _game.value!!.victories.secondPlayer
                        val victoriesUpdated = _game.value!!.victories.copy(
                            secondPlayer = secondVictories + 1
                        )
                        _game.value = _game.value!!.copy(victories = victoriesUpdated)
                        _winner.value = PlayerType.Second
                    }

                    !boardIsNotComplete -> {
                        val drawVictories = _game.value!!.victories.draw
                        val victoriesUpdated = _game.value!!.victories.copy(
                            draw = drawVictories + 1
                        )
                        _game.value = _game.value!!.copy(victories = victoriesUpdated)
                        _winner.value = PlayerType.Empty
                    }
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
        (game.value?.mainPlayer?.id == playerId) -> PlayerType.Main
        (game.value?.secondPlayer?.id == playerId) -> PlayerType.Second
        else -> null
    }

    private fun getOpponentPlayer(): PlayerModel? {
        return if (game.value?.mainPlayer?.id == playerId) game.value?.secondPlayer else game.value?.mainPlayer
    }

    fun onPlayAgain() {
        viewModelScope.launch {
            val gameUpdated = if (getPlayer() == PlayerType.Main) {
                _game.value!!.copy(
                    mainPlayerPlayAgain = true,
                )
            } else {
                _game.value!!.copy(
                    secondPlayerPlayAgain = true,
                )
            }
            firebaseService.updateGame(gameUpdated.toData())
        }
    }

    private fun resetGameData() {
        _winner.value = null
        val boardUpdated = _game.value!!.board.toMutableList().map { PlayerType.Empty }
        _game.value = _game.value!!.copy(
            board = boardUpdated,
            mainPlayerPlayAgain = false,
            secondPlayerPlayAgain = false
        )
    }

}