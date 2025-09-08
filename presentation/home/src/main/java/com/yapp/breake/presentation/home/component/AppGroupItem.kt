package com.yapp.breake.presentation.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.model.app.AppGroup

@Composable
internal fun AppGroupItem(
	appGroup: AppGroup,
	onEditClick: () -> Unit,
	modifier: Modifier = Modifier,
	clickable: Boolean = true,
	showSummary: Boolean = false,
) {
	AppGroupBox(
		modifier = modifier,
	) {
		AppGroupItemContent(
			appGroup = appGroup,
			clickable = clickable,
			showSummary = showSummary,
			onEditClick = onEditClick,
		)
	}
}

@Composable
internal fun AppGroupItemContent(
	appGroup: AppGroup,
	onEditClick: () -> Unit,
	modifier: Modifier = Modifier,
	clickable: Boolean = true,
	isDimmed: Boolean = false,
	showSummary: Boolean = false,
) {
	Column(
		modifier = modifier,
	) {
		AppGroupTitle(
			name = appGroup.name,
			appGroupState = appGroup.appGroupState,
			clickable = clickable,
			onEditClick = onEditClick,
		)
		VerticalSpacer(12.dp)
		AppsList(
			apps = appGroup.apps,
			isDimmed = isDimmed,
			showSummary = showSummary,
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
