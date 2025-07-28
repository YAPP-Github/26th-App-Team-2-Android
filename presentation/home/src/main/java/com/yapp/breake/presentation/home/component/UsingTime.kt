package com.yapp.breake.presentation.home.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray300
import com.yapp.breake.core.designsystem.theme.Gray400
import com.yapp.breake.core.util.extensions.getRemainingSeconds
import com.yapp.breake.core.util.extensions.toMinutesAndSeconds
import com.yapp.breake.presentation.home.R
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
internal fun UsingTime(
	endTime: LocalDateTime?,
	onStopClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	var remainingSeconds by remember(endTime) {
		mutableLongStateOf(endTime?.getRemainingSeconds() ?: 0L)
	}

	val totalDurationSeconds = remember(endTime) {
		endTime?.let { time ->
			val now = LocalDateTime.now()
			if (time.isAfter(now)) {
				ChronoUnit.SECONDS.between(now, time)
			} else {
				0L
			}
		} ?: 0L
	}

	val progress = if (totalDurationSeconds > 0) {
		1f - (remainingSeconds.toFloat() / totalDurationSeconds.toFloat())
	} else {
		0f
	}

	Column(
		modifier = modifier.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		CircularProgressTimer(
			progress = progress,
			strokeWidth = 8.dp,
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				Text(
					text = stringResource(R.string.remaining_usage_time),
					style = BrakeTheme.typography.body16M,
					color = Gray400,
				)
				RemainingTimeTextDisplay(
					remainingSeconds = remainingSeconds,
					onTimeChange = { seconds ->
						remainingSeconds = seconds
					},
					endTime = endTime,
				)
			}
		}

		VerticalSpacer(40.dp)
		StopButton(
			onStopClick = onStopClick,
			modifier = Modifier,
		)
	}
}

@Composable
private fun RemainingTimeTextDisplay(
	remainingSeconds: Long,
	onTimeChange: (Long) -> Unit,
	endTime: LocalDateTime?,
	modifier: Modifier = Modifier,
	minuteTextStyle: TextStyle = BrakeTheme.typography.body16M.copy(
		fontSize = 54.sp,
	),
	textBottomPadding: Int = 8,
) {

	LaunchedEffect(endTime) {
		endTime?.let { time ->
			onTimeChange(time.getRemainingSeconds())
			while (remainingSeconds > 0) {
				delay(1000)
				onTimeChange(time.getRemainingSeconds())
			}
		}
	}

	val (minutes, seconds) = remainingSeconds.toMinutesAndSeconds()

	AnimatedContent(
		targetState = minutes > 0,
		transitionSpec = { fadeIn() togetherWith fadeOut() },
		label = "remaining_time_animation",
		modifier = modifier,
	) { hasMinutes ->
		Row(
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.Bottom,
		) {
			if (hasMinutes) {
				Text(
					text = String.format(Locale.getDefault(), "%d", minutes),
					style = minuteTextStyle,
					color = MaterialTheme.colorScheme.onSurface,
				)
				Text(
					text = stringResource(R.string.minute),
					style = BrakeTheme.typography.body16M,
					color = Gray300,
					modifier = Modifier.padding(bottom = textBottomPadding.dp),
				)
				HorizontalSpacer(4.dp)
			}
			Text(
				text = String.format(Locale.getDefault(), "%02d", seconds),
				style = minuteTextStyle,
				color = MaterialTheme.colorScheme.onSurface,
			)
			Text(
				text = stringResource(R.string.second),
				style = BrakeTheme.typography.body16M,
				color = Gray300,
				modifier = Modifier.padding(bottom = textBottomPadding.dp),
			)
		}
	}
}

@Preview
@Composable
private fun UsingTimePreview() {
	BrakeTheme {
		UsingTime(
			endTime = LocalDateTime.now().plusMinutes(10).plusSeconds(30),
			onStopClick = { },
		)
	}
}

@Preview
@Composable
private fun UsingTimeWithoutMinutesPreview() {
	BrakeTheme {
		UsingTime(
			endTime = LocalDateTime.now().plusSeconds(45),
			onStopClick = { },
		)
	}
}
