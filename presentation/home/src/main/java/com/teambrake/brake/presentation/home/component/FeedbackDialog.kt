package com.teambrake.brake.presentation.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teambrake.brake.core.designsystem.component.TwoButtonDialog
import com.teambrake.brake.core.designsystem.component.VerticalSpacer
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.Gray300
import com.teambrake.brake.presentation.home.R

@Composable
internal fun FeedbackDialog(
	onAccept: () -> Unit,
	onDismiss: () -> Unit,
	onDismissRequest: () -> Unit,
) {
	TwoButtonDialog(
		dismissButtonText = stringResource(R.string.feedback_dialog_dismiss),
		confirmButtonText = stringResource(R.string.feedback_dialog_accept),
		onDismissRequest = onDismissRequest,
		onConfirmButtonClick = onAccept,
		onDismissButtonClick = onDismiss,
		dismissOnClickOutside = false,
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.fillMaxWidth(),
		) {
			Text(
				text = stringResource(R.string.feedback_dialog_title),
				style = BrakeTheme.typography.subtitle22SB,
				color = MaterialTheme.colorScheme.onSurface,
				textAlign = TextAlign.Center,
				modifier = Modifier.fillMaxWidth(),
			)
			VerticalSpacer(8.dp)
			Text(
				text = stringResource(R.string.feedback_dialog_message),
				style = BrakeTheme.typography.body16M,
				color = Gray300,
				textAlign = TextAlign.Center,
				modifier = Modifier.fillMaxWidth(),
			)
		}
	}
}

@Preview
@Composable
private fun FeedbackDialogPreview() {
	BrakeTheme {
		FeedbackDialog(
			onAccept = {},
			onDismiss = {},
			onDismissRequest = {},
		)
	}
}
