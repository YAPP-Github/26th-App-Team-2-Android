package com.yapp.breake.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.R
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.core.designsystem.theme.Gray700
import com.yapp.breake.core.designsystem.theme.Gray800
import com.yapp.breake.core.designsystem.theme.KakaoYellow
import com.yapp.breake.core.designsystem.theme.Red
import com.yapp.breake.core.designsystem.util.BooleanProvider
import com.yapp.breake.core.designsystem.util.MultipleEventsCutter
import com.yapp.breake.core.designsystem.util.get

@Composable
fun LargeButton(
	text: String,
	modifier: Modifier = Modifier,
	textStyle: TextStyle = BrakeTheme.typography.subtitle16B,
	paddingValues: PaddingValues = PaddingValues(vertical = 18.dp, horizontal = 16.dp),
	leadingIcon: @Composable (() -> Unit)? = null,
	colors: ButtonColors = ButtonDefaults.buttonColors(
		disabledContainerColor = Gray700,
		disabledContentColor = MaterialTheme.colorScheme.onPrimary,
	),
	onClick: () -> Unit,
	enabled: Boolean = true,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Button(
		shape = MaterialTheme.shapes.large,
		colors = colors,
		contentPadding = paddingValues,
		enabled = enabled,
		onClick = { multipleEventsCutter.processEvent(onClick) },
		modifier = modifier.fillMaxWidth(),
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Center,
		) {
			leadingIcon?.let {
				leadingIcon()
				HorizontalSpacer(6.dp)
			}
			Text(
				text = text,
				style = textStyle,
			)
		}
	}
}

@Composable
fun SmallButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Button(
		shape = CircleShape,
		colors = ButtonDefaults.buttonColors(
			containerColor = MaterialTheme.colorScheme.primary,
			contentColor = MaterialTheme.colorScheme.onPrimary,
			disabledContainerColor = MaterialTheme.colorScheme.background,
			disabledContentColor = Gray200,
		),
		contentPadding = PaddingValues(vertical = 13.dp, horizontal = 16.dp),
		enabled = enabled,
		onClick = { multipleEventsCutter.processEvent(onClick) },
		modifier = modifier,
	) {
		Text(
			text = text,
			style = BrakeTheme.typography.subtitle16B,
			textAlign = TextAlign.Center,
			modifier = Modifier.defaultMinSize(minWidth = 120.dp),
		)
	}
}

@Composable
fun BoxButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	color: Color = Red,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Button(
		shape = MaterialTheme.shapes.medium,
		colors = ButtonDefaults.buttonColors(
			containerColor = color.copy(alpha = 0.18f),
			contentColor = color,
		),
		contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
		onClick = { multipleEventsCutter.processEvent(onClick) },
		modifier = modifier,
	) {
		Text(
			text = text,
			style = BrakeTheme.typography.body14M,
			textAlign = TextAlign.Center,
		)
	}
}

@Composable
fun CircleButton(
	@DrawableRes icon: Int,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	containerColor: Color = MaterialTheme.colorScheme.secondary,
	contentColor: Color = Gray800,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Surface(
		shape = CircleShape,
		color = containerColor,
		onClick = { multipleEventsCutter.processEvent(onClick) },
		modifier = modifier.size(56.dp),
	) {
		Icon(
			painter = painterResource(id = icon),
			contentDescription = null,
			tint = contentColor,
			modifier = Modifier.padding(8.dp),
		)
	}
}

@Composable
fun KakaoLoginButton(
	modifier: Modifier = Modifier,
	text: String = "카카오 로그인",
	onClick: () -> Unit,
	enabled: Boolean = true,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Button(
		shape = MaterialTheme.shapes.large,
		colors = ButtonDefaults.buttonColors(
			containerColor = KakaoYellow,
			contentColor = MaterialTheme.colorScheme.onPrimary,
			disabledContainerColor = Gray700,
			disabledContentColor = Gray200,
		),
		contentPadding = PaddingValues(16.dp),
		enabled = enabled,
		onClick = { multipleEventsCutter.processEvent(onClick) },
		modifier = modifier.fillMaxWidth(),
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Start,
		) {
			// 왼쪽 카카오 로고 영역 (전체 폭의 20%)
			Box(
				modifier = Modifier
					.weight(0.2f)
					.wrapContentHeight(),
				contentAlignment = Alignment.Center,
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_kakao_logo),
					contentDescription = "카카오 로고",
					tint = MaterialTheme.colorScheme.onPrimary,
					modifier = Modifier.padding(end = 8.dp),
				)
			}

			// 가운데 텍스트 영역 (전체 폭의 60%)
			Box(
				modifier = Modifier
					.weight(0.6f)
					.wrapContentHeight(),
				contentAlignment = Alignment.Center,
			) {
				Text(
					text = text,
					style = BrakeTheme.typography.subtitle16B,
				)
			}

			// 오른쪽 빈 공간 영역 (전체 폭의 20%)
			Box(
				modifier = Modifier
					.weight(0.2f)
					.wrapContentHeight(),
			)
		}
	}
}

@Preview("Large Buttons")
@Composable
private fun LargeButtonPreview(
	@PreviewParameter(BooleanProvider::class) enabled: Boolean,
) {
	BrakeTheme {
		LargeButton(
			text = "Large Button",
			onClick = { },
			enabled = enabled,
		)
	}
}

@Preview("Small Buttons")
@Composable
private fun SmallButtonPreview(
	@PreviewParameter(BooleanProvider::class) enabled: Boolean,
) {
	BrakeTheme {
		SmallButton(
			text = "Small Button",
			onClick = { },
			enabled = enabled,
		)
	}
}

@Preview("Box Button")
@Composable
private fun BoxButtonPreview() {
	BrakeTheme {
		BoxButton(
			text = "Button",
			onClick = { },
		)
	}
}

@Preview("Box Button")
@Composable
private fun CircleButtonPreview() {
	BrakeTheme {
		CircleButton(
			icon = R.drawable.ic_close,
			onClick = { },
		)
	}
}

@Preview
@Composable
private fun KakaoLoginButtonPreview(
	@PreviewParameter(BooleanProvider::class) enabled: Boolean,
) {
	BrakeTheme {
		KakaoLoginButton(
			text = "카카오 로그인",
			onClick = { },
			enabled = enabled,
		)
	}
}
