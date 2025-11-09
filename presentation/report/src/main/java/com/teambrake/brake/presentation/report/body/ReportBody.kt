package com.teambrake.brake.presentation.report.body

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teambrake.brake.presentation.report.component.WeeklyChart
import com.teambrake.brake.presentation.report.contract.ReportUiState
import com.teambrake.brake.core.designsystem.component.HorizontalSpacer
import com.teambrake.brake.core.designsystem.component.VerticalSpacer
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.Gray100
import com.teambrake.brake.core.designsystem.theme.Gray200
import com.teambrake.brake.core.designsystem.theme.Gray300
import com.teambrake.brake.core.designsystem.theme.Gray850
import com.teambrake.brake.core.model.app.Statistics
import com.teambrake.brake.core.util.extensions.toShortDateFormat
import com.teambrake.brake.presentation.report.R
import java.time.Duration
import java.time.LocalDate

@Composable
internal fun ReportBody(
	reportUiState: ReportUiState.Success,
) {
	Column(
		modifier = Modifier.fillMaxSize(),
	) {
		VerticalSpacer(100.dp)
		Text(
			text = stringResource(R.string.today_total_usage_time),
			style = BrakeTheme.typography.body12M,
			color = Gray200,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 36.dp),
		)
		VerticalSpacer(4.dp)
		Row(
			verticalAlignment = Alignment.Bottom,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 36.dp),
		) {
			Text(
				text = reportUiState.todayUsageHours.toString(),
				style = BrakeTheme.typography.body16M,
				color = MaterialTheme.colorScheme.onSurface,
				fontSize = 48.sp,
			)
			Text(
				text = stringResource(R.string.hours),
				style = BrakeTheme.typography.subtitle22SB,
				color = Gray300,
				modifier = Modifier.padding(bottom = 6.dp),
			)
			HorizontalSpacer(8.dp)
			Text(
				text = reportUiState.todayUsageMinutes.toString(),
				style = BrakeTheme.typography.body16M,
				color = MaterialTheme.colorScheme.onSurface,
				fontSize = 48.sp,
			)
			Text(
				text = stringResource(R.string.minutes),
				style = BrakeTheme.typography.subtitle22SB,
				color = Gray300,
				modifier = Modifier.padding(bottom = 6.dp),
			)
		}
		VerticalSpacer(24.dp)
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f)
				.padding(horizontal = 16.dp)
				.clip(RoundedCornerShape(16.dp))
				.background(Gray850),
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						top = 16.dp,
						bottom = 20.dp,
						start = 16.dp,
						end = 16.dp,
					),
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
				) {
					Image(
						painter = painterResource(id = R.drawable.ic_calendar),
						contentDescription = null,
					)
					HorizontalSpacer(8.dp)
					Text(
						text = LocalDate.now().toShortDateFormat(),
						style = BrakeTheme.typography.body14M,
						color = MaterialTheme.colorScheme.onSurface,
					)
				}
				WeeklyChart(
					statistics = reportUiState.weeklyStatistics,
					modifier = Modifier.fillMaxWidth(),
				)
			}
		}
		VerticalSpacer(20.dp)
		Text(
			text = stringResource(R.string.usage_feedback_less),
			style = BrakeTheme.typography.body16M,
			color = Gray100,
			textAlign = TextAlign.Center,
			modifier = Modifier
				.fillMaxWidth(),
		)
		VerticalSpacer(30.dp)
	}
}

@Preview
@Composable
private fun ReportBodyPreview() {
	BrakeTheme {
		ReportBody(
			reportUiState = ReportUiState.Success(
				statisticList = listOf(
					Statistics(
						date = LocalDate.now(),
						dayOfWeek = "THURSDAY",
						actualTime = Duration.ofMinutes(120),
						goalTime = Duration.ofMinutes(180),
					),
				),
			),
		)
	}
}
