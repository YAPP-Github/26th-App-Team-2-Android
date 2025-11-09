package com.teambrake.brake.core.alarm.scheduler

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.teambrake.brake.core.common.AlarmAction
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.domain.repository.AlarmScheduler
import com.teambrake.brake.domain.repository.AppGroupRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber

/**
 * 재부팅 후 ReschedulerBootReceiver 를 통해 알람 재등록 업부 수행
 * 해당 worker 의 task 예약 시, setExpedited(true) 를 사용하여 Doze 모드 등 어떠한 제한에도 영향을 받지 않고 task를 수행할 수 있도록 함
 */
@HiltWorker
class ReschedulerWorker @AssistedInject constructor(
	@Assisted appContext: Context,
	@Assisted workerParams: WorkerParameters,
	private val groupRepository: AppGroupRepository,
	private val alarmScheduler: AlarmScheduler,
) : CoroutineWorker(appContext, workerParams) {
	override suspend fun doWork(): Result {
		return try {
			groupRepository.observeAppGroup().firstOrNull()?.let { appGroups ->
				appGroups.forEach { group ->
					when (val state = group.appGroupState) {
						AppGroupState.Using, AppGroupState.Blocking -> {
							alarmScheduler.scheduleAlarm(
								groupId = group.id,
								groupName = group.name,
								triggerTime = group.endTime ?: return@forEach,
								action = if (state == AppGroupState.Using) {
									AlarmAction.ACTION_USING
								} else {
									AlarmAction.ACTION_BLOCKING
								},
							).onFailure { exception ->
								Timber.e("알람 예약 실패: ${exception.message}")
							}
							Timber.i("AppGroupState.Off: ${group.name} 그룹은 알람 예약이 필요하지 않습니다.")
						}
						else -> {}
					}
				}
			}
			Result.success()
		} catch (_: Exception) {
			Timber.e("ReschedulerWorkManager 작업 실패")
			Result.failure()
		}
	}
}
