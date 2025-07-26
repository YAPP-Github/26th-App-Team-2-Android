package com.yapp.breake.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.theme.AppItemGradient
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.model.app.App
import com.yapp.breake.core.util.toImageBitmap
import com.yapp.breake.presentation.home.R

@Composable
internal fun AppGroupBox(
	modifier: Modifier = Modifier,
	content: @Composable ColumnScope.() -> Unit,
) {
	Box(
		modifier = modifier
			.clip(RoundedCornerShape(16.dp))
			.background(AppItemGradient),
	) {
		Column(
			content = content,
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
		)
	}
}

@Composable
internal fun AppGroupTitle(
	name: String,
	onEditClick: () -> Unit,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier.fillMaxWidth(),
	) {
		Image(
			painter = painterResource(R.drawable.ic_app_group),
			contentDescription = "app_group_icon",
		)
		HorizontalSpacer(8.dp)
		Text(
			text = name,
			style = BrakeTheme.typography.body16M,
			color = MaterialTheme.colorScheme.onSurface,
		)
		HorizontalSpacer(1f)
		Image(
			painter = painterResource(R.drawable.ic_edit),
			contentDescription = "edit_app_group_icon",
			modifier = Modifier
				.clickable(onClick = onEditClick),
		)
	}
}

@Composable
internal fun AppsList(
	apps: List<App>,
) {
	Row(
		horizontalArrangement = Arrangement.spacedBy(4.dp),
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 12.dp),
	) {
		apps.forEach { app ->
			val icon = app.icon?.toImageBitmap()
			if (icon == null) {
				Image(
					imageVector = Icons.Default.Circle,
					contentDescription = "default_app_icon",
					modifier = Modifier
						.size(24.dp),
				)
			} else {
				Image(
					bitmap = icon,
					contentDescription = "app_icon",
					modifier = Modifier
						.size(24.dp),
				)
			}
		}
	}
}
