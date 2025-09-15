package com.example.streamchatdemo

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MessagesScreen(vm: MessagesViewModel = viewModel()) {
    var input by remember { mutableStateOf(TextFieldValue("")) }
    val messages by vm.messages.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Type a message") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                vm.sendMessage(input.text)
                input = TextFieldValue("")
            }
        ) { Text("Send") }

        Divider()

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(messages, key = { idx, _ -> idx }) { _, msg ->
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Text(text = msg, modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}
