package com.yapp.breake.core.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.R
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray850

enum class BrakeSnackbarType {
	SUCCESS,
	ERROR,
}

@Composable
fun BrakeSnackbar(
	snackbarData: SnackbarData,
	modifier: Modifier = Modifier,
) {
	val type = try {
		BrakeSnackbarType.valueOf(snackbarData.visuals.actionLabel ?: "SUCCESS")
	} catch (e: IllegalArgumentException) {
		BrakeSnackbarType.SUCCESS
	}

	val icon = when (type) {
		BrakeSnackbarType.SUCCESS -> R.drawable.ic_snackbar_success
		BrakeSnackbarType.ERROR -> R.drawable.ic_snackbar_error
	}

	SnackbarContent(
		icon = icon,
		message = snackbarData.visuals.message,
		modifier = modifier,
	)
}

@Composable
private fun SnackbarContent(
	icon: Int,
	message: String,
	modifier: Modifier = Modifier,
) {
	val alpha by animateFloatAsState(
		targetValue = 1f,
		label = "snackbar_alpha",
	)

	Box(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp, vertical = 12.dp)
			.alpha(alpha)
			.background(
				color = Gray850,
				shape = RoundedCornerShape(16.dp),
			)
			.padding(16.dp),
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
		) {
			Icon(
				painter = painterResource(id = icon),
				contentDescription = null,
				tint = Color.Unspecified,
			)
			HorizontalSpacer(10.dp)
			Text(
				text = message,
				style = BrakeTheme.typography.body14M,
				color = Color.White,
				modifier = Modifier.weight(1f),
			)
		}
	}
}

@Preview
@Composable
private fun BrakeSnackbarPreview() {
	BrakeTheme {
		SnackbarContent(
			icon = R.drawable.ic_snackbar_success,
			message = "This is a success message!",
			modifier = Modifier.fillMaxWidth(),
		)
	}
}
