package com.yapp.breake.presentation.home.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.presentation.home.R
import com.yapp.breake.presentation.home.component.AddButton

@Composable
internal fun NothingScreen(
	onAddClick: () -> Unit,
) {
	Column(
		modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Image(
			painter = painterResource(R.drawable.img_home_init),
			contentDescription = stringResource(R.string.home_image_content_description),
			modifier = Modifier.size(160.dp),
		)
		VerticalSpacer(10.dp)
		Text(
			text = stringResource(R.string.nothing_screen_title),
			style = BrakeTheme.typography.subtitle22SB,
			textAlign = TextAlign.Center,
			color = MaterialTheme.colorScheme.onSurface,
			modifier = Modifier.fillMaxWidth(),
		)
		VerticalSpacer(10.dp)
		Text(
			text = stringResource(R.string.nothing_screen_subtitle),
			style = BrakeTheme.typography.body16M,
			textAlign = TextAlign.Center,
			color = Gray200,
			modifier = Modifier
				.fillMaxWidth(),
		)
		VerticalSpacer(24.dp)
		AddButton(
			onAddClick = onAddClick,
		)
	}
}

@Preview
@Composable
private fun NothingScreenPreview() {
	BrakeTheme {
		NothingScreen(
			onAddClick = {},
		)
	}
}
