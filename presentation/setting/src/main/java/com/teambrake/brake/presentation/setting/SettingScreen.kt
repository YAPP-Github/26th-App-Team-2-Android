package com.teambrake.brake.presentation.setting

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teambrake.brake.core.designsystem.component.HorizontalSpacer
import com.teambrake.brake.core.designsystem.component.CircleImage
import com.teambrake.brake.core.designsystem.component.SettingRow
import com.teambrake.brake.core.designsystem.component.VerticalSpacer
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.Gray500
import com.teambrake.brake.core.designsystem.theme.Gray600
import com.teambrake.brake.core.designsystem.theme.Gray800
import com.teambrake.brake.core.designsystem.theme.Gray850
import com.teambrake.brake.core.designsystem.theme.LocalPadding
import com.teambrake.brake.core.designsystem.theme.White
import com.teambrake.brake.core.navigation.compositionlocal.LocalMainAction
import com.teambrake.brake.core.navigation.compositionlocal.LocalNavigatorAction
import com.teambrake.brake.core.navigation.compositionlocal.LocalNavigatorProvider
import com.teambrake.brake.core.ui.SnackBarState
import com.teambrake.brake.presentation.setting.component.DeleteWarningDialog
import com.teambrake.brake.presentation.setting.model.SettingEffect
import com.teambrake.brake.presentation.setting.model.SettingUiState

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
	val navProvider = LocalNavigatorProvider.current
	val mainAction = LocalMainAction.current

	BackHandler {
		if (uiState is SettingUiState.SettingDeletingAccount) {
			viewModel.cancelDeletingAccount()
		} else {
			navAction.popBackStack()
		}
	}

	if (uiState is SettingUiState.SettingDeletingAccount) {
		mainAction.OnShowLoading()
	}

	LaunchedEffect(true) {
		viewModel.navigationFlow.collect {
			when (it) {
				is SettingEffect.NavigateToLogin -> navAction.navigateToLogin(
					navProvider.getNavOptionsClearingBackStack(),
				)
				is SettingEffect.NavigateToNickname -> navAction.navigateToNickname()
				is SettingEffect.NavigateToOpinion -> navAction.navigateToOpinion()
				is SettingEffect.NavigateToInquiry -> navAction.navigateToInquiry()
				is SettingEffect.NavigateToPrivacyPolicy -> navAction.navigateToPrivacy()
				is SettingEffect.NavigateToTermsOfService -> navAction.navigateToTerms()
			}
		}
	}

	LaunchedEffect(true) {
		viewModel.snackBarFlow.collect {
			when (it) {
				is SnackBarState.Success -> {
					mainAction.onShowSuccessMessage(
						message = it.uiString.asString(context = context),
					)
				}

				is SnackBarState.Error -> {
					mainAction.onShowErrorMessage(
						message = it.uiString.asString(context = context),
					)
				}
			}
		}
	}

	when (uiState) {
		is SettingUiState.SettingLogoutWarning -> {
			mainAction.OnShowLogoutDialog(
				onConfirm = {
					viewModel.dismissDialog()
					viewModel.logout()
				},
				onDismiss = viewModel::dismissDialog,
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
		onOpinionClick = viewModel::showOpinion,
		onInquiryClick = viewModel::showInquiry,
		onPrivacyClick = viewModel::showPrivacyPolicy,
		onTermsClick = viewModel::showTermsOfService,
		onDeleteAccount = viewModel::tryDeleteAccount,
		onLogout = viewModel::tryLogout,
	)
}

@Composable
fun SettingScreen(
	screenHorizontalPadding: Dp,
	paddingValue: PaddingValues,
	uiState: SettingUiState,
	onChangeProfile: () -> Unit,
	onOpinionClick: () -> Unit,
	onInquiryClick: () -> Unit,
	onPrivacyClick: () -> Unit,
	onTermsClick: () -> Unit,
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
				onClick = onOpinionClick,
			)
			HorizontalDivider(thickness = 1.dp, color = Gray800)
			SettingRow(
				id = R.string.setting_inquiry,
				onClick = onInquiryClick,
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
				onClick = onPrivacyClick,
			)
			HorizontalDivider(thickness = 1.dp, color = Gray800)
			SettingRow(
				id = R.string.setting_terms_of_service,
				onClick = onTermsClick,
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
