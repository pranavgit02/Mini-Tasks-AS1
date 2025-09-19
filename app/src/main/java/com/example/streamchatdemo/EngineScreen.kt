package com.example.streamchatdemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EngineScreen(vm: EngineViewModel = hiltViewModel()) {
    var prompt by remember { mutableStateOf(TextFieldValue("hello world from engine")) }
    val tokens by vm.tokens.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = { Text("Prompt") },
            modifier = Modifier.fillMaxWidth()
        )
        RowButtons(
            onStart = { vm.start(prompt.text) },
            onCancel = vm::cancel
        )
        val scroll = rememberScrollState()
        Text(
            text = tokens.joinToString(" "),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scroll)
        )
    }
}

@Composable
private fun RowButtons(onStart: () -> Unit, onCancel: () -> Unit) {
    androidx.compose.foundation.layout.Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = onStart) { Text("Start") }
        OutlinedButton(onClick = onCancel) { Text("Cancel") }
    }
}
