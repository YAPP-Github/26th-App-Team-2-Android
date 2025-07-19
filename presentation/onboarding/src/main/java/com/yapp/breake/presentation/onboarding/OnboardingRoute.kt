package com.yapp.breake.presentation.onboarding

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.theme.LocalPadding
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorAction

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun OnboardingRoute() {
	val screenWidth = LocalConfiguration.current.screenWidthDp.dp
	val screenHorizontalPadding = LocalPadding.current.screenPaddingHorizontal
	val navAction = LocalNavigatorAction.current

	GuideScreen(
		screenWidth = screenWidth,
		screenHorizontalPadding = screenHorizontalPadding,
		onBackClick = { navAction.popBackStack() },
		onNextClick = {
			// 권한 요청 화면 또는 준비완료 화면 이동
		},
	)
}
