package com.example.streamchatdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.streamchatdemo.di.Fake
import com.example.streamchatdemo.di.Real
import com.example.streamchatdemo.service.GreetingService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class GreetingViewModel @Inject constructor(
    @Real private val realService: GreetingService,
    @Fake private val fakeService: GreetingService
) : ViewModel() {

    private val _useFake = MutableStateFlow(false)
    val useFake: StateFlow<Boolean> = _useFake

    private val _greeting = MutableStateFlow<String?>(null)
    val greeting: StateFlow<String?> = _greeting

    fun setUseFake(on: Boolean) {
        _useFake.value = on
    }

    fun testGreet(name: String) {
        viewModelScope.launch {
            val svc = if (_useFake.value) fakeService else realService
            _greeting.value = svc.greet(name.ifBlank { "world" })
        }
    }
}
