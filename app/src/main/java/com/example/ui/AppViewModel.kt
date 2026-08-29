package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.DailyTrackerEntity
import com.example.data.local.FavoriteEntity
import com.example.data.local.QuranBookmarkEntity
import com.example.data.local.TasbihEntity
import com.example.data.model.CalculationMethod
import com.example.data.model.CityLocation
import com.example.data.model.DayPrayerTimes
import com.example.data.model.Mosque
import com.example.data.repository.DhikrRepository
import com.example.data.repository.MosqueRepository
import com.example.data.repository.PrayerRepository
import com.example.data.repository.QuranRepository
import com.example.data.repository.SettingsRepository
import com.example.service.AudioPlayerManager
import com.example.service.CompassSensorManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dao = db.appDao()

    val prayerRepository = PrayerRepository(application)
    val settingsRepository = SettingsRepository(application)
    val audioPlayerManager = AudioPlayerManager(application)
    val compassSensorManager = CompassSensorManager(application)

    val prayerTimes: StateFlow<DayPrayerTimes?> = prayerRepository.prayerTimes
    val selectedCity: StateFlow<CityLocation> = prayerRepository.selectedCity
    val calculationMethod: StateFlow<CalculationMethod> = prayerRepository.calculationMethod
    val manualOffsetMinutes: StateFlow<Int> = prayerRepository.manualOffsetMinutes
    val useDeviceClock: StateFlow<Boolean> = prayerRepository.useDeviceClock
    val settings = settingsRepository.settings
    val audioPlaybackState = audioPlayerManager.playbackState
    val downloadProgressMap = audioPlayerManager.downloadProgressMap
    val compassAzimuth = compassSensorManager.azimuth

    // Selected Quran Reciter
    private val _selectedReciterId = MutableStateFlow("afs")
    val selectedReciterId: StateFlow<String> = _selectedReciterId.asStateFlow()

    fun setSelectedReciter(reciterId: String) {
        _selectedReciterId.value = reciterId
    }

    fun playSurah(surahNumber: Int, reciterId: String? = null, surahName: String = "") {
        val reciter = reciterId ?: _selectedReciterId.value
        audioPlayerManager.playSurah(surahNumber, reciter, surahName)
    }

    fun isSurahDownloaded(surahNumber: Int, reciterId: String? = null): Boolean {
        val reciter = reciterId ?: _selectedReciterId.value
        return audioPlayerManager.isSurahDownloaded(surahNumber, reciter)
    }

    fun downloadSurah(surahNumber: Int, reciterId: String? = null) {
        val reciter = reciterId ?: _selectedReciterId.value
        audioPlayerManager.downloadSurah(surahNumber, reciter)
    }

    fun deleteDownloadedSurah(surahNumber: Int, reciterId: String? = null) {
        val reciter = reciterId ?: _selectedReciterId.value
        audioPlayerManager.deleteDownloadedSurah(surahNumber, reciter)
    }

    val mosques: StateFlow<List<Mosque>> = MosqueRepository.mosques
    val selectedMosque: StateFlow<Mosque?> = MosqueRepository.selectedMosque

    val allFavorites: StateFlow<List<FavoriteEntity>> = dao.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val quranBookmark: StateFlow<QuranBookmarkEntity?> = dao.getBookmark()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val todayKey: String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    val todayTracker: StateFlow<DailyTrackerEntity?> = dao.getDailyTracker(todayKey)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val tasbihCounters: StateFlow<List<TasbihEntity>> = dao.getAllTasbih()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Active Tasbih index / phrase
    private val _selectedTasbihIndex = MutableStateFlow(0)
    val selectedTasbihIndex: StateFlow<Int> = _selectedTasbihIndex.asStateFlow()

    private val _activeTasbihCount = MutableStateFlow(0)
    val activeTasbihCount: StateFlow<Int> = _activeTasbihCount.asStateFlow()

    private val _activeTasbihTarget = MutableStateFlow(33)
    val activeTasbihTarget: StateFlow<Int> = _activeTasbihTarget.asStateFlow()

    private val _activeTasbihTotal = MutableStateFlow(0)
    val activeTasbihTotal: StateFlow<Int> = _activeTasbihTotal.asStateFlow()

    // Global Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        ensureTodayTrackerExists()
        loadInitialTasbih()
    }

    private fun ensureTodayTrackerExists() {
        viewModelScope.launch {
            val existing = dao.getDailyTracker(todayKey).firstOrNull()
            if (existing == null) {
                dao.insertDailyTracker(DailyTrackerEntity(dateKey = todayKey))
            }
        }
    }

    private fun loadInitialTasbih() {
        viewModelScope.launch {
            val list = DhikrRepository.standardTasbihList
            val first = list.first()
            val saved = dao.getTasbih(first.id)
            if (saved != null) {
                _activeTasbihCount.value = saved.currentCount
                _activeTasbihTarget.value = saved.targetCount
                _activeTasbihTotal.value = saved.totalLifetimeCount
            }
        }
    }

    fun selectTasbihItem(index: Int) {
        val list = DhikrRepository.standardTasbihList
        if (index in list.indices) {
            _selectedTasbihIndex.value = index
            val item = list[index]
            viewModelScope.launch {
                val saved = dao.getTasbih(item.id)
                if (saved != null) {
                    _activeTasbihCount.value = saved.currentCount
                    _activeTasbihTarget.value = saved.targetCount
                    _activeTasbihTotal.value = saved.totalLifetimeCount
                } else {
                    _activeTasbihCount.value = 0
                    _activeTasbihTarget.value = item.target
                    _activeTasbihTotal.value = 0
                }
            }
        }
    }

    fun incrementTasbih() {
        val list = DhikrRepository.standardTasbihList
        val item = list.getOrNull(_selectedTasbihIndex.value) ?: return

        val newCount = _activeTasbihCount.value + 1
        val newTotal = _activeTasbihTotal.value + 1
        val target = _activeTasbihTarget.value

        if (target > 0 && newCount >= target) {
            _activeTasbihCount.value = 0
        } else {
            _activeTasbihCount.value = newCount
        }
        _activeTasbihTotal.value = newTotal

        viewModelScope.launch {
            dao.insertTasbih(
                TasbihEntity(
                    dhikrId = item.id,
                    currentCount = _activeTasbihCount.value,
                    targetCount = target,
                    totalLifetimeCount = newTotal
                )
            )
            // Update daily tracker tasbih count
            val currentTrk = dao.getDailyTracker(todayKey).firstOrNull() ?: DailyTrackerEntity(todayKey)
            dao.insertDailyTracker(currentTrk.copy(tasbihCount = currentTrk.tasbihCount + 1))
        }
    }

    fun resetTasbih() {
        val list = DhikrRepository.standardTasbihList
        val item = list.getOrNull(_selectedTasbihIndex.value) ?: return
        _activeTasbihCount.value = 0
        viewModelScope.launch {
            dao.insertTasbih(
                TasbihEntity(
                    dhikrId = item.id,
                    currentCount = 0,
                    targetCount = _activeTasbihTarget.value,
                    totalLifetimeCount = _activeTasbihTotal.value
                )
            )
        }
    }

    fun setTasbihTarget(newTarget: Int) {
        val list = DhikrRepository.standardTasbihList
        val item = list.getOrNull(_selectedTasbihIndex.value) ?: return
        _activeTasbihTarget.value = newTarget
        viewModelScope.launch {
            dao.insertTasbih(
                TasbihEntity(
                    dhikrId = item.id,
                    currentCount = _activeTasbihCount.value,
                    targetCount = newTarget,
                    totalLifetimeCount = _activeTasbihTotal.value
                )
            )
        }
    }

    fun toggleFavorite(id: String, type: String, title: String, subtitle: String = "") {
        viewModelScope.launch {
            val isFav = dao.isFavorite(id).first()
            if (isFav) {
                dao.deleteFavoriteById(id)
            } else {
                dao.insertFavorite(
                    FavoriteEntity(
                        id = id,
                        itemType = type,
                        title = title,
                        subtitle = subtitle
                    )
                )
            }
        }
    }

    fun isFavorite(id: String): Flow<Boolean> = dao.isFavorite(id)

    fun saveQuranBookmark(surahNumber: Int, ayahNumber: Int, surahName: String) {
        viewModelScope.launch {
            dao.saveBookmark(
                QuranBookmarkEntity(
                    surahNumber = surahNumber,
                    ayahNumber = ayahNumber,
                    surahName = surahName
                )
            )
        }
    }

    fun togglePrayerCompleted(prayerKey: String) {
        viewModelScope.launch {
            val current = dao.getDailyTracker(todayKey).firstOrNull() ?: DailyTrackerEntity(todayKey)
            val updated = when (prayerKey) {
                "fajr" -> current.copy(fajrDone = !current.fajrDone)
                "dhuhr" -> current.copy(dhuhrDone = !current.dhuhrDone)
                "asr" -> current.copy(asrDone = !current.asrDone)
                "maghrib" -> current.copy(maghribDone = !current.maghribDone)
                "isha" -> current.copy(ishaDone = !current.ishaDone)
                else -> current
            }
            dao.insertDailyTracker(updated)
        }
    }

    fun incrementDhikrTracker(isMorning: Boolean) {
        viewModelScope.launch {
            val current = dao.getDailyTracker(todayKey).firstOrNull() ?: DailyTrackerEntity(todayKey)
            val updated = if (isMorning) {
                current.copy(morningDhikrCount = (current.morningDhikrCount + 1).coerceAtMost(10))
            } else {
                current.copy(eveningDhikrCount = (current.eveningDhikrCount + 1).coerceAtMost(10))
            }
            dao.insertDailyTracker(updated)
        }
    }

    fun incrementQuranPages() {
        viewModelScope.launch {
            val current = dao.getDailyTracker(todayKey).firstOrNull() ?: DailyTrackerEntity(todayKey)
            dao.insertDailyTracker(current.copy(quranPages = current.quranPages + 1))
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerManager.release()
        compassSensorManager.stop()
    }
}
