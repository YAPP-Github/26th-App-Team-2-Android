package com.yapp.breake.overlay.timer.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapperLayoutInfo
import dev.chrisbanes.snapper.rememberLazyListSnapperLayoutInfo
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.collections.immutable.ImmutableList
import kotlin.math.absoluteValue

@OptIn(ExperimentalSnapperApi::class)
@Composable
internal fun TextPicker(
	modifier: Modifier = Modifier,
	texts: ImmutableList<String>,
	startIndex: Int = 0,
	count: Int,
	rowCount: Int,
	size: DpSize = DpSize(120.dp, 180.dp),
	onItemSelected: (String) -> Unit,
) {
	val lazyListState = rememberLazyListState(startIndex)
	val snapperLayoutInfo = rememberLazyListSnapperLayoutInfo(lazyListState = lazyListState)
	var currentValue by remember { mutableStateOf("") }
	val isScrollInProgress = lazyListState.isScrollInProgress

	LaunchedEffect(isScrollInProgress, count) {
		if (!isScrollInProgress) {
			onItemSelected(currentValue)
			lazyListState.scrollToItem(lazyListState.firstVisibleItemIndex)
		}
	}

	Box(
		contentAlignment = Alignment.Center,
		modifier = modifier,
	) {
		LazyColumn(
			modifier = Modifier
				.height(size.height)
				.width(size.width),
			state = lazyListState,
			contentPadding = PaddingValues(vertical = size.height / rowCount * ((rowCount - 1) / 2)),
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
						.height(size.height / rowCount)
						.width(size.width)
						.alpha(
							calculateAnimatedAlpha(
								lazyListState = lazyListState,
								snapperLayoutInfo = snapperLayoutInfo,
								index = index,
								rowCount = rowCount,
							),
						),
					contentAlignment = Alignment.Center,
				) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
					) {
						Text(
							text = texts[temp],
							fontSize = 26.sp,
							style = BrakeTheme.typography.subtitle22SB,
							color = MaterialTheme.colorScheme.onSurface,
							maxLines = 1,
						)
					}
				}
			}
		}
	}
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
private fun calculateAnimatedAlpha(
	lazyListState: LazyListState,
	snapperLayoutInfo: SnapperLayoutInfo,
	index: Int,
	rowCount: Int,
): Float {

	val distanceToIndexSnap = snapperLayoutInfo.distanceToIndexSnap(index).absoluteValue
	val layoutInfo = remember { derivedStateOf { lazyListState.layoutInfo } }.value
	val viewPortHeight = layoutInfo.viewportSize.height.toFloat()
	val singleViewPortHeight = viewPortHeight / rowCount

	return if (distanceToIndexSnap in 0..singleViewPortHeight.toInt()) {
		1.2f - (distanceToIndexSnap / singleViewPortHeight)
	} else {
		0.2f
	}
}
