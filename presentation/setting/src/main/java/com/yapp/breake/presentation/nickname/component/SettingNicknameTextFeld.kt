package com.yapp.breake.presentation.nickname.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.BaseTextField
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.core.designsystem.theme.Green
import com.yapp.breake.core.designsystem.theme.Red
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.core.util.isValidInput

@Composable
internal fun SettingNicknameTextField(
	modifier: Modifier = Modifier,
	value: String,
	onValueChange: (String) -> Unit,
	label: String,
	placeholder: String,
	trailingIcon: Painter,
	warningGuideText: String,
	validGuideText: String,
	keyboardActions: KeyboardActions,
) {
	Column(
		modifier = modifier,
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp),
			horizontalArrangement = Arrangement.Absolute.SpaceBetween,
		) {
			Text(
				text = label,
				color = Gray200,
				style = BrakeTheme.typography.body16M,
			)

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

		VerticalSpacer(8.dp)

		BaseTextField(
			modifier = Modifier.fillMaxWidth(),
			value = value,
			onValueChange = onValueChange,
			placeholder = placeholder,
			trailingIcon = if (value.isValidInput()) trailingIcon else null,
			supportingText = {
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.Start,
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
				}
			},
			keyboardActions = keyboardActions,
		)
	}
}
