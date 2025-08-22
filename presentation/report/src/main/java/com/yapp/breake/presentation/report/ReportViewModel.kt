package com.yapp.breake.presentation.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.ui.UiString
import com.yapp.breake.domain.repository.StatisticRepository
import com.yapp.breake.presentation.report.contract.ReportUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ReportViewModel @Inject constructor(
	private val statisticRepository: StatisticRepository,
) : ViewModel() {

	private val trigger = MutableSharedFlow<Unit>(replay = 1)

	@OptIn(ExperimentalCoroutinesApi::class)
	val reportUiState: StateFlow<ReportUiState> = trigger.onStart {
		refreshReport()
	}.flatMapLatest {
		statisticRepository.getStatistics {
			_snackBarFlow.emit(
				UiString.DynamicString(it.message ?: "Unknown error occurred"),
			)
		}
	}.map { statistics ->
		if (statistics == null) {
			ReportUiState.Error
		} else {
			ReportUiState.Success(
				statisticList = statistics,
			)
		}
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5_000),
		initialValue = ReportUiState.Loading,
	)

	private val _snackBarFlow = MutableSharedFlow<UiString>()
	val snackBarFlow = _snackBarFlow.asSharedFlow()

	fun refreshReport() {
		viewModelScope.launch {
			trigger.emit(Unit)
		}
	}
}
