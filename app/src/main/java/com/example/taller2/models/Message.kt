package com.example.taller2.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class Message(

    val author: String = "",

    val content: String = "",

    val timestamp: Long = 0L

)
