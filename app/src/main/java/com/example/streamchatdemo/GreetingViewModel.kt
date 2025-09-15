package com.example.streamchatdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.streamchatdemo.service.GreetingService
import com.example.streamchatdemo.service.SettingsRepository
import com.example.streamchatdemo.di.Fake
import com.example.streamchatdemo.di.Real
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class GreetingViewModel @Inject constructor(
    private val settings: SettingsRepository,
    @Real private val realService: GreetingService,
    @Fake private val fakeService: GreetingService,
) : ViewModel() {

    // Persisted settings
    val useFake = settings.useFakeFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val lastName = settings.lastNameFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, "world")

    // UI state
    private val _greeting = MutableStateFlow<String?>(null)
    val greeting: StateFlow<String?> = _greeting

    fun setUseFake(value: Boolean) {
        viewModelScope.launch { settings.setUseFake(value) }
    }

    fun testGreet(name: String) {
        viewModelScope.launch {
            settings.setLastName(name)   // persist for next app launch
            val svc = if (useFake.value) fakeService else realService
            _greeting.value = svc.greet(name)
        }
    }
}
