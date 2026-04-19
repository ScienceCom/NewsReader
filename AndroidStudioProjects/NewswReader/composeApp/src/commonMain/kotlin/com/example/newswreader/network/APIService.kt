package com.example.newswreader.network

import com.example.newswreader.model.Note
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ApiService {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getPosts(): List<Note> {
        return client.get("https://jsonplaceholder.typicode.com/posts").body()
    }
}