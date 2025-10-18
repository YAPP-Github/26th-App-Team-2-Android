package com.teambrake.brake.overlay.timer.component

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
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import kotlinx.collections.immutable.toImmutableList

@Composable
fun TimePicker(
	modifier: Modifier = Modifier,
	rowCount: Int = 5,
	onSnappedTime: (snappedTime: Int) -> Unit = {},
	onScrollStateChanged: (isScrolling: Boolean) -> Unit = {},
) {

	val minutes = (1..12).map { index ->
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
					startIndex = (Int.MAX_VALUE / 2) - 2,
					onItemSelected = { selectedText ->
						val selectedMinute = minutes.find { it.text == selectedText }
						selectedMinute?.let { minute ->
							onSnappedTime(minute.value)
						}
					},
					onScrollStateChanged = onScrollStateChanged,
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
			onSnappedTime = { time ->
			},
		)
	}
}
