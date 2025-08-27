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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.drawable.toBitmap
import com.yapp.breake.core.designsystem.component.BaseScaffold
import com.yapp.breake.core.designsystem.component.BrakeTopAppbar
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.component.LargeButton
import com.yapp.breake.core.designsystem.component.TopAppbarType
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.modifier.clickableSingle
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.ButtonYellow
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.core.designsystem.theme.Gray300
import com.yapp.breake.core.designsystem.theme.Gray500
import com.yapp.breake.core.designsystem.theme.Gray800
import com.yapp.breake.core.designsystem.theme.Gray850
import com.yapp.breake.core.designsystem.theme.Gray900
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.core.ui.isValidInput
import com.yapp.breake.presentation.home.R
import com.yapp.breake.presentation.registry.component.GroupNameTextField
import com.yapp.breake.presentation.registry.component.LazyColumnScrollBar
import com.yapp.breake.presentation.registry.model.AppModel
import com.yapp.breake.presentation.registry.model.RegistryUiState
import com.yapp.breake.presentation.registry.model.SelectedAppModel
import kotlinx.collections.immutable.persistentListOf
import com.yapp.breake.core.designsystem.R as DesignSystemRes

@Composable
fun GroupRegistryScreen(
	padding: Dp,
	registryUiState: RegistryUiState,
	focusManager: FocusManager,
	onGroupNameChange: (String) -> Unit,
	onStartSelectingApps: () -> Unit,
	onRemoveApp: (Int) -> Unit,
	onRemoveGroup: () -> Unit,
	onRegisterGroup: () -> Unit,
	onBackClick: () -> Unit,
) {
	BackHandler {
		onBackClick()
	}

	BaseScaffold(
		contentPadding = PaddingValues(padding),
		topBar = {
			BrakeTopAppbar(
				title = stringResource(R.string.registry_group_screen_title),
				appbarType = TopAppbarType.Cancel,
				onClick = onBackClick,
			)
		},
		bottomBar = {
			LargeButton(
				text = stringResource(R.string.registry_group_completion_button),
				onClick = {
					focusManager.clearFocus()
					onRegisterGroup()
				},
				modifier = Modifier
					.padding(bottom = 24.dp)
					.padding(horizontal = padding),
				// 최소 한 개의 앱이 선택되어야 버튼이 활성화됨
				enabled = registryUiState.selectedApps.isNotEmpty() &&
					registryUiState.groupName.isValidInput(),
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
				.statusBarsPadding(),
		) {
			val (textField, listTitle, groupList, removeButton) = createRefs()

			GroupNameTextField(
				modifier = Modifier
					.fillMaxWidth()
					.constrainAs(textField) {
						top.linkTo(parent.top)
						start.linkTo(parent.start)
						end.linkTo(parent.end)
					}
					.padding(top = 28.dp),
				value = registryUiState.groupName,
				onValueChange = onGroupNameChange,
				trailingIcon = painterResource(DesignSystemRes.drawable.ic_check),
				warningGuideText = stringResource(R.string.registry_textfield_warning_text),
				validGuideText = stringResource(R.string.registry_textfield_validation_text),
				keyboardActions = KeyboardActions(
					onDone = {
						focusManager.clearFocus()
					},
				),
			)

			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = padding)
					.padding(top = 28.dp)
					.constrainAs(listTitle) {
						top.linkTo(textField.bottom)
						start.linkTo(parent.start)
						end.linkTo(parent.end)
					},
				horizontalArrangement = Arrangement.Absolute.SpaceBetween,
			) {
				Text(
					text = "목록",
					color = Gray200,
					style = BrakeTheme.typography.body16M,
				)

				Text(
					text = "${registryUiState.selectedApps.size} 개",
					color = White,
					style = BrakeTheme.typography.body12M,
				)
			}

			if (registryUiState is RegistryUiState.Group.Initial && registryUiState.selectedApps.isEmpty()) {
				Column(
					modifier = Modifier
						.padding(top = 8.dp)
						.fillMaxWidth()
						.constrainAs(groupList) {
							top.linkTo(listTitle.bottom)
							bottom.linkTo(parent.bottom)
							start.linkTo(parent.start)
							end.linkTo(parent.end)
							height = Dimension.fillToConstraints
						}
						.clip(RoundedCornerShape(16.dp))
						.background(Gray850),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Center,
				) {
					Text(
						text = stringResource(R.string.registry_group_list_guide),
						style = BrakeTheme.typography.body16M,
						color = White,
						textAlign = TextAlign.Center,
					)

					VerticalSpacer(28.dp)

					IconButton(
						modifier = Modifier
							.size(52.dp)
							.clip(CircleShape)
							.background(ButtonYellow),
						onClick = onStartSelectingApps,
					) {
						Icon(
							painter = painterResource(R.drawable.ic_plus),
							contentDescription = null,
							modifier = Modifier.size(24.dp),
							tint = Gray900,
						)
					}
				}
			} else {
				Column(
					modifier = Modifier
						.padding(top = 8.dp, bottom = 75.dp)
						.fillMaxWidth()
						.constrainAs(groupList) {
							top.linkTo(listTitle.bottom)
							bottom.linkTo(removeButton.top)
							start.linkTo(parent.start)
							end.linkTo(parent.end)
							height = Dimension.fillToConstraints
						}
						.clip(RoundedCornerShape(16.dp))
						.background(Gray850),
				) {
					val listState = rememberLazyListState()
					Box(
						modifier = Modifier
							.weight(1f)
							.clip(RoundedCornerShape(16.dp))
							.background(Gray850)
							.padding(16.dp)
							.clipToBounds(),
					) {
						LazyColumn(
							modifier = Modifier
								.fillMaxSize(),
							state = listState,
							horizontalAlignment = Alignment.CenterHorizontally,
						) {
							items(registryUiState.selectedApps.size) { index ->
								SelectedAppItem(
									app = registryUiState.selectedApps[index],
									onDeleteClick = { onRemoveApp(index) },
								)
							}
						}
						LazyColumnScrollBar(
							lazyListState = listState,
							hidable = true,
							color = Gray500,
							modifier = Modifier.fillMaxSize(),
						)
					}
					LargeButton(
						text = stringResource(R.string.registry_group_add_app_button),
						modifier = Modifier
							.padding(horizontal = padding)
							.padding(bottom = 16.dp),
						textStyle = BrakeTheme.typography.body14SB,
						paddingValues = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
						leadingIcon = {
							Icon(
								painter = painterResource(R.drawable.ic_plus),
								contentDescription = null,
							)
						},
						colors = ButtonColors(
							containerColor = Gray800,
							contentColor = White,
							disabledContainerColor = Gray800,
							disabledContentColor = White,
						),
						onClick = onStartSelectingApps,
					)
				}

				Row(
					modifier = Modifier
						.constrainAs(removeButton) {
							bottom.linkTo(parent.bottom)
							start.linkTo(parent.start)
							end.linkTo(parent.end)
						}
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
						) {
							focusManager.clearFocus()
							onRemoveGroup()
						},
					horizontalArrangement = Arrangement.Center,
					verticalAlignment = Alignment.CenterVertically,
				) {
					Icon(
						painter = painterResource(R.drawable.ic_delete),
						contentDescription = null,
						modifier = Modifier
							.size(24.dp)
							.padding(end = 8.dp),
						tint = Gray300,
					)

					HorizontalSpacer(4.dp)

					Text(
						text = stringResource(R.string.registry_group_delete_group_button),
						style = BrakeTheme.typography.body14M.copy(
							textDecoration = TextDecoration.Underline,
						),
						color = Gray300,
					)
				}
			}
		}
	}
}

@Composable
fun SelectedAppItem(
	app: SelectedAppModel,
	onDeleteClick: () -> Unit,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 8.dp, horizontal = 12.dp)
			.clickableSingle(onDeleteClick),
		horizontalArrangement = Arrangement.Absolute.SpaceBetween,
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Start,
		) {
			Image(
				painter = BitmapPainter(
					app.icon?.toBitmap()?.asImageBitmap() ?: ImageBitmap(24, 24),
				),
				contentDescription = null,
				modifier = Modifier
					.size(28.dp),
			)

			HorizontalSpacer(12.dp)

			Text(
				text = app.name,
				style = BrakeTheme.typography.body16M,
				color = White,
			)
		}

		Icon(
			painter = painterResource(R.drawable.ic_remove),
			contentDescription = null,
			modifier = Modifier
				.padding(8.dp)
				.size(16.dp)
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
				) {
					onDeleteClick()
				},
			tint = Gray300,
		)
	}
}

@Preview
@Composable
private fun GroupRegistryScreenPreview() {
	BrakeTheme {
		GroupRegistryScreen(
			padding = 16.dp,
			registryUiState = RegistryUiState.Group.Updated(
				groupId = 1L,
				groupName = "그룹 이름",
				selectedApps = persistentListOf(
					SelectedAppModel(index = 0, name = "앱1", packageName = "", icon = null, id = 1L),
					SelectedAppModel(index = 1, name = "앱2", packageName = "", icon = null, id = 2L),
					SelectedAppModel(index = 2, name = "앱3", packageName = "", icon = null, id = 3L),
				),
				apps = persistentListOf(
					AppModel(
						name = "앱1",
						packageName = "com.example.app1",
						icon = null,
						isSelected = true,
					),
					AppModel(
						name = "앱2",
						packageName = "com.example.app2",
						icon = null,
						isSelected = false,
					),
					AppModel(
						name = "앱3",
						packageName = "com.example.app3",
						icon = null,
						isSelected = true,
					),
				),
			),
			onGroupNameChange = {},
			onStartSelectingApps = {},
			onRemoveApp = {},
			onRemoveGroup = {},
			onBackClick = {},
			onRegisterGroup = {},
			focusManager = LocalFocusManager.current,
		)
	}
}
