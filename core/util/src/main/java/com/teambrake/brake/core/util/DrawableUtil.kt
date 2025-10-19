package com.teambrake.brake.core.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap
import java.io.ByteArrayOutputStream

fun Drawable.toByteArray(): ByteArray? {
	return try {
		val bitmap = if (this is BitmapDrawable) {
			bitmap
		} else {
			val bitmap = createBitmap(
				intrinsicWidth.takeIf { it > 0 } ?: 1,
				intrinsicHeight.takeIf { it > 0 } ?: 1,
			)
			val canvas = Canvas(bitmap)
			setBounds(0, 0, canvas.width, canvas.height)
			draw(canvas)
			bitmap
		}

		val stream = ByteArrayOutputStream()
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
		stream.toByteArray()
	} catch (e: Exception) {
		null
	}
}

fun ByteArray.toImageBitmap(): ImageBitmap? {
	return try {
		val bitmap = BitmapFactory.decodeByteArray(this, 0, size)
		bitmap?.asImageBitmap()
	} catch (e: Exception) {
		null
	}
}

fun ByteArray.toDrawable(): Drawable? {
	return try {
		val bitmap = BitmapFactory.decodeByteArray(this, 0, size)
		bitmap?.let { BitmapDrawable(null, it) }
	} catch (e: Exception) {
		null
	}
}
