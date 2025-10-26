package com.example.notekeeperapp

data class Note(
    val id: Int = 0,
    var title: String,
    var content: String,
    val timestamp: Long = System.currentTimeMillis()
)