package com.example.streamchatdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coreengine.ModelEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EngineViewModel @Inject constructor(
    private val engine: ModelEngine
) : ViewModel() {

    private val _tokens = MutableStateFlow<List<String>>(emptyList())
    val tokens: StateFlow<List<String>> = _tokens

    private var job: Job? = null

    fun start(prompt: String) {
        cancel()
        _tokens.value = emptyList()
        job = viewModelScope.launch {
            engine.stream(prompt).collect { t ->
                _tokens.value = _tokens.value + t
            }
        }
    }

    fun cancel() {
        job?.cancel()
        job = null
    }
}
