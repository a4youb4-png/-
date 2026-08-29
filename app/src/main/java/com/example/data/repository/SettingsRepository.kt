package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.service.AdhanVoice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppLanguage(val code: String, val titleAr: String, val titleNative: String) {
    ARABIC("ar", "العربية", "العربية"),
    ENGLISH("en", "الإنجليزية", "English"),
    FRENCH("fr", "الفرنسية", "Français")
}

enum class AppThemeMode(val id: String, val titleAr: String) {
    SYSTEM("system", "تلقائي حسب النظام"),
    LIGHT("light", "فاتح (نهاري)"),
    DARK("dark", "داكن (ليلي)")
}

data class AppSettings(
    val language: AppLanguage = AppLanguage.ARABIC,
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    val adhanVoice: AdhanVoice = AdhanVoice.ALAFASY,
    val notificationsEnabled: Boolean = true,
    val fajrNotification: Boolean = true,
    val dhuhrNotification: Boolean = true,
    val asrNotification: Boolean = true,
    val maghribNotification: Boolean = true,
    val ishaNotification: Boolean = true,
    val notifyBeforeAdhanMinutes: Int = 10,
    val quranFontSizeSp: Float = 22f,
    val dhikrVibrationEnabled: Boolean = true,
    val is24HourFormat: Boolean = true
)

class SettingsRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("siraj_settings", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    private fun loadSettings(): AppSettings {
        val langCode = prefs.getString("language", AppLanguage.ARABIC.code) ?: AppLanguage.ARABIC.code
        val lang = AppLanguage.values().find { it.code == langCode } ?: AppLanguage.ARABIC

        val themeId = prefs.getString("theme_mode", AppThemeMode.SYSTEM.id) ?: AppThemeMode.SYSTEM.id
        val theme = AppThemeMode.values().find { it.id == themeId } ?: AppThemeMode.SYSTEM

        val voiceId = prefs.getString("adhan_voice", AdhanVoice.ALAFASY.id) ?: AdhanVoice.ALAFASY.id
        val voice = AdhanVoice.values().find { it.id == voiceId } ?: AdhanVoice.ALAFASY

        return AppSettings(
            language = lang,
            themeMode = theme,
            adhanVoice = voice,
            notificationsEnabled = prefs.getBoolean("notif_enabled", true),
            fajrNotification = prefs.getBoolean("fajr_notif", true),
            dhuhrNotification = prefs.getBoolean("dhuhr_notif", true),
            asrNotification = prefs.getBoolean("asr_notif", true),
            maghribNotification = prefs.getBoolean("maghrib_notif", true),
            ishaNotification = prefs.getBoolean("isha_notif", true),
            notifyBeforeAdhanMinutes = prefs.getInt("notify_before_min", 10),
            quranFontSizeSp = prefs.getFloat("quran_font_size", 22f),
            dhikrVibrationEnabled = prefs.getBoolean("dhikr_vibration", true),
            is24HourFormat = prefs.getBoolean("is_24_hour", true)
        )
    }

    fun updateLanguage(lang: AppLanguage) {
        prefs.edit().putString("language", lang.code).apply()
        _settings.value = _settings.value.copy(language = lang)
    }

    fun updateTheme(theme: AppThemeMode) {
        prefs.edit().putString("theme_mode", theme.id).apply()
        _settings.value = _settings.value.copy(themeMode = theme)
    }

    fun updateAdhanVoice(voice: AdhanVoice) {
        prefs.edit().putString("adhan_voice", voice.id).apply()
        _settings.value = _settings.value.copy(adhanVoice = voice)
    }

    fun toggleNotification(prayerName: String, enabled: Boolean) {
        val editor = prefs.edit()
        val current = _settings.value
        val updated = when (prayerName) {
            "all" -> {
                editor.putBoolean("notif_enabled", enabled)
                current.copy(notificationsEnabled = enabled)
            }
            "fajr" -> {
                editor.putBoolean("fajr_notif", enabled)
                current.copy(fajrNotification = enabled)
            }
            "dhuhr" -> {
                editor.putBoolean("dhuhr_notif", enabled)
                current.copy(dhuhrNotification = enabled)
            }
            "asr" -> {
                editor.putBoolean("asr_notif", enabled)
                current.copy(asrNotification = enabled)
            }
            "maghrib" -> {
                editor.putBoolean("maghrib_notif", enabled)
                current.copy(maghribNotification = enabled)
            }
            "isha" -> {
                editor.putBoolean("isha_notif", enabled)
                current.copy(ishaNotification = enabled)
            }
            else -> current
        }
        editor.apply()
        _settings.value = updated
    }

    fun updateQuranFontSize(fontSizeSp: Float) {
        prefs.edit().putFloat("quran_font_size", fontSizeSp).apply()
        _settings.value = _settings.value.copy(quranFontSizeSp = fontSizeSp)
    }

    fun toggleDhikrVibration(enabled: Boolean) {
        prefs.edit().putBoolean("dhikr_vibration", enabled).apply()
        _settings.value = _settings.value.copy(dhikrVibrationEnabled = enabled)
    }

    fun toggle24Hour(enabled: Boolean) {
        prefs.edit().putBoolean("is_24_hour", enabled).apply()
        _settings.value = _settings.value.copy(is24HourFormat = enabled)
    }
}
