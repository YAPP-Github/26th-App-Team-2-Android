package com.yapp.breake.presentation.overlay // 패키지 변경

import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

val LocalOverlayViewHolder = compositionLocalOf<OverlayViewHolder> {
	error("No OverlayViewHolder provided")
}

class OverlayViewHolder(private val context: Context) {
	var view: ComposeView? = null
	var lifecycleManager: OverlayLifecycleManager? = null

	fun show(
		content: @Composable () -> Unit,
	) {
		val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

		if (this.view != null) {
			return
		}

		val lifecycleManager = OverlayLifecycleManager()
		this.lifecycleManager = lifecycleManager
		lifecycleManager.performCreate(null)

		val composeView = ComposeView(context).apply {
			setViewTreeLifecycleOwner(lifecycleManager)
			setViewTreeSavedStateRegistryOwner(lifecycleManager)
			setViewTreeViewModelStoreOwner(lifecycleManager)
			setContent {
				content()
			}
		}
		this.view = composeView

		val params = WindowManager.LayoutParams(
			WindowManager.LayoutParams.MATCH_PARENT,
			WindowManager.LayoutParams.MATCH_PARENT,
			WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
			WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
			PixelFormat.TRANSLUCENT,
		).apply {
			gravity = Gravity.CENTER
		}

		try {
			windowManager.addView(composeView, params)
			lifecycleManager.handleLifecycleEvent(Lifecycle.Event.ON_START)
			lifecycleManager.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
		} catch (e: Exception) {
			e.printStackTrace()
			this.view = null
			this.lifecycleManager?.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
			this.lifecycleManager?.clearViewModelStore()
			this.lifecycleManager = null
		}
	}

	fun remove() {
		this.view?.let { composeView ->
			try {
				val windowManager = context.getSystemService(
					Context.WINDOW_SERVICE,
				) as WindowManager
				windowManager.removeView(composeView)
				this.lifecycleManager?.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
				this.lifecycleManager?.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
				this.lifecycleManager?.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
				this.lifecycleManager?.clearViewModelStore()
				composeView.disposeComposition()
			} catch (e: Exception) {
				e.printStackTrace()
			} finally {
				this.view = null
				this.lifecycleManager = null
			}
		}
	}
}

class OverlayLifecycleManager : LifecycleOwner, SavedStateRegistryOwner, ViewModelStoreOwner {
	private val lifecycleRegistry = LifecycleRegistry(this)
	private val savedStateRegistryController = SavedStateRegistryController.create(this)
	private val _viewModelStore = ViewModelStore()

	override val lifecycle: Lifecycle
		get() = lifecycleRegistry

	override val savedStateRegistry: SavedStateRegistry
		get() = savedStateRegistryController.savedStateRegistry

	override val viewModelStore: ViewModelStore
		get() = _viewModelStore

	fun performCreate(savedState: Bundle?) {
		savedStateRegistryController.performRestore(savedState)
		lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
	}

	fun handleLifecycleEvent(event: Lifecycle.Event) {
		lifecycleRegistry.handleLifecycleEvent(event)
	}

	fun clearViewModelStore() {
		_viewModelStore.clear()
	}
}
