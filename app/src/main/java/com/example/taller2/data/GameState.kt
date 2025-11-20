package com.example.taller2.data

import com.example.taller2.models.Message // 👈 AÑADO ESTA LÍNEA
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class GameState(

    val players: Map<String, String> = emptyMap(),


    val playerEmojis: Map<String, String> = emptyMap(),


    val eliminatedPlayers: List<String> = emptyList(),


    val currentPlayer: String = "",


    val chatMessages: List<Message> = emptyList(),


    val isGameWon: Boolean = false,


    val winningPlayer: String = "",


    val roundEndTime: Long = 0L
)
