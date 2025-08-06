package com.yapp.breake.presentation.legal.terms

import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsIntent.SHARE_STATE_OFF
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.yapp.breake.core.designsystem.R
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorAction

@Composable
fun TermsRoute(
	viewModel: TermsViewModel = hiltViewModel(),
) {
	val navAction = LocalNavigatorAction.current

	TermsScreen(
		onBack = { viewModel.onBackPressed(navAction::popBackStack) },
	)
}

@Composable
fun TermsScreen(onBack: () -> Unit) {
	val context = LocalContext.current
	val lifecycleOwner = LocalLifecycleOwner.current

	DisposableEffect(Unit) {
		val observer = LifecycleEventObserver { _, event ->
			if (event == Lifecycle.Event.ON_RESUME) {
				onBack()
			}
		}
		lifecycleOwner.lifecycle.addObserver(observer)
		onDispose {
			lifecycleOwner.lifecycle.removeObserver(observer)
		}
	}

	val customTabsIntent = CustomTabsIntent.Builder().apply {
		setBookmarksButtonEnabled(false)
		setDownloadButtonEnabled(false)
		setShareState(SHARE_STATE_OFF)
		setUrlBarHidingEnabled(true)
		setShowTitle(true)
		setInstantAppsEnabled(false)
		setCloseButtonIcon(context.getDrawable(R.drawable.ic_back_24)!!.toBitmap())
	}.build()
	customTabsIntent.launchUrl(context, TERMS_BASE_URL.toUri())
}

private const val TERMS_BASE_URL =
	"https://ahnsh.notion.site/Brake-223b76e3040280f3b9e0d70c83e239ac?pvs=143"
