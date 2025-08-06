package com.yapp.breake.presentation.permission

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.navOptions
import com.yapp.breake.core.designsystem.component.BrakeTopAppbar
import com.yapp.breake.core.designsystem.component.LargeButton
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.core.designsystem.theme.Gray700
import com.yapp.breake.core.designsystem.theme.LocalPadding
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorAction
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorProvider
import com.yapp.breake.core.navigation.route.InitialRoute
import com.yapp.breake.core.navigation.route.stringRoute
import com.yapp.breake.presentation.permission.model.PermissionNavState
import com.yapp.breake.presentation.permission.model.PermissionItem
import com.yapp.breake.presentation.permission.model.PermissionModalState
import com.yapp.breake.presentation.permission.model.PermissionUiState
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PermissionRoute(
	viewModel: PermissionViewModel = hiltViewModel(),
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val context = LocalContext.current
	val lifecycleOwner = LocalLifecycleOwner.current
	val screenWidth = LocalConfiguration.current.screenWidthDp.dp
	val screenHorizontalPadding = LocalPadding.current.screenPaddingHorizontal
	val navAction = LocalNavigatorAction.current
	val mainAction = LocalMainAction.current
	val navProvider = LocalNavigatorProvider.current
	val modalState by viewModel.modalFlow.collectAsStateWithLifecycle()

	// ViewModel 초기화,
	// UiState 초기화시 context 사용이 필요하며, 이는 ViewModel 내부에서만 이루어질 수 없음
	LaunchedEffect(Unit) {
		viewModel.refreshPermissions(context)
	}

	LaunchedEffect(true) {
		viewModel.snackBarFlow.collect {
			mainAction.onShowErrorMessage(
				message = it.asString(context = context),
			)
		}
	}

	// 외부 화면으로 이동 side effect 처리
	LaunchedEffect(true) {
		viewModel.navigationFlow.collect { effect ->
			when (effect) {
				PermissionNavState.NavigateToLogin -> {
					navAction.navigateToLogin(navProvider.getNavOptionsClearingBackStack())
				}

				PermissionNavState.NavigateToBack -> navAction.popBackStack()

				is PermissionNavState.RequestPermission -> {
					// 권한 요청을 위한 Intent 실행
					context.startActivity(effect.intent)
				}

				PermissionNavState.NavigateToMain -> {
					navAction.navigateToHome(
						navOptions = navProvider.getNavOptionsClearingBackStack(),
					)
				}

				PermissionNavState.NavigateToComplete -> {
					navAction.navigateToComplete(
						navOptions {
							popUpTo(InitialRoute.Permission) { inclusive = true }
						},
					)
				}
			}
		}
	}

	// 권한 설정 창에서 복귀 시 권한 상태를 새로고침
	DisposableEffect(lifecycleOwner) {
		val observer = LifecycleEventObserver { _, event ->
			if (event == Lifecycle.Event.ON_RESUME) {
				viewModel.refreshPermissions(context)
			}
		}
		lifecycleOwner.lifecycle.addObserver(observer)
		onDispose {
			lifecycleOwner.lifecycle.removeObserver(observer)
		}
	}

	if (modalState is PermissionModalState.ShowLogoutModal) {
		mainAction.OnShowLogoutDialog(
			onConfirm = viewModel::logout,
			onDismiss = viewModel::dismissModal,
		)
	}

	PermissionScreen(
		uiState = uiState,
		screenWidth = screenWidth,
		screenHorizontalPadding = screenHorizontalPadding,
		onBackClick = {
			if (navProvider.getPreviousDestination() ==
				InitialRoute.Onboarding.Guide.stringRoute()
			) {
				viewModel.popBackStack()
			} else {
				viewModel.tryLogout()
			}
		},
		onRequestPermissionClick = { permissionItem ->
			viewModel.requestPermission(context, permissionItem)
		},
	)
}

@Composable
fun PermissionScreen(
	uiState: PermissionUiState,
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
								.padding(horizontal = 0.dp)
								.clickable(
									interactionSource = null,
									indication = null,
									onClick = {
										onRequestPermissionClick(uiState.permissions[pagerState.currentPage])
									},
								),
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
