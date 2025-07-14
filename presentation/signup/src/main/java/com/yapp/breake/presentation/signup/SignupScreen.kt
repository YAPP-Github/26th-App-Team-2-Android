package com.yapp.breake.presentation.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.breake.core.designsystem.component.BrakeTextField
import com.yapp.breake.core.designsystem.component.BrakeTopAppbar
import com.yapp.breake.core.designsystem.component.LargeButton
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.LocalPadding
import com.yapp.breake.core.designsystem.modifier.clearFocusOnKeyboardDismiss
import com.yapp.breake.core.util.isValidInput
import com.yapp.breake.presentation.signup.model.SignupEffect.NavigateToBack
import com.yapp.breake.presentation.signup.model.SignupEffect.NavigateToOnboarding
import kotlinx.coroutines.launch
import com.yapp.breake.core.designsystem.R as D

@Composable
fun SignupRoute(
	navigateToBack: () -> Unit,
	navigateToOnboarding: () -> Unit,
	onShowErrorSnackBar: (Throwable?) -> Unit,
	viewModel: SignupViewModel = hiltViewModel(),
) {
	val padding = LocalPadding.current.screenPaddingHorizontal
	val focusManager = LocalFocusManager.current
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	LaunchedEffect(true) {
		launch {
			viewModel.navigationFlow.collect {
				when (it) {
					NavigateToBack -> navigateToBack()
					NavigateToOnboarding -> navigateToOnboarding()
				}
			}
		}
		launch {
			viewModel.errorFlow.collect { onShowErrorSnackBar(it) }
		}
	}

	SignupScreen(
		padding = padding,
		focusManager = focusManager,
		typedName = uiState.name,
		onBackClick = viewModel::onBackPressed,
		onNameType = viewModel::onNameType,
		onContinueClick = viewModel::onNameSubmit,
	)
}

@Composable
fun SignupScreen(
	padding: Dp,
	focusManager: FocusManager,
	typedName: String,
	onBackClick: () -> Unit,
	onNameType: (String) -> Unit,
	onContinueClick: (String) -> Unit,
) {
	Scaffold(
		modifier = Modifier
			.navigationBarsPadding()
			.statusBarsPadding()
			.clickable(
				indication = null,
				interactionSource = remember { MutableInteractionSource() },
			) {
				focusManager.clearFocus()
			},
		topBar = {
			BrakeTopAppbar(
				onClick = onBackClick,
			)
		},
	) { paddingValues ->
		ConstraintLayout(
			modifier = Modifier
				.fillMaxSize()
				.padding(horizontal = padding)
				.padding(paddingValues = paddingValues),
		) {
			val (content, continueButton) = createRefs()

			Column(
				modifier = Modifier
					.constrainAs(content) {
						top.linkTo(parent.top)
						start.linkTo(parent.start)
						end.linkTo(parent.end)
					}
					.fillMaxSize(),
			) {
				VerticalSpacer(32.dp)

				Text(
					modifier = Modifier.padding(start = 16.dp),
					text = stringResource(R.string.signup_title_text),
					textAlign = TextAlign.Start,
					style = BrakeTheme.typography.title24B,
				)

				VerticalSpacer(4.dp)

				Text(
					modifier = Modifier.padding(start = 16.dp),
					text = stringResource(R.string.signup_subtitle_text),
					textAlign = TextAlign.Start,
					style = BrakeTheme.typography.body16M,
				)

				VerticalSpacer(36.dp)

				BrakeTextField(
					modifier = Modifier
						.fillMaxWidth()
						.clearFocusOnKeyboardDismiss(),
					value = typedName,
					onValueChange = {
						onNameType(it)
					},
					placeholder = stringResource(R.string.signup_text_field_placeholder_text),
					trailingIcon = painterResource(D.drawable.ic_check),
					warningGuideText = stringResource(R.string.signup_text_field_helper_warning_text),
					validGuideText = stringResource(R.string.signup_text_field_helper_valid_text),
				)
			}

			LargeButton(
				text = stringResource(R.string.signup_continue_button_text),
				onClick = { onContinueClick(typedName) },
				modifier = Modifier
					.constrainAs(continueButton) {
						bottom.linkTo(parent.bottom)
						start.linkTo(parent.start)
						end.linkTo(parent.end)
					}
					.padding(bottom = 24.dp)
					.imePadding(),
				// 임의 제한 조건
				enabled = typedName.isValidInput(),
			)
		}
	}
}
