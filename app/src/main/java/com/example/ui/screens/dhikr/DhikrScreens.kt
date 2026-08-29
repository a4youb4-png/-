package com.example.ui.screens.dhikr

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DhikrCategory
import com.example.data.model.DhikrItem
import com.example.data.model.DuaCategory
import com.example.data.model.DuaItem
import com.example.data.repository.DhikrRepository
import com.example.ui.AppViewModel
import com.example.ui.components.SirajTopAppBar
import com.example.ui.navigation.Screen
import com.example.ui.theme.GoldAccent

@Composable
fun DhikrHubScreen(
    viewModel: AppViewModel,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            SirajTopAppBar(
                title = "الأذكار والأدعية",
                onSearchClick = { onNavigateToRoute(Screen.GlobalSearch.route) }
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
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "حصن المسلم اليومي",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                DhikrHubCard(
                    title = "أذكار بعد الصلاة",
                    subtitle = "سنة المصطفى ﷺ عقب كل صلاة مكتوبة",
                    icon = "🕌",
                    badgeText = "خطوة بخطوة",
                    onClick = { onNavigateToRoute(Screen.PostPrayerDhikr.route) }
                )
            }

            item {
                DhikrHubCard(
                    title = "أذكار الصباح",
                    subtitle = "تحصين وبركة وتوكل في بداية اليوم",
                    icon = "🌅",
                    badgeText = "10 أذكار",
                    onClick = { onNavigateToRoute(Screen.MorningDhikr.route) }
                )
            }

            item {
                DhikrHubCard(
                    title = "أذكار المساء",
                    subtitle = "حفظ وسكينة واستعاذة من الشرور",
                    icon = "🌇",
                    badgeText = "8 أذكار",
                    onClick = { onNavigateToRoute(Screen.EveningDhikr.route) }
                )
            }

            item {
                DhikrHubCard(
                    title = "أذكار النوم",
                    subtitle = "آية الكرسي، خواتيم البقرة، وتسبيح فاطمة",
                    icon = "🌙",
                    badgeText = "سكينة",
                    onClick = { onNavigateToRoute(Screen.SleepDhikr.route) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "الأدعية والأذكار الجامعة",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                DhikrHubCard(
                    title = "مكتبة الأدعية المأثورة",
                    subtitle = "أدعية قرآنية ونبوية صحيحة في 17 باباً",
                    icon = "🤲",
                    badgeText = "شاملة",
                    onClick = { onNavigateToRoute(Screen.DuaLibrary.route) }
                )
            }

            item {
                DhikrHubCard(
                    title = "السبحة الإلكترونية الذكية",
                    subtitle = "تسبيح، تحميد، تكبير، استغفار مع عداد دائم",
                    icon = "📿",
                    badgeText = "تفاعلية",
                    onClick = { onNavigateToRoute(Screen.Tasbih.route) }
                )
            }
        }
    }
}

@Composable
fun DhikrHubCard(
    title: String,
    subtitle: String,
    icon: String,
    badgeText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("dhikr_hub_card_$title")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
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
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(text = icon, fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = badgeText,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
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

@Composable
fun PostPrayerDhikrScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = remember { DhikrRepository.getPostPrayerDhikrs() }
    var currentStepIndex by remember { mutableIntStateOf(0) }
    var stepCounters by remember { mutableStateOf(IntArray(items.size) { 0 }) }
    var isStepByStepMode by remember { mutableStateOf(true) }

    val currentItem = items.getOrNull(currentStepIndex)
    val currentCount = stepCounters.getOrElse(currentStepIndex) { 0 }

    Scaffold(
        topBar = {
            SirajTopAppBar(
                title = "أذكار بعد الصلاة",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(
                        onClick = { isStepByStepMode = !isStepByStepMode },
                        modifier = Modifier.testTag("toggle_mode_button")
                    ) {
                        Icon(
                            imageVector = if (isStepByStepMode) Icons.Default.List else Icons.Default.ViewCarousel,
                            contentDescription = "تبديل وضع العرض"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        if (isStepByStepMode && currentItem != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                // Progress Indicator
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "الذِكر ${currentStepIndex + 1} من ${items.size}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { (currentStepIndex + 1).toFloat() / items.size.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Main Dhikr Text Card
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = currentItem.textArabic,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            lineHeight = 36.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (currentItem.benefit.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "الفضل: ${currentItem.benefit}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }

                        if (currentItem.sourceCitation.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "المصدر: ${currentItem.sourceCitation}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Large Interactive Counter Button
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val isDone = currentCount >= currentItem.countTarget

                    Surface(
                        shape = CircleShape,
                        color = if (isDone) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primary,
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (currentCount < currentItem.countTarget) {
                                    val newCounters = stepCounters.clone()
                                    newCounters[currentStepIndex] = currentCount + 1
                                    stepCounters = newCounters
                                    if (newCounters[currentStepIndex] >= currentItem.countTarget && currentStepIndex < items.size - 1) {
                                        currentStepIndex++
                                    }
                                }
                            }
                            .testTag("step_count_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$currentCount / ${currentItem.countTarget}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isDone) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = if (isDone) "تمت القراءة" else "اضغط للتسبيح",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isDone) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Navigation buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = { if (currentStepIndex > 0) currentStepIndex-- },
                            enabled = currentStepIndex > 0
                        ) {
                            Text("السابق")
                        }

                        Button(
                            onClick = { if (currentStepIndex < items.size - 1) currentStepIndex++ },
                            enabled = currentStepIndex < items.size - 1
                        ) {
                            Text("التالي")
                        }
                    }
                }
            }
        } else {
            // Full List View Mode
            LazyColumn(
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 8.dp,
                    bottom = innerPadding.calculateBottomPadding() + 80.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items.size) { idx ->
                    val item = items[idx]
                    val count = stepCounters[idx]
                    val isDone = count >= item.countTarget

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDone) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = item.textArabic,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "المصدر: ${item.sourceCitation}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$count / ${item.countTarget}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                FilledTonalButton(
                                    onClick = {
                                        val newCounters = stepCounters.clone()
                                        if (newCounters[idx] < item.countTarget) {
                                            newCounters[idx]++
                                        } else {
                                            newCounters[idx] = 0
                                        }
                                        stepCounters = newCounters
                                    }
                                ) {
                                    Text(if (isDone) "إعادة" else "تكرار (+1)")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
