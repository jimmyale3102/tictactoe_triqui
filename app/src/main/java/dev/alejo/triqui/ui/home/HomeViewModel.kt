package dev.alejo.triqui.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alejo.triqui.data.network.FirebaseService
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val firebaseService: FirebaseService) : ViewModel() {

    fun onCreateGame() {

    }

    fun onJoinGame(gameId: String) {

    }

}