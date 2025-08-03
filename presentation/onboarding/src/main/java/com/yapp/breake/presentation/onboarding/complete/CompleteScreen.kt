package com.yapp.breake.presentation.onboarding.complete

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.breake.core.designsystem.component.LargeButton
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.LocalPadding
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorAction
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorProvider
import com.yapp.breake.presentation.onboarding.R
import com.yapp.breake.presentation.onboarding.complete.model.CompleteNavState

@Composable
fun CompleteRoute(
	viewModel: CompleteViewModel = hiltViewModel(),
) {
	val screenHorizontalPadding = LocalPadding.current.screenPaddingHorizontal
	val context = LocalContext.current
	val navAction = LocalNavigatorAction.current
	val navProvider = LocalNavigatorProvider.current
	val mainAction = LocalMainAction.current

	LaunchedEffect(true) {
		viewModel.snackBarFlow.collect {
			mainAction.onShowErrorMessage(
				message = it.asString(context = context),
			)
		}
	}

	LaunchedEffect(true) {
		viewModel.navigationFlow.collect { effect ->
			when (effect) {
				CompleteNavState.NavigateToMain -> navAction.navigateToHome(
					navOptions = navProvider.getNavOptionsClearingBackStack(),
				)

				CompleteNavState.NavigateToBack -> navAction.popBackStack()
			}
		}
	}

	CompleteScreen(
		screenHorizontalPadding = screenHorizontalPadding,
		onStartClick = viewModel::completeOnboarding,
	)
}

@Composable
fun CompleteScreen(
	screenHorizontalPadding: Dp,
	onStartClick: () -> Unit,
) {
	Box(
		modifier = Modifier.fillMaxSize(),
	) {
		Column(
			modifier = Modifier
				.align(Alignment.TopCenter)
				.padding(top = 120.dp),
		) {
			Text(
				text = stringResource(R.string.complete_title),
				modifier = Modifier.fillMaxWidth(),
				textAlign = TextAlign.Center,
				style = BrakeTheme.typography.title28B,
				color = White,
			)

			VerticalSpacer(16.dp)

			Text(
				text = stringResource(R.string.complete_description),
				modifier = Modifier.fillMaxWidth(),
				textAlign = TextAlign.Center,
				style = BrakeTheme.typography.subtitle20SB,
				color = White,
			)
		}

		Image(
			modifier = Modifier.fillMaxSize(),
			painter = painterResource(id = R.drawable.img_complete),
			contentDescription = "Complete Image",
			contentScale = ContentScale.FillBounds,
		)

		LargeButton(
			text = stringResource(R.string.complete_button),
			onClick = onStartClick,
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.fillMaxWidth()
				.widthIn(max = 500.dp)
				.navigationBarsPadding()
				.padding(bottom = 24.dp)
				.padding(horizontal = screenHorizontalPadding),
		)
	}
}
