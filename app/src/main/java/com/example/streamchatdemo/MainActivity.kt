package com.example.streamchatdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint

// Added Storage so it shows up in menu
enum class Screen { Stream, Messages, Greeting, Canvas, Settings, Storage }

@AndroidEntryPoint
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
                        DropdownMenuItem(text = { Text("Greeting") }, onClick = {
                            current = Screen.Greeting; menuOpen = false
                        })
                        DropdownMenuItem(text = { Text("Canvas") }, onClick = {
                            current = Screen.Canvas; menuOpen = false
                        })
                        DropdownMenuItem(text = { Text("Settings") }, onClick = {
                            current = Screen.Settings; menuOpen = false
                        })
                        DropdownMenuItem(text = { Text("Storage") }, onClick = {
                            current = Screen.Storage; menuOpen = false
                        })
                    }
                }
            )
        }
    ) { inner ->
        Column(Modifier.padding(inner)) {
            when (current) {
                Screen.Stream   -> StreamScreen()
                Screen.Messages -> MessagesScreen()
                Screen.Greeting -> GreetingScreen()
                Screen.Canvas   -> CanvasScreen()
                Screen.Settings -> SettingsScreen()
                Screen.Storage  -> StorageScreen()
            }
        }
    }
}
