package com.example.streamchatdemo

import androidx.compose.runtime.*
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StreamScreen(vm: StreamViewModel = viewModel()) {
    var input by remember {
        mutableStateOf(
            TextFieldValue("hello this is a streaming demo from coroutines and flow")
        )
    }
    val tokens by vm.tokens.collectAsStateWithLifecycle()

    // for the “watch Logcat” part of your sheet
    LaunchedEffect(tokens) {
        if (tokens.isNotEmpty()) Log.d("Stream", "Token: ${tokens.last()}")
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Input text to stream") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { vm.startStreaming(input.text) }) { Text("Start") }
            OutlinedButton(onClick = { vm.cancelStreaming() }) { Text("Cancel") }
        }

        Text("Output:")
        val scroll = rememberScrollState()
        Text(
            text = tokens.joinToString(" "),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scroll)
        )

        Text(
            "Tip: later, toggle buffer()/conflate()/collectLatest() in ViewModel to feel backpressure.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
