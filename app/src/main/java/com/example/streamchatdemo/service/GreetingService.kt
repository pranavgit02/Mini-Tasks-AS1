package com.example.streamchatdemo.service

interface GreetingService {
    suspend fun greet(name: String): String
}
