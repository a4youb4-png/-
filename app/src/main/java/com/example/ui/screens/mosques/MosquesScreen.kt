package com.example.ui.screens.mosques

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Mosque
import com.example.ui.AppViewModel
import com.example.ui.components.SirajTopAppBar
import com.example.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MosquesScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mosques by viewModel.mosques.collectAsState()
    val selectedMosque by viewModel.selectedMosque.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedMosqueDetail by remember { mutableStateOf<Mosque?>(null) }

    val filteredMosques = remember(searchQuery, mosques) {
        if (searchQuery.isBlank()) {
            mosques
        } else {
            mosques.filter {
                it.nameAr.contains(searchQuery, ignoreCase = true) ||
                it.city.contains(searchQuery, ignoreCase = true) ||
                it.address.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            SirajTopAppBar(
                title = "المساجد القريبة",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
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
            // Search field
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("ابحث عن مسجد أو مدينة...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("mosque_search_field")
                )
            }

            // Mosques list
            items(filteredMosques) { mosque ->
                val isSelected = mosque.id == selectedMosque?.id
                MosqueItemCard(
                    mosque = mosque,
                    isSelected = isSelected,
                    onClick = { selectedMosqueDetail = mosque },
                    onSetDefault = {
                        viewModel.prayerRepository.setCustomLocation(
                            nameAr = mosque.nameAr,
                            nameEn = mosque.nameEn,
                            lat = mosque.latitude,
                            lng = mosque.longitude
                        )
                    }
                )
            }
        }
    }

    // Mosque Detail Sheet / Dialog
    selectedMosqueDetail?.let { mosque ->
        val isFavFlow = remember(mosque.id) { viewModel.isFavorite(mosque.id) }
        val isFav by isFavFlow.collectAsState(initial = false)

        AlertDialog(
            onDismissRequest = { selectedMosqueDetail = null },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = mosque.nameAr,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            viewModel.toggleFavorite(
                                id = mosque.id,
                                type = "MOSQUE",
                                title = mosque.nameAr,
                                subtitle = "${mosque.city} • ${mosque.distanceKm} كم"
                            )
                        }
                    ) {
                        Icon(
                            imageVector = if (isFav) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "المفضلة",
                            tint = if (isFav) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "العنوان: ${mosque.address}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "المسافة التقريبية: ${mosque.distanceKm} كم",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "جدول أوقات الإقامة بعد الأذان:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("صلاة الفجر:")
                                Text("+${mosque.fajrIqamaMinutes} دقيقة", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("صلاة الظهر:")
                                Text("+${mosque.dhuhrIqamaMinutes} دقيقة", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("صلاة العصر:")
                                Text("+${mosque.asrIqamaMinutes} دقيقة", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("صلاة المغرب:")
                                Text("+${mosque.maghribIqamaMinutes} دقيقة", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("صلاة العشاء:")
                                Text("+${mosque.ishaIqamaMinutes} دقيقة", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (mosque.hasJumuah) {
                            AssistChip(
                                onClick = {},
                                label = { Text("صلاة الجمعة ✓") }
                            )
                        }
                        if (mosque.hasWomenArea) {
                            AssistChip(
                                onClick = {},
                                label = { Text("مصلى للنساء ✓") }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.prayerRepository.setCustomLocation(
                            nameAr = mosque.nameAr,
                            nameEn = mosque.nameEn,
                            lat = mosque.latitude,
                            lng = mosque.longitude
                        )
                        selectedMosqueDetail = null
                    }
                ) {
                    Text("اعتماده كمسجدي الأساسي")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedMosqueDetail = null }) {
                    Text("إغلاق")
                }
            }
        )
    }
}

@Composable
fun MosqueItemCard(
    mosque: Mosque,
    isSelected: Boolean,
    onClick: () -> Unit,
    onSetDefault: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("mosque_card_${mosque.id}")
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
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Mosque,
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = mosque.nameAr,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${mosque.distanceKm} كم • ${mosque.city}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "تفاصيل المسجد",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
