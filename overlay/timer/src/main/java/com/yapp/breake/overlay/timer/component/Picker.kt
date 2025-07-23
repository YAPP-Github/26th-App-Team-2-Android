package com.yapp.breake.overlay.timer.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalSnapperApi::class)
@Composable
internal fun Picker(
	modifier: Modifier = Modifier,
	texts: ImmutableList<String>,
	startIndex: Int = 0,
	count: Int,
	onItemSelected: (String) -> Unit,
) {
	val lazyListState = rememberLazyListState(startIndex)
	var currentValue by remember { mutableStateOf("") }
	var centerItemIndex by remember { mutableIntStateOf(startIndex) }

	val isScrollInProgress by remember {
		derivedStateOf { lazyListState.isScrollInProgress }
	}

	LaunchedEffect(lazyListState) {
		snapshotFlow {
			val layoutInfo = lazyListState.layoutInfo
			val viewportCenter = layoutInfo.viewportEndOffset / 2f

			layoutInfo.visibleItemsInfo.minByOrNull { item ->
				kotlin.math.abs((item.offset + item.size / 2f) - viewportCenter)
			}?.index ?: startIndex
		}.collect { index ->
			centerItemIndex = index
			currentValue = texts[index % count]
		}
	}

	LaunchedEffect(isScrollInProgress) {
		if (!isScrollInProgress) {
			onItemSelected(currentValue)
		}
	}

	val textHeight64 = measureTextHeight(
		text = "25",
		style = BrakeTheme.typography.subtitle22SB,
		fontSize = 64.sp,
	)
	val textHeight36 = measureTextHeight(
		text = "30",
		style = BrakeTheme.typography.subtitle22SB,
		fontSize = 36.sp,
	)
	val textHeight20 = measureTextHeight(
		text = "35",
		style = BrakeTheme.typography.subtitle22SB,
		fontSize = 20.sp,
	)

	Box(
		contentAlignment = Alignment.Center,
		modifier = modifier,
	) {
		// 투명한 스크롤 레이어
		LazyColumn(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier
				.height(textHeight20 * 2 + textHeight36 * 2 + textHeight64)
				.alpha(0f),
			state = lazyListState,
			contentPadding = PaddingValues(
				top = textHeight20 + textHeight36,
				bottom = textHeight20 + textHeight36,
			),
			flingBehavior = rememberSnapperFlingBehavior(
				lazyListState = lazyListState,
			),
		) {
			items(
				count = Int.MAX_VALUE,
			) { index ->
				Box(
					modifier = Modifier.height(textHeight64),
				)
			}
		}

		// 고정된 표시 레이어
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			// 맨 위 아이템
			Box(
				modifier = Modifier
					.height(textHeight20)
					.alpha(0.2f),
				contentAlignment = Alignment.Center,
			) {
				Text(
					text = texts[(centerItemIndex - 2 + count * 1000) % count],
					style = BrakeTheme.typography.subtitle22SB,
					fontSize = 20.sp,
					color = MaterialTheme.colorScheme.onSurface,
					maxLines = 1,
				)
			}

			// 위쪽 중간 아이템
			Box(
				modifier = Modifier
					.height(textHeight36)
					.alpha(0.4f),
				contentAlignment = Alignment.Center,
			) {
				Text(
					text = texts[(centerItemIndex - 1 + count * 1000) % count],
					style = BrakeTheme.typography.subtitle22SB,
					fontSize = 36.sp,
					color = MaterialTheme.colorScheme.onSurface,
					maxLines = 1,
				)
			}

			// 가운데 아이템 (고정)
			Box(
				modifier = Modifier
					.height(textHeight64)
					.alpha(1.0f),
				contentAlignment = Alignment.Center,
			) {
				Text(
					text = texts[centerItemIndex % count],
					style = BrakeTheme.typography.subtitle22SB,
					fontSize = 64.sp,
					color = MaterialTheme.colorScheme.onSurface,
					maxLines = 1,
				)
			}

			// 아래쪽 중간 아이템
			Box(
				modifier = Modifier
					.height(textHeight36)
					.alpha(0.4f),
				contentAlignment = Alignment.Center,
			) {
				Text(
					text = texts[(centerItemIndex + 1) % count],
					style = BrakeTheme.typography.subtitle22SB,
					fontSize = 36.sp,
					color = MaterialTheme.colorScheme.onSurface,
					maxLines = 1,
				)
			}

			// 맨 아래 아이템
			Box(
				modifier = Modifier
					.height(textHeight20)
					.alpha(0.2f),
				contentAlignment = Alignment.Center,
			) {
				Text(
					text = texts[(centerItemIndex + 2) % count],
					style = BrakeTheme.typography.subtitle22SB,
					fontSize = 20.sp,
					color = MaterialTheme.colorScheme.onSurface,
					maxLines = 1,
				)
			}
		}
	}
}

@Composable
private fun measureTextHeight(
	text: String,
	style: TextStyle,
	fontSize: TextUnit,
): Dp {
	val textMeasurer = rememberTextMeasurer()
	val density = LocalDensity.current
	val textLayoutResult = remember(text, style, fontSize) {
		textMeasurer.measure(
			text = text,
			style = style.copy(fontSize = fontSize),
		)
	}

	return with(density) { textLayoutResult.size.height.toDp() }
}
