package com.yapp.breake.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.core.designsystem.theme.Gray900
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.core.designsystem.util.MultipleEventsCutter
import com.yapp.breake.core.designsystem.util.get

@Composable
fun ErrorBody(
	onRetry: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.fillMaxSize()
			.padding(horizontal = 36.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		Image(
			painter = painterResource(R.drawable.img_error),
			contentDescription = null,
			modifier = Modifier.size(140.dp),
		)
		VerticalSpacer(28.dp)
		Text(
			text = stringResource(R.string.error_title),
			style = BrakeTheme.typography.subtitle22SB,
			color = MaterialTheme.colorScheme.onSurface,
			textAlign = TextAlign.Center,
		)
		VerticalSpacer(10.dp)
		Text(
			text = stringResource(R.string.error_message),
			style = BrakeTheme.typography.body16M,
			color = Gray200,
			textAlign = TextAlign.Center,
		)
		VerticalSpacer(24.dp)
		RetryButton(
			onRetryClick = onRetry,
		)
	}
}

@Composable
internal fun RetryButton(
	onRetryClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.get() }

	Button(
		shape = RoundedCornerShape(16.dp),
		colors = ButtonDefaults.buttonColors(
			containerColor = Gray900,
			contentColor = White,
		),
		contentPadding = PaddingValues(vertical = 15.dp, horizontal = 22.dp),
		onClick = { multipleEventsCutter.processEvent(onRetryClick) },
		modifier = modifier,
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Icon(
				painter = painterResource(id = R.drawable.ic_refresh),
				contentDescription = stringResource(R.string.retry_content_description),
			)
			HorizontalSpacer(6.dp)
			Text(
				text = stringResource(R.string.retry),
				style = BrakeTheme.typography.body14SB,
				textAlign = TextAlign.Center,
			)
		}
	}
}

@Preview
@Composable
private fun ErrorBodyPreview() {
	BrakeTheme {
		ErrorBody(
			onRetry = {},
		)
	}
}
