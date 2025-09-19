package com.example.coreengine

import kotlinx.coroutines.flow.Flow

/**
 * Minimal interface every engine will implement.
 * stream(prompt) should emit tokens as they are generated.
 */
interface ModelEngine {
    fun stream(prompt: String): Flow<String>
}
