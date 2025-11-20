package com.example.taller2.data

import com.example.taller2.models.Message // 👈 AÑADO ESTA LÍNEA
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class GameState(
    // Mapa del UID del jugador a su nombre de usuario
    val players: Map<String, String> = emptyMap(),

    // Mapa del UID del jugador a su emoji secreto
    val playerEmojis: Map<String, String> = emptyMap(),

    // Lista de los UIDs de los jugadores eliminados
    val eliminatedPlayers: List<String> = emptyList(),

    // UID del jugador cuyo turno es
    val currentPlayer: String = "",

    // Mensajes del chat
    val chatMessages: List<Message> = emptyList(),

    // Indica si el juego ha terminado
    val isGameWon: Boolean = false,

    // UID del ganador
    val winningPlayer: String = "",

    // Timestamp de cuándo termina la ronda actual
    val roundEndTime: Long = 0L
)
