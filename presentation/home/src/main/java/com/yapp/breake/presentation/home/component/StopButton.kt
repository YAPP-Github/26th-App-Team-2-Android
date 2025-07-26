package com.yapp.breake.presentation.home.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Red2
import com.yapp.breake.core.designsystem.util.MultipleEventsCutter
import com.yapp.breake.core.designsystem.util.get
import com.yapp.breake.presentation.home.R

@Composable
internal fun StopButton(
	onStopClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Button(
		shape = RoundedCornerShape(16.dp),
		colors = ButtonDefaults.buttonColors(
			containerColor = Red2.copy(0.1f),
			contentColor = Red2,
		),
		contentPadding = PaddingValues(vertical = 12.dp, horizontal = 20.dp),
		onClick = { multipleEventsCutter.processEvent(onStopClick) },
		modifier = modifier,
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Icon(
				painter = painterResource(id = R.drawable.ic_exit),
				contentDescription = "stop_button_content_description",
				modifier = Modifier.size(20.dp),
			)
			HorizontalSpacer(8.dp)
			Text(
				text = "사용 종료",
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
