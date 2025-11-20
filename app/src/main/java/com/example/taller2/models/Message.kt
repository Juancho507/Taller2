package com.example.taller2.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(

    // UID or username of the player who sent the message
    val author: String = "",

    // Actual text content of the chat message
    val content: String = "",

    // Timestamp (in milliseconds) when the message was sent
    val timestamp: Long = 0L
)
