package com.solqus.strova.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solqus.strova.data.DownloadEngine
import com.solqus.strova.data.FakeDownloadEngine
import com.solqus.strova.model.LinkSource
import com.solqus.strova.model.QueueItem
import com.solqus.strova.model.QueueStatus
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** One-off things the UI should react to (navigate, show a message). */
sealed interface UiEvent {
    data object OpenQueue : UiEvent
    data class Message(val text: String) : UiEvent
}

class StrovaViewModel : ViewModel() {

    private val engine: DownloadEngine = FakeDownloadEngine()

    private val _linkInput = MutableStateFlow("")
    val linkInput: StateFlow<String> = _linkInput.asStateFlow()

    private val _queue = MutableStateFlow<List<QueueItem>>(emptyList())
    val queue: StateFlow<List<QueueItem>> = _queue.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var nextId = 0L
    private var workerJob: Job? = null

    fun onLinkChange(value: String) {
        _linkInput.value = value
    }

    /** Called from the Home screen's Add button. */
    fun addLink() {
        val link = extractLink(_linkInput.value)
        if (link == null) {
            _events.trySend(UiEvent.Message("That doesn't look like a link. Paste a URL starting with https://"))
            return
        }
        if (enqueue(link)) {
            _linkInput.value = ""
            _events.trySend(UiEvent.OpenQueue)
        }
    }

    /** Called when another app shares text to Strova. */
    fun addSharedText(text: String) {
        val link = extractLink(text)
        if (link == null) {
            _events.trySend(UiEvent.Message("No link found in the shared text"))
            return
        }
        if (enqueue(link)) {
            _events.trySend(UiEvent.OpenQueue)
        }
    }

    fun remove(id: Long) {
        _queue.update { list -> list.filterNot { it.id == id } }
    }

    fun clearFinished() {
        _queue.update { list ->
            list.filterNot { it.status == QueueStatus.Done || it.status == QueueStatus.Failed }
        }
    }

    // ---- internals ----

    private fun extractLink(raw: String): String? =
        Regex("""https?://\S+""", RegexOption.IGNORE_CASE)
            .find(raw.trim())
            ?.value

    private fun enqueue(link: String): Boolean {
        val alreadyActive = _queue.value.any {
            it.url == link &&
                    (it.status == QueueStatus.Waiting || it.status == QueueStatus.InProgress)
        }
        if (alreadyActive) {
            _events.trySend(UiEvent.Message("That link is already in the queue"))
            return false
        }

        val item = QueueItem(
            id = nextId++,
            url = link,
            source = LinkSource.detect(link)
        )
        _queue.update { it + item }
        startWorkerIfNeeded()
        return true
    }

    private fun updateItem(id: Long, transform: (QueueItem) -> QueueItem) {
        _queue.update { list -> list.map { if (it.id == id) transform(it) else it } }
    }

    /** Processes waiting items one at a time until none are left. */
    private fun startWorkerIfNeeded() {
        if (workerJob?.isActive == true) return

        workerJob = viewModelScope.launch {
            while (true) {
                val next = _queue.value.firstOrNull { it.status == QueueStatus.Waiting } ?: break

                updateItem(next.id) { it.copy(status = QueueStatus.InProgress) }
                try {
                    engine.download(next).collect { progress ->
                        updateItem(next.id) { it.copy(progress = progress) }
                    }
                    updateItem(next.id) { it.copy(status = QueueStatus.Done, progress = 1f) }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    updateItem(next.id) { it.copy(status = QueueStatus.Failed) }
                }
            }
        }
    }
}