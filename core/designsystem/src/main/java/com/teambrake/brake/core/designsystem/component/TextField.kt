package com.teambrake.brake.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.Gray600
import com.teambrake.brake.core.designsystem.theme.Gray850

@Composable
fun BaseTextField(
	modifier: Modifier = Modifier,
	value: String,
	onValueChange: (String) -> Unit,
	placeholder: String,
	singleLine: Boolean = true,
	leadingIcon: Painter? = null,
	trailingIcon: Painter? = null,
	supportingText: @Composable (() -> Unit)? = null,
	keyboardActions: KeyboardActions,
) {
	OutlinedTextField(
		modifier = modifier,
		value = value,
		onValueChange = onValueChange,
		placeholder = {
			Text(
				text = placeholder,
				color = Gray600,
				style = BrakeTheme.typography.body16M,
			)
		},
		singleLine = singleLine,
		leadingIcon = leadingIcon?.let {
			{
				Image(
					painter = it,
					contentDescription = null,
				)
			}
		},
		trailingIcon = trailingIcon?.let {
			{
				Image(
					painter = it,
					contentDescription = null,
				)
			}
		},
		supportingText = supportingText,
		shape = RoundedCornerShape(12.dp),
		colors = OutlinedTextFieldDefaults.colors(
			unfocusedBorderColor = Color.Transparent,
			focusedBorderColor = Color.Transparent,
			disabledBorderColor = Color.Transparent,
			errorBorderColor = Color.Transparent,
			focusedContainerColor = Gray850,
			unfocusedContainerColor = Gray850,
			disabledContainerColor = Gray850,
			errorContainerColor = Gray850,
		),
		keyboardActions = keyboardActions,
	)
}
