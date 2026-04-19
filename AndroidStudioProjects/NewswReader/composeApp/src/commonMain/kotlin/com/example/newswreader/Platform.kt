package com.example.newswreader

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform