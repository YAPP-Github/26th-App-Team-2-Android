package com.teambrake.brake.overlay.timer.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.unit.sp
import com.teambrake.brake.core.designsystem.component.GradientScaffold
import com.teambrake.brake.core.designsystem.component.LargeButton
import com.teambrake.brake.core.designsystem.component.VerticalSpacer
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.Gray400
import com.teambrake.brake.core.designsystem.theme.LinerGradient
import com.teambrake.brake.core.util.extensions.toLocalizedTime
import com.teambrake.brake.overlay.ui.R
import java.time.LocalDateTime
import com.teambrake.brake.overlay.ui.R as UiRes

@Composable
internal fun SetCompleteScreen(
	durationMinutes: Int,
	endTime: LocalDateTime,
	onCloseOverlay: () -> Unit,
) {
	GradientScaffold(
		gradient = LinerGradient,
		bottomBar = {
			Column(
				modifier = Modifier.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				LargeButton(
					text = stringResource(id = UiRes.string.btn_use),
					onClick = onCloseOverlay,
					modifier = Modifier
						.padding(horizontal = 16.dp),
				)
				VerticalSpacer(28.dp)
			}
		},
	) {
		Column(
			modifier = Modifier
				.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Image(
				painter = painterResource(UiRes.drawable.img_time_set),
				contentDescription = stringResource(
					id = R.string.blocking_image_content_description,
				),
				modifier = Modifier
					.padding(horizontal = 110.dp)
					.fillMaxWidth()
					.aspectRatio(1f),
			)
			VerticalSpacer(12.dp)
			Row(
				verticalAlignment = Alignment.Bottom,
			) {
				Text(
					text = durationMinutes.toString(),
					style = BrakeTheme.typography.subtitle24SB,
					textAlign = TextAlign.Center,
					fontSize = 64.sp,
					color = MaterialTheme.colorScheme.onBackground,
				)
				Text(
					text = stringResource(id = UiRes.string.minute),
					style = BrakeTheme.typography.subtitle24SB,
					textAlign = TextAlign.Center,
					color = Gray400,
					modifier = Modifier.padding(start = 1.dp, bottom = 12.dp),
				)
			}
			VerticalSpacer(12.dp)
			Text(
				text = stringResource(id = UiRes.string.timer_complete_time, endTime.toLocalizedTime()),
				style = BrakeTheme.typography.subtitle24SB,
				textAlign = TextAlign.Center,
				color = MaterialTheme.colorScheme.onBackground,
				modifier = Modifier.fillMaxWidth(),
			)
		}
	}
}

@Preview
@Composable
private fun SetCompleteScreenPreview() {
	BrakeTheme {
		SetCompleteScreen(
			durationMinutes = 30,
			endTime = LocalDateTime.now(),
			onCloseOverlay = {},
		)
	}
}
