package com.example.streamchatdemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.streamchatdemo.WindowingViewModel.MessageItem

@Composable
fun WindowingScreen(vm: WindowingViewModel = viewModel()) {
    var input by remember { mutableStateOf("") }
    val items by vm.items.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("KV Windowing Demo", fontWeight = FontWeight.SemiBold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Type a message") },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Button(onClick = { vm.send(input); input = "" }) { Text("Send") }
        }

        val hasOlder = items.firstOrNull() is MessageItem.Summary
        Button(onClick = vm::summarizeOlder, enabled = hasOlder) {
            Text("Summarize older (placeholder)")
        }

        Spacer(Modifier.height(4.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(items, key = { idx, item -> "${item::class.simpleName}-$idx" }) { _, item ->
                when (item) {
                    is MessageItem.Summary -> SummaryCard(item.count)
                    is MessageItem.Text    -> MessageCard(item.text)
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(count: Int) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Text(
            text = "Earlier messages ($count) — summary placeholder",
            modifier = Modifier.padding(12.dp),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun MessageCard(text: String) {
    Card(Modifier.fillMaxWidth()) {
        Text(text = text, modifier = Modifier.padding(12.dp))
    }
}
