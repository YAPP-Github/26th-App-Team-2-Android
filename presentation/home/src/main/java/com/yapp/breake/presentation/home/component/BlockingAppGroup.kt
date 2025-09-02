package com.yapp.breake.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.presentation.home.R

@Composable
internal fun BlockingAppGroup(
	appGroup: AppGroup,
	onEditClick: () -> Unit,
	modifier: Modifier = Modifier,
	innerModifier: Modifier = Modifier,
) {
	AppGroupBox(
		modifier = modifier,
		innerModifier = innerModifier,
	) {
		AppGroupItemContent(
			appGroup = appGroup,
			clickable = false,
			onEditClick = onEditClick,
			isDimmed = true,
		)
		VerticalSpacer(16.dp)
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			ProgressTime(
				startTime = appGroup.startTime,
				endTime = appGroup.endTime,
				minuteTextStyle = BrakeTheme.typography.subtitle14B,
				startColor = Color(0xFF8E97B0),
				endColor = Color(0xFFF0F4FF),
				textBottomPadding = 0,
				modifier = Modifier.fillMaxWidth(),
			) {
				Image(
					painter = painterResource(id = R.drawable.img_lock),
					contentDescription = "Lock Icon",
					contentScale = ContentScale.FillWidth,
					modifier = Modifier
						.width(110.dp)
						.wrapContentHeight(),
				)
			}
		}
		VerticalSpacer(10.dp)
	}
}

@Preview
@Composable
private fun BlockingScreenPreview() {
	BrakeTheme {
		BlockingAppGroup(
			appGroup = AppGroup.sample,
			onEditClick = { /* TODO: Handle edit click */ },
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp),
			innerModifier = Modifier.padding(16.dp),
		)
	}
}
