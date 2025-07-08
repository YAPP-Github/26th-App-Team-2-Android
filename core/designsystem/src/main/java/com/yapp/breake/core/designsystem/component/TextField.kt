package com.yapp.breake.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray850
import com.yapp.breake.core.designsystem.theme.Green
import com.yapp.breake.core.designsystem.theme.Red
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.core.util.isValidInput

@Composable
fun BrakeTextField(
	modifier: Modifier = Modifier,
	value: String,
	onValueChange: (String) -> Unit,
	placeholder: String = "",
	trailingIcon: Painter,
	warningGuideText: String,
	validGuideText: String,
) {
	OutlinedTextField(
		modifier = modifier,
		value = value,
		onValueChange = {
			onValueChange(it)
		},
		placeholder = {
			Text(text = placeholder)
		},
		singleLine = true,
		trailingIcon = {
			if (value.isValidInput()) {
				Image(
					painter = trailingIcon,
					contentDescription = null,
				)
			}
		},
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
					color = value.isValidInput().let {
						if (it) Green else if (value.isEmpty()) White else Red
					},
					style = BrakeTheme.typography.body12M,
				)
			}
		},
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
	)
}
