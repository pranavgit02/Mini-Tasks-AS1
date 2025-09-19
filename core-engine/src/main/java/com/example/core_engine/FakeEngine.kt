package com.example.coreengine

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * A stub engine: emits 10 tokens at 100 ms intervals.
 * Later you can replace this with a real engine without touching UI.
 */
class FakeEngine : ModelEngine {
    override fun stream(prompt: String): Flow<String> = flow {
        // tiny demo: echo first 3 words, then numbered tokens
        val head = prompt.split(" ").take(3).joinToString(" ")
        if (head.isNotBlank()) {
            emit(head)
            delay(100)
        }
        repeat(10) { i ->
            emit("token_${i + 1}")
            delay(100)
        }
        emit("[end]")
    }
}
