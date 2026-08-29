package com.example.data.model

enum class DhikrCategory(val id: String, val titleAr: String, val titleEn: String, val icon: String) {
    POST_PRAYER("post_prayer", "أذكار بعد الصلاة", "After Prayer Dhikr", "🕌"),
    MORNING("morning", "أذكار الصباح", "Morning Dhikr", "🌅"),
    EVENING("evening", "أذكار المساء", "Evening Dhikr", "🌇"),
    SLEEP("sleep", "أذكار النوم", "Sleep Dhikr", "🌙"),
    WAKEUP("wakeup", "أذكار الاستيقاظ", "Waking Up", "☀️"),
    MOSQUE("mosque", "أذكار المسجد", "Mosque Dhikr", "🏛️")
}

data class DhikrItem(
    val id: String,
    val category: DhikrCategory,
    val textArabic: String,
    val translation: String = "",
    val transliteration: String = "",
    val countTarget: Int = 1,
    val countCurrent: Int = 0,
    val benefit: String = "",
    val sourceCitation: String = "",
    val audioUrl: String? = null,
    val isAyatAlKursi: Boolean = false,
    val isCompleted: Boolean = false
)

enum class DuaCategory(val id: String, val titleAr: String, val titleEn: String, val icon: String) {
    GENERAL("general", "أدعية عامة", "General Duas", "🤲"),
    PARENTS("parents", "أدعية للوالدين", "For Parents", "❤️"),
    KNOWLEDGE("knowledge", "أدعية العلم", "Seeking Knowledge", "📚"),
    PROVISION("provision", "أدعية الرزق", "Provision & Wealth", "💰"),
    PROTECTION("protection", "أدعية الحفظ", "Protection", "🛡️"),
    RELIEF("relief", "أدعية الكرب والهم", "Relief & Distress", "😔"),
    SLEEP("sleep", "أدعية النوم", "Sleep", "🌙"),
    TRAVEL("travel", "دعاء السفر", "Travel", "🚗"),
    FOOD("food", "أدعية الطعام", "Food & Drink", "🍽️"),
    MOSQUE("mosque", "أدعية المسجد", "Mosque", "🕌"),
    RAIN("rain", "أدعية المطر", "Rain & Storm", "☔"),
    ISTISQA("istisqa", "الاستسقاء", "Seeking Rain", "🌧️"),
    MARRIAGE("marriage", "أدعية الزواج", "Marriage", "💍"),
    CHILDREN("children", "أدعية للأبناء", "Children", "👶"),
    FORGIVENESS("forgiveness", "أدعية المغفرة", "Forgiveness", "🤍"),
    PARADISE("paradise", "أدعية الجنة", "Paradise", "🕊️"),
    HELLFIRE("hellfire", "الاستعاذة من النار", "Protection from Hellfire", "🔥")
}

data class DuaItem(
    val id: String,
    val category: DuaCategory,
    val title: String,
    val textArabic: String,
    val translation: String = "",
    val reference: String = "",
    val audioUrl: String? = null,
    val isQuranic: Boolean = false,
    val isFavorite: Boolean = false
)

data class TasbihItem(
    val id: String,
    val phraseAr: String,
    val phraseEn: String,
    val target: Int = 33,
    val currentCount: Int = 0,
    val lifetimeCount: Int = 0,
    val reward: String = ""
)
