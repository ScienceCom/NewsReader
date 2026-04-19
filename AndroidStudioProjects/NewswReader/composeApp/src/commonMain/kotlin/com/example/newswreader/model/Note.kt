package com.example.newswreader.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int
)