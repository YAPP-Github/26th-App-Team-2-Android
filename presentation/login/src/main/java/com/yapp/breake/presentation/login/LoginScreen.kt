package com.yapp.breake.presentation.login

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.yapp.breake.core.auth.kakao.KakaoScreen
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.LocalPadding
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorAction
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorProvider
import com.yapp.breake.core.ui.SnackBarState
import com.yapp.breake.presentation.login.component.KakaoLoginButton
import com.yapp.breake.presentation.login.component.LoginNoticeText
import com.yapp.breake.presentation.login.model.LoginNavState.NavigateToHome
import com.yapp.breake.presentation.login.model.LoginNavState.NavigateToOnboarding
import com.yapp.breake.presentation.login.model.LoginNavState.NavigateToPermission
import com.yapp.breake.presentation.login.model.LoginNavState.NavigateToPrivacyPolicy
import com.yapp.breake.presentation.login.model.LoginNavState.NavigateToSignup
import com.yapp.breake.presentation.login.model.LoginNavState.NavigateToTermsOfService
import com.yapp.breake.presentation.login.model.LoginUiState
import timber.log.Timber

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
			viewModel.cancelLogin()
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

	val authorizationLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.StartIntentSenderForResult(),
	) { result ->
		try {
			// 결과에서 AuthorizationResult 추출
			val authorizationResult = Identity.getAuthorizationClient(context)
				.getAuthorizationResultFromIntent(result.data)

			authorizationResult.serverAuthCode?.let { code ->
				Timber.d("Google One Tap 로그인 성공, code: $code")
				viewModel.loginWithGoogle(context = context, authCode = code)
			} ?: run {
				Timber.d("Google One Tap 로그인 취소 또는 실패")
				viewModel.cancelGoogleAuthorization()
			}
		} catch (e: ApiException) {
			when (e.statusCode) {
				7 -> {
					Timber.e("네트워크 문제로 Google 로그인에 실패했습니다")
					viewModel.failGoogleAuthorization()
				}
				16 -> {
					Timber.d("유저가 Google 로그인 창을 닫았습니다")
					viewModel.cancelGoogleAuthorization()
				}
				else -> {
					Timber.e(e, "Google 로그인 API 예외 발생")
					viewModel.failGoogleAuthorization()
				}
			}
		} catch (e: Exception) {
			Timber.e(e, "Google One Tap 로그인 알 수 없는 예외 발생")
			viewModel.failGoogleAuthorization()
		}
	}

	LoginScreen(
		padding = padding,
		onPrivacyClick = viewModel::showPrivacyPolicy,
		onTermsClick = viewModel::showTermsOfService,
		onGoogleLoginClick = {
			viewModel.getGoogleAuthorization(context) { intent ->
				authorizationLauncher.launch(intent)
			}
		},
		onkakaoLoginClick = viewModel::getKakaoAuthorization,
	)

	if (uiState == LoginUiState.LoginOnWebView) {
		KakaoScreen(
			onBack = viewModel::cancelKakaoAuthorization,
			onAuthSuccess = { viewModel.loginWithKakao(context = context, authCode = it) },
			onAuthError = viewModel::failKakaoAuthorization,
		)
	}
}

@Composable
fun LoginScreen(
	padding: Dp,
	onPrivacyClick: () -> Unit,
	onTermsClick: () -> Unit,
	onGoogleLoginClick: () -> Unit,
	onkakaoLoginClick: () -> Unit,
) {
	ConstraintLayout(
		modifier = Modifier.fillMaxSize(),
	) {
		val (title, notice, googleLoginButton, kakaoLoginButton) = createRefs()

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
					top.linkTo(title.bottom)
					bottom.linkTo(kakaoLoginButton.top, margin = 20.dp)
					start.linkTo(parent.start)
					end.linkTo(parent.end)
					// top, bottom 과 linkTo 관계가 설정되어 있을 때, 해당 컴포넌트 y 위치를 바텀(1f)으로 조정
					verticalBias = 1f
				}
				.padding(horizontal = padding),
			onPrivacyClick = onPrivacyClick,
			onTermsClick = onTermsClick,
		)

//		GoogleLoginButton(
//			modifier = Modifier
//				.padding(horizontal = padding)
//				.widthIn(max = 400.dp)
//				.constrainAs(googleLoginButton) {
//					bottom.linkTo(kakaoLoginButton.top, margin = 12.dp)
//					start.linkTo(parent.start)
//					end.linkTo(parent.end)
//				},
//			onClick = onGoogleLoginClick,
//		)

		KakaoLoginButton(
			modifier = Modifier
				.navigationBarsPadding()
				.padding(horizontal = padding)
				.padding(bottom = 24.dp)
				.widthIn(max = 400.dp)
				.constrainAs(kakaoLoginButton) {
					bottom.linkTo(parent.bottom)
					start.linkTo(parent.start)
					end.linkTo(parent.end)
				},
			onClick = onkakaoLoginClick,
		)
	}
}
