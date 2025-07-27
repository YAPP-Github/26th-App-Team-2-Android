package com.yapp.breake.core.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream

fun Drawable?.toByteArray(): ByteArray? {
	if (this == null) return null

	return try {
		val bitmap = this.toBitmap()
		val outputStream = ByteArrayOutputStream()
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
		outputStream.toByteArray()
	} catch (e: Exception) {
		null
	}
}
