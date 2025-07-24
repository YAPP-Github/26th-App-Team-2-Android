package com.yapp.breake.presentation.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.breake.core.auth.KakaoScreen
import com.yapp.breake.core.designsystem.component.KakaoLoginButton
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.LocalPadding
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorAction
import com.yapp.breake.presentation.login.model.LoginEffect.NavigateToHome
import com.yapp.breake.presentation.login.model.LoginEffect.NavigateToOnboarding
import com.yapp.breake.presentation.login.model.LoginEffect.NavigateToSignup
import com.yapp.breake.presentation.login.model.LoginUiState

@Composable
internal fun LoginRoute(viewModel: LoginViewModel = hiltViewModel()) {
	val context = LocalContext.current
	val padding = LocalPadding.current.screenPaddingHorizontal
	val navAction = LocalNavigatorAction.current
	val mainAction = LocalMainAction.current
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	LaunchedEffect(true) {
		viewModel.errorFlow.collect { mainAction.onShowSnackBar(it) }
	}

	LaunchedEffect(true) {
		viewModel.navigationFlow.collect { navigation ->
			when (navigation) {
				NavigateToHome -> navAction.navigateToHome()
				NavigateToSignup -> navAction.navigateToSignup()
				NavigateToOnboarding -> navAction.navigateToOnboarding()
			}
		}
	}

	if (uiState == LoginUiState.LoginIdle) {
		LoginScreen(
			padding = padding,
			onLoginClick = viewModel::loginWithKakao,
		)
	}

	// 서버 응답 대기 시간이 1초가 넘어 로딩창 추가
	if (uiState == LoginUiState.LoginLoading) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.statusBarsPadding(),
			contentAlignment = Alignment.Center,
		) {
			BackHandler { viewModel.loginCancel() }
			CircularProgressIndicator(modifier = Modifier.fillMaxSize(0.2f))
		}
	}

	if (uiState == LoginUiState.LoginOnWebView) {
		KakaoScreen(
			onBack = viewModel::loginCancel,
			onAuthSuccess = { viewModel.authSuccess(it, context) },
			onAuthError = viewModel::loginFailure,
		)
	}
}

@Composable
fun LoginScreen(
	padding: Dp,
	onLoginClick: () -> Unit = {},
) {
	ConstraintLayout(
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = padding),
	) {
		val (title, loginButton) = createRefs()

		Box(
			modifier = Modifier
				.padding(top = 141.dp)
				.constrainAs(title) {
					top.linkTo(parent.top)
					start.linkTo(parent.start)
					end.linkTo(parent.end)
				},
			contentAlignment = Alignment.Center,
		) {

			Text(
				text = stringResource(R.string.login_main_message),
				textAlign = TextAlign.Center,
				style = BrakeTheme.typography.subtitle22SB,
			)
		}

		KakaoLoginButton(
			modifier = Modifier
				.navigationBarsPadding()
				.padding(bottom = 24.dp)
				.widthIn(max = 400.dp)
				.constrainAs(loginButton) {
					bottom.linkTo(parent.bottom)
					start.linkTo(parent.start)
					end.linkTo(parent.end)
				},
			text = "카카오 로그인",
			onClick = onLoginClick,
		)
	}
}
