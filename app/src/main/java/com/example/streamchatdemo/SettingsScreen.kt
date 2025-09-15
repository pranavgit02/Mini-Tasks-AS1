package com.example.streamchatdemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsScreen(vm: GreetingViewModel = hiltViewModel()) {
    val useFake by vm.useFake.collectAsStateWithLifecycle()
    val greeting by vm.greeting.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("world") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Greeting Service")
        RowWithSwitch(
            title = "Use Fake service",
            checked = useFake,
            onCheckedChange = vm::setUseFake
        )

        Divider()

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name to greet") },
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        )

        Spacer(Modifier.height(8.dp))
        Button(onClick = { vm.testGreet(name) }) {
            Text("Test greet")
        }

        greeting?.let {
            Spacer(Modifier.height(12.dp))
            Text("Result: $it")
        }
    }
}

@Composable
private fun RowWithSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    androidx.compose.foundation.layout.Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(title)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
