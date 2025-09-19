package com.example.streamchatdemo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * KV windowing demo:
 * Keep last WINDOW messages as Text items.
 * Older messages collapse into a single Summary row.
 */
class WindowingViewModel : ViewModel() {

    companion object { private const val WINDOW = 10 }

    sealed interface MessageItem {
        data class Text(val text: String) : MessageItem
        data class Summary(val count: Int) : MessageItem
    }

    private val all = mutableListOf<String>()
    private val _items = MutableStateFlow<List<MessageItem>>(emptyList())
    val items: StateFlow<List<MessageItem>> = _items.asStateFlow()

    fun send(text: String) {
        if (text.isBlank()) return
        all += text
        rebuild()
    }

    fun summarizeOlder() {
        // For demo: placeholder exists automatically; this triggers a rebuild for UX.
        rebuild()
    }

    private fun rebuild() {
        val window = all.takeLast(WINDOW)
        val olderCount = (all.size - window.size).coerceAtLeast(0)

        val ui = buildList<MessageItem> {
            if (olderCount > 0) add(MessageItem.Summary(olderCount))
            window.forEach { add(MessageItem.Text(it)) }
        }
        _items.value = ui
    }
}
