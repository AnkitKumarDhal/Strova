package com.solqus.strova.model

import java.net.URI

enum class LinkSource(val label: String) {
    Tidal("Tidal"),
    YouTube("YouTube"),
    Spotify("Spotify"),
    SoundCloud("SoundCloud"),
    Other("Other");

    companion object {
        fun detect(url: String): LinkSource {
            val host = runCatching { URI(url).host?.lowercase() }
                .getOrNull()
                .orEmpty()

            return when {
                host.endsWith("tidal.com") -> Tidal
                host.endsWith("youtube.com") || host.endsWith("youtu.be") -> YouTube
                host.endsWith("spotify.com") -> Spotify
                host.endsWith("soundcloud.com") -> SoundCloud
                else -> Other
            }
        }
    }
}

enum class QueueStatus {
    Waiting,
    InProgress,
    Done,
    Failed
}

data class QueueItem(
    val id: Long,
    val url: String,
    val source: LinkSource,
    val status: QueueStatus = QueueStatus.Waiting,
    val progress: Float = 0f
)