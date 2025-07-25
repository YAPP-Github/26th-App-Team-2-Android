package com.yapp.breake.presentation.onboarding.complete

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.navOptions
import com.yapp.breake.core.designsystem.component.LargeButton
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.LocalPadding
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorAction
import com.yapp.breake.presentation.onboarding.R
import com.yapp.breake.presentation.onboarding.complete.model.CompleteEffect

@Composable
fun CompleteRoute(
	viewModel: CompleteViewModel = hiltViewModel(),
) {
	val screenHorizontalPadding = LocalPadding.current.screenPaddingHorizontal
	val navAction = LocalNavigatorAction.current
	val mainAction = LocalMainAction.current

	LaunchedEffect(true) {
		viewModel.errorFlow.collect { mainAction.onShowSnackBar(it) }
	}

	LaunchedEffect(true) {
		viewModel.navigationFlow.collect { effect ->
			when (effect) {
				CompleteEffect.NavigateToMain -> navAction.navigateToHome(
					navOptions = navAction.getNavOptionsClearingBackStack(),
				)

				CompleteEffect.NavigateToBack -> navAction.popBackStack()
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
	ConstraintLayout(
		modifier = Modifier
			.fillMaxSize()
			.navigationBarsPadding()
			.statusBarsPadding()
			.padding(horizontal = screenHorizontalPadding),
	) {
		val (title, description, image, button) = createRefs()
		Text(
			text = stringResource(R.string.complete_title),
			modifier = Modifier
				.fillMaxWidth()
				.constrainAs(title) {
					bottom.linkTo(description.top, margin = 16.dp)
					start.linkTo(parent.start)
					end.linkTo(parent.end)
				},
			textAlign = TextAlign.Center,
			style = BrakeTheme.typography.title28B,
			color = White,
		)

		Text(
			text = stringResource(R.string.complete_description),
			modifier = Modifier
				.fillMaxWidth()
				.constrainAs(description) {
					bottom.linkTo(image.top, margin = 36.dp)
					start.linkTo(parent.start)
					end.linkTo(parent.end)
				},
			textAlign = TextAlign.Center,
			style = BrakeTheme.typography.subtitle20SB,
			color = White,
		)

		Image(
			modifier = Modifier
				.fillMaxWidth()
				.widthIn(max = 360.dp)
				.constrainAs(image) {
					bottom.linkTo(button.top, margin = 60.dp)
					start.linkTo(parent.start)
					end.linkTo(parent.end)
				},
			painter = painterResource(id = R.drawable.img_complete),
			contentDescription = "Complete Image",
			contentScale = ContentScale.FillWidth,
		)

		LargeButton(
			text = stringResource(R.string.complete_button),
			onClick = onStartClick,
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
