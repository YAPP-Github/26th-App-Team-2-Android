package com.yapp.breake.presentation.legal.privacy

import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrivacyViewModel @Inject constructor(
	private val firebaseAnalytics: FirebaseAnalytics,
) : ViewModel() {
	fun onBackPressed(onBack: () -> Unit) {
		firebaseAnalytics.logEvent("privacy_policy_back_pressed", null)
		onBack()
	}
}
