package com.teambrake.brake.overlay.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teambrake.brake.core.designsystem.component.GradientScaffold
import com.teambrake.brake.core.designsystem.component.LargeButton
import com.teambrake.brake.core.designsystem.component.VerticalSpacer
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.LinerGradient
import com.teambrake.brake.core.util.addJosaEulReul
import com.teambrake.brake.overlay.timer.component.TimePicker
import timber.log.Timber
import com.teambrake.brake.overlay.ui.R as UiRes

@Composable
internal fun TimerScreen(
	appName: String,
	onTimeChange: (Int) -> Unit,
	onComplete: () -> Unit,
) {
	var isScrolling by remember { mutableStateOf(false) }

	GradientScaffold(
		gradient = LinerGradient,
		bottomBar = {
			Column(
				modifier = Modifier.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				LargeButton(
					text = stringResource(id = UiRes.string.btn_complete),
					onClick = onComplete,
					enabled = !isScrolling,
					modifier = Modifier
						.padding(horizontal = 16.dp),
				)
				VerticalSpacer(28.dp)
			}
		},
	) {
		Column(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center,
				modifier = Modifier.fillMaxWidth(),
			) {
				VerticalSpacer(80.dp)
				Text(
					text = stringResource(id = UiRes.string.timer_title, appName.addJosaEulReul()),
					style = BrakeTheme.typography.subtitle24SB,
					textAlign = TextAlign.Center,
					color = MaterialTheme.colorScheme.onBackground,
					modifier = Modifier.fillMaxWidth(),
				)
				VerticalSpacer(16.dp)
//				Text(
//					text = AnnotatedString.fromHtml(
//						stringResource(
//							id = UiRes.string.timer_content,
//							LocalDateTime.now().toLocalizedTime(),
//						),
//					),
//					style = BrakeTheme.typography.body16M,
//					textAlign = TextAlign.Center,
//					color = MaterialTheme.colorScheme.onBackground,
//					modifier = Modifier.fillMaxWidth(),
//				)
			}
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center,
				modifier = Modifier
					.fillMaxWidth()
					.weight(1f),
			) {
				TimePicker(
					onSnappedTime = {
						Timber.d("Snapped time: $it")
						onTimeChange(it)
					},
					onScrollStateChanged = { isScrolling = it },
				)
			}
		}
	}
}

@Preview
@Composable
private fun TimerScreenPreview() {
	BrakeTheme {
		TimerScreen(
			appName = "Sample App",
			onTimeChange = { /* Do nothing */ },
			onComplete = { /* Do nothing */ },
		)
	}
}
