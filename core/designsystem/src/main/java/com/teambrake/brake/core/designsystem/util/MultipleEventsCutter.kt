package com.teambrake.brake.core.designsystem.util

interface MultipleEventsCutter {
	fun processEvent(event: () -> Unit)

	companion object
}

fun MultipleEventsCutter.Companion.get(): MultipleEventsCutter =
	MultipleEventsCutterImpl()

private class MultipleEventsCutterImpl : MultipleEventsCutter {
	private val now: Long
		get() = System.currentTimeMillis()

	private var lastEventTimeMs: Long = 0

	override fun processEvent(event: () -> Unit) {
		if (now - lastEventTimeMs >= 300L) {
			event.invoke()
		}
		lastEventTimeMs = now
	}
}
