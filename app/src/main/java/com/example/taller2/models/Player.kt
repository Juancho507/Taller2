package com.example.taller2.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Player(

    // Unique identifier of the player (usually Firebase Auth UID)
    val uid: String = "",

    // Display name or username chosen by the player
    val name: String = "",

    // Emoji currently assigned to the player during the round
    var emoji: String = "",

    // Indicates whether the player has been eliminated from the game
    var isEliminated: Boolean = false
)
