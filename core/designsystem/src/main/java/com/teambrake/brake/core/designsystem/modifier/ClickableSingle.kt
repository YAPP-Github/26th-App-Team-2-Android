package com.teambrake.brake.core.designsystem.modifier

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import com.teambrake.brake.core.designsystem.util.MultipleEventsCutter
import com.teambrake.brake.core.designsystem.util.get

fun Modifier.clickableSingle(
	onClick: () -> Unit,
	enabled: Boolean = true,
	onClickLabel: String? = null,
	role: Role? = null,
) = composed(
	inspectorInfo = debugInspectorInfo {
		name = "clickable"
		properties["enabled"] = enabled
		properties["onClickLabel"] = onClickLabel
		properties["role"] = role
		properties["onClick"] = onClick
	},
) {
	val multipleEventsCutter = remember { MultipleEventsCutter.Companion.get() }
	Modifier.clickable(
		enabled = enabled,
		onClickLabel = onClickLabel,
		onClick = { multipleEventsCutter.processEvent(onClick) },
		role = role,
		indication = LocalIndication.current,
		interactionSource = remember { MutableInteractionSource() },
	)
}
