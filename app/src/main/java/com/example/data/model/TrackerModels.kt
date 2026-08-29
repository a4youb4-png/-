package com.example.data.model

data class DailyTrackerData(
    val dateKey: String, // "2026-08-29"
    val fajrCompleted: Boolean = false,
    val dhuhrCompleted: Boolean = false,
    val asrCompleted: Boolean = false,
    val maghribCompleted: Boolean = false,
    val ishaCompleted: Boolean = false,
    val morningDhikrCompletedCount: Int = 0,
    val morningDhikrTotalCount: Int = 10,
    val eveningDhikrCompletedCount: Int = 0,
    val eveningDhikrTotalCount: Int = 10,
    val quranPagesRead: Int = 0,
    val tasbihCount: Int = 0
)

data class IslamicCalendarEvent(
    val titleAr: String,
    val titleEn: String,
    val hijriDay: Int,
    val hijriMonth: Int,
    val hijriMonthNameAr: String,
    val descriptionAr: String,
    val isHoliday: Boolean = true
)
