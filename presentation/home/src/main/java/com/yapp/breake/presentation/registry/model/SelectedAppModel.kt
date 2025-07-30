package com.yapp.breake.presentation.registry.model

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Stable

@Stable
data class SelectedAppModel(
	val index: Int,
	val name: String,
	val packageName: String,
	val icon: Drawable?,
)
