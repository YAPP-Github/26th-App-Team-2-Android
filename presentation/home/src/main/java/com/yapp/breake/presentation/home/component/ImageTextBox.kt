package com.yapp.breake.presentation.home.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray100
import com.yapp.breake.presentation.home.R

@Composable
internal fun ImageTextBox(
	@DrawableRes imageRes: Int,
	text: String,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Image(
			painter = painterResource(id = imageRes),
			contentDescription = stringResource(R.string.list_screen_image_content_description),
			modifier = Modifier
				.fillMaxWidth()
				.wrapContentHeight(),
			contentScale = ContentScale.FillWidth,
		)

		VerticalSpacer(12.dp)

		Text(
			text = text,
			style = BrakeTheme.typography.subtitle20SB,
			textAlign = TextAlign.Center,
			color = Gray100,
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 24.dp),
		)
	}
}
