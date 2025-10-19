package com.teambrake.brake.core.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.Gray850
import com.teambrake.brake.core.designsystem.theme.Green
import com.teambrake.brake.core.designsystem.theme.White
import com.teambrake.brake.core.designsystem.util.BooleanProvider

@Composable
fun BrakeSwitch(
	checked: Boolean,
	onCheckedChange: ((Boolean) -> Unit),
	modifier: Modifier = Modifier,
	color: Color = Green,
) {
	CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
		Switch(
			checked = checked,
			colors = SwitchDefaults.colors(
				checkedThumbColor = White,
				uncheckedThumbColor = White,
				checkedTrackColor = color,
				uncheckedTrackColor = Gray850,
				checkedBorderColor = color,
				uncheckedBorderColor = Gray850,
			),
			thumbContent = {
				Canvas(
					modifier = Modifier.fillMaxSize(),
				) {
					drawCircle(
						color = White,
						radius = 38f,
						center = center,
					)
				}
			},
			onCheckedChange = onCheckedChange,
			modifier = modifier,
		)
	}
}

@Preview(showBackground = true)
@Composable
private fun SwitchPreview(
	@PreviewParameter(BooleanProvider::class) checked: Boolean,
) {
	BrakeTheme {
		BrakeSwitch(
			checked = checked,
			onCheckedChange = {},
		)
	}
}
