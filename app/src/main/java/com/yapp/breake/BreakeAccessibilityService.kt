package com.yapp.breake

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class BreakeAccessibilityService : AccessibilityService() {
	override fun onInterrupt() {}

	override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
}
