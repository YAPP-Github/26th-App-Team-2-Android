package com.yapp.breake.presentation.onboarding.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.yapp.breake.core.designsystem.theme.Gray700
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.presentation.onboarding.R
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
fun GuideScreen(
	startIndex: Int,
	screenWidth: Dp,
	screenHorizontalPadding: Dp,
	onBackClick: () -> Unit,
	onNextClick: () -> Unit,
) {
	val pagerState = rememberPagerState(initialPage = startIndex, pageCount = { 3 })
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
				.padding(paddingValues = paddingValues),
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
				// 각 페이지 이미지 및 설명 리스트
				val imgList = persistentListOf(
					R.drawable.img_guide1,
					R.drawable.img_guide2,
					R.drawable.img_guide3,
				)
				val descriptionList = persistentListOf(
					"앱을 켤 때, 사용 시간을\n설정해보세요.",
					"사용 시간이 지나면, 딱 2번,\n5분 더 사용할 수 있어요.",
					"사용이 끝나면, 3분 동안\n앱을 절대 사용할 수 없어요.",
				)

				Column(
					modifier = Modifier.width(screenWidth),
					horizontalAlignment = Alignment.CenterHorizontally,
				) {
					Box(
						modifier = Modifier
							.width(28.dp)
							.height(28.dp)
							.clip(shape = RoundedCornerShape(8.dp))
							.background(Gray700),
						contentAlignment = Alignment.Center,
					) {
						Text(
							text = "${index + 1}",
							style = BrakeTheme.typography.subtitle16SB,
						)
					}

					VerticalSpacer(28.dp)

					Image(
						modifier = Modifier
							.fillMaxWidth(0.7f)
							.widthIn(max = 300.dp)
							.aspectRatio(1f),
						painter = painterResource(
							id = imgList[index],
						),
						contentDescription = "Item $index",
					)

					VerticalSpacer(36.dp)

					Text(
						text = descriptionList[index],
						modifier = Modifier
							.fillMaxWidth(0.7f)
							.widthIn(max = 300.dp),
						textAlign = TextAlign.Center,
						style = BrakeTheme.typography.subtitle22SB,
					)

					VerticalSpacer(60.dp)
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
				repeat(3) { index ->
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
				text = if (pagerState.currentPage < 2) {
					stringResource(R.string.guide_continue_button_next)
				} else {
					stringResource(R.string.guide_continue_button_confirm)
				},
				onClick = {
					if (pagerState.currentPage < 2) {
						scope.launch {
							pagerState.animateScrollToPage(pagerState.currentPage + 1)
						}
					} else {
						onNextClick()
					}
				},
				modifier = Modifier
					.constrainAs(button) {
						bottom.linkTo(parent.bottom)
						start.linkTo(parent.start)
						end.linkTo(parent.end)
					}
					.padding(bottom = 24.dp)
					.padding(horizontal = screenHorizontalPadding),
			)
		}
	}
}
