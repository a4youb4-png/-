package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.calculations.HijriCalendarHelper
import com.example.data.calculations.PrayerTimeCalculator
import com.example.data.calculations.QiblaCalculator
import com.example.data.model.CalculationMethod
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Calendar

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("سراج", appName)
    }

    @Test
    fun `test prayer time calculations`() {
        val cal = Calendar.getInstance()
        val prayers = PrayerTimeCalculator.calculatePrayers(
            calendar = cal,
            latitude = 35.6971, // Oran, Algeria
            longitude = -0.6308,
            method = CalculationMethod.ALGERIAN_MINISTRY
        )

        assertEquals(6, prayers.size)
        assertNotNull(prayers.find { it.type.id == "fajr" })
        assertNotNull(prayers.find { it.type.id == "dhuhr" })
        assertNotNull(prayers.find { it.type.id == "asr" })
        assertNotNull(prayers.find { it.type.id == "maghrib" })
        assertNotNull(prayers.find { it.type.id == "isha" })
    }

    @Test
    fun `test qibla bearing calculation`() {
        // Bearing from Oran to Makkah is approx ~96.5 degrees
        val bearing = QiblaCalculator.calculateQiblaBearing(35.6971, -0.6308)
        assertTrue("Bearing should be roughly east-southeast", bearing in 85.0..115.0)

        val distance = QiblaCalculator.calculateDistanceToKaabaKm(35.6971, -0.6308)
        assertTrue("Distance should be around ~4200km", distance in 3500.0..5000.0)
    }

    @Test
    fun `test hijri calendar calculation`() {
        val cal = Calendar.getInstance()
        val hijriDate = HijriCalendarHelper.getHijriDateDetails(cal)
        assertTrue(hijriDate.day in 1..30)
        assertTrue(hijriDate.month in 1..12)
        assertTrue(hijriDate.year >= 1445)
    }
}
