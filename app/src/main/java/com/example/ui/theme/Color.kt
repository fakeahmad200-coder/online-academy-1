package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Clean Minimalism Core Theme Palette (Extracted from Design HTML)
val MinimalPrimary = Color(0xFF6750A4)       // M3 Deep Purple/Amethyst Brand Primary
val MinimalPrimaryDark = Color(0xFF4F378B)   // Dark Active Purple
val MinimalContainerLight = Color(0xFFEADDFF)// Lavender Soft Container
val MinimalContainerPill = Color(0xFFD0BCFF) // Accent Violet
val MinimalBackground = Color(0xFFFDF7FF)    // Clean airy lilac-white canvas background
val MinimalSurface = Color(0xFFFEF7FF)       // Light card/surface color
val MinimalSurfaceVariant = Color(0xFFF3EDF7)// Secondary grey-lavender surface
val MinimalBorder = Color(0xFFCAC4D0)        // Clean M3 thin styling border
val MinimalTextDark = Color(0xFF1D1B20)      // High-contrast primary dark body text
val MinimalTextDarkSec = Color(0xFF21005D)   // High-contrast deep purple heading/focus text
val MinimalTextLight = Color(0xFFFFFFFF)     // High-contrast clean white text

// Compatibility mappings to automatically retro-fit existing code seamlessly
val EmeraldPrimary = MinimalPrimary
val EmeraldDark = MinimalPrimaryDark
val GoldAccent = MinimalContainerPill
val GoldBright = MinimalTextDarkSec
val MintLight = MinimalContainerLight
val SlateBackground = MinimalBackground
val SlateSurface = MinimalSurface
val SlateBorder = MinimalBorder
val CreamLight = MinimalBackground
val TextDark = MinimalTextDark
val TextLight = MinimalTextDarkSec
val AlertRed = Color(0xFFBA1A1A)             // Minimalist modern M3 Red error accent
val SuccessGreen = Color(0xFF2E7D32)         // Minimalist status green
val LinkBlue = Color(0xFF005FAF)             // Minimalist deep indigo link blue

