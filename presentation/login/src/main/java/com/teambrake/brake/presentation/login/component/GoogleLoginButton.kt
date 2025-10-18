package com.teambrake.brake.presentation.login.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.Gray200
import com.teambrake.brake.core.designsystem.theme.Gray700
import com.teambrake.brake.core.designsystem.util.BooleanProvider
import com.teambrake.brake.core.designsystem.util.MultipleEventsCutter
import com.teambrake.brake.core.designsystem.util.get
import com.teambrake.brake.presentation.login.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GoogleLoginButton(
	modifier: Modifier = Modifier,
	onClick: () -> Unit,
	enabled: Boolean = true,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Button(
		shape = MaterialTheme.shapes.large,
		colors = ButtonDefaults.buttonColors(
			containerColor = Color(0xFF141414),
			contentColor = Color(0xFFe4e4e4),
			disabledContainerColor = Gray700,
			disabledContentColor = Gray200,
		),
		border = BorderStroke(
			width = 1.dp,
			color = Color(0xFF8f918f),
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
			Box(
				modifier = Modifier
					.weight(0.2f)
					.wrapContentHeight(),
				contentAlignment = Alignment.Center,
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_g),
					contentDescription = "구글 로고",
					modifier = Modifier.padding(end = 8.dp),
					tint = null,
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
					text = stringResource(R.string.login_google_button_text),
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

@Preview
@Composable
private fun GoogleLoginButtonPreview(
	@PreviewParameter(BooleanProvider::class) enabled: Boolean,
) {
	BrakeTheme {
		GoogleLoginButton(
			onClick = { },
			enabled = enabled,
		)
	}
}
