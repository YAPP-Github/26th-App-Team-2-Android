package com.yapp.breake.presentation.permission.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.BaseDialog
import com.yapp.breake.core.designsystem.component.DialogButton
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray300
import com.yapp.breake.core.designsystem.theme.Gray800
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.presentation.permission.R

@Composable
fun OnShowAccessibilityAgreementDialog(
	onConfirm: () -> Unit,
	onDismiss: () -> Unit,
) {
	BaseDialog(
		onDismissRequest = {},
		dismissButton = {
			DialogButton(
				text = stringResource(R.string.permission_dialog_accessibility_agreement_dismiss_button),
				onClick = onDismiss,
				containerColor = Gray800,
				contentColor = White,
			)
		},
		confirmButton = {
			DialogButton(
				text = stringResource(R.string.permission_dialog_accessibility_agreement_confirm_button),
				onClick = onConfirm,
			)
		},
	) {
		BackHandler {
			onDismiss()
		}

		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Text(
				modifier = Modifier.fillMaxWidth(),
				text = stringResource(R.string.permission_dialog_accessibility_agreement_title),
				style = BrakeTheme.typography.subtitle22B,
				color = Gray300,
				textAlign = TextAlign.Center,
			)

			VerticalSpacer(24.dp)

			Text(
				modifier = Modifier.fillMaxWidth(),
				text = stringResource(R.string.permission_dialog_accessibility_agreement_body),
				style = BrakeTheme.typography.body16M,
				color = Gray300,
				textAlign = TextAlign.Center,
			)
		}
	}
}
