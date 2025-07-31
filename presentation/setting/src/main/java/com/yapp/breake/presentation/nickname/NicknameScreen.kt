package com.yapp.breake.presentation.nickname

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.breake.core.designsystem.component.BaseScaffold
import com.yapp.breake.core.designsystem.component.BrakeTopAppbar
import com.yapp.breake.core.designsystem.component.CircleImage
import com.yapp.breake.core.designsystem.component.LargeButton
import com.yapp.breake.core.designsystem.component.TopAppbarType
import com.yapp.breake.core.designsystem.theme.LocalPadding
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorAction
import com.yapp.breake.core.ui.isValidInput
import com.yapp.breake.presentation.nickname.component.SettingNicknameTextField
import com.yapp.breake.presentation.nickname.model.NicknameNavState
import com.yapp.breake.presentation.nickname.model.NicknameSnackBarState
import com.yapp.breake.presentation.nickname.model.NicknameUiState
import com.yapp.breake.presentation.setting.R
import com.yapp.breake.core.designsystem.R as DesignSystemR

@Composable
fun NicknameRoute(
	viewModel: NicknameViewModel = hiltViewModel(),
) {
	val padding = LocalPadding.current.screenPaddingHorizontal
	val uiState by viewModel.nicknameUiState.collectAsStateWithLifecycle()
	val navAction = LocalNavigatorAction.current
	val mainAction = LocalMainAction.current
	val context = LocalContext.current
	val focusManager = LocalFocusManager.current
	val density = LocalDensity.current
	val imeVisible = WindowInsets.ime.getBottom(density) > 0
	var prevVisible by remember { mutableStateOf(imeVisible) }

	// 키보드가 사라졌을 때 포커스를 해제
	SideEffect {
		if (prevVisible && !imeVisible) {
			focusManager.clearFocus()
		}
		prevVisible = imeVisible
	}

	BackHandler {
		if (uiState is NicknameUiState.NicknameUpdating) {
			viewModel.cancelUpdateNickname()
		}
	}

	if (uiState is NicknameUiState.NicknameUpdating) {
		mainAction.OnShowLoading()
	}

	LaunchedEffect(true) {
		viewModel.navigationFlow.collect {
			when (it) {
				NicknameNavState.NavigateToSetting -> navAction.popBackStack()
			}
		}
	}

	LaunchedEffect(true) {
		viewModel.snackBarFlow.collect {
			when (it) {
				is NicknameSnackBarState.Success -> {
					mainAction.onShowSuccessMessage(
						message = it.uiString.asString(context = context),
					)
				}

				is NicknameSnackBarState.Error -> {
					mainAction.onShowErrorMessage(
						message = it.uiString.asString(context = context),
					)
				}
			}
		}
	}

	NicknameScreen(
		padding = padding,
		focusManager = focusManager,
		nickname = uiState.nickname,
		onTypeNickname = viewModel::typeNickname,
		onCompleteClick = viewModel::updateNickname,
		onBackClick = navAction::popBackStack,
	)
}

@Composable
fun NicknameScreen(
	padding: Dp,
	focusManager: FocusManager,
	nickname: String,
	onTypeNickname: (String) -> Unit = {},
	onCompleteClick: () -> Unit,
	onBackClick: () -> Unit,
) {
	BaseScaffold(
		contentPadding = PaddingValues(horizontal = padding),
		topBar = {
			BrakeTopAppbar(
				title = stringResource(R.string.nickname_title),
				appbarType = TopAppbarType.Cancel,
				onClick = onBackClick,
			)
		},
	) {
		ConstraintLayout(
			modifier = Modifier.fillMaxSize(),
		) {
			val (image, input, button) = createRefs()

			CircleImage(
				modifier = Modifier
					.constrainAs(image) {
						top.linkTo(parent.top, margin = 48.dp)
						start.linkTo(parent.start)
						end.linkTo(parent.end)
					}
					.size(100.dp),
				imageUrl = null,
			)

			SettingNicknameTextField(
				modifier = Modifier
					.constrainAs(input) {
						top.linkTo(image.bottom, margin = 57.dp)
						start.linkTo(parent.start)
						end.linkTo(parent.end)
					},
				value = nickname,
				onValueChange = onTypeNickname,
				label = stringResource(R.string.nickname_input_label),
				placeholder = stringResource(R.string.nickname_input_hint),
				trailingIcon = painterResource(DesignSystemR.drawable.ic_check),
				warningGuideText = stringResource(R.string.nickname_input_warning_guide_text),
				validGuideText = stringResource(R.string.nickname_input_valid_guide_text),
				keyboardActions = KeyboardActions(
					onDone = {
						focusManager.clearFocus()
					},
				),
			)

			LargeButton(
				text = stringResource(R.string.nickname_button_complete),
				onClick = {
					focusManager.clearFocus()
					onCompleteClick()
				},
				modifier = Modifier
					.constrainAs(button) {
						bottom.linkTo(parent.bottom, margin = 24.dp)
						start.linkTo(parent.start)
						end.linkTo(parent.end)
					}
					.navigationBarsPadding()
					.imePadding(),
				// 임의 제한 조건
				enabled = nickname.isValidInput(),
			)
		}
	}
}
