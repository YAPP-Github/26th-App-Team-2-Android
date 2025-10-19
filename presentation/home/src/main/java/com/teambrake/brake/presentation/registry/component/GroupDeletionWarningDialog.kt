package com.teambrake.brake.presentation.registry.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.teambrake.brake.core.designsystem.component.TwoButtonDialog
import com.teambrake.brake.core.designsystem.component.VerticalSpacer
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.White
import com.teambrake.brake.presentation.home.R

@Composable
internal fun GroupDeletionWarningDialog(
	onDismissRequest: () -> Unit,
	onConfirm: () -> Unit,
) {
	TwoButtonDialog(
		onDismissRequest = onDismissRequest,
		dismissButtonText = stringResource(R.string.registry_warning_dialog_button_cancel),
		confirmButtonText = stringResource(R.string.registry_warning_dialog_button_approve),
		onConfirmButtonClick = onConfirm,
	) {
		Text(
			modifier = Modifier.fillMaxWidth(),
			text = stringResource(R.string.registry_warning_dialog_title),
			style = BrakeTheme.typography.subtitle22SB,
			color = White,
			textAlign = TextAlign.Center,
		)

		VerticalSpacer(8.dp)

		Text(
			modifier = Modifier.fillMaxWidth(),
			text = stringResource(R.string.registry_warning_dialog_description),
			style = BrakeTheme.typography.body16M,
			color = White,
			textAlign = TextAlign.Center,
		)
	}
}
