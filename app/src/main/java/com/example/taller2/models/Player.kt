package com.example.taller2.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Player(
    val uid: String = "",
    val name: String = "",
    var emoji: String = "",
    var isEliminated: Boolean = false
)
