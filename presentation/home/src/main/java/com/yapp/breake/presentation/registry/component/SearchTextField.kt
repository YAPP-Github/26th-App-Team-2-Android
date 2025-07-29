package com.yapp.breake.presentation.registry.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.yapp.breake.core.designsystem.component.BaseTextField
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.presentation.home.R

@Composable
fun SearchTextField(
	modifier: Modifier = Modifier,
	value: String,
	onValueChange: (String) -> Unit,
	placeholder: String = "앱 또는 카테고리 검색",
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
