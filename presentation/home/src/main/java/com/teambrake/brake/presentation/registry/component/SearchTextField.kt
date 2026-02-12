package com.teambrake.brake.presentation.registry.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teambrake.brake.core.designsystem.component.BaseTextField
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.presentation.home.R

@Composable
fun SearchTextField(
	modifier: Modifier = Modifier,
	value: String,
	onValueChange: (String) -> Unit,
	placeholder: String = stringResource(R.string.registry_search),
	keyboardActions: KeyboardActions,
) {
	BaseTextField(
		modifier = modifier,
		value = value,
		onValueChange = onValueChange,
		placeholder = placeholder,
		leadingIcon = painterResource(R.drawable.ic_search),
		keyboardActions = keyboardActions,
	)
}

@Preview
@Composable
private fun SearchTextFieldPreview() {
	BrakeTheme {
		SearchTextField(
			modifier = Modifier.fillMaxWidth(),
			value = "",
			onValueChange = {},
			keyboardActions = KeyboardActions(),
		)
	}
}
