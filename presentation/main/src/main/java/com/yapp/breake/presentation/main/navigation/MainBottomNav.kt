package com.yapp.breake.presentation.main.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray700
import com.yapp.breake.core.designsystem.theme.Gray800
import com.yapp.breake.core.designsystem.theme.White
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun MainBottomNavBar(
	visible: Boolean,
	tabs: ImmutableList<MainTab>,
	currentTab: MainTab?,
	onTabSelected: (MainTab) -> Unit,
	modifier: Modifier = Modifier,
) {
	AnimatedVisibility(
		visible = visible,
		enter = fadeIn() + slideIn { IntOffset(0, it.height) },
		exit = fadeOut() + slideOut { IntOffset(0, it.height) },
	) {
		Row(
			modifier = modifier
				// REPORT 개발 후 width 조정 필요
				.fillMaxWidth(0.52f)
				.widthIn(max = 216.dp)
				.wrapContentHeight()
				.background(
					color = Gray800,
					shape = RoundedCornerShape(60.dp),
				)
				.padding(horizontal = 28.dp, vertical = 16.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically,
		) {
			tabs.forEach { tab ->
				MainBottomNavItem(
					tab = tab,
					selected = tab == currentTab,
					onClick = { onTabSelected(tab) },
				)
			}
		}
	}
}

@Composable
private fun RowScope.MainBottomNavItem(
	tab: MainTab,
	selected: Boolean,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.weight(1f)
			.selectable(
				selected = selected,
				indication = null,
				role = null,
				interactionSource = remember { MutableInteractionSource() },
				onClick = onClick,
			),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Icon(
			painter = painterResource(tab.iconResId),
			contentDescription = tab.contentDescription,
			modifier = Modifier.size(20.dp),
			tint = if (selected) {
				White
			} else {
				Gray700
			},
		)
		Spacer(modifier = Modifier.height(4.dp))
		Text(
			text = tab.contentDescription,
			style = BrakeTheme.typography.body12M,
			color = if (selected) {
				White
			} else {
				Gray700
			},
		)
	}
}
