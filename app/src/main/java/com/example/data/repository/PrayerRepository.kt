package com.example.data.repository

import android.content.Context
import com.example.data.calculations.PrayerTimeCalculator
import com.example.data.model.CalculationMethod
import com.example.data.model.CityLocation
import com.example.data.model.DayPrayerTimes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PrayerRepository(private val context: Context) {

    val availableCities = listOf(
        CityLocation("oran", "وهران", "Oran", "Oran", "الجزائر", "Algeria", 35.6987, -0.6349, 1.0),
        CityLocation("algiers", "الجزائر العاصمة", "Algiers", "Alger", "الجزائر", "Algeria", 36.7538, 3.0588, 1.0),
        CityLocation("constantine", "قسنطينة", "Constantine", "Constantine", "الجزائر", "Algeria", 36.3650, 6.6147, 1.0),
        CityLocation("annaba", "عنابة", "Annaba", "Annaba", "الجزائر", "Algeria", 36.9000, 7.7667, 1.0),
        CityLocation("setif", "سطيف", "Setif", "Sétif", "الجزائر", "Algeria", 36.1905, 5.4137, 1.0),
        CityLocation("tlemcen", "تلمسان", "Tlemcen", "Tlemcen", "الجزائر", "Algeria", 34.8828, -1.3167, 1.0),
        CityLocation("makkah", "مكة المكرمة", "Makkah", "La Mecque", "السعودية", "Saudi Arabia", 21.4225, 39.8262, 3.0),
        CityLocation("madinah", "المدينة المنورة", "Madinah", "Médine", "السعودية", "Saudi Arabia", 24.4672, 39.6111, 3.0),
        CityLocation("jerusalem", "القدس الشريف", "Jerusalem", "Jérusalem", "فلسطين", "Palestine", 31.7683, 35.2137, 2.0),
        CityLocation("cairo", "القاهرة", "Cairo", "Le Caire", "مصر", "Egypt", 30.0444, 31.2357, 2.0),
        CityLocation("casablanca", "الدار البيضاء", "Casablanca", "Casablanca", "المغرب", "Morocco", 33.5731, -7.5898, 1.0),
        CityLocation("tunis", "تونس", "Tunis", "Tunis", "تونس", "Tunisia", 36.8065, 10.1815, 1.0),
        CityLocation("paris", "باريس", "Paris", "Paris", "فرنسا", "France", 48.8566, 2.3522, 1.0),
        CityLocation("london", "لندن", "London", "Londres", "بريطانيا", "UK", 51.5074, -0.1278, 0.0),
        CityLocation("istanbul", "إسطنبول", "Istanbul", "Istanbul", "تركيا", "Turkey", 41.0082, 28.9784, 3.0)
    )

    private val _selectedCity = MutableStateFlow(availableCities.first()) // Oran by default
    val selectedCity: StateFlow<CityLocation> = _selectedCity.asStateFlow()

    private val _calculationMethod = MutableStateFlow(CalculationMethod.ALGERIA)
    val calculationMethod: StateFlow<CalculationMethod> = _calculationMethod.asStateFlow()

    private val _asrStandardFactor = MutableStateFlow(1.0) // 1.0 = Shafi'i/Maliki, 2.0 = Hanafi
    val asrStandardFactor: StateFlow<Double> = _asrStandardFactor.asStateFlow()

    private val _manualOffsetMinutes = MutableStateFlow(0)
    val manualOffsetMinutes: StateFlow<Int> = _manualOffsetMinutes.asStateFlow()

    private val _useDeviceClock = MutableStateFlow(false)
    val useDeviceClock: StateFlow<Boolean> = _useDeviceClock.asStateFlow()

    private val _prayerTimes = MutableStateFlow<DayPrayerTimes?>(null)
    val prayerTimes: StateFlow<DayPrayerTimes?> = _prayerTimes.asStateFlow()

    private val repositoryScope = CoroutineScope(Dispatchers.Default)

    init {
        refreshPrayerTimes()
        // Live second-by-second countdown updater
        repositoryScope.launch {
            while (true) {
                delay(1000)
                recalculateCurrentPrayer()
            }
        }
    }

    fun selectCity(city: CityLocation) {
        _selectedCity.value = city
        refreshPrayerTimes()
    }

    fun setCustomLocation(nameAr: String, nameEn: String, lat: Double, lng: Double, timezone: Double = 1.0) {
        val custom = CityLocation(
            id = "custom_${System.currentTimeMillis()}",
            nameAr = nameAr,
            nameEn = nameEn,
            nameFr = nameEn,
            countryAr = "موقعك الحالي",
            countryEn = "Current Location",
            latitude = lat,
            longitude = lng,
            timezone = timezone
        )
        _selectedCity.value = custom
        refreshPrayerTimes()
    }

    fun setCalculationMethod(method: CalculationMethod) {
        _calculationMethod.value = method
        refreshPrayerTimes()
    }

    fun setAsrHanafi(isHanafi: Boolean) {
        _asrStandardFactor.value = if (isHanafi) 2.0 else 1.0
        refreshPrayerTimes()
    }

    fun setManualOffsetMinutes(offsetMinutes: Int) {
        _manualOffsetMinutes.value = offsetMinutes
        refreshPrayerTimes()
    }

    fun setUseDeviceClock(useDevice: Boolean) {
        _useDeviceClock.value = useDevice
        refreshPrayerTimes()
    }

    fun refreshPrayerTimes() {
        val city = _selectedCity.value
        val method = _calculationMethod.value
        val asrFactor = _asrStandardFactor.value
        val offset = _manualOffsetMinutes.value
        val useDevice = _useDeviceClock.value

        val times = PrayerTimeCalculator.calculateDayPrayerTimes(
            calendar = null, // calculate from accurate city timezone
            latitude = city.latitude,
            longitude = city.longitude,
            timeZone = city.timezone,
            method = method,
            asrFactor = asrFactor,
            manualOffsetMinutes = offset,
            useDeviceClock = useDevice,
            cityName = "${city.nameAr}، ${city.countryAr}",
            countryName = city.countryAr
        )
        _prayerTimes.value = times
    }

    private fun recalculateCurrentPrayer() {
        val current = _prayerTimes.value ?: return
        val city = _selectedCity.value
        val method = _calculationMethod.value
        val asrFactor = _asrStandardFactor.value
        val offset = _manualOffsetMinutes.value
        val useDevice = _useDeviceClock.value

        _prayerTimes.value = PrayerTimeCalculator.calculateDayPrayerTimes(
            calendar = null,
            latitude = city.latitude,
            longitude = city.longitude,
            timeZone = city.timezone,
            method = method,
            asrFactor = asrFactor,
            manualOffsetMinutes = offset,
            useDeviceClock = useDevice,
            cityName = current.city,
            countryName = current.country
        )
    }
}
