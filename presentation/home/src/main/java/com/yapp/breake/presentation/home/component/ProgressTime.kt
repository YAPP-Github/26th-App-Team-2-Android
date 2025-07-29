package com.yapp.breake.presentation.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
internal fun ProgressTime(
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
			endTime = LocalDateTime.now().plusMinutes(10).plusSeconds(30),
		)
	}
}

@Preview
@Composable
private fun UsingTimeWithoutMinutesPreview() {
	BrakeTheme {
		ProgressTime(
			endTime = LocalDateTime.now().plusSeconds(45),
		)
	}
}
