package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CityLocation
import com.example.data.model.Mosque
import com.example.data.model.PrayerType
import com.example.ui.AppViewModel
import com.example.ui.components.*
import com.example.ui.navigation.Screen
import com.example.ui.theme.GoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val prayerTimes by viewModel.prayerTimes.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val tracker by viewModel.todayTracker.collectAsState()
    val mosques by viewModel.mosques.collectAsState()
    val nearestMosque = mosques.firstOrNull()

    var showCityDialog by remember { mutableStateOf(false) }
    var quickDhikrCount by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            SirajTopAppBar(
                title = "سِـرَاج",
                onSearchClick = { onNavigateToRoute(Screen.GlobalSearch.route) },
                onSettingsClick = { onNavigateToRoute(Screen.Settings.route) },
                actions = {
                    IconButton(
                        onClick = { onNavigateToRoute(Screen.Favorites.route) },
                        modifier = Modifier.testTag("favorites_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = "المفضلة"
                        )
                    }
                    IconButton(
                        onClick = { onNavigateToRoute(Screen.Tracker.route) },
                        modifier = Modifier.testTag("daily_tracker_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = "إنجازي اليومي"
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
            // Next Prayer Hero Card
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

            // Quick Shortcuts Grid (6 items)
            item {
                Column {
                    Text(
                        text = "الوصول السريع",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickShortcutItem(
                            icon = Icons.Filled.LocationCity,
                            title = "المساجد",
                            badgeText = "${mosques.size} مساجد",
                            onClick = { onNavigateToRoute(Screen.Mosques.route) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickShortcutItem(
                            icon = Icons.Filled.MenuBook,
                            title = "القرآن",
                            badgeText = "114 سورة",
                            onClick = { onNavigateToRoute(Screen.Quran.route) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickShortcutItem(
                            icon = Icons.Filled.WbSunny,
                            title = "أذكار الصباح",
                            badgeText = "حصن المسلم",
                            onClick = { onNavigateToRoute(Screen.MorningDhikr.route) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickShortcutItem(
                            icon = Icons.Filled.NightsStay,
                            title = "أذكار المساء",
                            badgeText = "تحصين",
                            onClick = { onNavigateToRoute(Screen.EveningDhikr.route) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickShortcutItem(
                            icon = Icons.Filled.Spa,
                            title = "السبحة",
                            badgeText = "تسبيح ذكي",
                            onClick = { onNavigateToRoute(Screen.Tasbih.route) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickShortcutItem(
                            icon = Icons.Filled.Explore,
                            title = "القبلة",
                            badgeText = "بوصلة مكة",
                            onClick = { onNavigateToRoute(Screen.Qibla.route) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Quick Daily Dhikr Card (Interactive)
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = GoldAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "ذِكر اليوم المقترح",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }

                            Text(
                                text = "الهدف: 33",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "«أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ»",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "مرات التكرار: $quickDhikrCount / 33",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )

                            Button(
                                onClick = {
                                    if (quickDhikrCount < 33) quickDhikrCount++ else quickDhikrCount = 0
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.testTag("quick_dhikr_count_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TouchApp,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "تسبيح (+1)")
                            }
                        }
                    }
                }
            }

            // Today's Prayer Times List
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "مواقيت صلاة اليوم",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(onClick = { onNavigateToRoute(Screen.PrayerTimes.route) }) {
                        Text("عرض الكل")
                    }
                }
            }

            prayerTimes?.prayers?.let { prayers ->
                items(prayers) { prayer ->
                    PrayerRowCard(
                        prayer = prayer,
                        onNotificationClick = { prayerType ->
                            viewModel.settingsRepository.toggleNotification(
                                prayerType.id,
                                true
                            )
                        }
                    )
                }
            }

            // Nearest Mosque Card
            nearestMosque?.let { mosque ->
                item {
                    Text(
                        text = "أقرب مسجد إليك",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToRoute(Screen.Mosques.route) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mosque,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = mosque.nameAr,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${mosque.distanceKm} كم • ${mosque.address}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }

    // City Selection Dialog
    if (showCityDialog) {
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = {
                Text(
                    text = "اختر المدينة",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 360.dp)) {
                    items(viewModel.prayerRepository.availableCities) { city ->
                        val isSelected = city.id == selectedCity.id
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.prayerRepository.selectCity(city)
                                    showCityDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = city.nameAr,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = city.countryAr,
                                        style = MaterialTheme.typography.bodySmall,
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
                    Text("إلغاء")
                }
            }
        )
    }
}
