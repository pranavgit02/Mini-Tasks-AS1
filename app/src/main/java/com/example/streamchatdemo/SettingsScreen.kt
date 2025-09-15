package com.example.streamchatdemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsScreen(vm: GreetingViewModel = hiltViewModel()) {
    // Persisted via DataStore through the ViewModel
    val useFake by vm.useFake.collectAsStateWithLifecycle()
    val greeting by vm.greeting.collectAsStateWithLifecycle()

    // Local test input for the “Test greet” button
    var name by remember { mutableStateOf("world") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Toggle which service the app uses (real vs fake)
        Text("Greeting Service", fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Use Fake service")
            Switch(
                checked = useFake,
                onCheckedChange = vm::setUseFake
            )
        }

        Divider()

        // Quick manual test of the active service
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name to greet") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { vm.testGreet(name) }) {
            Text("Test greet")
        }
        greeting?.let {
            Spacer(Modifier.height(8.dp))
            Text("Result: $it")
        }

        // Build / Variant info
        Spacer(Modifier.height(16.dp))
        BuildInfoCard()
    }
}

@Composable
private fun BuildInfoCard() {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Build Information", fontWeight = FontWeight.SemiBold)

            InfoRow("Build Type", BuildConfig.BUILD_TYPE)
            InfoRow("Version Name", BuildConfig.VERSION_NAME)
            InfoRow("Version Code", BuildConfig.VERSION_CODE.toString())
            InfoRow("Is Internal", BuildConfig.IS_INTERNAL.toString())

            if (BuildConfig.IS_INTERNAL) {
                Text("INTERNAL build", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(value, fontWeight = FontWeight.Medium)
    }
}
