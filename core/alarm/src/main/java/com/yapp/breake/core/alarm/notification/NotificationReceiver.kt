package com.yapp.breake.core.alarm.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yapp.breake.core.common.IntentConstants
import timber.log.Timber

class NotificationReceiver : BroadcastReceiver() {

	override fun onReceive(context: Context, intent: Intent) {
		Timber.d("onReceive 호출됨, action: ${intent.action}")
		if (intent.action == IntentConstants.ACTION_SHOW_NOTIFICATION) {
			Timber.d("${IntentConstants.ACTION_SHOW_NOTIFICATION} 수신됨.")

			val overlayIntent = Intent(
				context,
				Class.forName("com.yapp.breake.presentation.overlay.OverlayActivity"),
			).apply {
				action = IntentConstants.ACTION_SHOW_BREAK_TIME_OVERLAY
				flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
			}
			context.startActivity(overlayIntent)
			Timber.d("OverlayActivity 시작 인텐트 전송됨.")
		}
	}
}
