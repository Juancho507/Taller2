package com.example.taller2.models

import com.google.firebase.database.IgnoreExtraProperties

// Clase de datos pura, sin lógica, para ser compatible con Firebase.
@IgnoreExtraProperties
data class GameRoom(
    val roomId: String = "",
    val players: Map<String, Player> = emptyMap(),
    val chat: List<Message> = emptyList(),

    // Nuevo:
    val playerEmojis: Map<String, String> = emptyMap(),
    val correctPlayers: List<String> = emptyList(),
    val eliminatedPlayers: List<String> = emptyList(),
    val roundNumber: Int = 1,
    val roundEndTime: Long = 0L,
    val winnerId: String? = null,
    val isGameStarted: Boolean = false
)