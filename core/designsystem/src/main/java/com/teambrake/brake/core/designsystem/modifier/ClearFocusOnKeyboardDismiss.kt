package com.teambrake.brake.core.designsystem.modifier

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager

/**
* Modifier that clears focus when the keyboard is dismissed.
*
* To clear the focus of a TextField when the keyboard is dismissed,
* use the LocalFocusManager and call clearFocus() inside the modifier.clickable() of the root composable.
 * ```kotlin
 *    val focusManager = LocalFocusManager.current
 *    focusManager.clearFocus()
 * ```
 */
@OptIn(ExperimentalLayoutApi::class)
@Stable
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
	var isFocused by remember { mutableStateOf(false) }
	var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }

	if (isFocused) {
		val imeIsVisible = WindowInsets.isImeVisible
		val focusManager = LocalFocusManager.current

		LaunchedEffect(imeIsVisible) {
			if (imeIsVisible) {
				keyboardAppearedSinceLastFocused = true
			} else if (keyboardAppearedSinceLastFocused) {
				focusManager.clearFocus()
			}
		}
	}

	onFocusEvent {
		if (isFocused != it.isFocused) {
			isFocused = it.isFocused
			if (isFocused) keyboardAppearedSinceLastFocused = false
		}
	}
}
