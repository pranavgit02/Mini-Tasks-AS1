package com.example.streamchatdemo.service

import kotlinx.coroutines.delay
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealGreetingService @Inject constructor() : GreetingService {
    override suspend fun greet(name: String): String {
        delay(120) // pretend to hit a real backend
        return "Hello, $name — ${Instant.now()}"
    }
}
