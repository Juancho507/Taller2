package com.example.taller2.data

import com.example.taller2.models.GameRoom
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseService {

    // Reference to the "games" node in Firebase Realtime Database
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("games")

    // Creates a new game room in the database using its roomId as the key
    fun createGameRoom(gameRoom: GameRoom) {
        database.child(gameRoom.roomId).setValue(gameRoom)
    }

    // Listens for real-time updates to a specific game room
    fun listenToGameRoom(gameId: String): Flow<GameRoom?> = callbackFlow {
        val listener = object : ValueEventListener {

            // Called whenever the data at the specified node changes
            override fun onDataChange(snapshot: DataSnapshot) {
                val gameRoom = snapshot.getValue(GameRoom::class.java)
                trySend(gameRoom) // Sends the updated room through the Flow
            }

            // Called if the listener is cancelled or an error occurs
            override fun onCancelled(error: DatabaseError) {
                close(error.toException()) // Closes the Flow with the error
            }
        }

        // Attach the listener to the game room node
        database.child(gameId).addValueEventListener(listener)

        // Remove the listener when the Flow is closed (to avoid memory leaks)
        awaitClose { database.child(gameId).removeEventListener(listener) }
    }

    // Updates the entire game room with the new GameRoom object
    fun updateGameRoom(gameRoom: GameRoom) {
        database.child(gameRoom.roomId).setValue(gameRoom)
    }

    // Removes a player by updating the game room with the modified room state
    fun removePlayerFromGame(gameId: String, updatedRoom: GameRoom) {
        database.child(gameId).setValue(updatedRoom)
    }
}
