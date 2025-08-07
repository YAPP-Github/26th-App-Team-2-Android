package com.yapp.breake.presentation.onboarding.guide

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breake.core.permission.PermissionManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.yapp.breake.core.model.user.Destination
import com.yapp.breake.core.ui.UiString
import com.yapp.breake.domain.usecase.LogoutUseCase
import com.yapp.breake.presentation.onboarding.R
import com.yapp.breake.presentation.onboarding.guide.model.GuideModalState
import com.yapp.breake.presentation.onboarding.guide.model.GuideNavState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject constructor(
	private val permissionManager: PermissionManager,
	private val logoutUseCase: LogoutUseCase,
	private val firebaseAnalytics: FirebaseAnalytics,
) : ViewModel() {
	private val _snackBarFlow = MutableSharedFlow<UiString>()
	val snackBarFlow = _snackBarFlow.asSharedFlow()

	private val _modalFlow = MutableStateFlow<GuideModalState>(GuideModalState.GuideIdle)
	val modalFlow = _modalFlow.asStateFlow()

	private val _navigationFlow = MutableSharedFlow<GuideNavState>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	fun tryLogout() {
		_modalFlow.value = GuideModalState.ShowLogoutModal
	}

	fun dismissModal() {
		_modalFlow.value = GuideModalState.GuideIdle
	}

	fun logout() {
		viewModelScope.launch {
			val dest = logoutUseCase(
				onError = { error ->
					_snackBarFlow.emit(
						UiString.ResourceString(
							resId = R.string.onboarding_snackbar_logout_error,
						),
					)
				},
			)
			if (dest is Destination.Login) {
				firebaseAnalytics.logEvent("logout") {
					param("reason", "user_requested")
				}
				_navigationFlow.emit(GuideNavState.NavigateToLogin)
			}
		}
	}

	fun continueFromGuide(context: Context) {
		viewModelScope.launch {
			if (permissionManager.isAllGranted(context)) {
				_navigationFlow.emit(GuideNavState.NavigateToComplete)
			} else {
				_navigationFlow.emit(GuideNavState.NavigateToPermission)
			}
		}
	}
}
