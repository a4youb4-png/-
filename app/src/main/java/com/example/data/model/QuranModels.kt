package com.example.data.model

data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val nameEnglishTranslation: String,
    val revelationType: String, // "مكية" / "مدنية"
    val totalAyahs: Int,
    val startingPage: Int,
    val audioUrl: String = ""
)

data class Ayah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val textArabic: String,
    val textEnglish: String = "",
    val textFrench: String = "",
    val audioUrl: String = "",
    val isBookmarked: Boolean = false
)

data class QuranReciter(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val riwayah: String = "حفص عن عاصم",
    val serverBaseUrl: String,
    val description: String = ""
)

data class SurahDownloadStatus(
    val surahNumber: Int,
    val reciterId: String,
    val isDownloaded: Boolean = false,
    val isDownloading: Boolean = false,
    val progress: Float = 0f,
    val localFilePath: String? = null
)
