package dev.alejo.triqui.ui.game

import dev.alejo.triqui.ui.model.PlayerType

class SinglePlayerMode {

    fun findBestMove(board: MutableList<PlayerType>): Int? {
        var bestScore = Int.MIN_VALUE
        var bestMove: Int? = null
        for (i in board.indices) {
            if (board[i] == PlayerType.Empty) {
                board[i] = PlayerType.Second
                val score = minimax(board, false)
                board[i] = PlayerType.Empty
                if (score > bestScore) {
                    bestScore = score
                    bestMove = i
                }
            }
        }
        return bestMove
    }

    private fun minimax(board: MutableList<PlayerType>, isMaximizing: Boolean): Int {
        val winner = checkWinner(board)
        if (winner != null) {
            return when (winner) {
                PlayerType.Main -> -10
                PlayerType.Second -> 10
                else -> 0
            }
        }
        if (isBoardFull(board)) {
            return 0
        }

        return if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            for (i in board.indices) {
                if (board[i] == PlayerType.Empty) {
                    board[i] = PlayerType.Second
                    val score = minimax(board, false)
                    board[i] = PlayerType.Empty
                    bestScore = maxOf(score, bestScore)
                }
            }
            bestScore
        } else {
            var bestScore = Int.MAX_VALUE
            for (i in board.indices) {
                if (board[i] == PlayerType.Empty) {
                    board[i] = PlayerType.Main
                    val score = minimax(board, true)
                    board[i] = PlayerType.Empty
                    bestScore = minOf(score, bestScore)
                }
            }
            bestScore
        }
    }

    private fun checkWinner(board: List<PlayerType>): PlayerType? {
        val winningCombinations = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6)
        )

        for (combination in winningCombinations) {
            val (a, b, c) = combination
            if (board[a] == board[b] && board[b] == board[c] && board[a] != PlayerType.Empty) {
                return board[a]
            }
        }
        return null
    }

    private fun isBoardFull(board: List<PlayerType>): Boolean {
        return !board.contains(PlayerType.Empty)
    }
}