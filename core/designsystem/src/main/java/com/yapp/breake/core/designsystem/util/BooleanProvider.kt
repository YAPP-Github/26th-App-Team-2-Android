package com.yapp.breake.core.designsystem.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class BooleanProvider : PreviewParameterProvider<Boolean> {
	override val values = sequenceOf(
		true,
		false,
	)
}
