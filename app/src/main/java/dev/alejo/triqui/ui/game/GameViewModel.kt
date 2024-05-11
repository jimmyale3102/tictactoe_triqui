package dev.alejo.triqui.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alejo.triqui.data.network.FirebaseService
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private val firebaseService: FirebaseService) : ViewModel() {

    private lateinit var playerId: String

    fun joinToGame(gameId: String, playerId: String, isOwner: Boolean) {
        this.playerId = playerId
        if (isOwner) {
            joinToGameAsOwner(gameId)
        } else {
            joinToGameAsGuest(gameId)
        }
    }

    private fun joinToGameAsOwner(gameId: String) {
        viewModelScope.launch {
            firebaseService.joinToGame(gameId).collect { game ->
                Log.i("Jimmy ->", game.toString())
            }
        }
    }

    private fun joinToGameAsGuest(gameId: String) {
    }

}