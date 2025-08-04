package com.yapp.breake.presentation.main.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.yapp.breake.core.designsystem.component.TwoButtonDialog
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.presentation.main.R

@Composable
internal fun LogoutWarningDialog(
	onDismissRequest: () -> Unit,
	onConfirm: () -> Unit,
) {
	TwoButtonDialog(
		onDismissRequest = onDismissRequest,
		dismissButtonText = stringResource(R.string.logout_cancel),
		confirmButtonText = stringResource(R.string.logout_confirm),
		onConfirmButtonClick = onConfirm,
	) {
		Text(
			modifier = Modifier.fillMaxWidth(),
			text = stringResource(R.string.logout_message),
			style = BrakeTheme.typography.subtitle22SB,
			color = White,
			textAlign = TextAlign.Center,
		)
	}
}

@Preview
@Composable
fun LogoutWarningDialogPreview() {
	BrakeTheme {
		LogoutWarningDialog(
			onDismissRequest = {},
			onConfirm = {},
		)
	}
}
