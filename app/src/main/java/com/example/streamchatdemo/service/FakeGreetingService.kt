package com.example.streamchatdemo.service

import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeGreetingService @Inject constructor() : GreetingService {
    override suspend fun greet(name: String): String {
        delay(30) // fast, predictable
        return "Hi from Fake, $name"
    }
}
