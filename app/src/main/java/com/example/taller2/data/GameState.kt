package com.example.taller2.data

import com.example.taller2.models.Message // 👈 Added this import for chat messages
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class GameState(

    // Map of player UID to their username
    val players: Map<String, String> = emptyMap(),

    // Map of player UID to their assigned secret emoji
    val playerEmojis: Map<String, String> = emptyMap(),

    // List of UIDs representing players who have been eliminated
    val eliminatedPlayers: List<String> = emptyList(),

    // UID of the player whose turn it currently is
    val currentPlayer: String = "",

    // List of chat messages exchanged during the game
    val chatMessages: List<Message> = emptyList(),

    // Indicates whether the game has been won
    val isGameWon: Boolean = false,

    // UID of the player who won the game
    val winningPlayer: String = "",

    // Timestamp marking when the current round ends
    val roundEndTime: Long = 0L
)
