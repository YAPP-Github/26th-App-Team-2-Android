package com.yapp.breake.overlay.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.yapp.breake.core.designsystem.component.LargeButton
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray200

@Composable
fun OverlayBase(
	@DrawableRes imageRes: Int,
	title: String,
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
					.padding(horizontal = 70.dp)
					.fillMaxWidth()
					.aspectRatio(1f),
			)
			VerticalSpacer(24.dp)
			Text(
				text = title,
				style = BrakeTheme.typography.subtitle24SB,
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
		LargeButton(
			text = buttonText,
			onClick = onButtonClick,
			modifier = Modifier
				.padding(horizontal = 16.dp),
		)
		VerticalSpacer(4.dp)
		Surface(
			shape = MaterialTheme.shapes.large,
			color = MaterialTheme.colorScheme.background,
			onClick = onTextButtonClick,
			enabled = textButtonText.isNotEmpty(),
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp),
		) {
			Text(
				text = textButtonText,
				style = BrakeTheme.typography.subtitle16SB,
				color = Gray200,
				textAlign = TextAlign.Center,
				modifier = Modifier.padding(vertical = 16.dp),
			)
		}
		VerticalSpacer(28.dp)
	}
}

@Preview
@Composable
private fun OverlayBasePreview() {
	BrakeTheme {
		OverlayBase(
			imageRes = R.drawable.img_init,
			title = stringResource(R.string.blocking_title),
			buttonText = "Exit",
			onButtonClick = { /* Do nothing */ },
			textButtonText = "Check Time",
			onTextButtonClick = { /* Do nothing */ },
			contentDescriptionRes = stringResource(id = R.string.blocking_description, 3),
		)
	}
}
