package com.yapp.breake.presentation.onboarding

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.breake.core.designsystem.theme.LocalPadding
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorAction
import com.yapp.breake.presentation.onboarding.model.OnboardingEffect
import com.yapp.breake.presentation.onboarding.model.OnboardingUiState
import com.yapp.breake.presentation.onboarding.screen.GuideScreen
import com.yapp.breake.presentation.onboarding.screen.PermissionScreen

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun OnboardingRoute(
	viewModel: OnboardingViewModel = hiltViewModel(),
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val context = LocalContext.current
	val lifecycleOwner = LocalLifecycleOwner.current
	val screenWidth = LocalConfiguration.current.screenWidthDp.dp
	val screenHorizontalPadding = LocalPadding.current.screenPaddingHorizontal
	val navAction = LocalNavigatorAction.current

	// 외부 화면으로 이동 side effect 처리
	LaunchedEffect(true) {
		viewModel.navigationFlow.collect { effect ->
			when (effect) {
				OnboardingEffect.NavigateToBack -> {
					navAction.popBackStack()
				}

				is OnboardingEffect.RequestPermission -> {
					// 권한 요청을 위한 Intent 실행
					context.startActivity(effect.intent)
				}

				OnboardingEffect.NavigateToMain -> {
					navAction.navigateToHome()
				}
			}
		}
	}

	// 권한 설정 창에서 복귀 시 권한 상태를 새로고침
	DisposableEffect(lifecycleOwner) {
		val observer = LifecycleEventObserver { _, event ->
			if (uiState is OnboardingUiState.Permission && event == Lifecycle.Event.ON_RESUME) {
				viewModel.refreshPermissions(context)
			}
		}
		lifecycleOwner.lifecycle.addObserver(observer)
		onDispose {
			lifecycleOwner.lifecycle.removeObserver(observer)
		}
	}

	// uiState 에 따른 화면 전환 로직
	when (val state = uiState) {
		is OnboardingUiState.Guide -> {
			// 기본 가이드 화면 표시
			GuideScreen(
				startIndex = state.startPage,
				screenWidth = screenWidth,
				screenHorizontalPadding = screenHorizontalPadding,
				onBackClick = navAction::popBackStack,
				onNextClick = { viewModel.continueFromGuide(context) },
			)
		}

		is OnboardingUiState.Permission -> {
			// 권한 요청 화면으로 이동
			PermissionScreen(
				uiState = state,
				screenWidth = screenWidth,
				screenHorizontalPadding = screenHorizontalPadding,
				onBackClick = viewModel::moveBackToGuide,
				onRequestPermissionClick = { permissionItem ->
					viewModel.requestPermission(context, permissionItem)
				},
			)
		}

		OnboardingUiState.Complete -> {
			// 준비 완료 화면으로 이동
			// 예: MainScreen 또는 HomeScreen 등으로 이동
		}
	}
}
