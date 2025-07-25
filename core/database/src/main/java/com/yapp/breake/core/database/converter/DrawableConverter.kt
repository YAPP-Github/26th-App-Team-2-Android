package com.yapp.breake.core.database.converter

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class DrawableConverter {

	@TypeConverter
	fun fromDrawable(drawable: Drawable?): ByteArray? {
		if (drawable == null) return null

		return try {
			val bitmap = drawable.toBitmap()
			val outputStream = ByteArrayOutputStream()
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
			outputStream.toByteArray()
		} catch (e: Exception) {
			null
		}
	}

	@TypeConverter
	fun toDrawable(byteArray: ByteArray?): Drawable? {
		if (byteArray == null) return null

		return try {
			val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
			BitmapDrawable(Resources.getSystem(), bitmap)
		} catch (e: Exception) {
			null
		}
	}
}
