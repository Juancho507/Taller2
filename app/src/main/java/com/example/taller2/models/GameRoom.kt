package com.example.taller2.models

import com.google.firebase.database.IgnoreExtraProperties

// Pure data class with no game logic, designed to be fully compatible with Firebase.
@IgnoreExtraProperties
data class GameRoom(

    // Unique identifier for the game room
    val roomId: String = "",

    // Map of playerId -> Player object representing all players in the room
    val players: Map<String, Player> = emptyMap(),

    // List of chat messages exchanged in the room
    val chat: List<Message> = emptyList(),

    // --- Game state fields ---

    // Map of playerId -> emoji assigned to them for the current round
    val playerEmojis: Map<String, String> = emptyMap(),

    // List of players who correctly guessed their assigned emoji
    val correctPlayers: List<String> = emptyList(),

    // List of players eliminated at the end of the round
    val eliminatedPlayers: List<String> = emptyList(),

    // Current round number of the game
    val roundNumber: Int = 1,

    // Timestamp indicating when the current round ends
    val roundEndTime: Long = 0L,

    // ID of the winner (null if there is no winner yet)
    val winnerId: String? = null,

    // Indicates whether the game has officially started
    val isGameStarted: Boolean = false
)
