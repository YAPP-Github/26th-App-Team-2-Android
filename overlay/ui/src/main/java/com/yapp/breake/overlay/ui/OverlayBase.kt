package com.yapp.breake.overlay.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.BaseScaffold
import com.yapp.breake.core.designsystem.component.SmallButton
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray200

@Composable
fun OverlayBase(
	@DrawableRes imageRes: Int,
	@StringRes titleRes: Int,
	buttonText: String,
	onButtonClick: () -> Unit,
	modifier: Modifier = Modifier,
	textButtonText: String = "",
	contentDescriptionRes: String? = null,
	onTextButtonClick: () -> Unit = {},
) {
	BaseScaffold(
		bottomBar = {
			OverlayButtons(
				buttonText = buttonText,
				onButtonClick = onButtonClick,
				textButtonText = textButtonText,
				onTextButtonClick = onTextButtonClick,
			)
		},
	) {
		Column(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Image(
				painter = painterResource(imageRes),
				contentDescription = stringResource(
					id = R.string.blocking_image_content_description,
				),
				modifier = modifier
					.fillMaxWidth()
					.padding(horizontal = 70.dp),
			)
			VerticalSpacer(30.dp)
			Text(
				text = stringResource(id = titleRes),
				style = BrakeTheme.typography.title24B,
				textAlign = TextAlign.Center,
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier.fillMaxWidth(),
			)
			VerticalSpacer(14.dp)
			if (contentDescriptionRes != null) {
				Text(
					text = contentDescriptionRes,
					style = BrakeTheme.typography.subtitle16SB,
					textAlign = TextAlign.Center,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 16.dp)
						.padding(top = 8.dp),
				)
			}
			VerticalSpacer(30.dp)
		}
	}
}

@Composable
private fun OverlayButtons(
	buttonText: String,
	onButtonClick: () -> Unit,
	modifier: Modifier = Modifier,
	textButtonText: String = "",
	onTextButtonClick: () -> Unit = {},
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier.fillMaxWidth(),
	) {
		SmallButton(
			text = buttonText,
			onClick = onButtonClick,
		)
		VerticalSpacer(16.dp)
		Text(
			text = textButtonText,
			style = BrakeTheme.typography.subtitle16SB,
			color = Gray200,
			modifier = Modifier
				.clickable(onClick = onTextButtonClick, enabled = textButtonText.isNotEmpty())
				.padding(vertical = 13.dp, horizontal = 16.dp),
		)
		VerticalSpacer(20.dp)
	}
}

@Preview
@Composable
private fun OverlayBasePreview() {
	BrakeTheme {
		OverlayBase(
			imageRes = R.drawable.img_blocking,
			titleRes = R.string.blocking_title,
			buttonText = "Exit",
			onButtonClick = { /* Do nothing */ },
			textButtonText = "Check Time",
			onTextButtonClick = { /* Do nothing */ },
			contentDescriptionRes = stringResource(id = R.string.blocking_description, 3),
		)
	}
}
