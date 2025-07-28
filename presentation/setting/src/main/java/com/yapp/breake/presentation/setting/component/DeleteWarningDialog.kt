package com.yapp.breake.presentation.setting.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.BaseDialog
import com.yapp.breake.core.designsystem.component.DialogButton
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray300
import com.yapp.breake.core.designsystem.theme.Gray800
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.core.designsystem.R as Res
import com.yapp.breake.presentation.setting.R

@Composable
internal fun DeleteWarningDialog(
	onDismissRequest: () -> Unit,
	onConfirm: () -> Unit,
) {
	BaseDialog(
		onDismissRequest = onDismissRequest,
		dismissButton = {
			DialogButton(
				text = stringResource(R.string.setting_delete_dialog_dismiss_text),
				onClick = onDismissRequest,
				containerColor = Gray800,
				contentColor = White,
			)
		},
		confirmButton = {
			DialogButton(
				text = stringResource(R.string.setting_delete_dialog_confirm_text),
				onClick = onConfirm,
			)
		},
	) {
		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Image(
				painter = painterResource(id = Res.drawable.img_warning),
				contentDescription = null,
			)
			VerticalSpacer(16.dp)
			Text(
				text = stringResource(R.string.setting_delete_dialog_title),
				style = BrakeTheme.typography.subtitle22SB,
				color = White,
			)
			VerticalSpacer(12.dp)
			Text(
				text = stringResource(R.string.setting_delete_dialog_description),
				style = BrakeTheme.typography.body16M,
				color = Gray300,
				textAlign = TextAlign.Center,
			)
		}
	}
}

@Preview
@Composable
fun DeleteWarningDialogPreview() {
	BrakeTheme {
		DeleteWarningDialog(
			onDismissRequest = {},
			onConfirm = {},
		)
	}
}
