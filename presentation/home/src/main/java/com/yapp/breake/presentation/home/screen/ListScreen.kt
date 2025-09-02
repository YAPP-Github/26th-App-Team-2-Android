package com.yapp.breake.presentation.home.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.AppItemGradient
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.presentation.home.R
import com.yapp.breake.presentation.home.component.AppGroupItem
import com.yapp.breake.presentation.home.component.AppGroupSubtitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ListScreen(
	appGroups: List<AppGroup>,
	onEditClick: (AppGroup) -> Unit,
	onAddClick: () -> Unit,
) {
	val needSettingState = rememberLazyListState()
	// 현재 보이는 아이템의 index 추적
	val currentIndex by remember {
		derivedStateOf {
			// 첫 아이템 패딩 고려
			val lazyIndex = needSettingState.firstVisibleItemIndex
			val currentOffset = needSettingState.firstVisibleItemScrollOffset
			val adjustedIndex = when {
				currentOffset >= 80 -> lazyIndex + 1
				else -> lazyIndex
			}
			adjustedIndex + 1
		}
	}

	Scaffold(
		content = { paddingValue ->
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(paddingValue),
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				Image(
					painter = painterResource(id = R.drawable.img_home_list),
					contentDescription = null,
					modifier = Modifier.fillMaxWidth(),
					contentScale = ContentScale.FillWidth,
				)

				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 32.dp),
				) {
					Text(
						text = stringResource(R.string.group),
						style = BrakeTheme.typography.subtitle22SB,
						color = MaterialTheme.colorScheme.onSurface,
					)

					HorizontalSpacer(1f)

					IconButton(
						onClick = onAddClick,
						modifier = Modifier.align(Alignment.CenterVertically),
					) {
						Icon(
							imageVector = Icons.Default.Add,
							contentDescription = stringResource(R.string.add_button_content_description),
						)
					}
				}

				VerticalSpacer(18.dp)

				AppGroupSubtitle(
					modifier = Modifier.padding(horizontal = 28.dp),
					titleResId = R.string.group_title_need_setting,
					currentIndex = currentIndex,
					totalCount = appGroups.size,
				)

				VerticalSpacer(12.dp)

				CompositionLocalProvider(LocalOverscrollFactory provides null) {
					LazyRow(
						flingBehavior = ScrollableDefaults.flingBehavior(),
						state = needSettingState,
						contentPadding = PaddingValues(horizontal = 28.dp),
						horizontalArrangement = Arrangement.spacedBy(12.dp),
					) {
						itemsIndexed(appGroups) { index, appGroup ->
							AppGroupItem(
								appGroup = appGroup,
								onEditClick = { onEditClick(appGroup) },
								modifier = Modifier
									.width(214.dp)
									.background(AppItemGradient),
								innerModifier = Modifier.padding(16.dp),
							)
						}
					}
				}
			}
		},
	)
}

@Preview
@Composable
private fun ListScreenPreview() {
	BrakeTheme {
		ListScreen(
			appGroups = listOf(AppGroup.sample),
			onEditClick = { /* TODO: Handle app group click */ },
			onAddClick = { /* Handle add click */ },
		)
	}
}
