package com.example.galleryapp.presentation.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color.Companion.White

private val LightColorScheme = AppThemeColors(

    neutralPrimaryBackground = White,
    baseBackground = Neutral100,
    neutralSecondaryBackground = Neutral200,
    neutralLine = Neutral300,


    primaryBackground = Primary100,
    primaryDefault = Primary700,
    primaryHover = Primary800,
    primaryPressed = Primary900,

    secondaryBackground = Secondary100,
    secondaryDefaultLight = Secondary600,
    secondaryDefault = Secondary700,
    secondaryHover = Secondary800,
    secondaryPressed = Secondary900,

    successBackground = Primary100,
    successDefault = Primary700,
    successHover = Primary800,
    successPressed = Primary900,

    warningBackground = Warning100,
    warningDefault = Warning700,
    warningHover = Warning800,
    warningPressed = Warning900,

    dangerBackground = Danger100,
    dangerDefault = Danger700,
    dangerHover = Danger800,
    dangerPressed = Danger900,

    primaryIcon = Neutral600,
    secondaryIcon = Neutral500,
    disabledIcon = Neutral400,

    primaryText = Neutral600,
    secondaryText = Neutral500,
    disabledText = Neutral400,
    grayBobColor = GRAY_B0B,

    textPrimaryOnColor = White,
    textSecondaryOnSecondaryHover = GRAY,
    secondaryBarText = Secondary1000,
    neutralText = Neutral150,
    plateBackground = PlateBackground,
    grayBackground = gray100,
    grayPlaceholder = gray400,
    grayHover = gray600,
    grayText = gray800,
    filterBackground = filterBackground,
    grayBgF3 = gray200,
    disableCount = DisableCount,
    neutralsText = Neutral50,
    separator = Separator,

    )


private val DarkColorScheme = AppThemeColors(

    baseBackground = Neutral1000,
    neutralPrimaryBackground = Neutral900,
    neutralSecondaryBackground = Neutral800,
    neutralLine = Neutral700,

    primaryBackground = Primary100,
    primaryDefault = Primary700,
    primaryHover = Primary800,
    primaryPressed = Primary900,

    secondaryBackground = Secondary100,
    secondaryDefaultLight = Secondary600,
    secondaryDefault = Secondary700,
    secondaryHover = Secondary800,
    secondaryPressed = Secondary900,

    successBackground = Primary100,
    successDefault = Primary700,
    successHover = Primary800,
    successPressed = Primary900,

    warningBackground = Warning100,
    warningDefault = Warning700,
    warningHover = Warning800,
    warningPressed = Warning900,

    dangerBackground = Danger100,
    dangerDefault = Danger700,
    dangerHover = Danger800,
    dangerPressed = Danger900,

    primaryIcon = White,
    secondaryIcon = White75,
    disabledIcon = White60,
    grayBobColor = GRAY_B0B,
    primaryText = White,
    secondaryText = White75,
    disabledText = White60,

    textPrimaryOnColor = White,
    textSecondaryOnSecondaryHover = GRAY,
    secondaryBarText = Secondary1000,
    neutralText = Neutral150,

    plateBackground = PlateBackground,

    grayBackground = gray100,
    grayPlaceholder = gray400,
    grayHover = gray600,
    grayText = gray800,
    filterBackground = filterBackground,
    grayBgF3 = gray200,
    disableCount = DisableCount,
    neutralsText = Neutral50,
    separator = Separator

)

val LocalAppColors = staticCompositionLocalOf<AppThemeColors> {
    error("No colors provided")
}


@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalAppColors provides colors) {
        MaterialTheme(
            typography = Typography,
            content = content
        )
    }
}

object AppTheme {
    val colors: AppThemeColors
        @Composable
        get() = LocalAppColors.current
}