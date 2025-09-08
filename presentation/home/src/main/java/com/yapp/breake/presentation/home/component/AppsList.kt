package com.yapp.breake.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.core.model.app.App
import com.yapp.breake.core.util.toImageBitmap
import com.yapp.breake.presentation.home.R

@Composable
internal fun AppsList(
	apps: List<App>,
	isDimmed: Boolean = false,
	showSummary: Boolean = false,
) {
	val colorFilter = if (isDimmed) {
		ColorFilter.colorMatrix(
			ColorMatrix().apply {
				setToSaturation(0.3f)
			},
		)
	} else {
		null
	}

	val maxDisplayCount = if (showSummary) 6 else apps.size
	val displayApps = if (showSummary) apps.take(maxDisplayCount) else apps

	FlowRow(
		horizontalArrangement = Arrangement.spacedBy(4.dp),
		verticalArrangement = Arrangement.spacedBy(4.dp),
		modifier = Modifier,
	) {
		displayApps.forEach { app ->
			val icon = app.icon?.toImageBitmap()
			if (icon == null) {
				Image(
					imageVector = Icons.Default.Circle,
					contentDescription = stringResource(R.string.default_app_icon_content_description),
					colorFilter = colorFilter,
					modifier = Modifier
						.size(24.dp),
				)
			} else {
				Image(
					bitmap = icon,
					contentDescription = stringResource(R.string.app_icon_content_description),
					colorFilter = colorFilter,
					modifier = Modifier
						.size(24.dp),
				)
			}
		}

		if (showSummary && apps.size > maxDisplayCount) {
			Text(
				text = "+${apps.size - maxDisplayCount}",
				style = BrakeTheme.typography.body14M,
				color = Gray200,
				modifier = Modifier.align(Alignment.CenterVertically),
			)
		}
	}
}

@Preview
@Composable
private fun AppsListPreview() {
	AppsList(
		apps = listOf(
			App(
				packageName = "com.app1",
				id = null,
				icon = null,
				name = "App 1",
				category = "Category 1",
			),
			App(
				packageName = "com.app2",
				id = null,
				icon = null,
				name = "App 2",
				category = "Category 2",
			),
		),
	)
}

@Preview
@Composable
private fun AppsListOverPreview() {
	AppsList(
		apps = listOf(
			App(packageName = "com.app1", id = null, icon = null, name = "App 1", category = "Category 1"),
			App(packageName = "com.app2", id = null, icon = null, name = "App 2", category = "Category 2"),
			App(packageName = "com.app3", id = null, icon = null, name = "App 3", category = "Category 3"),
			App(packageName = "com.app4", id = null, icon = null, name = "App 4", category = "Category 4"),
			App(packageName = "com.app5", id = null, icon = null, name = "App 5", category = "Category 5"),
			App(packageName = "com.app6", id = null, icon = null, name = "App 6", category = "Category 6"),
			App(packageName = "com.app7", id = null, icon = null, name = "App 7", category = "Category 7"),
		),
	)
}

@Preview
@Composable
private fun AppsListFullPreview() {
	AppsList(
		apps = listOf(
			App(packageName = "com.app1", id = null, icon = null, name = "App 1", category = "Category 1"),
			App(packageName = "com.app2", id = null, icon = null, name = "App 2", category = "Category 2"),
			App(packageName = "com.app3", id = null, icon = null, name = "App 3", category = "Category 3"),
			App(packageName = "com.app4", id = null, icon = null, name = "App 4", category = "Category 4"),
			App(packageName = "com.app5", id = null, icon = null, name = "App 5", category = "Category 5"),
			App(packageName = "com.app6", id = null, icon = null, name = "App 6", category = "Category 6"),
			App(packageName = "com.app7", id = null, icon = null, name = "App 7", category = "Category 7"),
		),
		showSummary = false,
	)
}
