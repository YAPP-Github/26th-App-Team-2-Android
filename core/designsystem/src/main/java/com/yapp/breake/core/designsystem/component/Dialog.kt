package com.yapp.breake.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yapp.breake.core.designsystem.R
import com.yapp.breake.core.designsystem.modifier.clickableSingle
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.BrakeYellow
import com.yapp.breake.core.designsystem.theme.Gray300
import com.yapp.breake.core.designsystem.theme.Gray400
import com.yapp.breake.core.designsystem.theme.Gray800
import com.yapp.breake.core.designsystem.theme.Gray850
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.core.designsystem.util.MultipleEventsCutter
import com.yapp.breake.core.designsystem.util.get

@Composable
internal fun BaseDialog(
	onDismissRequest: () -> Unit,
	confirmButton: (@Composable () -> Unit)? = null,
	dismissButton: (@Composable () -> Unit)? = null,
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
				VerticalSpacer(42.dp)
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.spacedBy(
						6.dp,
						Alignment.CenterHorizontally,
					),
					verticalAlignment = Alignment.CenterVertically,
				) {
					dismissButton?.let {
						Box(modifier = Modifier.weight(1f)) {
							it()
						}
					}
					confirmButton?.let {
						Box(modifier = Modifier.weight(1f)) {
							it()
						}
					}
				}
			}
			if (dismissButton == null) {
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
}

@Composable
fun OneButtonDialog(
	buttonText: String,
	onButtonClick: () -> Unit,
	onDismissRequest: () -> Unit,
	content: @Composable () -> Unit,
) {
	BaseDialog(
		onDismissRequest = onDismissRequest,
		confirmButton = {
			DialogButton(
				text = buttonText,
				onClick = onButtonClick,
			)
		},
		content = content,
	)
}

@Composable
fun TwoButtonDialog(
	dismissButtonText: String,
	confirmButtonText: String,
	onDismissRequest: () -> Unit,
	onConfirmButtonClick: () -> Unit,
	onDismissButtonClick: () -> Unit = onDismissRequest,
	content: @Composable () -> Unit,
) {
	BaseDialog(
		onDismissRequest = onDismissRequest,
		dismissButton = {
			DialogButton(
				text = dismissButtonText,
				onClick = onDismissButtonClick,
				containerColor = Gray800,
				contentColor = White,
			)
		},
		confirmButton = {
			DialogButton(
				text = confirmButtonText,
				onClick = onConfirmButtonClick,
			)
		},
		content = content,
	)
}

@Composable
fun DialogButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	containerColor: Color = BrakeYellow,
	contentColor: Color = Gray850,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Button(
		shape = RoundedCornerShape(12.dp),
		colors = ButtonDefaults.buttonColors(
			containerColor = containerColor,
			contentColor = contentColor,
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
private fun TwoButtonsDialog() {
	BrakeTheme {
		BaseDialog(
			onDismissRequest = { },
			dismissButton = {
				DialogButton(
					text = "취소",
					onClick = {},
					containerColor = Gray800,
					contentColor = White,
				)
			},
			confirmButton = {
				DialogButton(
					text = "탈퇴",
					onClick = {},
				)
			},
		) {
			Column(
				modifier = Modifier.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				Image(
					painter = painterResource(id = R.drawable.img_warning),
					contentDescription = null,
				)
				VerticalSpacer(16.dp)
				Text(
					text = "정말 탈퇴하시겠어요?",
					style = BrakeTheme.typography.subtitle22SB,
					color = White,
				)
				VerticalSpacer(12.dp)
				Text(
					text = "탈퇴하면 모든 계정 정보와 이용 기록이 \n삭제되며, 복구할 수 없습니다.",
					style = BrakeTheme.typography.body16M,
					color = Gray300,
					textAlign = TextAlign.Center,
				)
			}
		}
	}
}

@Preview
@Composable
private fun OneButtonDialogPreview() {
	BrakeTheme {
		OneButtonDialog(
			buttonText = "확인",
			onButtonClick = { },
			onDismissRequest = { },
		) {

		}
	}
}
