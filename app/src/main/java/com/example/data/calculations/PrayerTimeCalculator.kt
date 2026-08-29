package com.example.data.calculations

import com.example.data.model.CalculationMethod
import com.example.data.model.DayPrayerTimes
import com.example.data.model.PrayerTimeItem
import com.example.data.model.PrayerType
import java.util.Calendar
import java.util.Locale
import java.util.SimpleTimeZone
import java.util.TimeZone
import kotlin.math.*

object PrayerTimeCalculator {

    private fun d2r(d: Double) = d * Math.PI / 180.0
    private fun r2d(r: Double) = r * 180.0 / Math.PI

    private fun fixHour(a: Double): Double {
        var res = a - 24.0 * floor(a / 24.0)
        if (res < 0) res += 24.0
        return res
    }

    private fun fixAngle(a: Double): Double {
        var res = a - 360.0 * floor(a / 360.0)
        if (res < 0) res += 360.0
        return res
    }

    private fun julianDay(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    data class SunCoordinates(val declination: Double, val equationOfTime: Double)

    private fun sunPosition(jd: Double): SunCoordinates {
        val d = jd - 2451545.0
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * sin(d2r(g)) + 0.020 * sin(d2r(2 * g)))
        val e = 23.439 - 0.00000036 * d
        val ra = r2d(atan2(cos(d2r(e)) * sin(d2r(l)), cos(d2r(l)))) / 15.0
        val decl = r2d(asin(sin(d2r(e)) * sin(d2r(l))))
        val eqt = q / 15.0 - fixHour(ra)
        return SunCoordinates(decl, eqt)
    }

    private fun computeMidDay(timeZone: Double, lng: Double, eqt: Double): Double {
        return fixHour(12 + timeZone - lng / 15.0 - eqt)
    }

    private fun computeTime(midDay: Double, angle: Double, lat: Double, decl: Double, direction: Int): Double {
        val cosT = (sin(d2r(-angle)) - sin(d2r(lat)) * sin(d2r(decl))) / (cos(d2r(lat)) * cos(d2r(decl)))
        if (cosT > 1.0 || cosT < -1.0) {
            return Double.NaN
        }
        val t = r2d(acos(cosT)) / 15.0
        return midDay + (if (direction == -1) -t else t)
    }

    private fun computeAsrTime(midDay: Double, factor: Double, lat: Double, decl: Double): Double {
        val diff = abs(lat - decl)
        val cotAngle = factor + tan(d2r(diff))
        val angle = r2d(atan(1.0 / cotAngle))
        val cosT = (sin(d2r(angle)) - sin(d2r(lat)) * sin(d2r(decl))) / (cos(d2r(lat)) * cos(d2r(decl)))
        if (cosT > 1.0 || cosT < -1.0) {
            return Double.NaN
        }
        val t = r2d(acos(cosT)) / 15.0
        return midDay + t
    }

    fun formatTime(hoursDecimal: Double): String {
        if (hoursDecimal.isNaN()) return "--:--"
        val fixed = fixHour(hoursDecimal)
        val h = fixed.toInt()
        val m = ((fixed - h) * 60 + 0.5).toInt()
        val finalH = if (m == 60) (h + 1) % 24 else h
        val finalM = if (m == 60) 0 else m
        return String.format(Locale.US, "%02d:%02d", finalH, finalM)
    }

    fun calculateDayPrayerTimes(
        calendar: Calendar? = null,
        latitude: Double,
        longitude: Double,
        timeZone: Double,
        method: CalculationMethod = CalculationMethod.ALGERIA,
        asrFactor: Double = 1.0, // 1.0 = Standard (Shafi'i/Maliki/Hanbali), 2.0 = Hanafi
        manualOffsetMinutes: Int = 0,
        useDeviceClock: Boolean = false,
        cityName: String = "وهران",
        countryName: String = "الجزائر"
    ): DayPrayerTimes {
        // Construct accurate TimeZone for the target city or device
        val rawOffsetMillis = (timeZone * 3600 * 1000).toInt()
        val cityTimeZone: TimeZone = if (useDeviceClock) {
            TimeZone.getDefault()
        } else {
            SimpleTimeZone(rawOffsetMillis, "CityTZ_${timeZone}")
        }

        // Current calendar in city's local timezone
        val currentCal = (calendar?.clone() as? Calendar ?: Calendar.getInstance(cityTimeZone)).apply {
            this.timeZone = cityTimeZone
            if (manualOffsetMinutes != 0) {
                add(Calendar.MINUTE, manualOffsetMinutes)
            }
        }

        val year = currentCal.get(Calendar.YEAR)
        val month = currentCal.get(Calendar.MONTH) + 1
        val day = currentCal.get(Calendar.DAY_OF_MONTH)
        val currentHour = currentCal.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentCal.get(Calendar.MINUTE)
        val currentSecond = currentCal.get(Calendar.SECOND)
        val currentSecondsFromMidnight = currentHour * 3600 + currentMinute * 60 + currentSecond

        val jd = julianDay(year, month, day)
        val sun = sunPosition(jd)
        val midDay = computeMidDay(timeZone, longitude, sun.equationOfTime)

        // Sunrise & Sunset (angle ~ 0.833)
        val sunriseHour = computeTime(midDay, 0.833, latitude, sun.declination, -1)
        val sunsetHour = computeTime(midDay, 0.833, latitude, sun.declination, 1)

        // Fajr
        val fajrHour = computeTime(midDay, method.fajrAngle, latitude, sun.declination, -1)

        // Dhuhr (midday + 1-2 min for sun crossing meridian)
        val dhuhrHour = midDay + (1.0 / 60.0)

        // Asr
        val asrHour = computeAsrTime(midDay, asrFactor, latitude, sun.declination)

        // Maghrib
        val maghribHour = sunsetHour + (2.0 / 60.0)

        // Isha
        val ishaHour = if (method == CalculationMethod.UMM_AL_QURA) {
            maghribHour + 1.5 // 90 min
        } else {
            computeTime(midDay, method.ishaAngle, latitude, sun.declination, 1)
        }

        val rawTimes = listOf(
            PrayerType.FAJR to fajrHour,
            PrayerType.SUNRISE to sunriseHour,
            PrayerType.DHUHR to dhuhrHour,
            PrayerType.ASR to asrHour,
            PrayerType.MAGHRIB to maghribHour,
            PrayerType.ISHA to ishaHour
        )

        val prayerItems = mutableListOf<PrayerTimeItem>()

        for ((type, hourDec) in rawTimes) {
            val formatted = formatTime(hourDec)
            val parts = formatted.split(":")
            val h = parts.getOrNull(0)?.toIntOrNull() ?: 0
            val m = parts.getOrNull(1)?.toIntOrNull() ?: 0

            val itemCal = (currentCal.clone() as Calendar).apply {
                set(Calendar.HOUR_OF_DAY, h)
                set(Calendar.MINUTE, m)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // Calculate Iqama
            val iqamaMinutes = when (type) {
                PrayerType.FAJR -> 20
                PrayerType.SUNRISE -> 0
                PrayerType.DHUHR -> 15
                PrayerType.ASR -> 15
                PrayerType.MAGHRIB -> 10
                PrayerType.ISHA -> 15
            }
            val iqamaCal = (itemCal.clone() as Calendar).apply {
                add(Calendar.MINUTE, iqamaMinutes)
            }
            val iqamaFormatted = if (type != PrayerType.SUNRISE) {
                String.format(Locale.US, "%02d:%02d", iqamaCal.get(Calendar.HOUR_OF_DAY), iqamaCal.get(Calendar.MINUTE))
            } else null

            prayerItems.add(
                PrayerTimeItem(
                    type = type,
                    adhanTime = formatted,
                    iqamaTime = iqamaFormatted,
                    timeInMillis = itemCal.timeInMillis
                )
            )
        }

        // Active prayer obligations for next prayer detection (excluding Sunrise)
        val standardPrayers = prayerItems.filter { it.type != PrayerType.SUNRISE }
        var currentPrayer: PrayerTimeItem? = null
        var nextPrayer: PrayerTimeItem? = null
        var secondsUntilNext = 0L
        var progress = 0.0f

        // Search for the next prayer today based on seconds from midnight
        for (i in standardPrayers.indices) {
            val p = standardPrayers[i]
            val pParts = p.adhanTime.split(":")
            val pH = pParts.getOrNull(0)?.toIntOrNull() ?: 0
            val pM = pParts.getOrNull(1)?.toIntOrNull() ?: 0
            val pSeconds = pH * 3600 + pM * 60

            if (currentSecondsFromMidnight < pSeconds) {
                nextPrayer = p
                currentPrayer = if (i > 0) standardPrayers[i - 1] else standardPrayers.last()
                secondsUntilNext = max(0L, (pSeconds - currentSecondsFromMidnight).toLong())
                break
            }
        }

        if (nextPrayer == null) {
            // All prayers today have passed (after Isha). Next is tomorrow's Fajr!
            val tomorrowFajr = standardPrayers.first { it.type == PrayerType.FAJR }
            nextPrayer = tomorrowFajr
            currentPrayer = standardPrayers.last()
            val fajrParts = tomorrowFajr.adhanTime.split(":")
            val fH = fajrParts.getOrNull(0)?.toIntOrNull() ?: 0
            val fM = fajrParts.getOrNull(1)?.toIntOrNull() ?: 0
            val fajrSeconds = fH * 3600 + fM * 60
            val secondsUntilMidnight = max(0, 24 * 3600 - currentSecondsFromMidnight)
            secondsUntilNext = (secondsUntilMidnight + fajrSeconds).toLong()
        }

        if (currentPrayer != null && nextPrayer != null) {
            val curParts = currentPrayer.adhanTime.split(":")
            val curH = curParts.getOrNull(0)?.toIntOrNull() ?: 0
            val curM = curParts.getOrNull(1)?.toIntOrNull() ?: 0
            val curSeconds = curH * 3600 + curM * 60

            val nxtParts = nextPrayer.adhanTime.split(":")
            val nxtH = nxtParts.getOrNull(0)?.toIntOrNull() ?: 0
            val nxtM = nxtParts.getOrNull(1)?.toIntOrNull() ?: 0
            val nxtSeconds = nxtH * 3600 + nxtM * 60

            val totalSpan = if (nxtSeconds > curSeconds) {
                nxtSeconds - curSeconds
            } else {
                (24 * 3600 - curSeconds) + nxtSeconds
            }

            val elapsed = if (currentSecondsFromMidnight >= curSeconds) {
                currentSecondsFromMidnight - curSeconds
            } else {
                (24 * 3600 - curSeconds) + currentSecondsFromMidnight
            }

            if (totalSpan > 0) {
                progress = (elapsed.toFloat() / totalSpan.toFloat()).coerceIn(0f, 1f)
            }
        }

        val updatedPrayers = prayerItems.map { item ->
            item.copy(
                isCurrent = item.type == currentPrayer?.type,
                isNext = item.type == nextPrayer?.type
            )
        }

        val hijri = HijriCalendarHelper.getHijriDate(currentCal)
        val gregorianFormatted = HijriCalendarHelper.getFormattedGregorianDate(currentCal)

        val time24Formatted = String.format(Locale.US, "%02d:%02d:%02d", currentHour, currentMinute, currentSecond)
        val h12 = if (currentHour % 12 == 0) 12 else currentHour % 12
        val ampm = if (currentHour < 12) "صباحاً" else "مساءً"
        val time12Formatted = String.format(Locale.US, "%02d:%02d %s", h12, currentMinute, ampm)

        return DayPrayerTimes(
            date = String.format(Locale.US, "%04d-%02d-%02d", year, month, day),
            city = cityName,
            country = countryName,
            hijriDate = hijri,
            gregorianDate = gregorianFormatted,
            currentTimeFormatted = time24Formatted,
            currentTime12hFormatted = time12Formatted,
            prayers = updatedPrayers,
            nextPrayer = nextPrayer,
            currentPrayer = currentPrayer,
            secondsUntilNextPrayer = secondsUntilNext,
            progressToNextPrayer = progress
        )
    }
}
