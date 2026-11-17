package com.solqus.strova.data

import com.solqus.strova.model.QueueItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/** Anything that can "download" an item and report progress from 0f to 1f. */
interface DownloadEngine {
    fun download(item: QueueItem): Flow<Float>
}

/** Pretend engine for UI development. Replace later with a real one. */
class FakeDownloadEngine : DownloadEngine {
    override fun download(item: QueueItem): Flow<Float> = flow {
        val steps = 20
        for (step in 1..steps) {
            delay(150)
            emit(step / steps.toFloat())
        }
    }
}