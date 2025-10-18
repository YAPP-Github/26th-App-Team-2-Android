package com.teambrake.brake.overlay.main

import androidx.lifecycle.ViewModel
import com.teambrake.brake.domain.repository.AppGroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OverlayViewModel @Inject constructor(
	private val appGroupRepository: AppGroupRepository,
) : ViewModel()
