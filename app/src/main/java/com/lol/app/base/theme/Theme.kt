package com.lol.app.base.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme =
  lightColorScheme(
    primary = PremiumGreen,
    secondary = PremiumGreenLight,
    tertiary = PremiumGreenDark,
    onPrimary = OnPremiumGreen,
    background = PremiumGreenLight,
    surface = Color.White,
    surfaceContainer = Color.Black
  )

@Composable
fun CompanionAppTheme(
  // darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  // dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  val colorScheme = LightColorScheme
  // TODO: Add dark theme color adaptation
  /*when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }

    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }*/

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

private fun Context.findActivity(): Activity {
  var context = this
  while (context is ContextWrapper &&
    context !is Activity
  ) {
    context = context.baseContext
  }
  return context as Activity
}
