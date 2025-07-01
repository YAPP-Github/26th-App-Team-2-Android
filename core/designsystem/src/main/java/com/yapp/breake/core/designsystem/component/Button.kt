package com.yapp.breake.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.core.designsystem.theme.Gray700
import com.yapp.breake.core.designsystem.util.BooleanProvider
import com.yapp.breake.core.designsystem.util.MultipleEventsCutter
import com.yapp.breake.core.designsystem.util.get

@Composable
fun LargeButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Button(
		shape = MaterialTheme.shapes.large,
		colors = ButtonDefaults.buttonColors(
			disabledContainerColor = Gray700,
			disabledContentColor = MaterialTheme.colorScheme.onPrimary,
		),
		contentPadding = PaddingValues(vertical = 18.dp, horizontal = 16.dp),
		enabled = enabled,
		onClick = { multipleEventsCutter.processEvent(onClick) },
		modifier = modifier.fillMaxWidth(),
	) {
		Text(
			text = text,
			style = BrakeTheme.typography.subtitle16B,
		)
	}
}

@Composable
fun SmallButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Button(
		shape = CircleShape,
		colors = ButtonDefaults.buttonColors(
			containerColor = MaterialTheme.colorScheme.primary,
			contentColor = MaterialTheme.colorScheme.onPrimary,
			disabledContainerColor = MaterialTheme.colorScheme.background,
			disabledContentColor = Gray200,
		),
		contentPadding = PaddingValues(vertical = 13.dp, horizontal = 16.dp),
		enabled = enabled,
		onClick = { multipleEventsCutter.processEvent(onClick) },
		modifier = modifier,
	) {
		Text(
			text = text,
			style = BrakeTheme.typography.subtitle16B,
			textAlign = TextAlign.Center,
			modifier = Modifier.defaultMinSize(minWidth = 120.dp),
		)
	}
}

@Preview("Large Buttons")
@Composable
private fun LargeButtonPreview(
	@PreviewParameter(BooleanProvider::class) enabled: Boolean,
) {
	BrakeTheme {
		LargeButton(
			text = "Large Button",
			onClick = { },
			enabled = enabled,
		)
	}
}

@Preview("Small Buttons")
@Composable
private fun SmallButtonPreview(
	@PreviewParameter(BooleanProvider::class) enabled: Boolean,
) {
	BrakeTheme {
		SmallButton(
			text = "Small Button",
			onClick = { },
			enabled = enabled,
		)
	}
}
