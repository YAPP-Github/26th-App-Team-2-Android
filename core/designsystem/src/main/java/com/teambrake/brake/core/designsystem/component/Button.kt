package com.teambrake.brake.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.teambrake.brake.core.designsystem.R
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.Gray200
import com.teambrake.brake.core.designsystem.theme.Gray700
import com.teambrake.brake.core.designsystem.theme.Gray800
import com.teambrake.brake.core.designsystem.theme.LocalDynamicPaddings
import com.teambrake.brake.core.designsystem.theme.Red
import com.teambrake.brake.core.designsystem.util.BooleanProvider
import com.teambrake.brake.core.designsystem.util.MultipleEventsCutter
import com.teambrake.brake.core.designsystem.util.get

@Composable
fun LargeButton(
	text: String,
	modifier: Modifier = Modifier,
	textStyle: TextStyle = BrakeTheme.typography.subtitle16B,
	paddingValues: PaddingValues = PaddingValues(vertical = 18.dp, horizontal = 16.dp),
	leadingIcon: @Composable (() -> Unit)? = null,
	colors: ButtonColors = ButtonDefaults.buttonColors(
		disabledContainerColor = Gray700,
		disabledContentColor = MaterialTheme.colorScheme.onPrimary,
	),
	onClick: () -> Unit,
	enabled: Boolean = true,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	val density = LocalDensity.current
	val dynamicPaddingsProvider = LocalDynamicPaddings.current

	Button(
		shape = MaterialTheme.shapes.large,
		colors = colors,
		contentPadding = paddingValues,
		enabled = enabled,
		onClick = { multipleEventsCutter.processEvent(onClick) },
		modifier = modifier
			.fillMaxWidth()
			.onGloballyPositioned { coordinates ->
				with(density) {
					dynamicPaddingsProvider.updateOneButtonHeight(
						coordinates.size.height.toDp() + (24 + 12).dp,
					)
				}
			},
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Center,
		) {
			leadingIcon?.let {
				leadingIcon()
				HorizontalSpacer(6.dp)
			}
			Text(
				text = text,
				style = textStyle,
			)
		}
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

@Composable
fun BoxButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	color: Color = Red,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Button(
		shape = MaterialTheme.shapes.medium,
		colors = ButtonDefaults.buttonColors(
			containerColor = color.copy(alpha = 0.18f),
			contentColor = color,
		),
		contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
		onClick = { multipleEventsCutter.processEvent(onClick) },
		modifier = modifier,
	) {
		Text(
			text = text,
			style = BrakeTheme.typography.body14M,
			textAlign = TextAlign.Center,
		)
	}
}

@Composable
fun CircleButton(
	@DrawableRes icon: Int,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	containerColor: Color = MaterialTheme.colorScheme.secondary,
	contentColor: Color = Gray800,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Surface(
		shape = CircleShape,
		color = containerColor,
		onClick = { multipleEventsCutter.processEvent(onClick) },
		modifier = modifier.size(56.dp),
	) {
		Icon(
			painter = painterResource(id = icon),
			contentDescription = null,
			tint = contentColor,
			modifier = Modifier.padding(8.dp),
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

@Preview("Box Button")
@Composable
private fun BoxButtonPreview() {
	BrakeTheme {
		BoxButton(
			text = "Button",
			onClick = { },
		)
	}
}

@Preview("Box Button")
@Composable
private fun CircleButtonPreview() {
	BrakeTheme {
		CircleButton(
			icon = R.drawable.ic_close,
			onClick = { },
		)
	}
}
