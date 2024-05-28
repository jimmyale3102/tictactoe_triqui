package dev.alejo.triqui.data.network

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.snapshots
import dev.alejo.triqui.data.network.model.GameData
import dev.alejo.triqui.data.network.model.toModel
import dev.alejo.triqui.ui.model.GameModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirebaseService @Inject constructor(private val databaseRef: DatabaseReference) {

    companion object {
        private const val PATH = "games"
    }

    fun createGame(gameData: GameData): String {
        val gameRef = databaseRef.child(PATH).push()
        val gameId = gameRef.key
        gameRef.setValue(gameData.copy(gameId = gameId))
        return gameId.orEmpty()
    }

    fun joinToGame(gameId: String): Flow<GameModel?> {
        return databaseRef.database.reference.child("$PATH/$gameId").snapshots.map { dataSnapshot ->
            dataSnapshot.getValue(GameData::class.java)?.toModel()
        }
    }

    fun updateGame(gameData: GameData) {
        Log.d("=======UPDATE->", gameData.toString())
        if (gameData.gameId != null) {
            databaseRef.child(PATH).child(gameData.gameId).setValue(gameData)
        }
    }
}