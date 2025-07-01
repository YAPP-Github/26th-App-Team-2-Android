package com.yapp.breake.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.R
import com.yapp.breake.core.designsystem.modifier.clickableSingle
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray200

sealed interface TopAppbarType {
	data object Back : TopAppbarType
	data object Cancel : TopAppbarType
	data class TextButton(val text: String, val onClick: () -> Unit) : TopAppbarType
}

@Composable
fun BrakeTopAppbar(
	modifier: Modifier = Modifier,
	title: String = "",
	appbarType: TopAppbarType = TopAppbarType.Back,
	onClick: () -> Unit = {},
) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(IntrinsicSize.Min)
			.background(MaterialTheme.colorScheme.background)
			.padding(horizontal = 16.dp, vertical = 10.dp)
			.statusBarsPadding(),
	) {
		if (appbarType == TopAppbarType.Back || appbarType is TopAppbarType.TextButton) {
			TopAppbarIcon(
				icon = R.drawable.ic_back_28,
				onClick = onClick,
				modifier = Modifier.align(Alignment.CenterStart),
			)
		}
		Text(
			text = title,
			modifier = Modifier.align(Alignment.Center),
			color = MaterialTheme.colorScheme.onSurface,
			style = BrakeTheme.typography.subtitle16SB,
			maxLines = 1,
			textAlign = TextAlign.Center,
		)
		when (appbarType) {
			TopAppbarType.Back -> {}
			TopAppbarType.Cancel -> {
				TopAppbarIcon(
					icon = R.drawable.ic_close,
					onClick = onClick,
					modifier = Modifier.align(Alignment.CenterEnd),
				)
			}
			is TopAppbarType.TextButton -> {
				Box(
					modifier = Modifier
						.fillMaxHeight()
						.clip(MaterialTheme.shapes.large)
						.align(Alignment.CenterEnd)
						.clickableSingle(appbarType.onClick)
				) {
					Text(
						modifier = Modifier.align(Alignment.Center),
						text = appbarType.text,
						color = Gray200,
						style = BrakeTheme.typography.body16M,
						textAlign = TextAlign.Center,
					)
				}
			}
		}

	}
}

@Composable
fun TopAppbarIcon(
	@DrawableRes icon: Int,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.clickableSingle(onClick = onClick),
	) {
		Icon(
			painter = painterResource(icon),
			contentDescription = "menu Icon",
			tint = Gray200,
			modifier = Modifier.align(Alignment.Center),
		)
	}
}

@Preview(showBackground = true)
@Composable
fun BrakeTopAppbarPreview() {
	BrakeTheme {
		BrakeTopAppbar(
			title = "title",
		)
	}
}

@Preview(showBackground = true)
@Composable
fun CancelPreview() {
	BrakeTheme {
		BrakeTopAppbar(
			title = "title",
			appbarType = TopAppbarType.Cancel,
		)
	}
}

@Preview(showBackground = true)
@Composable
fun TextButtonPreview() {
	BrakeTheme {
		BrakeTopAppbar(
			title = "title",
			appbarType = TopAppbarType.TextButton(
				text = "button",
				onClick = {},
			),
		)
	}
}
