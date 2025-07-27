package com.yapp.breake.overlay.timer.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray400
import com.yapp.breake.core.designsystem.theme.Gray50
import com.yapp.breake.core.designsystem.theme.Gray600
import com.yapp.breake.core.designsystem.theme.Gray850
import com.yapp.breake.core.designsystem.theme.Gray900
import com.yapp.breake.core.designsystem.theme.White
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapperLayoutInfo
import dev.chrisbanes.snapper.rememberLazyListSnapperLayoutInfo
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.collections.immutable.ImmutableList
import kotlin.math.absoluteValue

@SuppressLint("FrequentlyChangedStateReadInComposition")
@OptIn(ExperimentalSnapperApi::class)
@Composable
internal fun TextPicker(
	modifier: Modifier = Modifier,
	texts: ImmutableList<String>,
	startIndex: Int = 0,
	count: Int,
	rowCount: Int,
	onItemSelected: (String) -> Unit,
) {
	val lazyListState = rememberLazyListState(startIndex)
	val snapperLayoutInfo = rememberLazyListSnapperLayoutInfo(lazyListState = lazyListState)
	var currentValue by remember { mutableStateOf("") }
	val isScrollInProgress = lazyListState.isScrollInProgress

	val itemHeight = 80.dp
	val totalHeight = itemHeight * rowCount
	val totalWidth = 200.dp

	LaunchedEffect(isScrollInProgress, count) {
		if (!isScrollInProgress) {
			onItemSelected(currentValue)
			lazyListState.scrollToItem(lazyListState.firstVisibleItemIndex)
		}
	}

	Box(
		contentAlignment = Alignment.Center,
		modifier = modifier.fillMaxWidth(),
	) {

		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(totalHeight + 16.dp)
				.padding(horizontal = 16.dp),
			contentAlignment = Alignment.Center,
		) {
			Box(
				modifier = Modifier
					.height(itemHeight + 44.dp)
					.fillMaxWidth()
					.clip(RoundedCornerShape(16.dp))
					.background(Gray850),
			)
			Box(
				modifier = Modifier
					.height(itemHeight + 12.dp)
					.width(120.dp)
					.clip(RoundedCornerShape(16.dp))
					.background(Gray900),
			)
		}

		LazyColumn(
			modifier = Modifier
				.height(totalHeight)
				.width(totalWidth),
			state = lazyListState,
			contentPadding = PaddingValues(vertical = itemHeight * ((rowCount - 1) / 2)),
			flingBehavior = rememberSnapperFlingBehavior(
				lazyListState = lazyListState,
			),
		) {
			items(
				count = Int.MAX_VALUE,
			) { index ->
				val temp = index % count
				if (index == lazyListState.firstVisibleItemIndex) {
					currentValue = texts[temp]
				}

				Box(
					modifier = Modifier
						.height(itemHeight)
						.width(totalWidth),
					contentAlignment = Alignment.Center,
				) {
					Text(
						text = texts[temp],
						fontSize = calculateAnimatedScale(
							lazyListState = lazyListState,
							snapperLayoutInfo = snapperLayoutInfo,
							index = index,
							rowCount = rowCount,
						).sp,
						style = BrakeTheme.typography.subtitle22SB,
						color = calculateAnimatedColor(
							lazyListState = lazyListState,
							snapperLayoutInfo = snapperLayoutInfo,
							index = index,
							rowCount = rowCount,
						),
						maxLines = 1,
					)
				}
			}
		}

		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(totalHeight),
			contentAlignment = Alignment.Center,
		) {
			Text(
				text = "분",
				style = BrakeTheme.typography.body16M,
				color = Gray50,
				maxLines = 1,
				modifier = Modifier.padding(start = 150.dp, top = 40.dp),
			)
		}

	}
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
private fun calculateAnimatedScale(
	lazyListState: LazyListState,
	snapperLayoutInfo: SnapperLayoutInfo,
	index: Int,
	rowCount: Int,
): Int {
	val distanceToIndexSnap = snapperLayoutInfo.distanceToIndexSnap(index).absoluteValue
	val layoutInfo = remember { derivedStateOf { lazyListState.layoutInfo } }.value
	val viewPortHeight = layoutInfo.viewportSize.height.toFloat()
	val singleViewPortHeight = viewPortHeight / rowCount

	val normalizedDistance = (distanceToIndexSnap / singleViewPortHeight).coerceIn(0f, 2f)

	val fontSize = when {
		normalizedDistance <= 0.5f -> {
			(64 - (normalizedDistance / 0.5f) * 28).toInt()
		}

		normalizedDistance <= 1.5f -> {
			(36 - ((normalizedDistance - 0.5f) / 1.0f) * 16).toInt()
		}

		else -> 20
	}

	return fontSize.coerceIn(20, 64)
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
private fun calculateAnimatedColor(
	lazyListState: LazyListState,
	snapperLayoutInfo: SnapperLayoutInfo,
	index: Int,
	rowCount: Int,
): Color {
	val distanceToIndexSnap = snapperLayoutInfo.distanceToIndexSnap(index).absoluteValue
	val layoutInfo = remember { derivedStateOf { lazyListState.layoutInfo } }.value
	val viewPortHeight = layoutInfo.viewportSize.height.toFloat()
	val singleViewPortHeight = viewPortHeight / rowCount

	val normalizedDistance = (distanceToIndexSnap / singleViewPortHeight).coerceIn(0f, 2f)

	val centerColor = White
	val sideColor = Gray400
	val edgeColor = Gray600

	return when {
		normalizedDistance <= 0.5f -> {
			lerp(centerColor, sideColor, normalizedDistance / 0.5f)
		}

		normalizedDistance <= 1.5f -> {
			lerp(sideColor, edgeColor, (normalizedDistance - 0.5f) / 1.0f)
		}

		else -> edgeColor
	}
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
private fun isCenterItem(
	lazyListState: LazyListState,
	snapperLayoutInfo: SnapperLayoutInfo,
	index: Int,
	rowCount: Int,
): Boolean {
	val distanceToIndexSnap = snapperLayoutInfo.distanceToIndexSnap(index).absoluteValue
	val layoutInfo = remember { derivedStateOf { lazyListState.layoutInfo } }.value
	val viewPortHeight = layoutInfo.viewportSize.height.toFloat()
	val singleViewPortHeight = viewPortHeight / rowCount

	val normalizedDistance = (distanceToIndexSnap / singleViewPortHeight).coerceIn(0f, 2f)

	return normalizedDistance <= 0.3f // 중앙에 가까운 아이템만
}
