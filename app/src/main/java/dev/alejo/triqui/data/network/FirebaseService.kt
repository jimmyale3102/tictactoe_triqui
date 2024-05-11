package dev.alejo.triqui.data.network

import com.google.firebase.database.DatabaseReference
import dev.alejo.triqui.data.network.model.GameModel
import javax.inject.Inject

class FirebaseService @Inject constructor(private val databaseRef: DatabaseReference) {

    companion object {
        private const val PATH = "games"
    }

    fun createGame(gameData: GameModel) {
        val gameRef = databaseRef.child(PATH).push()
        gameRef.setValue(gameData)
    }
}