package com.yapp.breake.presentation.home.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray300
import com.yapp.breake.core.util.extensions.getRemainingSeconds
import com.yapp.breake.core.util.extensions.toMinutesAndSeconds
import com.yapp.breake.presentation.home.R
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.util.Locale

@Composable
private fun AnimatedDigit(
	digit: Int,
	textStyle: TextStyle,
	modifier: Modifier = Modifier,
) {
	val density = LocalDensity.current
	val digitWidth = with(density) { textStyle.fontSize.toDp() * 0.6f }

	AnimatedContent(
		targetState = digit,
		transitionSpec = {
			slideInVertically { -it } + fadeIn() togetherWith
				slideOutVertically { it } + fadeOut()
		},
		label = "digit_animation",
		modifier = modifier.width(digitWidth),
	) { animatedDigit ->
		Text(
			text = animatedDigit.toString(),
			style = textStyle,
			color = MaterialTheme.colorScheme.onSurface,
			textAlign = TextAlign.Center,
		)
	}
}

@Composable
private fun AnimatedNumber(
	number: Long,
	textStyle: TextStyle,
	modifier: Modifier = Modifier,
	isMinute: Boolean = false,
) {
	val digits = if (isMinute) {
		number.toString().map { it.digitToInt() }
	} else {
		String.format(Locale.getDefault(), "%02d", number).map { it.digitToInt() }
	}

	Row(
		horizontalArrangement = Arrangement.Center,
		modifier = modifier,
	) {
		digits.forEach { digit ->
			AnimatedDigit(
				digit = digit,
				textStyle = textStyle,
			)
		}
	}
}

@Composable
internal fun RemainingTimeTextDisplay(
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
				delay(50)
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
				AnimatedNumber(
					number = minutes,
					textStyle = minuteTextStyle,
					isMinute = true,
				)
				Text(
					text = stringResource(R.string.minute),
					style = BrakeTheme.typography.body16M,
					color = Gray300,
					modifier = Modifier.padding(bottom = textBottomPadding.dp),
				)
				HorizontalSpacer(4.dp)
			}
			AnimatedNumber(
				number = seconds,
				textStyle = minuteTextStyle,
				isMinute = false,
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
private fun RemainingTimeDisplayPreview() {
	BrakeTheme {
		RemainingTimeTextDisplay(
			remainingSeconds = 330L,
			onTimeChange = {},
			endTime = LocalDateTime.now().plusMinutes(5).plusSeconds(30),
		)
	}
}
