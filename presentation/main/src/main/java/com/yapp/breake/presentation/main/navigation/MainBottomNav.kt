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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun MainBottomNavBar(
	visible: Boolean,
	tabs: ImmutableList<MainTab>,
	currentTab: MainTab?,
	onTabSelected: (MainTab) -> Unit,
	modifier: Modifier = Modifier
) {
	AnimatedVisibility(
		visible = visible,
		enter = fadeIn() + slideIn { IntOffset(0, it.height) },
		exit = fadeOut() + slideOut { IntOffset(0, it.height) }
	) {
		Row(
			modifier = modifier
				.fillMaxWidth()
				.wrapContentHeight()
				.background(
					// TODO : 디자인시스템 컬러 적용
					color = Color(0xFF3A3D45),
					shape = RoundedCornerShape(60.dp),
				)
				.padding(horizontal = 52.dp, vertical = 16.dp),
			horizontalArrangement = Arrangement.spacedBy(48.dp),
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
	modifier: Modifier = Modifier
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
			modifier = Modifier.size(28.dp),
			tint = if (selected) {
				Color(0xFFFFFFFF) // TODO : 디자인시스템 컬러 적용
			} else {
				Color.Unspecified
			},
		)
		Spacer(modifier = Modifier.height(4.dp))
		Text(
			text = tab.contentDescription,
			style = MaterialTheme.typography.labelLarge,
			color = if (selected) {
				Color(0xFFFFFFFF) // TODO : 디자인시스템 컬러 적용
			} else {
				Color(0xFF5A5F6C) // TODO : 디자인시스템 컬러 적용
			},
		)
	}
}
