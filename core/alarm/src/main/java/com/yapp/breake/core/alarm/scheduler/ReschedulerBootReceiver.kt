package com.yapp.breake.core.alarm.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.yapp.breake.core.model.worker.WorkerConfig.RESCHEDULE_ALARM
import dagger.hilt.android.AndroidEntryPoint

/**
 * 재부팅 후 부팅 감지 및 workmanager 를 통해 알람 재예약을 위한 BroadcastReceiver
 * 재부팅 시, WorkManager를 통해 알람 재예약, 즉 Worker 에게 Task 요청
 * 부팅 직후 메인 쓰레드 사용이 불가하며, IO 쓰레드 또한 그러하므로 이곳에서 즉각 알람 예약을 하지 않음
 * WorkManager를 통해 task 예약 시, setExpedited(true) 를 사용하여 Doze 모드 등 어떠한 제한에도 영향을 받지 않고 task를 수행할 수 있도록 함
 */
@AndroidEntryPoint
class ReschedulerBootReceiver : BroadcastReceiver() {

	override fun onReceive(context: Context, intent: Intent) {
		when (intent.action) {
			Intent.ACTION_BOOT_COMPLETED, "android.intent.action.QUICKBOOT_POWERON", "com.htc.intent.action.QUICKBOOT_POWERON" -> {

				val workManager = WorkManager.getInstance(context)
				val request = OneTimeWorkRequestBuilder<ReschedulerWorker>()
					.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
					.build()
				workManager.enqueueUniqueWork(
					RESCHEDULE_ALARM,
					ExistingWorkPolicy.APPEND_OR_REPLACE,
					request,
				)
			}
		}
	}
}
