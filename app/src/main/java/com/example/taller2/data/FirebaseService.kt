package com.example.taller2.data

import com.example.taller2.models.GameRoom
import com.example.taller2.models.Player
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseService {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("games")
    private val availableEmojis = listOf("😀", "🐶", "🍕", "🚗", "🌈", "👑", "🦄", "🔥", "⚽️", "🎸")

    fun createGameRoom(gameRoom: GameRoom) {
        database.child(gameRoom.roomId).setValue(gameRoom)
    }

    fun listenToGameRoom(gameId: String): Flow<GameRoom?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(GameRoom::class.java))
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        database.child(gameId).addValueEventListener(listener)
        awaitClose { database.child(gameId).removeEventListener(listener) }
    }

    fun updateGameRoom(gameRoom: GameRoom) {
        database.child(gameRoom.roomId).setValue(gameRoom)
    }

    // --- OPERACIONES ATÓMICAS COMPLETAS ---

    suspend fun joinGameAtomically(gameId: String, player: Player): Boolean {
        return suspendCancellableCoroutine { continuation ->
            database.child(gameId).runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val room = currentData.getValue(GameRoom::class.java)
                    if (room == null || room.isGameStarted) {
                        return Transaction.abort()
                    }
                    room.players[player.uid] = player
                    currentData.value = room
                    return Transaction.success(currentData)
                }

                override fun onComplete(error: DatabaseError?, committed: Boolean, data: DataSnapshot?) {
                    if (continuation.isActive) {
                        continuation.resume(error == null && committed)
                    }
                }
            })
        }
    }

    suspend fun leaveGameAtomically(gameId: String, playerId: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            database.child(gameId).runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val room = currentData.getValue(GameRoom::class.java) ?: return Transaction.abort()
                    room.players.remove(playerId)
                    currentData.value = room
                    return Transaction.success(currentData)
                }

                override fun onComplete(error: DatabaseError?, committed: Boolean, data: DataSnapshot?) {
                    if (continuation.isActive) {
                        continuation.resume(error == null && committed)
                    }
                }
            })
        }
    }

    suspend fun startGameAtomically(gameId: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            database.child(gameId).runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val room = currentData.getValue(GameRoom::class.java)
                    if (room == null || room.players.size <= 1) {
                        return Transaction.abort()
                    }

                    val activePlayers = room.players.values.filter { !it.isEliminated }
                    val shuffledEmojis = availableEmojis.shuffled()

                    room.players.values.forEach { player ->
                        player.isEliminated = false
                        val emojiIndex = activePlayers.indexOfFirst { it.uid == player.uid }
                        if (emojiIndex != -1) {
                            player.emoji = shuffledEmojis[emojiIndex % shuffledEmojis.size]
                        }
                    }

                    room.isGameStarted = true
                    room.winnerId = null
                    room.currentPlayerId = activePlayers.first().uid
                    room.roundEndTime = System.currentTimeMillis() + 30000

                    currentData.value = room
                    return Transaction.success(currentData)
                }

                override fun onComplete(error: DatabaseError?, committed: Boolean, data: DataSnapshot?) {
                    if (continuation.isActive) {
                        continuation.resume(error == null && committed)
                    }
                }
            })
        }
    }
}
