package com.yapp.breake.presentation.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorProvider
import com.yapp.breake.core.ui.SnackBarState
import com.yapp.breake.presentation.login.component.LoginNoticeText
import com.yapp.breake.presentation.login.model.LoginNavState.NavigateToHome
import com.yapp.breake.presentation.login.model.LoginNavState.NavigateToOnboarding
import com.yapp.breake.presentation.login.model.LoginNavState.NavigateToPermission
import com.yapp.breake.presentation.login.model.LoginNavState.NavigateToPrivacyPolicy
import com.yapp.breake.presentation.login.model.LoginNavState.NavigateToSignup
import com.yapp.breake.presentation.login.model.LoginNavState.NavigateToTermsOfService
import com.yapp.breake.presentation.login.model.LoginUiState

@Composable
internal fun LoginRoute(viewModel: LoginViewModel = hiltViewModel()) {
	val context = LocalContext.current
	val padding = LocalPadding.current.screenPaddingHorizontal
	val navAction = LocalNavigatorAction.current
	val navProvider = LocalNavigatorProvider.current
	val mainAction = LocalMainAction.current
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	if (uiState == LoginUiState.LoginLoading) {
		mainAction.OnShowLoading()
		BackHandler {
			viewModel.loginCancel()
		}
	} else {
		mainAction.OnFinishBackHandler()
	}

	LaunchedEffect(true) {
		viewModel.snackBarFlow.collect {
			when (it) {
				is SnackBarState.Error -> mainAction.onShowErrorMessage(
					message = it.uiString.asString(context),
				)

				is SnackBarState.Success -> mainAction.onShowSuccessMessage(
					message = it.uiString.asString(context),
				)
			}
		}
	}

	LaunchedEffect(true) {
		viewModel.navigationFlow.collect { navigation ->
			when (navigation) {
				NavigateToPrivacyPolicy -> navAction.navigateToPrivacy()
				NavigateToTermsOfService -> navAction.navigateToTerms()
				NavigateToHome -> navAction.navigateToHome(
					navOptions = navProvider.getNavOptionsClearingBackStack(),
				)
				NavigateToSignup -> navAction.navigateToSignup()
				NavigateToOnboarding -> navAction.navigateToGuide()
				NavigateToPermission -> navAction.navigateToPermission()
			}
		}
	}

	LoginScreen(
		padding = padding,
		onPrivacyClick = viewModel::showPrivacyPolicy,
		onTermsClick = viewModel::showTermsOfService,
		onLoginClick = viewModel::loginWithKakao,
	)

	if (uiState == LoginUiState.LoginOnWebView) {
		KakaoScreen(
			onBack = viewModel::authCancel,
			onAuthSuccess = { viewModel.authSuccess(it, context) },
			onAuthError = viewModel::loginFailure,
		)
	}
}

@Composable
fun LoginScreen(
	padding: Dp,
	onPrivacyClick: () -> Unit,
	onTermsClick: () -> Unit,
	onLoginClick: () -> Unit,
) {
	ConstraintLayout(
		modifier = Modifier.fillMaxSize(),
	) {
		val (title, notice, loginButton) = createRefs()

		Box(
			modifier = Modifier
				.constrainAs(title) {
					top.linkTo(parent.top)
					start.linkTo(parent.start)
					end.linkTo(parent.end)
				},
			contentAlignment = Alignment.TopCenter,
		) {
			Image(
				modifier = Modifier
					.align(Alignment.TopCenter)
					.padding(bottom = 8.dp),
				painter = painterResource(R.drawable.img_login),
				contentDescription = null,
			)

			Text(
				text = stringResource(R.string.login_main_message),
				modifier = Modifier.align(Alignment.BottomCenter),
				textAlign = TextAlign.Center,
				style = BrakeTheme.typography.subtitle22SB,
			)
		}

		LoginNoticeText(
			modifier = Modifier
				.constrainAs(notice) {
					bottom.linkTo(loginButton.top, margin = 20.dp)
					start.linkTo(parent.start)
					end.linkTo(parent.end)
				}
				.padding(horizontal = padding),
			onPrivacyClick = onPrivacyClick,
			onTermsClick = onTermsClick,
		)

		KakaoLoginButton(
			modifier = Modifier
				.navigationBarsPadding()
				.padding(horizontal = padding)
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
