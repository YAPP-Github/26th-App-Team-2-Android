package com.yapp.breake.presentation.registry.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.drawable.toBitmap
import com.yapp.breake.core.designsystem.component.BaseScaffold
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.component.LargeButton
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray500
import com.yapp.breake.core.designsystem.theme.Gray700
import com.yapp.breake.core.designsystem.theme.Gray850
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.presentation.home.R
import com.yapp.breake.presentation.registry.component.LazyColumnScrollBar
import com.yapp.breake.presentation.registry.component.SearchTextField
import com.yapp.breake.presentation.registry.model.AppModel
import com.yapp.breake.presentation.registry.model.RegistryUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun AppRegistryScreen(
	padding: Dp,
	registryUiState: RegistryUiState.App,
	lazyColumnIndexFlow: SharedFlow<Int>,
	focusManager: FocusManager,
	onSearchTextChange: (String) -> Unit,
	onSearchApp: () -> Unit,
	onSelectApp: (Int) -> Unit,
	onRegisterApps: () -> Unit,
	onBackClick: () -> Unit,
) {
	val lazyListState = rememberLazyListState()

	BackHandler {
		onBackClick()
	}

	LaunchedEffect(true) {
		lazyColumnIndexFlow.collect {
			lazyListState.scrollToItem(index = it)
		}
	}

	BaseScaffold(
		contentPadding = PaddingValues(padding),
		bottomBar = {
			LargeButton(
				text = stringResource(R.string.registry_app_button),
				onClick = {
					focusManager.clearFocus()
					onRegisterApps()
				},
				modifier = Modifier
					.padding(bottom = 24.dp)
					.padding(horizontal = padding),
				// 최소 한 개의 앱이 선택되어야 버튼이 활성화됨
				enabled = registryUiState.apps.any { it.isSelected },
			)
		},
		modifier = Modifier
			.fillMaxSize()
			.statusBarsPadding()
			.navigationBarsPadding()
			.clickable(
				indication = null,
				interactionSource = remember { MutableInteractionSource() },
			) {
				focusManager.clearFocus()
			},
	) {
		ConstraintLayout(
			modifier = Modifier
				.fillMaxSize()
				.statusBarsPadding()
				.clickable(
					indication = null,
					interactionSource = remember { MutableInteractionSource() },
				) {
					focusManager.clearFocus()
				},
		) {
			val (title, searchBar, appList) = createRefs()

			Column(
				modifier = Modifier
					.constrainAs(title) {
						top.linkTo(parent.top)
						start.linkTo(parent.start)
						end.linkTo(parent.end)
					}
					.padding(top = 37.dp)
					.navigationBarsPadding(),
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				Text(
					text = stringResource(R.string.registry_app_titel),
					style = BrakeTheme.typography.body16M,
					color = White,
				)

				VerticalSpacer(8.dp)

				Text(
					text = "${registryUiState.apps.filter { it.isSelected }.size}개",
					style = BrakeTheme.typography.subtitle18SB,
					color = White,
				)
			}

			SearchTextField(
				modifier = Modifier
					.fillMaxWidth()
					.constrainAs(searchBar) {
						top.linkTo(title.bottom)
						start.linkTo(parent.start)
						end.linkTo(parent.end)
					}
					.padding(vertical = 26.dp),
				value = registryUiState.searchingText,
				onValueChange = onSearchTextChange,
				keyboardActions = KeyboardActions(
					onDone = {
						onSearchApp()
						focusManager.clearFocus()
					},
				),
			)
			Box(
				modifier = Modifier
					.padding(bottom = 83.dp)
					.clip(RoundedCornerShape(16.dp))
					.background(Gray850)
					.padding(16.dp)
					.constrainAs(appList) {
						top.linkTo(searchBar.bottom)
						bottom.linkTo(parent.bottom)
						start.linkTo(parent.start)
						end.linkTo(parent.end)
						height = Dimension.fillToConstraints
					}
					.clipToBounds(),
			) {
				LazyColumn(
					modifier = Modifier.fillMaxSize(),
					state = lazyListState,
					horizontalAlignment = Alignment.CenterHorizontally,
				) {
					items(registryUiState.apps.size) { index ->
						AppItem(
							app = registryUiState.apps[index],
							onSelectClick = { onSelectApp(index) },
						)
					}
				}
				LazyColumnScrollBar(
					lazyListState = lazyListState,
					hidable = true,
					color = Gray500,
					modifier = Modifier.fillMaxSize(),
				)
			}
		}
	}
}

@Composable
fun AppItem(
	app: AppModel,
	onSelectClick: () -> Unit,
) {
	val interactionSource = remember { MutableInteractionSource() }

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 12.dp, vertical = 8.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Start,
	) {
		RadioButton(
			selected = app.isSelected,
			onClick = onSelectClick,
			modifier = Modifier.size(24.dp),
			colors = RadioButtonColors(
				selectedColor = White,
				unselectedColor = Gray700,
				disabledSelectedColor = White,
				disabledUnselectedColor = Gray700,
			),
			interactionSource = interactionSource,
		)

		HorizontalSpacer(16.dp)

		Image(
			painter = BitmapPainter(
				app.icon?.toBitmap()?.asImageBitmap() ?: ImageBitmap(24, 24),
			),
			contentDescription = null,
			modifier = Modifier
				.size(28.dp)
				.clickable(
					indication = null,
					interactionSource = interactionSource,
					onClick = onSelectClick,
				),
		)

		HorizontalSpacer(12.dp)

		Text(
			text = app.name,
			modifier = Modifier
				.clickable(
					indication = null,
					interactionSource = interactionSource,
					onClick = onSelectClick,
				),
			style = BrakeTheme.typography.body16M,
			color = White,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
		)
	}
}

@Preview
@Composable
private fun AppScreenPreview() {
	val apps = persistentListOf(
		AppModel(
			name = "Instagram",
			packageName = "com.example.testapp",
			icon = null,
			isSelected = false,
		),
		AppModel(
			name = "Facebook",
			packageName = "com.example.testapp2",
			icon = null,
			isSelected = true,
		),
	)

	AppRegistryScreen(
		padding = 16.dp,
		registryUiState = RegistryUiState.App(
			groupId = 1L,
			groupName = "",
			apps = apps,
			selectedApps = persistentListOf(),
		),
		lazyColumnIndexFlow = MutableSharedFlow<Int>(),
		onSearchTextChange = {},
		onSearchApp = {},
		onSelectApp = {},
		onBackClick = {},
		onRegisterApps = {},
		focusManager = LocalFocusManager.current,
	)
}

@Preview
@Composable
private fun AppItemPreview() {
	val app = AppModel(
		name = "Instagram",
		packageName = "com.example.testapp",
		icon = null,
		isSelected = false,
	)

	AppItem(
		app = app,
		onSelectClick = {},
	)
}
