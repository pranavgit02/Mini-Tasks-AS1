package com.example.streamchatdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.Column

enum class Screen { Stream, Messages, Canvas }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    HomeMenuOnly()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeMenuOnly() {
    var current by remember { mutableStateOf(Screen.Stream) }
    var menuOpen by remember { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(current.name) },
                actions = {
                    IconButton(onClick = { menuOpen = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = menuOpen,
                        onDismissRequest = { menuOpen = false }
                    ) {
                        DropdownMenuItem(text = { Text("Stream") }, onClick = {
                            current = Screen.Stream; menuOpen = false
                        })
                        DropdownMenuItem(text = { Text("Messages") }, onClick = {
                            current = Screen.Messages; menuOpen = false
                        })
                        DropdownMenuItem(text = { Text("Canvas") }, onClick = {
                            current = Screen.Canvas; menuOpen = false
                        })
                    }
                }
            )
        }
    ) { inner ->
        Column(Modifier.padding(inner)) {
            when (current) {
                Screen.Stream -> StreamScreen()
                Screen.Messages -> MessagesScreen()
                Screen.Canvas -> CanvasScreen()
            }
        }
    }
}
