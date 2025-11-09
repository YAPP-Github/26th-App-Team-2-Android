package com.teambrake.brake.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.coil.CoilImage
import com.teambrake.brake.core.designsystem.R
import com.teambrake.brake.core.designsystem.theme.Gray850

@Composable
fun CircleImage(
	modifier: Modifier = Modifier,
	imageUrl: String? = null,
) {
	CoilImage(
		imageModel = { imageUrl },
		modifier = modifier
			.widthIn(min = 40.dp)
			.aspectRatio(1f)
			.clip(shape = CircleShape),
		// 이미지 로딩 중 표시 내용
		loading = {
			Box(modifier = Modifier.matchParentSize()) {
				CircularProgressIndicator(
					modifier = Modifier.align(Alignment.Center),
				)
			}
		},
		// 이미지 요청 실패 시 표시 내용
		failure = {
			Box(
				modifier = Modifier.matchParentSize().background(Gray850).padding(15.dp),
				contentAlignment = Alignment.Center,
			) {
				Image(
					painter = painterResource(id = R.drawable.img_profile_default),
					contentDescription = null,
					modifier = Modifier.matchParentSize(),
				)
			}
		},
	)
}
