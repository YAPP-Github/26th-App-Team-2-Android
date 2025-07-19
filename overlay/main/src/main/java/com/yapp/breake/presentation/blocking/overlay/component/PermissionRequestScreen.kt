package com.yapp.breake.presentation.blocking.overlay.component

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequestScreen(
	multiplePermissionsState: MultiplePermissionsState,
	onRequestSystemAlertWindow: () -> Unit,
	systemAlertWindowGranted: Boolean,
	onRequestScheduleExactAlarm: () -> Unit,
	scheduleExactAlarmGranted: Boolean,
	onRequestUsageStatsPermission: () -> Unit,
	usageStatsPermissionGranted: Boolean,
	onRequestAccessibilityService: () -> Unit,
	accessibilityServiceEnabled: Boolean,
	onRequestIgnoreBatteryOptimizations: () -> Unit,
	batteryOptimizationIgnored: Boolean,
	onPermissionsUpdated: () -> Unit,
) {
	LaunchedEffect(Unit) {
		onPermissionsUpdated()
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Text(
			"앱을 사용하기 위해 다음 권한이 필요합니다.",
			style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
		)
		Spacer(modifier = Modifier.height(20.dp))

		if (!multiplePermissionsState.allPermissionsGranted) {
			multiplePermissionsState.permissions.forEach { perm: PermissionState ->
				if (perm.status != PermissionStatus.Granted) {
					Button(onClick = { perm.launchPermissionRequest() }) {
						Text("${perm.permission.substringAfterLast('.')} 권한 요청")
					}
					Spacer(modifier = Modifier.height(8.dp))
				}
			}
		}

		if (!systemAlertWindowGranted) {
			Button(onClick = onRequestSystemAlertWindow) {
				Text("다른 앱 위에 표시 권한 요청 (SYSTEM_ALERT_WINDOW)")
			}
			Spacer(modifier = Modifier.height(8.dp))
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !scheduleExactAlarmGranted) {
			Button(onClick = onRequestScheduleExactAlarm) {
				Text("정확한 알람 설정 권한 요청 (SCHEDULE_EXACT_ALARM)")
			}
			Spacer(modifier = Modifier.height(8.dp))
		}

		if (!usageStatsPermissionGranted) {
			Button(onClick = onRequestUsageStatsPermission) {
				Text("사용 정보 접근 권한 요청 (PACKAGE_USAGE_STATS)")
			}
			Spacer(modifier = Modifier.height(8.dp))
		}

		if (!accessibilityServiceEnabled) {
			Button(onClick = onRequestAccessibilityService) {
				Text("접근성 서비스 활성화 요청")
			}
			Spacer(modifier = Modifier.height(8.dp))
		}

		if (!batteryOptimizationIgnored) {
			Button(onClick = onRequestIgnoreBatteryOptimizations) {
				Text("배터리 최적화 예외 요청 (IGNORE_BATTERY_OPTIMIZATIONS)")
			}
			Spacer(modifier = Modifier.height(8.dp))
		}

		Spacer(modifier = Modifier.height(16.dp))
		Button(onClick = onPermissionsUpdated) {
			Text("권한 상태 새로고침")
		}

		if (multiplePermissionsState.allPermissionsGranted &&
			systemAlertWindowGranted &&
			scheduleExactAlarmGranted &&
			accessibilityServiceEnabled &&
			batteryOptimizationIgnored
		) {
			Text("모든 필수 권한이 허용되었습니다. 앱을 다시 시작하거나 이 화면이 자동으로 닫힙니다.")
		}
	}
}
