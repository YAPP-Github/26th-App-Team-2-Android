package com.teambrake.brake.presentation.signup.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.teambrake.brake.core.designsystem.component.BaseTextField
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.Green
import com.teambrake.brake.core.designsystem.theme.Red
import com.teambrake.brake.core.designsystem.theme.White
import com.teambrake.brake.core.ui.isValidInput

@Composable
fun NicknameTextField(
	modifier: Modifier = Modifier,
	value: String,
	onValueChange: (String) -> Unit,
	placeholder: String = "",
	trailingIcon: Painter,
	warningGuideText: String,
	validGuideText: String,
	keyboardActions: KeyboardActions,
) {
	BaseTextField(
		modifier = modifier,
		value = value,
		onValueChange = onValueChange,
		placeholder = placeholder,
		trailingIcon = if (value.isValidInput()) trailingIcon else null,
		supportingText = {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Absolute.SpaceBetween,
			) {
				if (!value.isValidInput()) {
					Text(
						text = if (value.isEmpty()) {
							" "
						} else {
							warningGuideText
						},
						color = Red,
						style = BrakeTheme.typography.body12M,
					)
				} else {
					Text(
						text = validGuideText,
						color = Green,
						style = BrakeTheme.typography.body12M,
					)
				}
				Text(
					text = "${value.length} / 10",
					color = when {
						value.isValidInput() -> Green
						value.isEmpty() -> White
						else -> Red
					},
					style = BrakeTheme.typography.body12M,
				)
			}
		},
		keyboardActions = keyboardActions,
	)
}
