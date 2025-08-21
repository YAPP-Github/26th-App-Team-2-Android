package com.yapp.breake.presentation.report

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction
import com.yapp.breake.core.ui.ErrorBody
import com.yapp.breake.presentation.report.body.ReportBody
import com.yapp.breake.presentation.report.contract.ReportUiState

@Composable
internal fun ReportRoute(
	padding: PaddingValues,
	viewModel: ReportViewModel = hiltViewModel(),
) {
	val reportUiState by viewModel.reportUiState.collectAsStateWithLifecycle()
	val mainAction = LocalMainAction.current
	val context = LocalContext.current

	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(padding),
	) {
		ReportContent(
			reportUiState = reportUiState,
			onRetry = viewModel::refreshReport,
			loadingContent = {
				mainAction.OnShowLoading()
			},
		)
	}

	LaunchedEffect(true) {
		viewModel.snackBarFlow.collect {
			mainAction.onShowErrorMessage(
				message = it.asString(context = context),
			)
		}
	}
}

@Composable
private fun ReportContent(
	reportUiState: ReportUiState,
	onRetry: () -> Unit,
	loadingContent: @Composable () -> Unit,
) {
	AnimatedContent(
		targetState = reportUiState,
		transitionSpec = {
			fadeIn() togetherWith fadeOut()
		},
		label = "ReportContent",
	) { state ->
		when (state) {
			ReportUiState.Error -> {
				ErrorBody(
					onRetry = onRetry,
				)
			}

			ReportUiState.Loading -> {
				loadingContent()
			}

			is ReportUiState.Success -> {
				ReportBody(
					reportUiState = state,
				)
			}
		}
	}
}
