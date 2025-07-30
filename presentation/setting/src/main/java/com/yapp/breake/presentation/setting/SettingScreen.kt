package com.yapp.breake.presentation.setting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.component.CircleImage
import com.yapp.breake.core.designsystem.component.DotProgressIndicator
import com.yapp.breake.core.designsystem.component.SettingRow
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray500
import com.yapp.breake.core.designsystem.theme.Gray600
import com.yapp.breake.core.designsystem.theme.Gray800
import com.yapp.breake.core.designsystem.theme.Gray850
import com.yapp.breake.core.designsystem.theme.Gray900
import com.yapp.breake.core.designsystem.theme.LocalPadding
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorAction
import com.yapp.breake.presentation.setting.component.DeleteWarningDialog
import com.yapp.breake.presentation.setting.component.LogoutWarningDialog
import com.yapp.breake.presentation.setting.model.SettingEffect
import com.yapp.breake.presentation.setting.model.SettingSnackBarState
import com.yapp.breake.presentation.setting.model.SettingUiState

@Composable
fun SettingRoute(
	paddingValue: PaddingValues,
	onChangeDarkTheme: (Boolean) -> Unit,
	viewModel: SettingViewModel = hiltViewModel(),
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val screenHorizontalPadding = LocalPadding.current.screenPaddingHorizontal
	val context = LocalContext.current
	val navAction = LocalNavigatorAction.current
	val mainAction = LocalMainAction.current

	LaunchedEffect(true) {
		viewModel.navigationFlow.collect {
			when (it) {
				is SettingEffect.NavigateToLogin -> {
					navAction.navigateToLogin(navAction.getNavOptionsClearingBackStack())
				}

				is SettingEffect.NavigateToNickname -> {
					navAction.navigateToNickname()
				}
			}
		}
	}

	LaunchedEffect(true) {
		viewModel.snackBarFlow.collect {
			when (it) {
				is SettingSnackBarState.Success -> {
					mainAction.onShowSuccessMessage(
						message = it.uiString.asString(context = context),
					)
				}

				is SettingSnackBarState.Error -> {
					mainAction.onShowErrorMessage(
						message = it.uiString.asString(context = context),
					)
				}
			}
		}
	}

	when (uiState) {
		is SettingUiState.SettingLogoutWarning -> {
			LogoutWarningDialog(
				onDismissRequest = viewModel::dismissDialog,
				onConfirm = {
					viewModel.dismissDialog()
					viewModel.logout()
				},
			)
		}

		is SettingUiState.SettingDeleteWarning -> {
			DeleteWarningDialog(
				onDismissRequest = viewModel::dismissDialog,
				onConfirm = {
					viewModel.dismissDialog()
					viewModel.deleteAccount()
				},
			)
		}

		else -> {}
	}

	SettingScreen(
		screenHorizontalPadding = screenHorizontalPadding,
		paddingValue = paddingValue,
		uiState = uiState,
		onChangeProfile = viewModel::modifyNickname,
		onDeleteAccount = viewModel::tryDeleteAccount,
		onLogout = viewModel::tryLogout,
	)

	if (uiState is SettingUiState.SettingDeletingAccount) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Gray900.copy(alpha = 0.9f))
				.pointerInput(Unit) {}
				.statusBarsPadding(),
			contentAlignment = Alignment.Center,
		) {
			BackHandler { viewModel.cancelDeletingAccount() }
			DotProgressIndicator()
		}
	}
}

@Composable
fun SettingScreen(
	screenHorizontalPadding: Dp,
	paddingValue: PaddingValues,
	uiState: SettingUiState,
	onChangeProfile: () -> Unit,
	onDeleteAccount: () -> Unit,
	onLogout: () -> Unit,
) {
	var scrollState = rememberScrollState()

	Column(
		modifier = Modifier
			.padding(screenHorizontalPadding)
			.padding(paddingValue)
			.scrollable(state = scrollState, orientation = Orientation.Vertical),
		horizontalAlignment = Alignment.Start,
	) {
		VerticalSpacer(52.dp)
		Box(
			modifier = Modifier.fillMaxWidth(),
			contentAlignment = Alignment.CenterStart,
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.Start,
			) {
				HorizontalSpacer(16.dp)

				CircleImage(
					modifier = Modifier
						.fillMaxWidth(0.166f)
						.widthIn(max = 64.dp),
					imageUrl = uiState.user.imageUrl,
				)

				HorizontalSpacer(20.dp)

				Text(
					text = if (uiState.user.name.isBlank()) {
						stringResource(R.string.setting_profile_name_not_set)
					} else {
						uiState.user.name
					},
					style = BrakeTheme.typography.subtitle22SB,
					color = White,
				)
			}

			Text(
				text = stringResource(R.string.setting_profile_image_change),
				modifier = Modifier
					.align(Alignment.CenterEnd)
					.clip(RoundedCornerShape(8.dp))
					.clickable(
						interactionSource = null,
						indication = LocalIndication.current,
						onClick = onChangeProfile,
					)
					.padding(horizontal = 16.dp, vertical = 6.dp),
				style = BrakeTheme.typography.body14SB,
				color = Gray500,
			)
		}

		VerticalSpacer(32.dp)

		Column(
			modifier = Modifier
				.clip(RoundedCornerShape(16.dp))
				.background(Gray850),
		) {
			SettingRow(
				id = R.string.setting_opinion_title,
				onClick = {},
			)
			HorizontalDivider(thickness = 1.dp, color = Gray800)
			SettingRow(
				id = R.string.setting_inquiry,
				onClick = {},
			)
		}

		VerticalSpacer(16.dp)

		Column(
			modifier = Modifier
				.clip(RoundedCornerShape(16.dp))
				.background(Gray850),
		) {
			SettingRow(
				id = R.string.setting_privacy_policy,
				onClick = {},
			)
			HorizontalDivider(thickness = 1.dp, color = Gray800)
			SettingRow(
				id = R.string.setting_terms_of_service,
				onClick = {},
			)
			HorizontalDivider(thickness = 1.dp, color = Gray800)
			SettingRow(
				id = R.string.setting_app_version,
				onClick = {},
			) {
				Text(
					text = uiState.appInfo.version,
					style = BrakeTheme.typography.body14M,
					color = Gray600,
				)
			}
		}

		VerticalSpacer(16.dp)

		Column(
			modifier = Modifier
				.clip(RoundedCornerShape(16.dp))
				.background(Gray850),
		) {
			SettingRow(
				id = R.string.setting_logout,
				onClick = onLogout,
			)
			HorizontalDivider(thickness = 1.dp, color = Gray800)
			SettingRow(
				id = R.string.setting_delete_account,
				onClick = onDeleteAccount,
			)
		}
	}
}
