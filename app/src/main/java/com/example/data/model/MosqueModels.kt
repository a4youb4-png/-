package com.example.data.model

data class Mosque(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val distanceKm: Double,
    val city: String,
    val address: String,
    val fajrIqamaMinutes: Int = 20,
    val dhuhrIqamaMinutes: Int = 15,
    val asrIqamaMinutes: Int = 15,
    val maghribIqamaMinutes: Int = 10,
    val ishaIqamaMinutes: Int = 15,
    val latitude: Double,
    val longitude: Double,
    val hasJumuah: Boolean = true,
    val hasWomenArea: Boolean = true,
    val isFavorite: Boolean = false
)
