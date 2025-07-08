package com.yapp.breake.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.breake.core.designsystem.component.KakaoLoginButton
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.LocalPadding
import com.yapp.breake.presentation.login.model.LoginUiState
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun LoginRoute(
	navigateToSignup: () -> Unit,
	navigateToHome: () -> Unit,
	onShowErrorSnackBar: (Throwable?) -> Unit,
	viewModel: LoginViewModel = hiltViewModel(),
) {
	val context = LocalContext.current
	val padding = LocalPadding.current.screenPaddingHorizontal

	LaunchedEffect(true) {
		viewModel.uiState.collectLatest {
			when (it) {
				LoginUiState.LoginIdle -> {}
				LoginUiState.LoginAsRegistered -> {
					viewModel.resetUiState()
					navigateToHome()
				}

				LoginUiState.LoginAsNewUser -> {
					viewModel.resetUiState()
					navigateToSignup()
				}

				LoginUiState.LoginInvalidUser -> {
					Toast.makeText(
						context,
						R.string.login_invalid_user_message,
						Toast.LENGTH_SHORT,
					).show()
				}
			}
		}
		viewModel.errorFlow.collect { onShowErrorSnackBar(it) }
	}

	LoginScreen(
		padding = padding,
		onLoginClick = {
			viewModel.loginWithKakao(context)
		},
	)
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
