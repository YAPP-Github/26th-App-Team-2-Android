package com.teambrake.brake.presentation.home.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teambrake.brake.core.designsystem.component.HorizontalSpacer
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.Error
import com.teambrake.brake.core.designsystem.util.MultipleEventsCutter
import com.teambrake.brake.core.designsystem.util.get
import com.teambrake.brake.presentation.home.R

@Composable
internal fun StopButton(
	onStopClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Button(
		shape = RoundedCornerShape(8.dp),
		colors = ButtonDefaults.buttonColors(
			containerColor = BrakeTheme.colorScheme.background.copy(alpha = 0.35f),
			contentColor = Error,
		),
		contentPadding = PaddingValues(vertical = 12.dp, horizontal = 20.dp),
		onClick = { multipleEventsCutter.processEvent(onStopClick) },
		modifier = modifier.fillMaxWidth(),
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Icon(
				painter = painterResource(id = R.drawable.ic_exit),
				contentDescription = stringResource(R.string.stop_button_content_description),
				modifier = Modifier.size(20.dp),
			)
			HorizontalSpacer(8.dp)
			Text(
				text = stringResource(R.string.stop_usage),
				style = BrakeTheme.typography.body14SB,
				textAlign = TextAlign.Center,
			)
		}
	}
}

@Composable
@Preview
private fun AddButtonPreview() {
	BrakeTheme {
		StopButton(onStopClick = {})
	}
}
