package com.yapp.breake.presentation.legal.terms

import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
	private val firebaseAnalytics: FirebaseAnalytics,
) : ViewModel() {
	fun onBackPressed(onBack: () -> Unit) {
		firebaseAnalytics.logEvent("privacy_policy_back_pressed", null)
		onBack()
	}
}
