package com.yapp.breake.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.model.app.AppGroup

@Composable
internal fun AppGroupList(
	appGroups: List<AppGroup>,
	onEditClick: (AppGroup) -> Unit,
	modifier: Modifier = Modifier,
) {
	LazyColumn(
		verticalArrangement = Arrangement.spacedBy(12.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		items(appGroups) { appGroup ->
			AppGroupItem(
				appGroup = appGroup,
				onEditClick = {
					onEditClick(appGroup)
				},
				modifier = modifier.fillMaxWidth(),
			)
		}
	}
}

@Composable
private fun AppGroupItem(
	appGroup: AppGroup,
	onEditClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	AppGroupBox(
		modifier = modifier,
	) {
		AppGroupItemContent(
			appGroup = appGroup,
			onEditClick = onEditClick,
		)
	}
}

@Composable
internal fun AppGroupItemContent(
	appGroup: AppGroup,
	onEditClick: () -> Unit,
	modifier: Modifier = Modifier,
	isDimmed: Boolean = false,
) {
	Column(
		modifier = modifier,
	) {
		AppGroupTitle(
			name = appGroup.name,
			onEditClick = onEditClick,
		)
		VerticalSpacer(12.dp)
		AppsList(
			apps = appGroup.apps,
			isDimmed = isDimmed,
		)
	}
}

@Preview
@Composable
private fun AppGroupItemPreview() {
	BrakeTheme {
		AppGroupItem(
			appGroup = AppGroup.sample,
			onEditClick = { /* TODO: Handle edit click */ },
			modifier = Modifier
				.fillMaxWidth(),
		)
	}
}
