package com.todayrecord.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.todayrecord.designsystem.R

class TodayRecordTextStyle internal constructor(
    private val fontSize: TextUnit,
    private val fontWeight: FontWeight,
    private val letterSpacing: TextUnit,
    private val lineHeight: TextUnit
) {

    private val fontFamily = FontFamily(
        Font(resId = R.font.pretendard_regular, weight = FontWeight.Normal),
        Font(resId = R.font.pretendard_bold, weight = FontWeight.Bold),
    )

    private val defaultLineHeightStyle = LineHeightStyle(
        LineHeightStyle.Alignment.Center, // or any kind of Alignment
        LineHeightStyle.Trim.None
    )

    @Stable
    fun asComposeStyle(): TextStyle = TextStyle(
        fontSize = fontSize,
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        lineHeightStyle = defaultLineHeightStyle
    )

    companion object {
        val Title: TodayRecordTextStyle = TodayRecordTextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.6).sp
        )

        val Body1: TodayRecordTextStyle = TodayRecordTextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.6).sp
        )

        val Body2: TodayRecordTextStyle = TodayRecordTextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.6).sp
        )

        val Body3: TodayRecordTextStyle = TodayRecordTextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.6).sp
        )

        val Body4: TodayRecordTextStyle = TodayRecordTextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.6).sp
        )
    }
}