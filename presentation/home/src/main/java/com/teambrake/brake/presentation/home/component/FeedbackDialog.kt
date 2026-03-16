package com.teambrake.brake.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.teambrake.brake.core.designsystem.component.DialogButton
import com.teambrake.brake.core.designsystem.component.VerticalSpacer
import com.teambrake.brake.core.designsystem.modifier.clickableSingle
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.Gray300
import com.teambrake.brake.core.designsystem.theme.Gray800
import com.teambrake.brake.core.designsystem.theme.Gray850
import com.teambrake.brake.core.designsystem.theme.White
import com.teambrake.brake.presentation.home.R

@Composable
internal fun FeedbackDialog(
	onAccept: () -> Unit,
	onLater: () -> Unit,
	onReject: () -> Unit,
	onDismissRequest: () -> Unit,
) {
	Dialog(
		properties = DialogProperties(dismissOnClickOutside = true),
		onDismissRequest = onDismissRequest,
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.clip(RoundedCornerShape(20.dp))
				.background(Gray850),
		) {
			Column(
				modifier = Modifier.padding(16.dp),
			) {
				VerticalSpacer(30.dp)
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
				VerticalSpacer(42.dp)
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.spacedBy(
						6.dp,
						Alignment.CenterHorizontally,
					),
					verticalAlignment = Alignment.CenterVertically,
				) {
					Box(modifier = Modifier.weight(1f)) {
						DialogButton(
							text = stringResource(R.string.feedback_dialog_dismiss),
							onClick = onLater,
							containerColor = Gray800,
							contentColor = White,
						)
					}
					Box(modifier = Modifier.weight(1f)) {
						DialogButton(
							text = stringResource(R.string.feedback_dialog_accept),
							onClick = onAccept,
						)
					}
				}
				VerticalSpacer(12.dp)
				Text(
					text = stringResource(R.string.feedback_dialog_reject),
					style = BrakeTheme.typography.body14M,
					color = Gray300,
					textDecoration = TextDecoration.Underline,
					textAlign = TextAlign.Center,
					modifier = Modifier
						.fillMaxWidth()
						.clickableSingle(onReject),
				)
			}
		}
	}
}

@Preview
@Composable
private fun FeedbackDialogPreview() {
	BrakeTheme {
		FeedbackDialog(
			onAccept = {},
			onLater = {},
			onReject = {},
			onDismissRequest = {},
		)
	}
}
