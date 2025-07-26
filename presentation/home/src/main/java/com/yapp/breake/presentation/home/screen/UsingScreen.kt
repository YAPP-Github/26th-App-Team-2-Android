package com.yapp.breake.presentation.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BackgroundGradient
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.core.designsystem.theme.Gray300
import com.yapp.breake.core.designsystem.theme.Gray400
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.util.extensions.getRemainingSeconds
import com.yapp.breake.core.util.extensions.toMinutesAndSeconds
import com.yapp.breake.presentation.home.component.AppGroupBox
import com.yapp.breake.presentation.home.component.AppGroupItemContent
import com.yapp.breake.presentation.home.component.StopButton
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
internal fun UsingScreen(
	appGroup: AppGroup,
	onEditClick: () -> Unit,
	onStopClick: () -> Unit,
) {
	Box(
		modifier = Modifier.fillMaxSize(),
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(360.dp)
				.background(brush = BackgroundGradient),
		)
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
					text = "그룹",
					style = BrakeTheme.typography.subtitle22SB,
					color = MaterialTheme.colorScheme.onSurface,
				)
				HorizontalSpacer(1f)
				Text(
					text = "1개",
					style = BrakeTheme.typography.body12M,
					color = Gray200,
				)
			}
			VerticalSpacer(16.dp)
			UsingAppGroup(
				appGroup = appGroup,
				onEditClick = onEditClick,
				onStopClick = onStopClick,
				modifier = Modifier
					.fillMaxWidth(),
			)
		}
	}
}

@Composable
private fun UsingAppGroup(
	appGroup: AppGroup,
	onEditClick: () -> Unit,
	onStopClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	var remainingSeconds by remember { mutableLongStateOf(0L) }

	LaunchedEffect(appGroup.endTime) {
		while (true) {
			appGroup.endTime?.let { endTime ->
				remainingSeconds = endTime.getRemainingSeconds()
			}
			delay(1000)
		}
	}

	val (minutes, seconds) = remainingSeconds.toMinutesAndSeconds()

	AppGroupBox(
		modifier = modifier,
	) {
		AppGroupItemContent(
			appGroup = appGroup,
			onEditClick = onEditClick,
		)
		VerticalSpacer(16.dp)
		HorizontalDivider(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 4.dp),
		)
		VerticalSpacer(30.dp)
		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Text(
				text = "남은 사용 시간",
				style = BrakeTheme.typography.body16M,
				color = Gray400,
			)
			Row(
				horizontalArrangement = Arrangement.Center,
				verticalAlignment = Alignment.Bottom,
			) {
				Text(
					text = String.format(Locale.getDefault(), "%02d", minutes),
					style = BrakeTheme.typography.body16M,
					fontSize = 54.sp,
					color = MaterialTheme.colorScheme.onSurface,
				)
				HorizontalSpacer(6.dp)
				Text(
					text = "분",
					style = BrakeTheme.typography.body16M,
					color = Gray300,
					modifier = Modifier.padding(bottom = 8.dp),
				)
				HorizontalSpacer(4.dp)
				Text(
					text = String.format(Locale.getDefault(), "%02d", seconds),
					style = BrakeTheme.typography.body16M,
					fontSize = 54.sp,
					color = MaterialTheme.colorScheme.onSurface,
				)
				Text(
					text = "초",
					style = BrakeTheme.typography.body16M,
					color = Gray300,
					modifier = Modifier.padding(bottom = 8.dp),
				)
			}
			VerticalSpacer(40.dp)
			StopButton(
				onStopClick = onStopClick,
				modifier = Modifier,
			)
			VerticalSpacer(10.dp)
		}
	}
}

@Preview
@Composable
private fun UsingScreenPreview() {
	BrakeTheme {
		UsingScreen(
			appGroup = AppGroup.sample,
			onEditClick = { /* TODO: Handle edit click */ },
			onStopClick = { /* TODO: Handle stop click */ },
		)
	}
}
