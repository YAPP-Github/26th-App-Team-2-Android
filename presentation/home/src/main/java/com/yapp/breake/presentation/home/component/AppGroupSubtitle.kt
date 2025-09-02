package com.yapp.breake.presentation.home.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.BrakeYellow
import com.yapp.breake.core.designsystem.theme.Gray700

@Composable
internal fun AppGroupSubtitle(
	modifier: Modifier = Modifier,
	@StringRes titleResId: Int,
	currentIndex: Int,
	totalCount: Int,
) {
	Row(
		verticalAlignment = Alignment.Bottom,
		modifier = Modifier.fillMaxWidth().then(modifier),
	) {
		Text(
			text = stringResource(titleResId),
			style = BrakeTheme.typography.subtitle18SB,
			color = MaterialTheme.colorScheme.onSurface,
		)

		HorizontalSpacer(1f)
		val annotatedString = buildAnnotatedString {
			withStyle(
				style = SpanStyle(
					color = BrakeYellow,
				),
			) {
				append("$currentIndex")
			}
			append("/$totalCount")
		}
		Text(
			text = annotatedString,
			modifier = Modifier.padding(end = 12.dp),
			style = BrakeTheme.typography.body12B,
			color = Gray700,
		)
	}
}
