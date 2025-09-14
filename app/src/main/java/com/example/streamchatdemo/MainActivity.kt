package com.example.streamchatdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // draws behind system bars; we’ll pad via Scaffold
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    HomeWithTabsAndMenu()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeWithTabsAndMenu() {
    var tab by remember { mutableStateOf(0) }   // 0 = Stream, 1 = Messages
    val titles = listOf("Stream", "Messages")
    var menuOpen by remember { mutableStateOf(false) }

    Scaffold(
        // This ensures content is padded for status/navigation bars
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(titles[tab]) },
                actions = {
                    // vertical “tabs” menu
                    IconButton(onClick = { menuOpen = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = menuOpen,
                        onDismissRequest = { menuOpen = false }
                    ) {
                        titles.forEachIndexed { index, label ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    tab = index
                                    menuOpen = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            // Horizontal tabs visible under the app bar (no overlap)
            TabRow(selectedTabIndex = tab) {
                titles.forEachIndexed { index, label ->
                    Tab(
                        selected = tab == index,
                        onClick = { tab = index },
                        text = { Text(label) }
                    )
                }
            }

            // Screen content
            when (tab) {
                0 -> StreamScreen()
                1 -> MessagesScreen()
            }
        }
    }
}
