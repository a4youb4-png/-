package com.example.ui.navigation

sealed class Screen(val route: String, val titleAr: String, val titleEn: String) {
    object Home : Screen("home", "الرئيسية", "Home")
    object PrayerTimes : Screen("prayer_times", "مواقيت الصلاة", "Prayer Times")
    object Dhikr : Screen("dhikr", "الأذكار", "Dhikr")
    object PostPrayerDhikr : Screen("post_prayer_dhikr", "أذكار بعد الصلاة", "Post-Prayer Dhikr")
    object MorningDhikr : Screen("morning_dhikr", "أذكار الصباح", "Morning Dhikr")
    object EveningDhikr : Screen("evening_dhikr", "أذكار المساء", "Evening Dhikr")
    object SleepDhikr : Screen("sleep_dhikr", "أذكار النوم", "Sleep Dhikr")
    object DuaLibrary : Screen("dua_library", "الأدعية والأذكار", "Duas")
    object DuaCategoryDetail : Screen("dua_category/{categoryId}", "الأدعية", "Duas") {
        fun createRoute(categoryId: String) = "dua_category/$categoryId"
    }
    object Quran : Screen("quran", "القرآن الكريم", "Holy Quran")
    object QuranReader : Screen("quran_reader/{surahNumber}", "تلاوة القرآن", "Quran Reader") {
        fun createRoute(surahNumber: Int) = "quran_reader/$surahNumber"
    }
    object Mosques : Screen("mosques", "المساجد", "Mosques")
    object Qibla : Screen("qibla", "القبلة", "Qibla")
    object Tasbih : Screen("tasbih", "السبحة", "Tasbih")
    object Calendar : Screen("calendar", "التقويم الهجري", "Hijri Calendar")
    object Tracker : Screen("tracker", "إنجازي اليومي", "Daily Tracker")
    object Favorites : Screen("favorites", "المفضلة", "Favorites")
    object GlobalSearch : Screen("search", "البحث", "Search")
    object Settings : Screen("settings", "الإعدادات", "Settings")
}
