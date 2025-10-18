package com.teambrake.brake.presentation.home.screen

import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teambrake.brake.core.designsystem.component.HorizontalSpacer
import com.teambrake.brake.core.designsystem.component.VerticalSpacer
import com.teambrake.brake.core.designsystem.theme.AppItemGradient
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.Gray700
import com.teambrake.brake.core.designsystem.theme.Gray900
import com.teambrake.brake.core.designsystem.theme.LocalDynamicPaddings
import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.presentation.home.R
import com.teambrake.brake.presentation.home.component.AppGroupItem
import com.teambrake.brake.presentation.home.component.AppGroupSubtitle
import com.teambrake.brake.presentation.home.component.BlockingAppGroup
import com.teambrake.brake.presentation.home.component.UsingAppGroup
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun TickingScreen(
	appGroups: PersistentList<AppGroup>,
	onAddClick: () -> Unit,
	onEditClick: (AppGroup) -> Unit,
	onStopClick: (AppGroup) -> Unit,
) {
	val (tickingGroups, notUsingGroups) = remember(appGroups) {
		derivedStateOf {
			val blockingBuilder = persistentListOf<AppGroup>().builder()
			val remainingBuilder = persistentListOf<AppGroup>().builder()

			appGroups.forEach { appGroup ->
				if (appGroup.appGroupState != AppGroupState.NeedSetting) {
					blockingBuilder.add(appGroup)
				} else {
					remainingBuilder.add(appGroup)
				}
			}

			blockingBuilder.build() to remainingBuilder.build()
		}
	}.value
	val tickingPagerState = rememberPagerState(pageCount = { tickingGroups.size })
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

	val bottomPadding = LocalDynamicPaddings.current.paddings.bottomNavBarHeight

	// LazyColumn, LazyRow 의 오버 스크롤시 내부 요소의 늘어짐 방지
	CompositionLocalProvider(LocalOverscrollFactory provides null) {
		Scaffold(
			content = { paddingValues ->
				LazyColumn(
					modifier = Modifier
						.fillMaxSize()
						.padding(top = paddingValues.calculateTopPadding()),
					state = rememberLazyListState(),
				) {
					stickyHeader {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.fillMaxWidth()
								.background(Gray900)
								.padding(top = 16.dp)
								.padding(horizontal = 24.dp),
						) {
							Text(
								text = stringResource(R.string.group),
								style = BrakeTheme.typography.subtitle22SB,
								color = MaterialTheme.colorScheme.onSurface,
							)

							HorizontalSpacer(1f)

							IconButton(onClick = onAddClick) {
								Icon(
									imageVector = Icons.Default.Add,
									contentDescription = stringResource(R.string.add_button_content_description),
								)
							}
						}
					}

					if (tickingGroups.isNotEmpty()) {

						item { VerticalSpacer(24.dp) }

						item {
							// currentPage가 범위를 벗어나지 않도록 보장
							val safeCurrentPage = tickingPagerState.currentPage.coerceIn(0, tickingGroups.size - 1)

							AppGroupSubtitle(
								modifier = Modifier.padding(horizontal = 28.dp),
								titleResId = if (tickingGroups.getOrNull(safeCurrentPage)?.appGroupState == AppGroupState.Using) {
									R.string.group_state_using
								} else {
									R.string.group_state_blocking
								},
								currentIndex = safeCurrentPage + 1,
								totalCount = tickingGroups.size,
							)

							VerticalSpacer(16.dp)

							HorizontalPager(
								state = tickingPagerState,
								contentPadding = PaddingValues(horizontal = 28.dp),
								pageSpacing = 16.dp,
								modifier = Modifier.fillMaxWidth(),
							) { index ->
								if (tickingGroups[index].appGroupState == AppGroupState.Using) {
									val bg = painterResource(R.drawable.using_group_background)

									UsingAppGroup(
										appGroup = tickingGroups[index],
										onEditClick = { /* 사용중일 땐 그룹 수정 금지 */ },
										onStopClick = { onStopClick(tickingGroups[index]) },
										modifier = Modifier
											.fillMaxWidth()
											.drawBehind {
												with(bg) {
													draw(
														size = size,
														alpha = 1f,
														colorFilter = null,
													)
												}
											}
											.padding(horizontal = 24.dp)
											.padding(top = 16.dp, bottom = 10.dp),
									)
								} else {
									val bg = painterResource(R.drawable.blocking_group_background)

									BlockingAppGroup(
										appGroup = tickingGroups[index],
										onEditClick = { onEditClick(tickingGroups[index]) },
										modifier = Modifier
											.fillMaxWidth()
											.background(Gray700.copy(alpha = 0.3f))
											.drawBehind {
												with(bg) {
													draw(
														size = size,
														alpha = 1f,
														colorFilter = null,
													)
												}
											}
											.padding(horizontal = 24.dp)
											.padding(top = 16.dp, bottom = 19.dp),
									)
								}
							}
						}
					}

					if (notUsingGroups.isNotEmpty()) {
						item { VerticalSpacer(40.dp) }

						item {
							AppGroupSubtitle(
								modifier = Modifier.padding(horizontal = 28.dp),
								titleResId = R.string.group_title_need_setting,
								currentIndex = currentIndex,
								totalCount = notUsingGroups.size,
							)
						}

						item { VerticalSpacer(12.dp) }

						item {
							BoxWithConstraints {
								val containerWidth = this.maxWidth

								CompositionLocalProvider(LocalOverscrollFactory provides null) {
									LazyRow(
										modifier = Modifier.fillMaxWidth(),
										flingBehavior = ScrollableDefaults.flingBehavior(),
										state = needSettingState,
										contentPadding = PaddingValues(horizontal = 28.dp),
										horizontalArrangement = if (appGroups.size == 1) {
											Arrangement.Start
										} else {
											Arrangement.spacedBy(12.dp)
										},
									) {
										itemsIndexed(notUsingGroups) { index, appGroup ->
											AppGroupItem(
												appGroup = appGroup,
												onEditClick = { onEditClick(appGroup) },
												showSummary = true,
												modifier = Modifier
													.width(containerWidth * 0.6f)
													.background(AppItemGradient)
													.padding(16.dp),
											)
										}
									}
								}
							}
						}
					}

					item { VerticalSpacer(bottomPadding) }
				}
			},
		)
	}
}

@Preview
@Composable
private fun UsingScreenPreview() {
	BrakeTheme {
		TickingScreen(
			appGroups = persistentListOf(AppGroup.sample),
			onAddClick = { },
			onEditClick = { },
			onStopClick = { },
		)
	}
}
