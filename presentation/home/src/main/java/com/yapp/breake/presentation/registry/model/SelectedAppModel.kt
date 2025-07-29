package com.yapp.breake.presentation.registry.model

import android.graphics.drawable.Drawable

data class SelectedAppModel(
	val index: Int,
	val name: String,
	val packageName: String,
	val icon: Drawable?,
)
