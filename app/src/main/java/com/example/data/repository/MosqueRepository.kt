package com.example.data.repository

import com.example.data.model.Mosque
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object MosqueRepository {

    private val baseMosques = listOf(
        Mosque(
            id = "mosque_oran_1",
            nameAr = "جامع عبد الحميد بن باديس",
            nameEn = "Abdelhamid Ibn Badis Grand Mosque",
            distanceKm = 1.2,
            city = "وهران",
            address = "حي الصباح، وهران، الجزائر",
            fajrIqamaMinutes = 25,
            dhuhrIqamaMinutes = 20,
            asrIqamaMinutes = 20,
            maghribIqamaMinutes = 10,
            ishaIqamaMinutes = 20,
            latitude = 35.6881,
            longitude = -0.5891,
            hasJumuah = true,
            hasWomenArea = true
        ),
        Mosque(
            id = "mosque_oran_2",
            nameAr = "مسجد الإمام الهواري",
            nameEn = "Sidi El Houari Mosque",
            distanceKm = 2.4,
            city = "وهران",
            address = "المدينة العتيقة، سيدي الهواري، وهران",
            fajrIqamaMinutes = 20,
            dhuhrIqamaMinutes = 15,
            asrIqamaMinutes = 15,
            maghribIqamaMinutes = 10,
            ishaIqamaMinutes = 15,
            latitude = 35.7058,
            longitude = -0.6558,
            hasJumuah = true,
            hasWomenArea = false
        ),
        Mosque(
            id = "mosque_oran_3",
            nameAr = "مسجد الباشا",
            nameEn = "Pasha Mosque",
            distanceKm = 2.8,
            city = "وهران",
            address = "حي سيدي الهواري التاريخي، وهران",
            fajrIqamaMinutes = 20,
            dhuhrIqamaMinutes = 15,
            asrIqamaMinutes = 15,
            maghribIqamaMinutes = 10,
            ishaIqamaMinutes = 15,
            latitude = 35.7061,
            longitude = -0.6534,
            hasJumuah = true,
            hasWomenArea = true
        ),
        Mosque(
            id = "mosque_oran_4",
            nameAr = "مسجد القدس",
            nameEn = "Al-Quds Mosque",
            distanceKm = 3.5,
            city = "وهران",
            address = "حي السلام، وهران",
            fajrIqamaMinutes = 20,
            dhuhrIqamaMinutes = 15,
            asrIqamaMinutes = 15,
            maghribIqamaMinutes = 10,
            ishaIqamaMinutes = 15,
            latitude = 35.6890,
            longitude = -0.6120,
            hasJumuah = true,
            hasWomenArea = true
        ),
        Mosque(
            id = "mosque_alg_1",
            nameAr = "جامع الجزائر الأعظم",
            nameEn = "Djamaa el Djazair (Great Mosque of Algiers)",
            distanceKm = 5.1,
            city = "الجزائر العاصمة",
            address = "المحمدية، الجزائر العاصمة",
            fajrIqamaMinutes = 25,
            dhuhrIqamaMinutes = 20,
            asrIqamaMinutes = 20,
            maghribIqamaMinutes = 10,
            ishaIqamaMinutes = 20,
            latitude = 36.7358,
            longitude = 3.1417,
            hasJumuah = true,
            hasWomenArea = true
        ),
        Mosque(
            id = "mosque_alg_2",
            nameAr = "مسجد كتشاوة",
            nameEn = "Ketchaoua Mosque",
            distanceKm = 4.3,
            city = "الجزائر العاصمة",
            address = "القصبة السفلى، الجزائر العاصمة",
            fajrIqamaMinutes = 20,
            dhuhrIqamaMinutes = 15,
            asrIqamaMinutes = 15,
            maghribIqamaMinutes = 10,
            ishaIqamaMinutes = 15,
            latitude = 36.7850,
            longitude = 3.0603,
            hasJumuah = true,
            hasWomenArea = true
        ),
        Mosque(
            id = "mosque_const_1",
            nameAr = "جامع الأمير عبد القادر",
            nameEn = "Emir Abdelkader Grand Mosque",
            distanceKm = 3.2,
            city = "قسنطينة",
            address = "شارع الأمير عبد القادر، قسنطينة",
            fajrIqamaMinutes = 25,
            dhuhrIqamaMinutes = 20,
            asrIqamaMinutes = 20,
            maghribIqamaMinutes = 10,
            ishaIqamaMinutes = 20,
            latitude = 36.3533,
            longitude = 6.6122,
            hasJumuah = true,
            hasWomenArea = true
        ),
        Mosque(
            id = "mosque_makkah_1",
            nameAr = "المسجد الحرام",
            nameEn = "Masjid al-Haram",
            distanceKm = 0.5,
            city = "مكة المكرمة",
            address = "مكة المكرمة، المملكة العربية السعودية",
            fajrIqamaMinutes = 20,
            dhuhrIqamaMinutes = 15,
            asrIqamaMinutes = 15,
            maghribIqamaMinutes = 10,
            ishaIqamaMinutes = 15,
            latitude = 21.4225,
            longitude = 39.8262,
            hasJumuah = true,
            hasWomenArea = true
        ),
        Mosque(
            id = "mosque_madinah_1",
            nameAr = "المسجد النبوي الشريف",
            nameEn = "Al-Masjid an-Nabawi",
            distanceKm = 0.8,
            city = "المدينة المنورة",
            address = "المدينة المنورة، المملكة العربية السعودية",
            fajrIqamaMinutes = 20,
            dhuhrIqamaMinutes = 15,
            asrIqamaMinutes = 15,
            maghribIqamaMinutes = 10,
            ishaIqamaMinutes = 15,
            latitude = 24.4672,
            longitude = 39.6111,
            hasJumuah = true,
            hasWomenArea = true
        ),
        Mosque(
            id = "mosque_aqsa_1",
            nameAr = "المسجد الأقصى المبارك",
            nameEn = "Al-Aqsa Mosque",
            distanceKm = 0.3,
            city = "القدس الشريف",
            address = "البلدة القديمة، القدس الشريف، فلسطين",
            fajrIqamaMinutes = 20,
            dhuhrIqamaMinutes = 15,
            asrIqamaMinutes = 15,
            maghribIqamaMinutes = 10,
            ishaIqamaMinutes = 15,
            latitude = 31.7761,
            longitude = 35.2358,
            hasJumuah = true,
            hasWomenArea = true
        )
    )

    private val _mosques = MutableStateFlow(baseMosques)
    val mosques: StateFlow<List<Mosque>> = _mosques.asStateFlow()

    private val _selectedMosque = MutableStateFlow<Mosque?>(baseMosques.first())
    val selectedMosque: StateFlow<Mosque?> = _selectedMosque.asStateFlow()

    fun selectMosque(mosque: Mosque) {
        _selectedMosque.value = mosque
    }

    fun recalculateDistances(userLat: Double, userLng: Double) {
        val updated = _mosques.value.map { m ->
            val d = calculateDistanceKm(userLat, userLng, m.latitude, m.longitude)
            m.copy(distanceKm = Math.round(d * 10.0) / 10.0)
        }.sortedBy { it.distanceKm }
        _mosques.value = updated
    }

    private fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}
