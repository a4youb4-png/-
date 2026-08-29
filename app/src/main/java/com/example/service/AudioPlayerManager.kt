package com.example.service

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.data.repository.QuranRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

enum class AdhanVoice(val id: String, val nameAr: String, val nameEn: String, val audioUrl: String) {
    ALAFASY("alafasy", "مشاري راشد العفاسي", "Mishary Alafasy", "https://media.sd.ma/assabile/adhan_3452425/af9f7ecbebf6.mp3"),
    MAKKAH("makkah", "أذان الحرم المكي", "Makkah Adhan", "https://media.sd.ma/assabile/adhan_3452425/0858172c91ba.mp3"),
    MADINAH("madinah", "أذان الحرم النبوي", "Madinah Adhan", "https://media.sd.ma/assabile/adhan_3452425/d9b1db3df28d.mp3"),
    ABDULBASIT("abdulbasit", "عبد الباسط عبد الصمد", "Abdul Basit Abdul Samad", "https://media.sd.ma/assabile/adhan_3452425/5faebfffa401.mp3"),
    HUSSARY("hussary", "محمود خليل الحصري", "Mahmoud Khalil Al-Hussary", "https://media.sd.ma/assabile/adhan_3452425/7112005e1975.mp3")
}

data class AudioPlaybackState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentTitle: String = "",
    val subtitle: String = "",
    val surahNumber: Int = 0,
    val reciterId: String = "afs",
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val progress: Float = 0f,
    val playbackSpeed: Float = 1.0f,
    val isRepeatOne: Boolean = false,
    val isLocalFile: Boolean = false,
    val error: String? = null
)

class AudioPlayerManager(private val context: Context) {

    private var exoPlayer: ExoPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var progressJob: Job? = null

    private val _playbackState = MutableStateFlow(AudioPlaybackState())
    val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    // Download state tracking: key is "$reciterId-$surahNumber" -> progress (0.0 to 1.0)
    private val _downloadProgressMap = MutableStateFlow<Map<String, Float>>(emptyMap())
    val downloadProgressMap: StateFlow<Map<String, Float>> = _downloadProgressMap.asStateFlow()

    init {
        initPlayer()
    }

    private fun initPlayer() {
        if (exoPlayer == null) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH)
                .setUsage(C.USAGE_MEDIA)
                .build()

            exoPlayer = ExoPlayer.Builder(context)
                .setAudioAttributes(audioAttributes, true)
                .setHandleAudioBecomingNoisy(true)
                .setWakeMode(C.WAKE_MODE_LOCAL)
                .build().apply {
                    addListener(object : Player.Listener {
                        override fun onPlaybackStateChanged(state: Int) {
                            when (state) {
                                Player.STATE_BUFFERING -> {
                                    _playbackState.value = _playbackState.value.copy(
                                        isBuffering = true,
                                        error = null
                                    )
                                }
                                Player.STATE_READY -> {
                                    val dur = duration.coerceAtLeast(0L)
                                    _playbackState.value = _playbackState.value.copy(
                                        isBuffering = false,
                                        isPlaying = playWhenReady,
                                        durationMs = dur,
                                        error = null
                                    )
                                    if (playWhenReady) startProgressTracker()
                                }
                                Player.STATE_ENDED -> {
                                    stopProgressTracker()
                                    _playbackState.value = _playbackState.value.copy(
                                        isPlaying = false,
                                        isBuffering = false,
                                        currentPositionMs = 0L,
                                        progress = 0f
                                    )
                                }
                                Player.STATE_IDLE -> {
                                    _playbackState.value = _playbackState.value.copy(
                                        isPlaying = false,
                                        isBuffering = false
                                    )
                                }
                            }
                        }

                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            _playbackState.value = _playbackState.value.copy(isPlaying = isPlaying)
                            if (isPlaying) {
                                startProgressTracker()
                            } else {
                                stopProgressTracker()
                            }
                        }

                        override fun onPlayerError(error: PlaybackException) {
                            Log.e("SirajAudioPlayer", "ExoPlayer error: ${error.message}", error)
                            _playbackState.value = _playbackState.value.copy(
                                isPlaying = false,
                                isBuffering = false,
                                error = "تعذر تشغيل التلاوة. يرجى التحقق من الاتصال بالإنترنت."
                            )
                        }
                    })
                }
        }
    }

    fun playSurah(
        surahNumber: Int,
        reciterId: String = "afs",
        surahName: String = ""
    ) {
        val reciter = QuranRepository.reciters.find { it.id == reciterId } ?: QuranRepository.reciters.first()
        val localFile = getLocalAudioFile(surahNumber, reciter.id)
        val hasLocal = localFile.exists() && localFile.length() > 1024

        val mediaUri = if (hasLocal) {
            Uri.fromFile(localFile)
        } else {
            Uri.parse(QuranRepository.getAudioUrlForSurah(surahNumber, reciter.id))
        }

        val displayName = if (surahName.isNotEmpty()) surahName else "سورة رقم $surahNumber"
        val title = "سورة $displayName"
        val subtitle = "${reciter.nameAr} (${reciter.riwayah})"

        playMediaUri(
            uri = mediaUri,
            title = title,
            subtitle = subtitle,
            surahNumber = surahNumber,
            reciterId = reciter.id,
            isLocal = hasLocal
        )
    }

    fun playAdhan(voice: AdhanVoice) {
        playMediaUri(
            uri = Uri.parse(voice.audioUrl),
            title = "أذان بصوت الشيخ ${voice.nameAr}",
            subtitle = "معاينة صوت الأذان",
            surahNumber = 0,
            reciterId = voice.id,
            isLocal = false
        )
    }

    fun playUrl(url: String, title: String, subtitle: String = "") {
        playMediaUri(
            uri = Uri.parse(url),
            title = title,
            subtitle = subtitle,
            surahNumber = 0,
            reciterId = "custom",
            isLocal = false
        )
    }

    private fun playMediaUri(
        uri: Uri,
        title: String,
        subtitle: String,
        surahNumber: Int,
        reciterId: String,
        isLocal: Boolean
    ) {
        try {
            initPlayer()
            _playbackState.value = _playbackState.value.copy(
                isBuffering = true,
                currentTitle = title,
                subtitle = subtitle,
                surahNumber = surahNumber,
                reciterId = reciterId,
                isLocalFile = isLocal,
                error = null,
                currentPositionMs = 0L,
                progress = 0f
            )

            val mediaMetadata = MediaMetadata.Builder()
                .setTitle(title)
                .setArtist(subtitle)
                .build()

            val mediaItem = MediaItem.Builder()
                .setUri(uri)
                .setMediaMetadata(mediaMetadata)
                .build()

            exoPlayer?.apply {
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
        } catch (e: Exception) {
            Log.e("SirajAudioPlayer", "Error starting playback", e)
            _playbackState.value = _playbackState.value.copy(
                isBuffering = false,
                isPlaying = false,
                error = "حدث خطأ أثناء بدء التشغيل: ${e.localizedMessage ?: ""}"
            )
        }
    }

    fun togglePlayPause() {
        exoPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
            } else {
                if (player.playbackState == Player.STATE_ENDED) {
                    player.seekTo(0)
                }
                player.play()
            }
        }
    }

    fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
        _playbackState.value = _playbackState.value.copy(
            currentPositionMs = positionMs,
            progress = if (_playbackState.value.durationMs > 0) positionMs.toFloat() / _playbackState.value.durationMs.toFloat() else 0f
        )
    }

    fun seekRelative(offsetMs: Long) {
        exoPlayer?.let { player ->
            val newPos = (player.currentPosition + offsetMs).coerceIn(0L, player.duration.coerceAtLeast(0L))
            seekTo(newPos)
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        exoPlayer?.setPlaybackSpeed(speed)
        _playbackState.value = _playbackState.value.copy(playbackSpeed = speed)
    }

    fun toggleRepeatMode() {
        val currentRepeatOne = _playbackState.value.isRepeatOne
        val newRepeatOne = !currentRepeatOne
        exoPlayer?.repeatMode = if (newRepeatOne) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
        _playbackState.value = _playbackState.value.copy(isRepeatOne = newRepeatOne)
    }

    fun playNextSurah() {
        val currentSurah = _playbackState.value.surahNumber
        if (currentSurah in 1..113) {
            val nextSurah = QuranRepository.surahs.find { it.number == currentSurah + 1 }
            if (nextSurah != null) {
                playSurah(nextSurah.number, _playbackState.value.reciterId, nextSurah.nameArabic)
            }
        }
    }

    fun playPreviousSurah() {
        val currentSurah = _playbackState.value.surahNumber
        if (currentSurah in 2..114) {
            val prevSurah = QuranRepository.surahs.find { it.number == currentSurah - 1 }
            if (prevSurah != null) {
                playSurah(prevSurah.number, _playbackState.value.reciterId, prevSurah.nameArabic)
            }
        }
    }

    fun stop() {
        stopProgressTracker()
        try {
            exoPlayer?.stop()
            exoPlayer?.clearMediaItems()
        } catch (e: Exception) {
            // Ignore
        }
        _playbackState.value = AudioPlaybackState()
    }

    private fun startProgressTracker() {
        stopProgressTracker()
        progressJob = scope.launch {
            while (isActive) {
                exoPlayer?.let { player ->
                    if (player.isPlaying) {
                        val current = player.currentPosition.coerceAtLeast(0L)
                        val dur = player.duration.coerceAtLeast(0L)
                        val prog = if (dur > 0) (current.toFloat() / dur.toFloat()).coerceIn(0f, 1f) else 0f
                        _playbackState.value = _playbackState.value.copy(
                            currentPositionMs = current,
                            durationMs = dur,
                            progress = prog
                        )
                    }
                }
                delay(400)
            }
        }
    }

    private fun stopProgressTracker() {
        progressJob?.cancel()
        progressJob = null
    }

    // --- Offline Download Management ---

    fun getLocalAudioFile(surahNumber: Int, reciterId: String): File {
        val dir = File(context.filesDir, "quran_audio/$reciterId")
        if (!dir.exists()) dir.mkdirs()
        return File(dir, "surah_${String.format("%03d", surahNumber)}.mp3")
    }

    fun isSurahDownloaded(surahNumber: Int, reciterId: String): Boolean {
        val file = getLocalAudioFile(surahNumber, reciterId)
        return file.exists() && file.length() > 1024
    }

    fun deleteDownloadedSurah(surahNumber: Int, reciterId: String): Boolean {
        val file = getLocalAudioFile(surahNumber, reciterId)
        val success = if (file.exists()) file.delete() else true
        // trigger state update if current playing
        if (_playbackState.value.surahNumber == surahNumber && _playbackState.value.reciterId == reciterId) {
            _playbackState.value = _playbackState.value.copy(isLocalFile = false)
        }
        val key = "$reciterId-$surahNumber"
        val newMap = _downloadProgressMap.value.toMutableMap()
        newMap.remove(key)
        _downloadProgressMap.value = newMap
        return success
    }

    fun downloadSurah(
        surahNumber: Int,
        reciterId: String,
        onComplete: (Boolean) -> Unit = {}
    ) {
        val key = "$reciterId-$surahNumber"
        if (_downloadProgressMap.value.containsKey(key) && _downloadProgressMap.value[key]!! < 1f) {
            return // already downloading
        }

        val destinationFile = getLocalAudioFile(surahNumber, reciterId)
        val downloadUrl = QuranRepository.getAudioUrlForSurah(surahNumber, reciterId)

        scope.launch(Dispatchers.IO) {
            var success = false
            try {
                // Update state: starting
                updateDownloadProgress(key, 0.01f)

                val url = URL(downloadUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 15000
                connection.readTimeout = 20000
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val fileLength = connection.contentLength
                    val input = connection.inputStream
                    val output = FileOutputStream(destinationFile)

                    val buffer = ByteArray(8192)
                    var total: Long = 0
                    var count: Int

                    while (input.read(buffer).also { count = it } != -1) {
                        total += count.toLong()
                        output.write(buffer, 0, count)
                        if (fileLength > 0) {
                            val progress = (total.toFloat() / fileLength.toFloat()).coerceIn(0f, 1f)
                            updateDownloadProgress(key, progress)
                        }
                    }

                    output.flush()
                    output.close()
                    input.close()
                    success = true
                    updateDownloadProgress(key, 1.0f)
                } else {
                    Log.e("SirajAudioDownload", "Server returned code ${connection.responseCode}")
                }
            } catch (e: Exception) {
                Log.e("SirajAudioDownload", "Error downloading surah", e)
                destinationFile.delete()
                updateDownloadProgress(key, -1f) // error indicator
            } finally {
                withContext(Dispatchers.Main) {
                    onComplete(success)
                }
            }
        }
    }

    private suspend fun updateDownloadProgress(key: String, progress: Float) {
        withContext(Dispatchers.Main) {
            val newMap = _downloadProgressMap.value.toMutableMap()
            if (progress >= 1.0f || progress < 0f) {
                newMap.remove(key)
            } else {
                newMap[key] = progress
            }
            _downloadProgressMap.value = newMap
        }
    }

    fun release() {
        stop()
        exoPlayer?.release()
        exoPlayer = null
    }
}
