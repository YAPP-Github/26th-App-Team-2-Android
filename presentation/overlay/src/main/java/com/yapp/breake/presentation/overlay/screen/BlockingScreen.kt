package com.yapp.breake.presentation.overlay.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.SmallButton
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.presentation.overlay.R

@Composable
internal fun BlockingRoute() {

}

@Composable
private fun BlockingScreen(
	snoozeCount: Int,
	snoozeEnabled: Boolean,
	onExit: () -> Unit,
	onSnooze: () -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Image(
				painter = painterResource(id = R.drawable.img_blocking),
				contentDescription = stringResource(
					id = R.string.blocking_image_content_description,
				),
			)
			VerticalSpacer(30.dp)
			Text(
				text = stringResource(id = R.string.blocking_title),
				style = BrakeTheme.typography.title24B,
				textAlign = TextAlign.Center,
				modifier = Modifier.fillMaxWidth(),
			)
			VerticalSpacer(30.dp)
		}
		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			SmallButton(
				text = if (snoozeEnabled) {
					stringResource(id = R.string.blocking_exit)
				} else {
					stringResource(id = R.string.blocking_complete)
				},
				onClick = onExit,
			)
			Text(
				text = if (snoozeEnabled) {
					stringResource(id = R.string.blocking_check_time, snoozeCount)
				} else {
					""
				},
				style = BrakeTheme.typography.subtitle16SB,
				color = Gray200,
				modifier = Modifier
					.clickable(onClick = onSnooze, enabled = snoozeEnabled)
					.padding(vertical = 13.dp, horizontal = 16.dp),
			)
			VerticalSpacer(20.dp)
		}
	}
}

@Preview
@Composable
fun BlockingScreenPreview() {
	BrakeTheme {
		BlockingScreen(
			snoozeCount = 2,
			snoozeEnabled = true,
			onExit = { /* Do nothing */ },
			onSnooze = { /* Do nothing */ },
		)
	}
}
