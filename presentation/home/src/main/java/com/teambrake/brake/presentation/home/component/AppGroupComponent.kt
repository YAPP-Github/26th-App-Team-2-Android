package com.teambrake.brake.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.teambrake.brake.core.designsystem.component.HorizontalSpacer
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.presentation.home.R

@Composable
internal fun AppGroupBox(
	modifier: Modifier = Modifier,
	content: @Composable ColumnScope.() -> Unit,
) {
	Column(
		content = content,
		modifier = Modifier
			.clip(RoundedCornerShape(16.dp))
			.then(modifier),
	)
}

@Composable
internal fun AppGroupTitle(
	name: String,
	appGroupState: AppGroupState,
	clickable: Boolean,
	onEditClick: () -> Unit,
) {
	Row(
		verticalAlignment = Alignment.Top,
		modifier = Modifier.fillMaxWidth(),
	) {
		Text(
			text = name,
			style = BrakeTheme.typography.body16M,
			color = MaterialTheme.colorScheme.onSurface,
			maxLines = 1,
			modifier = Modifier.weight(1f),
			overflow = TextOverflow.Ellipsis,
		)
		HorizontalSpacer(8.dp)
		GroupStateIcon.entries.find { it.groupState == appGroupState }?.icon?.invoke(Modifier)
		HorizontalSpacer(8.dp)
		Image(
			painter = painterResource(R.drawable.ic_edit),
			contentDescription = stringResource(R.string.edit_app_group_icon_content_description),
			modifier = Modifier
				.size(24.dp)
				.clip(CircleShape)
				.clickable(
					enabled = clickable,
					onClick = onEditClick,
				),
			alpha = if (clickable) 1f else 0.4f,
		)
	}
}
