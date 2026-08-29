package com.example.ui.screens.quran

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Ayah
import com.example.data.model.QuranReciter
import com.example.data.model.Surah
import com.example.data.repository.QuranRepository
import com.example.ui.AppViewModel
import com.example.ui.components.AyahEndNumberBadge
import com.example.ui.components.IslamicEightPointStar
import com.example.ui.components.IslamicOrnamentDivider
import com.example.ui.components.QuranPlaybackControlCard
import com.example.ui.components.ReciterSelectionModalSheet
import com.example.ui.components.SirajTopAppBar
import com.example.ui.components.SurahOrnamentHeader
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.IslamicGoldAccent
import com.example.ui.theme.IslamicGoldPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranListScreen(
    viewModel: AppViewModel,
    onNavigateToSurah: (Int) -> Unit,
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val surahs = remember { QuranRepository.surahs }
    var searchQuery by remember { mutableStateOf("") }
    val selectedReciterId by viewModel.selectedReciterId.collectAsState()
    val audioState by viewModel.audioPlaybackState.collectAsState()
    val downloadProgressMap by viewModel.downloadProgressMap.collectAsState()
    val bookmark by viewModel.quranBookmark.collectAsState()
    val context = LocalContext.current

    var showReciterSheet by remember { mutableStateOf(false) }

    val currentReciter = remember(selectedReciterId) {
        QuranRepository.reciters.find { it.id == selectedReciterId } ?: QuranRepository.reciters.first()
    }

    val filteredSurahs = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            surahs
        } else {
            surahs.filter {
                it.nameArabic.contains(searchQuery, ignoreCase = true) ||
                it.nameEnglish.contains(searchQuery, ignoreCase = true) ||
                it.number.toString() == searchQuery
            }
        }
    }

    Scaffold(
        topBar = {
            SirajTopAppBar(
                title = "القرآن الكريم",
                onSearchClick = onNavigateToSearch
            )
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 90.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Last Read Bookmark Card
            if (bookmark != null) {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToSurah(bookmark!!.surahNumber) }
                            .testTag("last_read_bookmark_card")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = "موضع القراءة الأخير",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                    )
                                    Text(
                                        text = "سورة ${bookmark!!.surahName} (الآية ${bookmark!!.ayahNumber})",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
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

            // Reciter Selection Header Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showReciterSheet = true }
                        .testTag("reciter_selector_banner")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RecordVoiceOver,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "القارئ المعتمد للتلاوة",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${currentReciter.nameAr} (${currentReciter.riwayah})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "تغيير",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Search Box
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("ابحث باسم السورة (مثلاً: الكهف) أو رقمها...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "مسح")
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("surah_search_field")
                )
            }

            // Surahs List Items
            items(filteredSurahs, key = { it.number }) { surah ->
                val isThisSurahPlaying = audioState.surahNumber == surah.number && (audioState.isPlaying || audioState.isBuffering)
                val downloadKey = "${selectedReciterId}-${surah.number}"
                val downloadProg = downloadProgressMap[downloadKey]
                val isDownloaded = viewModel.isSurahDownloaded(surah.number, selectedReciterId)

                SurahItemCard(
                    surah = surah,
                    isPlaying = isThisSurahPlaying,
                    isDownloaded = isDownloaded,
                    downloadProgress = downloadProg,
                    onCardClick = { onNavigateToSurah(surah.number) },
                    onPlayClick = {
                        if (isThisSurahPlaying) {
                            viewModel.audioPlayerManager.togglePlayPause()
                        } else {
                            viewModel.playSurah(surah.number, selectedReciterId, surah.nameArabic)
                        }
                    },
                    onDownloadClick = {
                        if (isDownloaded) {
                            Toast.makeText(context, "السورة محملة مسبقاً وتعمل بدون إنترنت", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "جاري تحميل سورة ${surah.nameArabic}...", Toast.LENGTH_SHORT).show()
                            viewModel.downloadSurah(surah.number, selectedReciterId)
                        }
                    }
                )
            }
        }
    }

    if (showReciterSheet) {
        ReciterSelectionModalSheet(
            reciters = QuranRepository.reciters,
            selectedReciterId = selectedReciterId,
            onSelectReciter = { reciter ->
                viewModel.setSelectedReciter(reciter.id)
                showReciterSheet = false
            },
            onDismiss = { showReciterSheet = false }
        )
    }
}

@Composable
fun SurahItemCard(
    surah: Surah,
    isPlaying: Boolean,
    isDownloaded: Boolean,
    downloadProgress: Float?,
    onCardClick: () -> Unit,
    onPlayClick: () -> Unit,
    onDownloadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val goldColor = MaterialTheme.colorScheme.secondary

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlaying) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPlaying) 4.dp else 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .border(
                width = if (isPlaying) 1.5.dp else 0.5.dp,
                color = if (isPlaying) goldColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                shape = RoundedCornerShape(18.dp)
            )
            .testTag("surah_item_${surah.number}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Surah Number Star Badge
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(44.dp)
                ) {
                    IslamicEightPointStar(
                        modifier = Modifier.fillMaxSize(),
                        color = if (isPlaying) goldColor else MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        strokeWidth = 2.5f
                    )
                    Text(
                        text = "${surah.number}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "سورة ${surah.nameArabic}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (isDownloaded) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "محملة بدون إنترنت",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = "${surah.nameEnglish} • ${surah.revelationType} • ${surah.totalAyahs} آية",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Actions: Play Button & Download Button
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (downloadProgress != null && downloadProgress in 0f..0.99f) {
                    CircularProgressIndicator(
                        progress = { downloadProgress },
                        strokeWidth = 2.5.dp,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(2.dp)
                    )
                } else if (!isDownloaded) {
                    IconButton(
                        onClick = onDownloadClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CloudDownload,
                            contentDescription = "تحميل",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                FilledIconButton(
                    onClick = onPlayClick,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = if (isPlaying) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("play_surah_${surah.number}_btn")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "تشغيل التلاوة",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranReaderScreen(
    surahNumber: Int,
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val surah = remember(surahNumber) {
        QuranRepository.surahs.find { it.number == surahNumber } ?: QuranRepository.surahs.first()
    }
    val ayahs = remember(surahNumber) { QuranRepository.getAyahsForSurah(surahNumber) }
    val audioState by viewModel.audioPlaybackState.collectAsState()
    val selectedReciterId by viewModel.selectedReciterId.collectAsState()
    val downloadProgressMap by viewModel.downloadProgressMap.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val context = LocalContext.current

    var fontSize by remember { mutableFloatStateOf(settings.quranFontSizeSp.coerceIn(18f, 36f)) }
    var showReciterSheet by remember { mutableStateOf(false) }

    val currentReciter = remember(selectedReciterId) {
        QuranRepository.reciters.find { it.id == selectedReciterId } ?: QuranRepository.reciters.first()
    }

    val isDownloaded = viewModel.isSurahDownloaded(surah.number, selectedReciterId)
    val downloadProg = downloadProgressMap["${selectedReciterId}-${surah.number}"]

    Scaffold(
        topBar = {
            SirajTopAppBar(
                title = "سورة ${surah.nameArabic}",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack,
                actions = {
                    // Font Size Decrease
                    IconButton(
                        onClick = { if (fontSize > 18f) fontSize -= 2f },
                        modifier = Modifier.testTag("font_decrease_button")
                    ) {
                        Text(
                            text = "A-",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Font Size Increase
                    IconButton(
                        onClick = { if (fontSize < 36f) fontSize += 2f },
                        modifier = Modifier.testTag("font_increase_button")
                    ) {
                        Text(
                            text = "A+",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Bookmark Surah
                    IconButton(
                        onClick = {
                            viewModel.saveQuranBookmark(surah.number, 1, surah.nameArabic)
                            Toast.makeText(context, "تم حفظ موضع القراءة بنجاح", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.testTag("bookmark_surah_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdd,
                            contentDescription = "حفظ الموضع",
                            tint = MaterialTheme.colorScheme.primary
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
                bottom = innerPadding.calculateBottomPadding() + 90.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Surah Ornamental Header
            item {
                SurahOrnamentHeader(
                    surahNameArabic = surah.nameArabic,
                    revelationType = surah.revelationType,
                    ayahsCount = surah.totalAyahs
                )
            }

            // Media3 Quran Playback Controls Card (Play, Pause, Download, Speed, Seek, Reciter)
            item {
                QuranPlaybackControlCard(
                    surahNumber = surah.number,
                    surahName = surah.nameArabic,
                    audioState = audioState,
                    selectedReciter = currentReciter,
                    isDownloaded = isDownloaded,
                    downloadProgress = downloadProg,
                    onPlayPause = {
                        if (audioState.surahNumber == surah.number && (audioState.isPlaying || audioState.isBuffering)) {
                            viewModel.audioPlayerManager.togglePlayPause()
                        } else {
                            viewModel.playSurah(surah.number, selectedReciterId, surah.nameArabic)
                        }
                    },
                    onDownloadClick = {
                        Toast.makeText(context, "بدء تحميل سورة ${surah.nameArabic} بصوت ${currentReciter.nameAr}...", Toast.LENGTH_SHORT).show()
                        viewModel.downloadSurah(surah.number, selectedReciterId)
                    },
                    onDeleteDownload = {
                        viewModel.deleteDownloadedSurah(surah.number, selectedReciterId)
                        Toast.makeText(context, "تم حذف الملف المحمل", Toast.LENGTH_SHORT).show()
                    },
                    onSeekTo = { posMs -> viewModel.audioPlayerManager.seekTo(posMs) },
                    onSeekRelative = { offsetMs -> viewModel.audioPlayerManager.seekRelative(offsetMs) },
                    onSpeedChange = { speed -> viewModel.audioPlayerManager.setPlaybackSpeed(speed) },
                    onSelectReciterClick = { showReciterSheet = true }
                )
            }

            // Basmalah Banner (except Surah At-Tawbah 9)
            if (surah.number != 9) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        IslamicOrnamentDivider()
                    }
                }
            }

            // Ayahs Cards
            items(ayahs, key = { "${it.surahNumber}-${it.ayahNumber}" }) { ayah ->
                AyahItemCard(
                    ayah = ayah,
                    surahName = surah.nameArabic,
                    fontSize = fontSize,
                    onBookmark = {
                        viewModel.saveQuranBookmark(ayah.surahNumber, ayah.ayahNumber, surah.nameArabic)
                        Toast.makeText(context, "تم حفظ الآية ${ayah.ayahNumber} كعلامة مرجعية", Toast.LENGTH_SHORT).show()
                    },
                    onCopy = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Ayah", "${ayah.textArabic} [${surah.nameArabic}: ${ayah.ayahNumber}]")
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "تم نسخ الآية الكريمة", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    if (showReciterSheet) {
        ReciterSelectionModalSheet(
            reciters = QuranRepository.reciters,
            selectedReciterId = selectedReciterId,
            onSelectReciter = { reciter ->
                viewModel.setSelectedReciter(reciter.id)
                showReciterSheet = false
                // Auto play if already playing
                if (audioState.isPlaying || audioState.isBuffering) {
                    viewModel.playSurah(surah.number, reciter.id, surah.nameArabic)
                }
            },
            onDismiss = { showReciterSheet = false }
        )
    }
}

@Composable
fun AyahItemCard(
    ayah: Ayah,
    surahName: String,
    fontSize: Float,
    onBookmark: () -> Unit,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header row with Ayah number star badge & actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AyahEndNumberBadge(ayahNumber = ayah.ayahNumber)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = "نسخ الآية",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onBookmark,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = "حفظ الآية",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Arabic Text
            Text(
                text = ayah.textArabic,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = fontSize.sp,
                    lineHeight = (fontSize * 1.8).sp
                ),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Right,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            // English Translation (if present)
            if (ayah.textEnglish.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = ayah.textEnglish,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
