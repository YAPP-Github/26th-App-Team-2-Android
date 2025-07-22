package com.yapp.breake.presentation.onboarding.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.yapp.breake.core.designsystem.component.BrakeTopAppbar
import com.yapp.breake.core.designsystem.component.LargeButton
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.core.designsystem.theme.Gray700
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.presentation.onboarding.R
import com.yapp.breake.presentation.onboarding.model.OnboardingUiState
import com.yapp.breake.presentation.onboarding.model.PermissionItem
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.launch

@Composable
fun PermissionScreen(
	uiState: OnboardingUiState.Permission,
	screenWidth: Dp,
	screenHorizontalPadding: Dp,
	onBackClick: () -> Unit,
	onRequestPermissionClick: (PermissionItem) -> Unit,
) {
	val pageSize = uiState.permissions.size
	val pagerState = rememberPagerState(pageCount = { pageSize })
	val scope = rememberCoroutineScope()
	val handleBackPress: () -> Unit = {
		if (pagerState.currentPage == 0) {
			onBackClick()
		} else {
			scope.launch {
				pagerState.animateScrollToPage(pagerState.currentPage - 1)
			}
		}
	}

	BackHandler {
		handleBackPress()
	}

	Scaffold(
		modifier = Modifier
			.fillMaxSize()
			.navigationBarsPadding()
			.statusBarsPadding(),
		topBar = {
			BrakeTopAppbar(
				onClick = handleBackPress,
			)
		},
	) { paddingValues ->
		ConstraintLayout(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues = paddingValues)
				.padding(horizontal = screenHorizontalPadding),
		) {
			val (content, pointer, button) = createRefs()

			HorizontalPager(
				state = pagerState,
				modifier = Modifier
					.fillMaxWidth()
					.constrainAs(content) {
						top.linkTo(parent.top)
						bottom.linkTo(pointer.top)
						start.linkTo(parent.start)
						end.linkTo(parent.end)
					},
			) { index ->
				// 각 페이지의 타이틀, 설명, 이미지
				val contentMap = persistentMapOf<PermissionItem, Triple<String, String, Int>>(
					PermissionItem.OVERLAY to Triple(
						stringResource(R.string.permission_overlay_title),
						stringResource(R.string.permission_overlay_description),
						R.drawable.img_permission1,
					),
					PermissionItem.STATS to Triple(
						stringResource(R.string.permission_stats_title),
						stringResource(R.string.permission_stats_description),
						R.drawable.img_permission1,
					),
					PermissionItem.EXACT_ALARM to Triple(
						stringResource(R.string.permission_exact_alarm_title),
						stringResource(R.string.permission_exact_alarm_description),
						R.drawable.img_permission2,
					),
					PermissionItem.ACCESSIBILITY to Triple(
						stringResource(R.string.permission_accessibility_title),
						stringResource(R.string.permission_accessibility_description),
						R.drawable.img_permission3,
					),
				)

				Column(
					modifier = Modifier
						.width(screenWidth)
						.fillMaxHeight(0.8f)
						.heightIn(max = 700.dp),
					horizontalAlignment = Alignment.CenterHorizontally,
				) {
					VerticalSpacer(29.dp)

					Text(
						text = uiState.permissions[index].let { contentMap[it]?.first } ?: "",
						modifier = Modifier.fillMaxWidth(),
						textAlign = TextAlign.Center,
						style = BrakeTheme.typography.subtitle22SB,
					)

					VerticalSpacer(20.dp)

					Text(
						text = uiState.permissions[index].let { contentMap[it]?.second } ?: "",
						modifier = Modifier.fillMaxWidth(),
						textAlign = TextAlign.Center,
						style = BrakeTheme.typography.body16M,
						color = Gray200,
					)

					VerticalSpacer(80.dp)

					uiState.permissions[index].run {
						Image(
							modifier = Modifier
								.fillMaxWidth(
									if (this == PermissionItem.OVERLAY ||
										this == PermissionItem.STATS
									) {
										1.0f
									} else {
										0.9f
									},
								)
								.padding(horizontal = 0.dp),
							painter = painterResource(
								id = contentMap[this]!!.third,
							),
							contentScale = ContentScale.FillWidth,
							contentDescription = "Item $index",
						)
					}
				}
			}

			Row(
				modifier = Modifier
					.constrainAs(pointer) {
						bottom.linkTo(button.top)
						start.linkTo(parent.start)
						end.linkTo(parent.end)
					}
					.padding(bottom = 24.dp),
				horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
			) {
				repeat(pageSize) { index ->
					Box(
						modifier = Modifier
							.width(8.dp)
							.height(8.dp)
							.clip(shape = CircleShape)
							.background(if (index == pagerState.currentPage) White else Gray700),
					)
				}
			}

			LargeButton(
				text = stringResource(R.string.permission_allow_button),
				onClick = {
					onRequestPermissionClick(uiState.permissions[pagerState.currentPage])
				},
				modifier = Modifier
					.constrainAs(button) {
						bottom.linkTo(parent.bottom)
						start.linkTo(parent.start)
						end.linkTo(parent.end)
					}
					.fillMaxWidth()
					.widthIn(max = 500.dp)
					.padding(bottom = 24.dp),
			)
		}
	}
}
