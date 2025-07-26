package com.yapp.breake.presentation.home.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.presentation.home.R
import com.yapp.breake.presentation.home.component.AppGroupList
import com.yapp.breake.presentation.home.component.ImageTextBox

@Composable
internal fun ListScreen(
	appGroups: List<AppGroup>,
	onEditClick: (AppGroup) -> Unit,
) {
	Column(
		modifier = Modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		ImageTextBox(
			imageRes = R.drawable.img_home_list,
			text = stringResource(R.string.list_screen_description),
			modifier = Modifier,
		)
		VerticalSpacer(30.dp)
		Row(
			verticalAlignment = Alignment.Bottom,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 24.dp),
		) {
			Text(
				text = stringResource(R.string.group),
				style = BrakeTheme.typography.subtitle22SB,
				color = MaterialTheme.colorScheme.onSurface,
			)
			HorizontalSpacer(1f)
			Text(
				text = stringResource(R.string.group_count_format, appGroups.size),
				style = BrakeTheme.typography.body12M,
				color = Gray200,
			)
		}
		VerticalSpacer(16.dp)
		AppGroupList(
			appGroups = appGroups,
			onEditClick = onEditClick,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp),
		)
	}
}

@Preview
@Composable
private fun ListScreenPreview() {
	BrakeTheme {
		ListScreen(
			appGroups = listOf(AppGroup.sample),
			onEditClick = { /* TODO: Handle app group click */ },
		)
	}
}
