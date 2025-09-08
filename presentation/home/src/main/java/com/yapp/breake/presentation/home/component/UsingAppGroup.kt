package com.yapp.breake.presentation.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray400
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.presentation.home.R

@Composable
internal fun UsingAppGroup(
	appGroup: AppGroup,
	onEditClick: () -> Unit,
	onStopClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	AppGroupBox(
		modifier = modifier,
	) {
		AppGroupItemContent(
			appGroup = appGroup,
			clickable = false,
			onEditClick = onEditClick,
		)
		VerticalSpacer(16.dp)
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			ProgressTime(
				startTime = appGroup.startTime,
				endTime = appGroup.endTime,
				modifier = Modifier.fillMaxWidth(),
			) {
				Text(
					text = stringResource(R.string.remaining_usage_time),
					style = BrakeTheme.typography.body16M,
					color = Gray400,
				)
			}
			VerticalSpacer(10.dp)
			StopButton(
				onStopClick = onStopClick,
				modifier = Modifier,
			)
		}
		VerticalSpacer(10.dp)
	}
}

@Preview
@Composable
private fun UsingAppGroupPreview() {
	BrakeTheme {
		UsingAppGroup(
			appGroup = AppGroup.sample,
			onEditClick = {},
			onStopClick = {},
		)
	}
}
