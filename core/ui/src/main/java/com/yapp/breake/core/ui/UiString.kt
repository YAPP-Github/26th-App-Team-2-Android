package com.yapp.breake.core.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * For use of StringRes in ViewModel
 */
sealed class UiString {
	data class DynamicString(val value: String) : UiString()

	// data class 의 주 생성자 vararg 사용 불가
	class ResourceString(
		@StringRes val resId: Int,
		vararg val args: Any,
	) : UiString()

	/**
	 * Used in Composable fun
	 */
	@Composable
	fun asString(): String {
		return when (this) {
			is DynamicString -> value
			is ResourceString -> stringResource(resId, *args)
		}
	}

	/**
	 * Used in non-Composable fun
	 */
	fun asString(context: Context): String {
		return when (this) {
			is DynamicString -> value
			is ResourceString -> context.getString(resId, *args)
		}
	}
}
