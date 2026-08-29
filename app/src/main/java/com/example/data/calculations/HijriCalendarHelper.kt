package com.example.data.calculations

import com.example.data.model.IslamicCalendarEvent
import java.util.Calendar
import java.util.Locale
import kotlin.math.floor

object HijriCalendarHelper {

    val HIJRI_MONTHS_AR = listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الآخر",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    val HIJRI_MONTHS_EN = listOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    val DAY_NAMES_AR = listOf(
        "الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت"
    )

    val GREGORIAN_MONTHS_AR = listOf(
        "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
        "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
    )

    data class HijriDate(val day: Int, val month: Int, val year: Int)

    /**
     * Converts a Gregorian Calendar to Islamic Tabular Hijri date
     */
    fun getHijriDateDetails(calendar: Calendar): HijriDate {
        val y = calendar.get(Calendar.YEAR)
        val m = calendar.get(Calendar.MONTH) + 1
        val d = calendar.get(Calendar.DAY_OF_MONTH)

        var year = y
        var month = m
        if (month < 3) {
            year -= 1
            month += 12
        }

        val a = floor(year / 100.0)
        val b = 2 - a + floor(a / 4.0)
        val jd = floor(365.25 * (year + 4716)) + floor(30.6001 * (month + 1)) + d + b - 1524.5

        // Julian Day to Hijri
        val l = jd.toLong() - 1948440 + 10632
        val n = ((l - 1) / 10631).toInt()
        val l2 = l - 10631 * n + 354
        val j = (((10985 - l2) / 5316).toInt()) * ((50 * l2 / 17719).toInt()) +
                ((l2 / 5670).toInt()) * ((43 * l2 / 15238).toInt())
        val l3 = l2 - (((30 - j) / 15).toInt()) * ((17719 * j / 50).toInt()) -
                ((j / 16).toInt()) * ((15238 * j / 43).toInt()) + 29
        val hMonth = ((24 * l3 / 709).toInt())
        val hDay = l3 - ((709 * hMonth / 24).toInt())
        val hYear = (30 * n + j - 30)

        return HijriDate(
            day = hDay.toInt().coerceIn(1, 30),
            month = hMonth.coerceIn(1, 12),
            year = hYear
        )
    }

    fun getHijriDate(calendar: Calendar): String {
        val h = getHijriDateDetails(calendar)
        val monthName = HIJRI_MONTHS_AR.getOrElse(h.month - 1) { "هجري" }
        return "${h.day} $monthName ${h.year} هـ"
    }

    fun getFormattedGregorianDate(calendar: Calendar): String {
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // 1 = Sunday
        val dayName = DAY_NAMES_AR.getOrElse(dayOfWeek - 1) { "" }
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val monthName = GREGORIAN_MONTHS_AR.getOrElse(month) { "" }
        val year = calendar.get(Calendar.YEAR)

        return "$dayName $dayOfMonth $monthName $year"
    }

    fun getImportantEvents(): List<IslamicCalendarEvent> {
        return listOf(
            IslamicCalendarEvent("رأس السنة الهجرية", "Islamic New Year", 1, 1, "محرم", "بداية العام الهجري الجديد 1448"),
            IslamicCalendarEvent("يوم عاشوراء", "Day of Ashura", 10, 1, "محرم", "صيام يوم عاشوراء يكفر السنة الماضية"),
            IslamicCalendarEvent("المولد النبوي الشريف", "Prophet's Birthday", 12, 3, "ربيع الأول", "ذكرى مولد خير الأنام محمد ﷺ"),
            IslamicCalendarEvent("الإسراء والمعراج", "Isra and Mi'raj", 27, 7, "رجب", "ذكرى معجزة الإسراء والمعراج وفرض الصلوات الخمس"),
            IslamicCalendarEvent("ليلة النصف من شعبان", "Mid-Sha'ban", 15, 8, "ش شعبان", "ليلة مباركة يستحب فيها الدعاء والاستغفار"),
            IslamicCalendarEvent("بداية شهر رمضان المبارك", "Start of Ramadan", 1, 9, "رمضان", "شهر الصيام والقرآن والرحمة والمغفرة"),
            IslamicCalendarEvent("ليلة القدر المباركة", "Laylat al-Qadr", 27, 9, "رمضان", "خير من ألف شهر (في العشر الأواخر)"),
            IslamicCalendarEvent("عيد الفطر المبارك", "Eid al-Fitr", 1, 10, "شوال", "يوم الفرحة والسرور بإتمام صيام رمضان"),
            IslamicCalendarEvent("يوم عرفة", "Day of Arafah", 9, 12, "ذو الحجة", "أعظم أيام السنة وصيامه يكفر سنتين"),
            IslamicCalendarEvent("عيد الأضحى المبارك", "Eid al-Adha", 10, 12, "ذو الحجة", "يوم النحر وأعظم أيام الحج")
        )
    }
}
