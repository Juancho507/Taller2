package com.example.taller2.data

import com.example.taller2.models.GameRoom
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseService {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("games")

    fun createGameRoom(gameRoom: GameRoom) {
        database.child(gameRoom.roomId).setValue(gameRoom)
    }


    fun listenToGameRoom(gameId: String): Flow<GameRoom?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val gameRoom = snapshot.getValue(GameRoom::class.java)
                trySend(gameRoom)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        database.child(gameId).addValueEventListener(listener)
        awaitClose { database.child(gameId).removeEventListener(listener) }
    }


    fun updateGameRoom(gameRoom: GameRoom) {
        database.child(gameRoom.roomId).setValue(gameRoom)
    }


    fun removePlayerFromGame(gameId: String, updatedRoom: GameRoom) {
        database.child(gameId).setValue(updatedRoom)
    }
}
