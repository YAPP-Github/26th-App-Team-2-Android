package com.yapp.breake.presentation.feeback.opinion

import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsIntent.SHARE_STATE_OFF
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.yapp.breake.core.designsystem.R
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorAction

@Composable
fun OpinionRoute() {
	val navAction = LocalNavigatorAction.current

	OpinionScreen(
		onBack = navAction::popBackStack,
	)
}

@Composable
fun OpinionScreen(onBack: () -> Unit) {
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
	"https://ahnsh.notion.site/245b76e304028092925dd625cd38ceeb?pvs=105"
