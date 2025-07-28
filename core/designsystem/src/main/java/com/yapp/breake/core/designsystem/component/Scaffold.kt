package com.yapp.breake.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BaseScaffold(
	modifier: Modifier = Modifier,
	contentPadding: PaddingValues = PaddingValues(0.dp),
	topBar: @Composable () -> Unit = {},
	bottomBar: @Composable () -> Unit = {},
	snackBarHost: @Composable () -> Unit = {},
	floatingActionButton: @Composable () -> Unit = {},
	containerColor: Color = MaterialTheme.colorScheme.background,
	contentColor: Color = contentColorFor(containerColor),
	statusBarColor: Color = MaterialTheme.colorScheme.background,
	content: @Composable ColumnScope.() -> Unit,
) {
	Scaffold(
		topBar = topBar,
		bottomBar = bottomBar,
		snackbarHost = snackBarHost,
		floatingActionButton = floatingActionButton,
		containerColor = containerColor,
		contentColor = contentColor,
		modifier = modifier
			.navigationBarsPadding()
			.background(color = statusBarColor)
			.statusBarsPadding(),
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(it)
				.padding(contentPadding),
		) {
			content()
		}
	}
}

@Composable
fun GradientScaffold(
	gradient: Brush,
	modifier: Modifier = Modifier,
	contentPadding: PaddingValues = PaddingValues(0.dp),
	topBar: @Composable () -> Unit = {},
	bottomBar: @Composable () -> Unit = {},
	snackBarHost: @Composable () -> Unit = {},
	floatingActionButton: @Composable () -> Unit = {},
	containerColor: Color = MaterialTheme.colorScheme.background,
	contentColor: Color = contentColorFor(containerColor),
	statusBarColor: Color = MaterialTheme.colorScheme.background,
	content: @Composable ColumnScope.() -> Unit,
) {
	Scaffold(
		topBar = topBar,
		bottomBar = bottomBar,
		snackbarHost = snackBarHost,
		floatingActionButton = floatingActionButton,
		containerColor = containerColor,
		contentColor = contentColor,
		modifier = modifier
			.navigationBarsPadding()
			.background(color = statusBarColor)
			.statusBarsPadding(),
	) {
		Box(
			modifier = Modifier
				.fillMaxSize(),
		) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.fillMaxHeight(0.4f)
					.background(gradient),
			)
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(it)
					.padding(contentPadding),
			) {
				content()
			}
		}
	}
}
