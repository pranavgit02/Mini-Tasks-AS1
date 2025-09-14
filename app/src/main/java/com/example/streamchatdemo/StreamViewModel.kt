package com.example.streamchatdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class StreamViewModel : ViewModel() {

    private val _tokens = MutableStateFlow<List<String>>(emptyList())
    val tokens: StateFlow<List<String>> = _tokens

    private var genJob: Job? = null

    fun startStreaming(text: String) {
        cancelStreaming()            // stop any previous run
        _tokens.value = emptyList()

        genJob = viewModelScope.launch {
            flow {
                text.split(" ").forEach { w ->
                    delay(200)       // simulate streaming delay
                    emit(w)
                }
            }
                // Try these later to feel backpressure:
                //.buffer()
                //.conflate()
                //.collectLatest { w -> delay(150); _tokens.value = _tokens.value + w }

                .collect { w ->
                    _tokens.value = _tokens.value + w
                }
        }
    }

    fun cancelStreaming() {
        genJob?.cancel()
        genJob = null
    }
}
