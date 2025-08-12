package com.yapp.breake.presentation.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.util.extensions.getRemainingSeconds
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
internal fun ProgressTime(
	startTime: LocalDateTime?,
	endTime: LocalDateTime?,
	modifier: Modifier = Modifier,
	minuteTextStyle: TextStyle = BrakeTheme.typography.body16M.copy(
		fontSize = 54.sp,
	),
	startColor: Color = Color(0xFFB6C1E0),
	endColor: Color = Color(0xFFF2FF5E),
	textBottomPadding: Int = 8,
	centerContent: @Composable () -> Unit = { },
) {
	var remainingSeconds by remember(startTime, endTime) {
		mutableLongStateOf(getRemainingSeconds(endTime))
	}

	var progress by remember(startTime, endTime) {
		mutableFloatStateOf(0f)
	}

	val totalDurationSeconds = remember(startTime, endTime) {
		if (startTime != null && endTime != null && endTime.isAfter(startTime)) {
			ChronoUnit.SECONDS.between(startTime, endTime)
		} else {
			0L
		}
	}

	LaunchedEffect(endTime, startTime) {
		if (endTime != null && startTime != null && totalDurationSeconds > 0) {
			while (true) {
				val now = LocalDateTime.now()
				val newRemainingSeconds = getRemainingSeconds(endTime)
				val elapsedSeconds = ChronoUnit.SECONDS.between(startTime, now)

				remainingSeconds = newRemainingSeconds
				progress =
					(elapsedSeconds.toFloat() / totalDurationSeconds.toFloat()).coerceIn(0f, 1f)

				if (newRemainingSeconds <= 0) break
				delay(100)
			}
		}
	}

	Column(
		modifier = modifier.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		CircularProgressTimer(
			progress = progress,
			startColor = startColor,
			endColor = endColor,
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				centerContent()
				RemainingTimeTextDisplay(
					remainingSeconds = remainingSeconds,
					onTimeChange = { seconds ->
						remainingSeconds = seconds
					},
					endTime = endTime,
					modifier = Modifier,
					minuteTextStyle = minuteTextStyle,
					textBottomPadding = textBottomPadding,
				)
			}
		}
	}
}

@Preview
@Composable
private fun UsingTimePreview() {
	BrakeTheme {
		ProgressTime(
			startTime = LocalDateTime.now().minusMinutes(10).minusSeconds(30),
			endTime = LocalDateTime.now().plusMinutes(10).plusSeconds(30),
		)
	}
}

@Preview
@Composable
private fun UsingTimeWithoutMinutesPreview() {
	BrakeTheme {
		ProgressTime(
			startTime = LocalDateTime.now().minusSeconds(30),
			endTime = LocalDateTime.now().plusSeconds(45),
		)
	}
}
