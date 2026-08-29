package com.example.data.model

enum class PrayerType(val id: String, val nameAr: String, val nameEn: String, val nameFr: String) {
    FAJR("fajr", "الفجر", "Fajr", "Fajr"),
    SUNRISE("sunrise", "الشروق", "Sunrise", "Chourouk"),
    DHUHR("dhuhr", "الظهر", "Dhuhr", "Dhuhr"),
    ASR("asr", "العصر", "Asr", "Asr"),
    MAGHRIB("maghrib", "المغرب", "Maghrib", "Maghrib"),
    ISHA("isha", "العشاء", "Isha", "Icha")
}

data class PrayerTimeItem(
    val type: PrayerType,
    val adhanTime: String,       // "05:03"
    val iqamaTime: String? = null, // "05:20"
    val timeInMillis: Long,
    val isCurrent: Boolean = false,
    val isNext: Boolean = false,
    val statusText: String = ""
)

data class DayPrayerTimes(
    val date: String,
    val city: String,
    val country: String,
    val hijriDate: String,
    val gregorianDate: String,
    val currentTimeFormatted: String = "",
    val currentTime12hFormatted: String = "",
    val prayers: List<PrayerTimeItem>,
    val nextPrayer: PrayerTimeItem?,
    val currentPrayer: PrayerTimeItem?,
    val secondsUntilNextPrayer: Long,
    val progressToNextPrayer: Float // 0.0f to 1.0f
)

data class CityLocation(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val nameFr: String,
    val countryAr: String,
    val countryEn: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: Double
)

enum class CalculationMethod(
    val id: Int,
    val titleAr: String,
    val titleEn: String,
    val fajrAngle: Double,
    val ishaAngle: Double
) {
    ALGERIA(19, "وزارة الشؤون الدينية (الجزائر)", "Algerian Ministry of Religious Affairs", 18.0, 17.0),
    UMM_AL_QURA(4, "أم القرى (مكة المكرمة)", "Umm Al-Qura (Makkah)", 18.5, 90.0), // 90 min after Maghrib
    MWL(3, "رابطة العالم الإسلامي", "Muslim World League", 18.0, 17.0),
    EGYPT(5, "الهيئة المصرية العامة للمساحة", "Egyptian General Authority of Survey", 19.5, 17.5),
    ISNA(2, "الجمعية الإسلامية لأمريكا الشمالية (ISNA)", "ISNA (North America)", 15.0, 15.0),
    KARACHI(1, "جامعة العلوم الإسلامية بكراتشي", "University of Islamic Sciences, Karachi", 18.0, 18.0)
}
