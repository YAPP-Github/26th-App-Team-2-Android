package com.yapp.breake.overlay.timer.component

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.BaseScaffold
import com.yapp.breake.core.designsystem.component.LargeButton
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.LinerGradient
import com.yapp.breake.core.util.addJosaEulReul
import com.yapp.breake.core.util.extensions.toLocalizedTime
import java.time.LocalDateTime
import com.yapp.breake.overlay.ui.R as UiRes

@Composable
internal fun TimerScreen(
	appName: String,
	onTimeChange: (Int) -> Unit,
	onComplete: () -> Unit,
) {
	BaseScaffold(
		bottomBar = {
			Column(
				modifier = Modifier.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				LargeButton(
					text = stringResource(id = UiRes.string.btn_complete),
					onClick = onComplete,
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
				modifier = Modifier
					.fillMaxWidth()
					.background(brush = LinerGradient),
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
				Text(
					text = AnnotatedString.fromHtml(
						stringResource(
							id = UiRes.string.timer_content,
							LocalDateTime.now().toLocalizedTime(),
						),
					),
					style = BrakeTheme.typography.body16M,
					textAlign = TextAlign.Center,
					color = MaterialTheme.colorScheme.onBackground,
					modifier = Modifier.fillMaxWidth(),
				)
			}
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center,
				modifier = Modifier
					.fillMaxWidth()
					.weight(1f),
			) {
				TimePicker(
					onSnappedTime = onTimeChange,
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
