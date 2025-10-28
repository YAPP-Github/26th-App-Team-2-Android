package com.teambrake.brake.presentation.report.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teambrake.brake.core.designsystem.component.HorizontalSpacer
import com.teambrake.brake.core.designsystem.component.VerticalSpacer
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.BrakeYellow
import com.teambrake.brake.core.designsystem.theme.Gray100
import com.teambrake.brake.core.designsystem.theme.Gray400
import com.teambrake.brake.core.designsystem.theme.Gray700
import com.teambrake.brake.core.designsystem.theme.pretendard
import com.teambrake.brake.core.model.app.Statistics
import com.teambrake.brake.core.util.DateUtil
import com.teambrake.brake.presentation.report.R
import java.time.Duration
import java.time.LocalDate
import kotlin.math.max

@Composable
internal fun WeeklyChart(
	statistics: List<Statistics>,
	modifier: Modifier = Modifier,
) {
	val colorScheme = BrakeTheme.colorScheme
	val textMeasurer = rememberTextMeasurer()
	var startAnimation by remember { mutableStateOf(false) }

	val animatedProgress by animateFloatAsState(
		targetValue = if (startAnimation) 1f else 0f,
		animationSpec = tween(
			durationMillis = 1200,
			easing = LinearEasing,
		),
		label = "chartAnimation",
	)

	LaunchedEffect(statistics) {
		startAnimation = true
	}

	val maxHours = statistics.maxOfOrNull {
		max(it.actualTime.toMinutes(), it.goalTime.toMinutes()) / 60.0
	}?.let { rawMax ->
		when {
			rawMax <= 2.5 -> 2.5
			rawMax <= 5 -> 5.0
			rawMax <= 10 -> 10.0
			rawMax <= 20 -> 20.0
			else -> kotlin.math.ceil(rawMax / 4.0) * 4.0
		}
	} ?: 2.5

	val timeLabels = remember(maxHours) {
		when {
			maxHours <= 2.5 -> (0..4).map { it * 0.5 }
			maxHours <= 5 -> (0..4).map { it * 1.0 }
			maxHours <= 10 -> (0..4).map { it * 2.0 }
			else -> (0..4).map { it * (maxHours / 4.0) }
		}
	}

	val dayLabels = remember {
		DateUtil.getShortDayOfWeekNames()
	}

	Column(modifier = modifier) {
		Canvas(
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f),
		) {
			val chartSpacing = 2.dp.toPx()
			val availableWidth = size.width - 40.dp.toPx()
			val totalSpacing = chartSpacing * 6
			val barWidth = (availableWidth - totalSpacing) / 7
			val startX = 20.dp.toPx()

			val labelTextStyle = TextStyle(
				fontSize = 12.sp,
				fontWeight = FontWeight.Medium,
				fontFamily = pretendard,
				color = Gray700,
			)
			val labelHalf = textMeasurer.measure("0", labelTextStyle).size.height / 2f

			val chartHeight = size.height - (labelHalf * 2)

			timeLabels.forEach { time ->
				val y = labelHalf + chartHeight - ((time / maxHours) * chartHeight).toFloat()

				val displayText = if (time % 1.0 == 0.0) {
					time.toInt().toString()
				} else {
					time.toString()
				}

				drawText(
					textMeasurer = textMeasurer,
					text = displayText,
					topLeft = Offset(startX - 20.dp.toPx(), y - labelHalf),
					style = labelTextStyle,
				)
			}

			statistics.forEachIndexed { index, stat ->
				val x = startX + index * (barWidth + chartSpacing)
				val cornerRadius = CornerRadius(7.dp.toPx())

				val goalHeight =
					(stat.goalTime.toMinutes() / 60.0 / maxHours * chartHeight).toFloat()

				drawRoundRect(
					color = Color(0x40DADFED),
					topLeft = Offset(x, labelHalf + chartHeight - goalHeight),
					size = androidx.compose.ui.geometry.Size(barWidth, goalHeight),
					cornerRadius = cornerRadius,
				)

				val clipPath = Path().apply {
					addRoundRect(
						androidx.compose.ui.geometry.RoundRect(
							left = x,
							top = labelHalf + chartHeight - goalHeight,
							right = x + barWidth,
							bottom = labelHalf + chartHeight,
							cornerRadius = cornerRadius,
						),
					)
				}

				clipPath(clipPath) {
					drawStripePattern(
						x = x,
						y = labelHalf + chartHeight - goalHeight,
						width = barWidth,
						height = goalHeight,
					)
				}

				val fullActualHeight =
					(stat.actualTime.toMinutes() / 60.0 / maxHours * chartHeight).toFloat()
				val animatedActualHeight = fullActualHeight * animatedProgress

				val isToday = stat.date == LocalDate.now()
				val actualBarColor = if (isToday) BrakeYellow else Color(0xFFa2ab4c)

				drawRoundRect(
					color = actualBarColor,
					topLeft = Offset(x, labelHalf + chartHeight - animatedActualHeight),
					size = androidx.compose.ui.geometry.Size(barWidth, animatedActualHeight),
					cornerRadius = cornerRadius,
				)
			}
		}

		Canvas(
			modifier = Modifier
				.fillMaxWidth()
				.height(24.dp),
		) {
			val chartSpacing = 2.dp.toPx()
			val availableWidth = size.width - 40.dp.toPx()
			val totalSpacing = chartSpacing * 6
			val barWidth = (availableWidth - totalSpacing) / 7
			val startX = 20.dp.toPx()

			dayLabels.forEachIndexed { index, day ->
				val x = startX + index * (barWidth + chartSpacing) + barWidth / 2
				val isToday = statistics.getOrNull(index)?.date == LocalDate.now()

				if (isToday) {
					drawCircle(
						color = Gray100,
						radius = 16.dp.toPx(),
						center = Offset(x, 12.dp.toPx()),
					)
				}

				val textStyle = TextStyle(
					fontSize = 14.sp,
					fontWeight = FontWeight.Medium,
					fontFamily = pretendard,
					color = if (isToday) colorScheme.onPrimary else Gray100,
				)

				val textLayoutResult = textMeasurer.measure(day, textStyle)
				val textWidth = textLayoutResult.size.width
				val textHeight = textLayoutResult.size.height

				drawText(
					textMeasurer = textMeasurer,
					text = day,
					topLeft = Offset(
						x - textWidth / 2,
						12.dp.toPx() - textHeight / 2,
					),
					style = textStyle,
				)
			}
		}
		VerticalSpacer(30.dp)
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 20.dp),
			horizontalArrangement = Arrangement.Start,
		) {

			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.Center,
				modifier = Modifier.weight(1f),
			) {
				Image(
					painter = painterResource(R.drawable.ic_circle),
					contentDescription = null,
				)
				HorizontalSpacer(4.dp)
				Text(
					text = stringResource(R.string.chart_legend_used_time),
					style = BrakeTheme.typography.body12M,
					color = Gray400,
				)
			}
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.Center,
				modifier = Modifier.weight(1f),
			) {
				Image(
					painter = painterResource(R.drawable.ic_stripe_circle),
					contentDescription = null,
				)
				HorizontalSpacer(4.dp)
				Text(
					text = stringResource(R.string.chart_legend_planned_time),
					style = BrakeTheme.typography.body12M,
					color = Gray400,
				)
			}
		}
	}
}

private fun DrawScope.drawStripePattern(
	x: Float,
	y: Float,
	width: Float,
	height: Float,
) {
	val rotationAngle = -136.03f
	val strokeWidth = 6.dp.toPx()
	val lineSpacing = 70f

	val extendedSize = width + height
	val centerX = x + width / 2
	val centerY = y + height / 2

	rotate(rotationAngle, Offset(centerX, centerY)) {
		var offset = -extendedSize
		while (offset < extendedSize) {
			drawLine(
				color = Color(0xFF373B44),
				start = Offset(centerX + offset, centerY - extendedSize),
				end = Offset(centerX + offset, centerY + extendedSize),
				strokeWidth = strokeWidth,
				cap = StrokeCap.Round,
			)
			offset += lineSpacing
		}
	}
}

@Preview
@Composable
private fun WeeklyChartPreview() {
	BrakeTheme {
		WeeklyChart(
			statistics = listOf(
				Statistics(
					date = LocalDate.now().minusDays(6),
					dayOfWeek = "MONDAY",
					actualTime = Duration.ofMinutes(180),
					goalTime = Duration.ofMinutes(240),
				),
				Statistics(
					date = LocalDate.now().minusDays(5),
					dayOfWeek = "TUESDAY",
					actualTime = Duration.ofMinutes(120),
					goalTime = Duration.ofMinutes(240),
				),
				Statistics(
					date = LocalDate.now().minusDays(4),
					dayOfWeek = "WEDNESDAY",
					actualTime = Duration.ofMinutes(300),
					goalTime = Duration.ofMinutes(240),
				),
				Statistics(
					date = LocalDate.now().minusDays(3),
					dayOfWeek = "THURSDAY",
					actualTime = Duration.ofMinutes(90),
					goalTime = Duration.ofMinutes(240),
				),
				Statistics(
					date = LocalDate.now().minusDays(2),
					dayOfWeek = "FRIDAY",
					actualTime = Duration.ofMinutes(150),
					goalTime = Duration.ofMinutes(240),
				),
				Statistics(
					date = LocalDate.now().minusDays(1),
					dayOfWeek = "SATURDAY",
					actualTime = Duration.ofMinutes(60),
					goalTime = Duration.ofMinutes(240),
				),
				Statistics(
					date = LocalDate.now(),
					dayOfWeek = "SUNDAY",
					actualTime = Duration.ofMinutes(200),
					goalTime = Duration.ofMinutes(240),
				),
			),
			modifier = Modifier.fillMaxWidth(),
		)
	}
}
