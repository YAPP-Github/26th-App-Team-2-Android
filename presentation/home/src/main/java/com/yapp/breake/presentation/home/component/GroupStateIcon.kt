package com.yapp.breake.presentation.home.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.BrakeYellow
import com.yapp.breake.core.designsystem.theme.Gray50
import com.yapp.breake.core.designsystem.theme.Gray800
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.presentation.home.R

internal enum class GroupStateIcon(
	val groupState: AppGroupState,
	val icon: @Composable (Modifier) -> Unit,
) {
	NEED_SETTING(
		groupState = AppGroupState.NeedSetting,
		icon = { modifier ->
			ChipIconTemplate(
				modifier = modifier,
				iconResId = R.drawable.ic_need_setting,
				contentResId = R.string.group_state_need_setting,
				contentColor = Gray50,
				contentDescription = "사용전",
			)
		},
	),
	USING(
		groupState = AppGroupState.Using,
		icon = { modifier ->
			ChipIconTemplate(
				modifier = modifier,
				iconResId = R.drawable.ic_using,
				contentResId = R.string.group_state_using,
				contentColor = BrakeYellow,
				contentDescription = "사용중",
			)
		},
	),
	BLOCKING(
		groupState = AppGroupState.Blocking,
		icon = { modifier ->
			ChipIconTemplate(
				modifier = modifier,
				contentColor = Color(0xFFFF6E31),
				iconResId = R.drawable.ic_blocking,
				contentResId = R.string.group_state_blocking,
				contentDescription = "차단중",
			)
		},
	),
}

@Composable
private fun ChipIconTemplate(
	modifier: Modifier = Modifier,
	contentColor: Color,
	@DrawableRes iconResId: Int,
	@StringRes contentResId: Int,
	contentDescription: String,
) {
	Row(
		modifier = modifier
			.clip(RoundedCornerShape(36.dp))
			.background(Gray800)
			.padding(horizontal = 12.dp)
			.padding(vertical = 4.dp),
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Icon(
			painter = painterResource(id = iconResId),
			contentDescription = contentDescription,
			tint = contentColor,
		)

		HorizontalSpacer(8.dp)

		Text(
			text = stringResource(id = contentResId),
			style = BrakeTheme.typography.subtitle14SB,
			color = contentColor,
			maxLines = 1,
		)
	}
}
