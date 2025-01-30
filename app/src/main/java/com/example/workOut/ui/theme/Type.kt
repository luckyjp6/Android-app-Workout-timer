package com.example.workOut.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

//@Composable
//fun getDynamicTypography() :Typography {
//    val configuration = LocalConfiguration.current
//    val screenWidth = configuration.screenWidthDp
//
//    val percentage005Size = (screenWidth * 0.05f).sp
//    val percentage008Size = (screenWidth * 0.08f).sp
//    val percentage01Size = (screenWidth * 0.1f).sp
//    val percentage015Size = (screenWidth * 0.15f).sp
//    val percentage02Size = (screenWidth * 0.2f).sp
//    val percentage025Size = (screenWidth * 0.25f).sp
//    val percentage03Size = (screenWidth * 0.3f).sp
//    val percentage033Size = (screenWidth * 0.33f).sp
//    val percentage04Size = (screenWidth * 0.4f).sp
//    val percentage05Size = (screenWidth * 0.5f).sp
//
//    return Typography(
//        normal005 = TextStyle(
//            fontSize = percentage005Size,
//            fontWeight = FontWeight.Normal
//        )
//    )
//}
//val displayLarge: TextStyle = TypographyTokens.DisplayLarge,
//val displayMedium: TextStyle = TypographyTokens.DisplayMedium,
//val displaySmall: TextStyle = TypographyTokens.DisplaySmall,
//val headlineLarge: TextStyle = TypographyTokens.HeadlineLarge,
//val headlineMedium: TextStyle = TypographyTokens.HeadlineMedium,
//val headlineSmall: TextStyle = TypographyTokens.HeadlineSmall,
//val titleLarge: TextStyle = TypographyTokens.TitleLarge,
//val titleMedium: TextStyle = TypographyTokens.TitleMedium,
//val titleSmall: TextStyle = TypographyTokens.TitleSmall,
//val bodyLarge: TextStyle = TypographyTokens.BodyLarge,
//val bodyMedium: TextStyle = TypographyTokens.BodyMedium,
//val bodySmall: TextStyle = TypographyTokens.BodySmall,
//val labelLarge: TextStyle = TypographyTokens.LabelLarge,
//val labelMedium: TextStyle = TypographyTokens.LabelMedium,
//val labelSmall: TextStyle = TypographyTokens.LabelSmall