package com.yapp.breake.presentation.permission

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breake.core.permission.PermissionManager
import com.breake.core.permission.PermissionType
import com.yapp.breake.core.model.user.Destination
import com.yapp.breake.domain.usecase.DecideNextDestinationFromPermissionUseCase
import com.yapp.breake.presentation.permission.model.PermissionEffect
import com.yapp.breake.presentation.permission.model.PermissionEffect.RequestPermission
import com.yapp.breake.presentation.permission.model.PermissionItem
import com.yapp.breake.presentation.permission.model.PermissionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
	private val permissionManager: PermissionManager,
	private val decideDestinationUseCase: DecideNextDestinationFromPermissionUseCase,
) : ViewModel() {

	private val _errorFlow = MutableSharedFlow<Throwable>()
	val errorFlow = _errorFlow.asSharedFlow()

	private val _uiState =
		MutableStateFlow<PermissionUiState>(PermissionUiState(permissions = persistentListOf()))
	val uiState = _uiState.asStateFlow()

	private val _navigationFlow = MutableSharedFlow<PermissionEffect>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	private fun stackPermissions(context: Context): PersistentList<PermissionItem> {
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
			return permissionList.toPersistentList()
		} catch (e: Exception) {
			viewModelScope.launch {
				_errorFlow.emit(e)
			}
			// 모든 권한을 포함
			return persistentListOf(
				PermissionItem.OVERLAY,
				PermissionItem.STATS,
				PermissionItem.EXACT_ALARM,
				PermissionItem.ACCESSIBILITY,
			)
		}
	}

	private fun decideNextDestination() {
		viewModelScope.launch {
			val status = decideDestinationUseCase.invoke(
				onError = { error ->
					_errorFlow.emit(error)
				},
			)
			when (status) {
				is Destination.Home -> _navigationFlow.emit(PermissionEffect.NavigateToMain)
				is Destination.Onboarding -> _navigationFlow.emit(
					PermissionEffect.NavigateToComplete,
				)
				else -> {}
			}
		}
	}

	fun refreshPermissions(context: Context) {
		val permissionList = stackPermissions(context)
		if (permissionList.isEmpty()) {
			decideNextDestination()
		} else {
			_uiState.value = PermissionUiState(permissions = permissionList)
		}
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

	fun popBackStack() {
		viewModelScope.launch {
			_navigationFlow.emit(PermissionEffect.NavigateToBack)
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
