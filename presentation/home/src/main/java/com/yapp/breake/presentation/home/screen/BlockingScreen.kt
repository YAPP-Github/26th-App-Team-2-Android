package com.yapp.breake.presentation.home.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.presentation.home.R
import com.yapp.breake.presentation.home.component.AppGroupBox
import com.yapp.breake.presentation.home.component.AppGroupItemContent
import com.yapp.breake.presentation.home.component.RemainingTimeDisplay

@Composable
internal fun BlockingScreen(
	appGroup: AppGroup,
	onEditClick: () -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = 16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		VerticalSpacer(50.dp)
		Row(
			verticalAlignment = Alignment.Bottom,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 8.dp),
		) {
			Text(
				text = stringResource(R.string.group),
				style = BrakeTheme.typography.subtitle22SB,
				color = MaterialTheme.colorScheme.onSurface,
			)
			HorizontalSpacer(1f)
			Text(
				text = stringResource(R.string.single_group_count),
				style = BrakeTheme.typography.body12M,
				color = Gray200,
			)
		}
		VerticalSpacer(16.dp)
		BlockingAppGroup(
			appGroup = appGroup,
			onEditClick = onEditClick,
			modifier = Modifier
				.fillMaxWidth(),
		)
	}
}

@Composable
private fun BlockingAppGroup(
	appGroup: AppGroup,
	onEditClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	AppGroupBox(
		modifier = modifier,
	) {
		AppGroupItemContent(
			appGroup = appGroup,
			onEditClick = onEditClick,
			isDimmed = true,
		)
		VerticalSpacer(16.dp)
		HorizontalDivider(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 4.dp),
		)
		VerticalSpacer(30.dp)
		Column(
			modifier = modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			RemainingTimeDisplay(
				endTime = appGroup.endTime,
				minuteTextStyle = BrakeTheme.typography.subtitle14B,
				textBottomPadding = 0,
			)
			VerticalSpacer(30.dp)
		}
		VerticalSpacer(10.dp)
	}
}

@Preview
@Composable
private fun BlockingScreenPreview() {
	BrakeTheme {
		BlockingScreen(
			appGroup = AppGroup.sample,
			onEditClick = { /* TODO: Handle edit click */ },
		)
	}
}
