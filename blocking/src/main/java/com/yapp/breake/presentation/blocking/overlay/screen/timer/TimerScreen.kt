package com.yapp.breake.presentation.blocking.overlay.screen.timer

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
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
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.overlay.R

@Composable
internal fun TimerOverlay(
	groupId: Long,
	onGoBack: () -> Unit,
	viewModel: TimerViewModel = hiltViewModel(),
) {
	val context = LocalContext.current

	TimerScreen(
		time = viewModel.time.intValue,
		onTimeChange = viewModel::setTime,
		onComplete = {
			viewModel.setBreakTimeAlarm(context, groupId)
			onGoBack()
		},
	)

	LaunchedEffect(Unit) {
		viewModel.toastEffect.collect { message ->
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
				value = time.toString(),
				onValueChange = {
					val newValue = it.toIntOrNull() ?: 0
					onTimeChange(newValue)
				},
				keyboardOptions = KeyboardOptions.Default.copy(
					keyboardType = KeyboardType.Number,
				),
				singleLine = true,
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.background(Gray200),
			)
		}
		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			LargeButton(
				text = stringResource(id = R.string.timer_complete),
				onClick = onComplete,
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
