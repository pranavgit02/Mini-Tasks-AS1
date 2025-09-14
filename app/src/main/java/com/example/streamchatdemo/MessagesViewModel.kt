package com.example.streamchatdemo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MessagesViewModel : ViewModel() {
    private val _messages = MutableStateFlow(
        (1..50).map { "Fake message #$it" }
    )
    val messages: StateFlow<List<String>> = _messages

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        _messages.value = listOf(text.trim()) + _messages.value
    }
}
