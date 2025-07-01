package com.yapp.breake.core.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray850
import com.yapp.breake.core.designsystem.theme.Green
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.core.designsystem.util.BooleanProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrakeSwitch(
	checked: Boolean,
	onCheckedChange: ((Boolean) -> Unit),
	modifier: Modifier = Modifier,
	color: Color = Green,
) {
	CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
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
