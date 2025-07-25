package com.yapp.breake.presentation.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.component.VerticalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray100
import com.yapp.breake.core.designsystem.theme.Gray200
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.presentation.home.component.AppGroupList

@Composable
internal fun ListScreen(
	appGroups: List<AppGroup>,
	onAppGroupClick: (AppGroup) -> Unit,
) {
	Column(
		modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Text(
			text = "등록한 앱을 사용할 때\n사용 시간을 설정할 수 있어요",
			style = BrakeTheme.typography.subtitle20SB,
			textAlign = TextAlign.Center,
			color = Gray100,
			modifier = Modifier.fillMaxWidth(),
		)
		VerticalSpacer(30.dp)
		Row(
			verticalAlignment = Alignment.Bottom,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 24.dp),
		) {
			Text(
				text = "그룹",
				style = BrakeTheme.typography.subtitle22SB,
				color = MaterialTheme.colorScheme.onSurface,
			)
			HorizontalSpacer(1f)
			Text(
				text = "총 ${appGroups.size}개",
				style = BrakeTheme.typography.body12M,
				color = Gray200,
			)
		}
		VerticalSpacer(16.dp)
		AppGroupList(
			appGroups = appGroups,
			onAppGroupClick = onAppGroupClick,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp),
		)
	}
}

@Preview
@Composable
private fun ListScreenPreview() {
	BrakeTheme {
		ListScreen(
			appGroups = listOf(AppGroup.sample),
			onAppGroupClick = { /* TODO: Handle app group click */ },
		)
	}
}
