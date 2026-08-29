package com.example.ui.screens.prayer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CalculationMethod
import com.example.data.model.PrayerType
import com.example.service.AdhanVoice
import com.example.ui.AppViewModel
import com.example.ui.components.NextPrayerHeroCard
import com.example.ui.components.PrayerRowCard
import com.example.ui.components.SirajTopAppBar
import com.example.ui.navigation.Screen
import com.example.ui.theme.GoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerTimesScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val prayerTimes by viewModel.prayerTimes.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val calculationMethod by viewModel.calculationMethod.collectAsState()
    val manualOffsetMinutes by viewModel.manualOffsetMinutes.collectAsState()
    val useDeviceClock by viewModel.useDeviceClock.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val audioState by viewModel.audioPlaybackState.collectAsState()

    var showMethodDialog by remember { mutableStateOf(false) }
    var showAdhanVoiceDialog by remember { mutableStateOf(false) }
    var showCityDialog by remember { mutableStateOf(false) }
    var showTimeAdjustDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SirajTopAppBar(
                title = "مواقيت الصلاة والأذان",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(
                        onClick = { showTimeAdjustDialog = true },
                        modifier = Modifier.testTag("time_adjust_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "ضبط فارق التوقيت"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 80.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Next Prayer Hero
            item {
                NextPrayerHeroCard(
                    nextPrayer = prayerTimes?.nextPrayer,
                    secondsRemaining = prayerTimes?.secondsUntilNextPrayer ?: 0L,
                    progress = prayerTimes?.progressToNextPrayer ?: 0f,
                    cityName = "${selectedCity.nameAr} (${selectedCity.countryAr})",
                    hijriDate = prayerTimes?.hijriDate ?: "هجري",
                    gregorianDate = prayerTimes?.gregorianDate ?: "",
                    currentTime = prayerTimes?.currentTimeFormatted ?: "",
                    currentTime12h = prayerTimes?.currentTime12hFormatted ?: "",
                    onLocationClick = { showCityDialog = true }
                )
            }

            // Quick Info & Time Adjust Banner
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTimeAdjustDialog = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "الوقت المعتمد في الحساب: ${prayerTimes?.currentTime12hFormatted ?: ""}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (manualOffsetMinutes != 0) "فارق التعديل: ${if (manualOffsetMinutes > 0) "+$manualOffsetMinutes" else "$manualOffsetMinutes"} دقيقة" else "توقيت دقيق بحسب إحداثيات المدينة",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        TextButton(onClick = { showTimeAdjustDialog = true }) {
                            Text("ضبط")
                        }
                    }
                }
            }

            // Section Header
            item {
                Text(
                    text = "أوقات الصلوات والإقامة",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Prayers list
            prayerTimes?.prayers?.let { prayers ->
                items(prayers) { prayer ->
                    PrayerRowCard(
                        prayer = prayer,
                        onNotificationClick = { prayerType ->
                            viewModel.settingsRepository.toggleNotification(prayerType.id, true)
                        }
                    )
                }
            }

            // Adhan Audio Voice Selector & Preview
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "صوت الأذان والتنبيه",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "بصوت: ${settings.adhanVoice.nameAr}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row {
                                FilledTonalButton(
                                    onClick = {
                                        if (audioState.isPlaying) {
                                            viewModel.audioPlayerManager.stop()
                                        } else {
                                            viewModel.audioPlayerManager.playAdhan(settings.adhanVoice)
                                        }
                                    },
                                    modifier = Modifier.testTag("preview_adhan_button")
                                ) {
                                    Icon(
                                        imageVector = if (audioState.isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (audioState.isPlaying) "إيقاف" else "استماع")
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                OutlinedButton(
                                    onClick = { showAdhanVoiceDialog = true },
                                    modifier = Modifier.testTag("change_adhan_voice_button")
                                ) {
                                    Text("تغيير")
                                }
                            }
                        }
                    }
                }
            }

            // Calculation Method Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showMethodDialog = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "طريقة حساب المواقيت",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = calculationMethod.titleAr,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }

    // Time Adjustment Dialog
    if (showTimeAdjustDialog) {
        AlertDialog(
            onDismissRequest = { showTimeAdjustDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "ضبط الوقت وفارق التوقيت", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "الوقت المحسوب حالياً:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = prayerTimes?.currentTime12hFormatted ?: "",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "تنسيق 24 ساعة: ${prayerTimes?.currentTimeFormatted ?: ""}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    HorizontalDivider()

                    Text(
                        text = "تعديل فارق التوقيت (يدوي):",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledTonalButton(
                            onClick = {
                                viewModel.prayerRepository.setManualOffsetMinutes(manualOffsetMinutes - 60)
                            }
                        ) {
                            Text("-1 ساعة")
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Text(
                                text = "${if (manualOffsetMinutes > 0) "+$manualOffsetMinutes" else "$manualOffsetMinutes"} د",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        FilledTonalButton(
                            onClick = {
                                viewModel.prayerRepository.setManualOffsetMinutes(manualOffsetMinutes + 60)
                            }
                        ) {
                            Text("+1 ساعة")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.prayerRepository.setManualOffsetMinutes(manualOffsetMinutes - 5)
                            }
                        ) {
                            Text("-5 د")
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.prayerRepository.setManualOffsetMinutes(0)
                            }
                        ) {
                            Text("إعادة ضبط")
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.prayerRepository.setManualOffsetMinutes(manualOffsetMinutes + 5)
                            }
                        ) {
                            Text("+5 د")
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showTimeAdjustDialog = false }) {
                    Text("تم وحفظ")
                }
            }
        )
    }

    // Calculation Method Dialog
    if (showMethodDialog) {
        AlertDialog(
            onDismissRequest = { showMethodDialog = false },
            title = {
                Text(text = "اختر طريقة الحساب", fontWeight = FontWeight.Bold)
            },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 340.dp)) {
                    items(CalculationMethod.values()) { method ->
                        val isSelected = method == calculationMethod
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    viewModel.prayerRepository.setCalculationMethod(method)
                                    showMethodDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = method.titleAr,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMethodDialog = false }) {
                    Text("إغلاق")
                }
            }
        )
    }

    // Adhan Voice Dialog
    if (showAdhanVoiceDialog) {
        AlertDialog(
            onDismissRequest = { showAdhanVoiceDialog = false },
            title = {
                Text(text = "اختر مؤذن التطبيق", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    AdhanVoice.values().forEach { voice ->
                        val isSelected = voice == settings.adhanVoice
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    viewModel.settingsRepository.updateAdhanVoice(voice)
                                    showAdhanVoiceDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = voice.nameAr,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAdhanVoiceDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // City Selection Dialog
    if (showCityDialog) {
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = {
                Text(text = "اختر المدينة", fontWeight = FontWeight.Bold)
            },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 350.dp)) {
                    items(viewModel.prayerRepository.availableCities) { city ->
                        val isSelected = city.id == selectedCity.id
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    viewModel.prayerRepository.selectCity(city)
                                    showCityDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${city.nameAr} (${city.countryAr})",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${city.nameEn}, ${city.countryEn}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCityDialog = false }) {
                    Text("إغلاق")
                }
            }
        )
    }
}
