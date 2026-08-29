package com.example.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.AppViewModel
import com.example.ui.components.AudioMiniPlayerBar
import com.example.ui.components.SirajBottomNavigationBar
import com.example.ui.screens.dhikr.DhikrHubScreen
import com.example.ui.screens.dhikr.DuaLibraryScreen
import com.example.ui.screens.dhikr.MorningEveningDhikrScreen
import com.example.ui.screens.dhikr.PostPrayerDhikrScreen
import com.example.ui.screens.dhikr.SleepDhikrScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.more.DailyTrackerScreen
import com.example.ui.screens.more.FavoritesScreen
import com.example.ui.screens.more.GlobalSearchScreen
import com.example.ui.screens.more.HijriCalendarScreen
import com.example.ui.screens.more.SettingsScreen
import com.example.ui.screens.mosques.MosquesScreen
import com.example.ui.screens.prayer.PrayerTimesScreen
import com.example.ui.screens.qibla.QiblaScreen
import com.example.ui.screens.quran.QuranListScreen
import com.example.ui.screens.quran.QuranReaderScreen
import com.example.ui.screens.tasbih.TasbihScreen

@Composable
fun SirajMainContent(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val audioPlaybackState by viewModel.audioPlaybackState.collectAsState()

    val showBottomNav = currentRoute in listOf(
        Screen.Home.route,
        Screen.PrayerTimes.route,
        Screen.Quran.route,
        Screen.Dhikr.route,
        Screen.Qibla.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                SirajBottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigateToRoute = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToRoute = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.PrayerTimes.route) {
                    PrayerTimesScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Quran.route) {
                    QuranListScreen(
                        viewModel = viewModel,
                        onNavigateToSurah = { surahNumber ->
                            navController.navigate(Screen.QuranReader.createRoute(surahNumber))
                        },
                        onNavigateToSearch = {
                            navController.navigate(Screen.GlobalSearch.route)
                        }
                    )
                }

                composable(
                    route = Screen.QuranReader.route,
                    arguments = listOf(navArgument("surahNumber") { type = NavType.IntType })
                ) { backStack ->
                    val surahNumber = backStack.arguments?.getInt("surahNumber") ?: 1
                    QuranReaderScreen(
                        surahNumber = surahNumber,
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Dhikr.route) {
                    DhikrHubScreen(
                        viewModel = viewModel,
                        onNavigateToRoute = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.PostPrayerDhikr.route) {
                    PostPrayerDhikrScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.MorningDhikr.route) {
                    MorningEveningDhikrScreen(
                        viewModel = viewModel,
                        isMorning = true,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.EveningDhikr.route) {
                    MorningEveningDhikrScreen(
                        viewModel = viewModel,
                        isMorning = false,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.SleepDhikr.route) {
                    SleepDhikrScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.DuaLibrary.route) {
                    DuaLibraryScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Mosques.route) {
                    MosquesScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Qibla.route) {
                    QiblaScreen(viewModel = viewModel)
                }

                composable(Screen.Tasbih.route) {
                    TasbihScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Calendar.route) {
                    HijriCalendarScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Tracker.route) {
                    DailyTrackerScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.GlobalSearch.route) {
                    GlobalSearchScreen(
                        viewModel = viewModel,
                        onNavigateToSurah = { surahNumber ->
                            navController.navigate(Screen.QuranReader.createRoute(surahNumber))
                        },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }

            // Floating Mini Audio Player Bar (At bottom of screen above Nav Bar)
            AudioMiniPlayerBar(
                state = audioPlaybackState,
                onTogglePlayPause = { viewModel.audioPlayerManager.togglePlayPause() },
                onClose = { viewModel.audioPlayerManager.stop() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = if (showBottomNav) 8.dp else 16.dp)
            )
        }
    }
}
