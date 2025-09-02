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
	modifier: Modifier = Modifier,
	innerModifier: Modifier = Modifier,
	appGroup: AppGroup,
	clickable: Boolean = true,
	onEditClick: () -> Unit,
) {
	AppGroupBox(
		modifier = modifier,
		innerModifier = innerModifier,
	) {
		AppGroupItemContent(
			appGroup = appGroup,
			clickable = clickable,
			onEditClick = onEditClick,
		)
	}
}

@Composable
internal fun AppGroupItemContent(
	modifier: Modifier = Modifier,
	appGroup: AppGroup,
	clickable: Boolean = true,
	onEditClick: () -> Unit,
	isDimmed: Boolean = false,
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
