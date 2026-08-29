package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String, // e.g. "dua_1", "surah_36", "dhikr_post_1", "mosque_oran_1"
    val itemType: String,       // "DUA", "SURAH", "DHIKR", "MOSQUE"
    val title: String,
    val subtitle: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_tracker")
data class DailyTrackerEntity(
    @PrimaryKey val dateKey: String, // "YYYY-MM-DD"
    val fajrDone: Boolean = false,
    val dhuhrDone: Boolean = false,
    val asrDone: Boolean = false,
    val maghribDone: Boolean = false,
    val ishaDone: Boolean = false,
    val morningDhikrCount: Int = 0,
    val eveningDhikrCount: Int = 0,
    val quranPages: Int = 0,
    val tasbihCount: Int = 0
)

@Entity(tableName = "tasbih_counters")
data class TasbihEntity(
    @PrimaryKey val dhikrId: String,
    val currentCount: Int,
    val targetCount: Int,
    val totalLifetimeCount: Int
)

@Entity(tableName = "quran_bookmarks")
data class QuranBookmarkEntity(
    @PrimaryKey val id: Int = 1,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahName: String,
    val timestamp: Long = System.currentTimeMillis()
)
