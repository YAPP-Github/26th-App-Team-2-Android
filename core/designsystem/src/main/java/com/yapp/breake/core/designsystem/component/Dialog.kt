package com.yapp.breake.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yapp.breake.core.designsystem.R
import com.yapp.breake.core.designsystem.modifier.clickableSingle
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray400
import com.yapp.breake.core.designsystem.theme.Gray850
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.core.designsystem.util.MultipleEventsCutter
import com.yapp.breake.core.designsystem.util.get

@Composable
fun BaseDialog(
	buttonText: String,
	onButtonClick: () -> Unit,
	onDismissRequest: () -> Unit,
	content: @Composable () -> Unit = { },
) {
	Dialog(
		properties = DialogProperties(),
		onDismissRequest = onDismissRequest,
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.clip(RoundedCornerShape(20.dp))
				.background(Gray850),
		) {
			Column(
				modifier = Modifier
					.padding(16.dp),
			) {
				VerticalSpacer(30.dp)
				content()
				VerticalSpacer(30.dp)
				DialogButton(
					text = buttonText,
					onClick = onButtonClick,
					modifier = Modifier.fillMaxWidth(),
				)
			}
			Icon(
				painter = painterResource(R.drawable.ic_close),
				contentDescription = "Close",
				tint = Gray400,
				modifier = Modifier
					.padding(16.dp)
					.align(Alignment.TopEnd)
					.clickableSingle(onDismissRequest),
			)
		}
	}
}

@Composable
private fun DialogButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Button(
		shape = MaterialTheme.shapes.medium,
		colors = ButtonDefaults.buttonColors(
			containerColor = White,
			contentColor = Gray850,
		),
		contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
		enabled = enabled,
		onClick = { multipleEventsCutter.processEvent(onClick) },
		modifier = modifier,
	) {
		Text(
			text = text,
			style = BrakeTheme.typography.subtitle16SB,
			textAlign = TextAlign.Center,
			modifier = Modifier.fillMaxWidth(),
		)
	}
}

@Preview
@Composable
private fun BaseDialogPreview() {
	BrakeTheme {
		BaseDialog(
			buttonText = "확인",
			onButtonClick = {},
			onDismissRequest = {},
		)
	}
}
