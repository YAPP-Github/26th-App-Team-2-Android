package com.yapp.breake.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yapp.breake.core.designsystem.R

val pretendard = FontFamily(
	Font(R.font.pretendard_bold, FontWeight.Bold),
	Font(R.font.pretendard_reqular, FontWeight.SemiBold),
	Font(R.font.pretendard_medium, FontWeight.Medium),
	Font(R.font.pretendard_reqular, FontWeight.Normal),
)

private val pretendardStyle = TextStyle(
	fontFamily = pretendard,
	fontWeight = FontWeight.Normal,
	color = White,
	letterSpacing = 0.sp,
	platformStyle = PlatformTextStyle(
		includeFontPadding = false,
	),
)

val Typography = BrakeTypography(
	title48B = pretendardStyle.copy(
		fontSize = 48.sp,
		lineHeight = 48.sp,
		fontWeight = FontWeight.Bold,
	),
	title40B = pretendardStyle.copy(
		fontSize = 40.sp,
		lineHeight = 40.sp,
		fontWeight = FontWeight.Bold,
	),
	title28B = pretendardStyle.copy(
		fontSize = 28.sp,
		lineHeight = 28.sp,
		fontWeight = FontWeight.Bold,
	),
	title24B = pretendardStyle.copy(
		fontSize = 24.sp,
		lineHeight = 24.sp,
		fontWeight = FontWeight.Bold,
	),
	subtitle22B = pretendardStyle.copy(
		fontSize = 22.sp,
		lineHeight = 22.sp,
		fontWeight = FontWeight.Bold,
	),
	subtitle22SB = pretendardStyle.copy(
		fontSize = 22.sp,
		lineHeight = 22.sp,
		fontWeight = FontWeight.SemiBold,
	),
	subtitle20B = pretendardStyle.copy(
		fontSize = 20.sp,
		lineHeight = 20.sp,
		fontWeight = FontWeight.Bold,
	),
	subtitle20SB = pretendardStyle.copy(
		fontSize = 20.sp,
		lineHeight = 20.sp,
		fontWeight = FontWeight.SemiBold,
	),
	subtitle18B = pretendardStyle.copy(
		fontSize = 18.sp,
		lineHeight = 18.sp,
		fontWeight = FontWeight.Bold,
	),
	subtitle18SB = pretendardStyle.copy(
		fontSize = 18.sp,
		lineHeight = 18.sp,
		fontWeight = FontWeight.SemiBold,
	),
	subtitle16B = pretendardStyle.copy(
		fontSize = 16.sp,
		lineHeight = 16.sp,
		fontWeight = FontWeight.Bold,
	),
	subtitle16SB = pretendardStyle.copy(
		fontSize = 16.sp,
		lineHeight = 16.sp,
		fontWeight = FontWeight.SemiBold,
	),
	subtitle14B = pretendardStyle.copy(
		fontSize = 14.sp,
		lineHeight = 14.sp,
		fontWeight = FontWeight.Bold,
	),
	body16M = pretendardStyle.copy(
		fontSize = 16.sp,
		lineHeight = 16.sp,
		fontWeight = FontWeight.Medium,
	),
	body14SB = pretendardStyle.copy(
		fontSize = 14.sp,
		lineHeight = 14.sp,
		fontWeight = FontWeight.SemiBold,
	),
	body14M = pretendardStyle.copy(
		fontSize = 14.sp,
		lineHeight = 14.sp,
		fontWeight = FontWeight.Medium,
	),
	body12B = pretendardStyle.copy(
		fontSize = 12.sp,
		lineHeight = 12.sp,
		fontWeight = FontWeight.Bold,
	),
	body12M = pretendardStyle.copy(
		fontSize = 12.sp,
		lineHeight = 12.sp,
		fontWeight = FontWeight.Medium,
	),
	body10B = pretendardStyle.copy(
		fontSize = 10.sp,
		lineHeight = 10.sp,
		fontWeight = FontWeight.Bold,
	),
)

@Immutable
data class BrakeTypography(
	val title48B: TextStyle,
	val title40B: TextStyle,
	val title28B: TextStyle,
	val title24B: TextStyle,
	val subtitle22B: TextStyle,
	val subtitle22SB: TextStyle,
	val subtitle20B: TextStyle,
	val subtitle20SB: TextStyle,
	val subtitle18B: TextStyle,
	val subtitle18SB: TextStyle,
	val subtitle16B: TextStyle,
	val subtitle16SB: TextStyle,
	val subtitle14B: TextStyle,
	val body16M: TextStyle,
	val body14SB: TextStyle,
	val body14M: TextStyle,
	val body12B: TextStyle,
	val body12M: TextStyle,
	val body10B: TextStyle,
)

val LocalTypography = staticCompositionLocalOf {
	BrakeTypography(
		title48B = pretendardStyle,
		title40B = pretendardStyle,
		title28B = pretendardStyle,
		title24B = pretendardStyle,
		subtitle22B = pretendardStyle,
		subtitle22SB = pretendardStyle,
		subtitle20B = pretendardStyle,
		subtitle20SB = pretendardStyle,
		subtitle18B = pretendardStyle,
		subtitle18SB = pretendardStyle,
		subtitle16B = pretendardStyle,
		subtitle16SB = pretendardStyle,
		subtitle14B = pretendardStyle,
		body16M = pretendardStyle,
		body14SB = pretendardStyle,
		body14M = pretendardStyle,
		body12B = pretendardStyle,
		body12M = pretendardStyle,
		body10B = pretendardStyle,
	)
}
