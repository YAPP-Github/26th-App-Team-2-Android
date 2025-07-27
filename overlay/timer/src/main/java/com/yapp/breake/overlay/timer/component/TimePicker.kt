package com.yapp.breake.overlay.timer.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import kotlinx.collections.immutable.toImmutableList

@Composable
fun TimePicker(
	modifier: Modifier = Modifier,
	startMinute: Int = 5,
	minMinute: Int = 5,
	maxMinute: Int = 60,
	stepMinute: Int = 5,
	onSnappedTime: (minutes: Int) -> Unit = {},
) {
	var snappedMinute by remember { mutableIntStateOf(startMinute) }

	val minutes = (minMinute..maxMinute step stepMinute).map {
		Minute(
			text = it.toString().padStart(2, '0'),
			value = it,
			index = (it - minMinute) / stepMinute,
		)
	}

	Picker(
		modifier = modifier.padding(vertical = 20.dp),
		texts = minutes.map { it.text }.toImmutableList(),
		count = minutes.size,
		startIndex = minutes.find { it.value == startMinute }?.index?.plus(300) ?: 0,
		onItemSelected = { snappedText ->
			val newMinute = minutes.find { it.text == snappedText }?.value ?: 0

			if (newMinute in minMinute..maxMinute) {
				snappedMinute = newMinute
			}
			onSnappedTime(snappedMinute)
		},
	)
}

private data class Minute(
	val text: String,
	val value: Int,
	val index: Int,
)

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TimePickerPreview() {
	BrakeTheme {
		TimePicker(
			startMinute = 10,
			minMinute = 5,
			maxMinute = 60,
			stepMinute = 5,
			onSnappedTime = { minutes ->
				println("Selected minutes: $minutes")
			},
		)
	}
}
