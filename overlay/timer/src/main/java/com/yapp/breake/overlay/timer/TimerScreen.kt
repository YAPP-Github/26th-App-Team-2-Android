package com.yapp.breake.overlay.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.breake.core.designsystem.component.BaseScaffold
import com.yapp.breake.core.designsystem.component.LargeButton
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction

@Composable
fun TimerOverlay(
	groupId: Long,
	onCloseOverlay: () -> Unit,
) {
	TimerContent(
		groupId = groupId,
		onFinishApp = onCloseOverlay,
	)
}

@Composable
private fun TimerContent(
	groupId: Long,
	onFinishApp: () -> Unit,
	viewModel: TimerViewModel = hiltViewModel(),
) {
	val mainAction = LocalMainAction.current

	TimerScreen(
		time = viewModel.time.intValue,
		onTimeChange = viewModel::setTime,
		onComplete = {
			viewModel.setBreakTimeAlarm(groupId)
			onFinishApp()
		},
	)

	LaunchedEffect(Unit) {
		viewModel.toastEffect.collect { message ->
			mainAction.onShowToast(message)
		}
	}
}

@Composable
private fun TimerScreen(
	time: Int,
	onTimeChange: (Int) -> Unit,
	onComplete: () -> Unit,
) {
	BaseScaffold {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			VerticalSpacer(30.dp)
			Text(
				text = stringResource(id = R.string.timer_title),
				style = BrakeTheme.typography.title24B,
				textAlign = TextAlign.Center,
				color = MaterialTheme.colorScheme.onBackground,
				modifier = Modifier.fillMaxWidth(),
			)
			VerticalSpacer(30.dp)
			TextField(
				value = if (time == 0) "" else time.toString(),
				onValueChange = {
					val newValue = if (it.isEmpty()) 0 else it.toIntOrNull() ?: 0
					onTimeChange(newValue)
				},
				keyboardOptions = KeyboardOptions.Default.copy(
					keyboardType = KeyboardType.Number,
				),
				singleLine = true,
				placeholder = { Text("초 입력") },
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp),
			)
		}
		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			LargeButton(
				text = stringResource(id = R.string.timer_complete),
				onClick = onComplete,
				enabled = time > 0,
				modifier = Modifier
					.padding(horizontal = 16.dp),
			)
			VerticalSpacer(20.dp)
		}
	}
}

@Preview
@Composable
private fun TimerScreenPreview() {
	BrakeTheme {
		TimerScreen(
			time = 0,
			onTimeChange = { /* Do nothing */ },
			onComplete = { /* Do nothing */ },
		)
	}
}
