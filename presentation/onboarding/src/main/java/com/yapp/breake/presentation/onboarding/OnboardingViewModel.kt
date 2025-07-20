package com.yapp.breake.presentation.onboarding

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breake.core.permission.PermissionManager
import com.breake.core.permission.PermissionType
import com.yapp.breake.presentation.onboarding.model.OnboardingEffect
import com.yapp.breake.presentation.onboarding.model.OnboardingEffect.RequestPermission
import com.yapp.breake.presentation.onboarding.model.OnboardingUiState
import com.yapp.breake.presentation.onboarding.model.PermissionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
	private val permissionManager: PermissionManager,
) : ViewModel() {

	private val _errorFlow = MutableSharedFlow<Throwable>()
	val errorFlow = _errorFlow.asSharedFlow()

	private val _uiState = MutableStateFlow<OnboardingUiState>(OnboardingUiState.Guide(0))
	val uiState = _uiState.asStateFlow()

	private val _navigationFlow = MutableSharedFlow<OnboardingEffect>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	private fun checkCurrentPermissions(context: Context) {
		try {
			val permissionList = mutableListOf<PermissionItem>()
			if (!permissionManager.isGranted(context, PermissionType.OVERLAY)) {
				permissionList.add(PermissionItem.OVERLAY)
			}
			if (!permissionManager.isGranted(context, PermissionType.STATS)) {
				permissionList.add(PermissionItem.STATS)
			}
			if (!permissionManager.isGranted(context, PermissionType.EXACT_ALARM)) {
				permissionList.add(PermissionItem.EXACT_ALARM)
			}
			if (!permissionManager.isGranted(context, PermissionType.ACCESSIBILITY)) {
				permissionList.add(PermissionItem.ACCESSIBILITY)
			}
			if (permissionList.isEmpty()) {
				_uiState.value = OnboardingUiState.Complete
			} else {
				_uiState.value =
					OnboardingUiState.Permission(permissions = permissionList.toPersistentList())
			}
		} catch (e: Exception) {
			viewModelScope.launch {
				_errorFlow.emit(e)
			}
		}
	}

	fun continueFromGuide(context: Context) = checkCurrentPermissions(context)

	fun refreshPermissions(context: Context) = checkCurrentPermissions(context)

	fun moveBackToGuide() {
		_uiState.value = OnboardingUiState.Guide(startPage = 2)
	}

	fun requestPermission(context: Context, type: PermissionItem) {
		viewModelScope.launch {
			_navigationFlow.emit(
				RequestPermission(
					permissionManager.getIntent(context, parsePermissionItemToType(type)),
				),
			)
		}
	}

	companion object {
		private val parsePermissionItemToType = { permissionItem: PermissionItem ->
			when (permissionItem) {
				PermissionItem.OVERLAY -> PermissionType.OVERLAY
				PermissionItem.STATS -> PermissionType.STATS
				PermissionItem.EXACT_ALARM -> PermissionType.EXACT_ALARM
				PermissionItem.ACCESSIBILITY -> PermissionType.ACCESSIBILITY
			}
		}
	}
}
