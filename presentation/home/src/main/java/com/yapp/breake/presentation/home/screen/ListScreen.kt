package com.yapp.breake.presentation.home.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray100
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.core.designsystem.theme.Gray900
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.presentation.home.R
import com.yapp.breake.presentation.home.component.AppGroupItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ListScreen(
	appGroups: List<AppGroup>,
	onEditClick: (AppGroup) -> Unit,
	onAddClick: () -> Unit,
) {
	val listState = rememberLazyListState()
	val showTitle by remember {
		derivedStateOf {
			listState.firstVisibleItemIndex > 0
		}
	}
	val alpha by animateFloatAsState(
		targetValue = if (showTitle) 1f else 0f,
		animationSpec = tween(20, easing = LinearEasing),
		label = "appbarAlpha",
	)
	val container = Gray900.copy(alpha = alpha)
	val headerKey = "groupsHeader"

	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					AnimatedVisibility(
						visible = showTitle,
						modifier = Modifier.fillMaxWidth(),
						enter = fadeIn(),
						exit = fadeOut(),
					) {
						Text(
							text = stringResource(R.string.list_screen_title),
							style = BrakeTheme.typography.subtitle16SB,
							color = Gray100,
							textAlign = TextAlign.Center,
						)
					}
				},
				modifier = Modifier.animateContentSize(),
				navigationIcon = {
					// 공간만 차지하는 네비게이션 아이콘
					HorizontalSpacer(48.dp)
				},
				actions = {
					IconButton(onClick = onAddClick) {
						Icon(
							imageVector = Icons.Default.Add,
							contentDescription = stringResource(R.string.add_button_content_description),
						)
					}
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = container,
					scrolledContainerColor = container,
					navigationIconContentColor = Gray100,
					titleContentColor = Gray100,
					actionIconContentColor = Gray100,
				),
			)
		},
		content = { innerPadding ->
			CompositionLocalProvider(LocalOverscrollFactory provides null) {
				LazyColumn(
					modifier = Modifier
						.fillMaxSize()
						.padding(bottom = 16.dp),
					state = listState,
					horizontalAlignment = Alignment.CenterHorizontally,
				) {
					item {
						Column(
							modifier = Modifier
								.fillMaxWidth()
								.padding(horizontal = 16.dp),
							horizontalAlignment = Alignment.CenterHorizontally,
						) {
							Image(
								painter = painterResource(id = R.drawable.img_home_list),
								contentDescription = null,
								modifier = Modifier.fillMaxWidth(),
								contentScale = ContentScale.FillWidth,
							)
							VerticalSpacer(12.dp)
							Text(
								text = stringResource(R.string.list_screen_description),
								style = BrakeTheme.typography.subtitle22SB,
								color = Gray100,
								textAlign = TextAlign.Center,
							)
						}
					}

					stickyHeader(key = headerKey) {
						val isPinned by remember {
							derivedStateOf {
								val info = listState.layoutInfo.visibleItemsInfo
									.firstOrNull { it.key == headerKey }
								info?.offset == 0
							}
						}

						// 핀일 때만 AppBar 높이 적용, 아니면 0
						val topInset = innerPadding.calculateTopPadding()
						val spacer by animateDpAsState(
							targetValue = if (isPinned) topInset else 36.dp,
							animationSpec = tween(500, easing = LinearOutSlowInEasing),
							label = "HeaderTopInset",
						)
						Column(
							modifier = Modifier.background(container),
						) {
							VerticalSpacer(spacer)
							Row(
								verticalAlignment = Alignment.Bottom,
								modifier = Modifier
									.fillMaxWidth()
									.padding(top = 4.dp, bottom = 16.dp)
									.padding(horizontal = 24.dp),
							) {
								Text(
									text = stringResource(R.string.group),
									style = BrakeTheme.typography.subtitle22SB,
									color = MaterialTheme.colorScheme.onSurface,
								)
								HorizontalSpacer(1f)
								Text(
									text = stringResource(
										R.string.group_count_format,
										appGroups.size,
									),
									style = BrakeTheme.typography.body12M,
									color = Gray200,
								)
							}
						}
					}

					itemsIndexed(
						appGroups,
						key = { _, appGroup -> appGroup.id },
					) { index, appGroup ->
						AppGroupItem(
							appGroup = appGroup,
							onEditClick = { onEditClick(appGroup) },
							modifier = Modifier
								.fillMaxWidth()
								.padding(horizontal = 16.dp),
						)
						if (index != appGroups.lastIndex) {
							VerticalSpacer(12.dp)
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
