package com.yapp.breake.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.R
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray300

@Composable
fun SettingRow(
	@StringRes id: Int,
	onClick: () -> Unit,
	trailing: @Composable (() -> Unit) = {
		Image(
			painter = painterResource(R.drawable.ic_arraow),
			contentDescription = null,
		)
	},
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clickable(
				enabled = true,
				onClick = onClick,
				indication = LocalIndication.current,
				interactionSource = null,
			)
			.padding(16.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Absolute.SpaceBetween,
	) {
		Text(
			text = stringResource(id),
			style = BrakeTheme.typography.body16M,
			color = Gray300,
			modifier = Modifier,
		)

		trailing()
	}
}

@Preview
@Composable
fun SettingRowPreview() {
	BrakeTheme {
		SettingRow(
			id = R.string.preview_string,
			onClick = {},
		)
	}
}
