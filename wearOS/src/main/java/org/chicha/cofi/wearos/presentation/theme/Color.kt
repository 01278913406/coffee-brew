package org.chicha.cofi.wearos.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import org.chicha.cofi.share.brown200
import org.chicha.cofi.share.brown700
import org.chicha.cofi.share.iconBackground

val Red400 = Color(0xFFCF6679)

internal val wearColorPalette: Colors = Colors(
    primary = brown200,
    primaryVariant = brown700,
    secondary = iconBackground,
    secondaryVariant = iconBackground,
    error = Red400,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onError = Color.Black
)
