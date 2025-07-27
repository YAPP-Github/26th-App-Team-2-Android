package com.yapp.breake.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.BaseDialog
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray300
import com.yapp.breake.presentation.home.R

@Composable
internal fun StopUsingDialog(
	onStopUsing: () -> Unit,
	onDismissRequest: () -> Unit,
) {
	BaseDialog(
		buttonText = stringResource(R.string.stop_using_dialog_button),
		onButtonClick = onStopUsing,
		onDismissRequest = onDismissRequest,
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.fillMaxWidth(),
		) {
			Image(
				painter = painterResource(id = R.drawable.img_clap),
				contentDescription = stringResource(R.string.clap_image_content_description),
				contentScale = ContentScale.FillWidth,
				modifier = Modifier
					.width(100.dp)
					.wrapContentHeight(),
			)
			VerticalSpacer(28.dp)
			Text(
				text = stringResource(R.string.stop_using_dialog_title),
				style = BrakeTheme.typography.subtitle22SB,
				color = MaterialTheme.colorScheme.onSurface,
				textAlign = TextAlign.Center,
				modifier = Modifier.fillMaxWidth(),
			)
			VerticalSpacer(8.dp)
			Text(
				text = stringResource(R.string.stop_using_dialog_message),
				style = BrakeTheme.typography.body16M,
				color = Gray300,
				textAlign = TextAlign.Center,
				modifier = Modifier.fillMaxWidth(),
			)
			VerticalSpacer(12.dp)
		}
	}
}

@Preview
@Composable
private fun StopUsingDialogPreview() {
	BrakeTheme {
		StopUsingDialog(
			onStopUsing = {},
			onDismissRequest = {},
		)
	}
}
