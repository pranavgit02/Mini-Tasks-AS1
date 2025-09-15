package com.example.streamchatdemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Task 5: Greeting Service Demo
 * Uses GreetingViewModel (Hilt DI) to call Real or Fake service
 * based on the toggle in Settings.
 */
@Composable
fun GreetingScreen(vm: GreetingViewModel = hiltViewModel()) {
    val useFake by vm.useFake.collectAsStateWithLifecycle()
    val greeting by vm.greeting.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("world") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Greeting Service Demo", fontWeight = FontWeight.SemiBold)
        Text("Current source: ${if (useFake) "FakeGreetingService" else "RealGreetingService"}")

        Divider()

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { vm.testGreet(name) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Greet") }

        Spacer(Modifier.height(8.dp))

        Text(
            text = when (val g = greeting) {
                null -> "Result will appear here…"
                else -> "Result: $g"
            }
        )
    }
}
