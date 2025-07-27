package com.yapp.breake.overlay.timer.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalTime

@Composable
fun TimePicker(
	modifier: Modifier = Modifier,
	startTime: LocalTime = LocalTime.now(),
	rowCount: Int = 5,
	onSnappedTime: (snappedTime: Int) -> Unit = {},
) {

	val minutes = (0..11).map { index ->
		val value = index * 5
		Minute(
			text = value.toString(),
			value = value,
			index = index,
		)
	}

	Column {
		Box(
			modifier = modifier
				.fillMaxWidth()
				.padding(top = 20.dp),
			contentAlignment = Alignment.Center,
		) {
			Row(
				modifier = Modifier.padding(vertical = 20.dp),
			) {
				TextPicker(
					texts = minutes.map { it.text }.toImmutableList(),
					count = minutes.size,
					rowCount = rowCount,
					startIndex = minutes.find { minute ->
						minute.value <= startTime.minute && startTime.minute < minute.value + 5
					}?.index ?: 0,
					onItemSelected = {
						onSnappedTime(it.toInt())
						return@TextPicker
					},
				)
			}
		}
	}
}

private data class Minute(
	val text: String,
	val value: Int,
	val index: Int,
)

@Preview
@Composable
private fun TimePickerPreview() {
	BrakeTheme {
		TimePicker(
			startTime = LocalTime.of(0, 25),
			onSnappedTime = { time ->
			},
		)
	}
}
