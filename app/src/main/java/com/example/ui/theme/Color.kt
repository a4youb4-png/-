package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Premium Islamic Light Palette - Royal Emerald, Silk White & Gilded Gold
val IslamicEmeraldDark = Color(0xFF064E3B)
val IslamicEmeraldPrimary = Color(0xFF047857)
val IslamicEmeraldMedium = Color(0xFF059669)
val IslamicEmeraldLight = Color(0xFF10B981)
val IslamicEmeraldContainerLight = Color(0xFFE6F4EA)
val OnIslamicEmeraldContainerLight = Color(0xFF022C22)

// Aliases for legacy component compatibility
val EmeraldDark = IslamicEmeraldDark
val EmeraldPrimary = IslamicEmeraldPrimary
val EmeraldLight = IslamicEmeraldLight
val EmeraldContainerLight = IslamicEmeraldContainerLight
val OnEmeraldContainerLight = OnIslamicEmeraldContainerLight

val IslamicGoldPrimary = Color(0xFFD97706)
val IslamicGoldAccent = Color(0xFFF59E0B)
val IslamicGoldLight = Color(0xFFFDE68A)
val IslamicGoldContainerLight = Color(0xFFFEF3C7)
val OnIslamicGoldContainerLight = Color(0xFF78350F)

val GoldAccent = IslamicGoldPrimary
val GoldLight = IslamicGoldLight
val GoldContainerLight = IslamicGoldContainerLight
val OnGoldContainerLight = OnIslamicGoldContainerLight

val BackgroundLight = Color(0xFFF8FAF7)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceVariantLight = Color(0xFFF0FDF4)
val TextPrimaryLight = Color(0xFF0F241F)
val TextSecondaryLight = Color(0xFF374151)
val OutlineLight = Color(0xFFD1E3DA)

// Premium Islamic Dark Palette - Obsidian Jade, Midnight Teal & Gilded Amber
val ObsidianBackgroundDark = Color(0xFF051311)
val ObsidianSurfaceDark = Color(0xFF0A221E)
val ObsidianSurfaceVariantDark = Color(0xFF0F2E29)
val ObsidianCardDark = Color(0xFF143933)
val ObsidianCardElevatedDark = Color(0xFF1B4941)

val BackgroundDark = ObsidianBackgroundDark
val SurfaceDark = ObsidianSurfaceDark
val SurfaceVariantDark = ObsidianSurfaceVariantDark

val EmeraldAccentDark = Color(0xFF34D399)
val EmeraldContainerDark = Color(0xFF064E3B)
val TextPrimaryDark = Color(0xFFECFDF5)
val TextSecondaryDark = Color(0xFFA7F3D0)
val OutlineDark = Color(0xFF1E4D43)

val GoldAccentDark = Color(0xFFFBBF24)
val GoldContainerDark = Color(0xFF3D2705)
val OnGoldContainerDark = Color(0xFFFDE68A)

// Status & Functional Colors
val SoftGreen = Color(0xFF10B981)
val SoftRed = Color(0xFFEF4444)
val SoftBlue = Color(0xFF3B82F6)
val SoftAmber = Color(0xFFF59E0B)

// Luxury Gradients
val EmeraldGoldGradient = Brush.horizontalGradient(
    colors = listOf(IslamicEmeraldPrimary, IslamicEmeraldMedium, IslamicGoldAccent)
)

val ObsidianGoldGradient = Brush.linearGradient(
    colors = listOf(ObsidianSurfaceVariantDark, ObsidianCardElevatedDark)
)

val GoldenGlowGradient = Brush.linearGradient(
    colors = listOf(IslamicGoldAccent, IslamicGoldPrimary)
)
