package com.example.taller2.models

import com.google.firebase.database.IgnoreExtraProperties

// Convertido a una clase mutable simple (estilo POJO) para máxima compatibilidad con Firebase.
@IgnoreExtraProperties
class GameRoom {
    var roomId: String = ""
    var players: HashMap<String, Player> = hashMapOf() // Usamos HashMap mutable
    var chat: List<Message> = emptyList() // Las listas son generalmente seguras
    var currentPlayerId: String? = null
    var roundEndTime: Long = 0L
    var winnerId: String? = null
    var isGameStarted: Boolean = false
}
