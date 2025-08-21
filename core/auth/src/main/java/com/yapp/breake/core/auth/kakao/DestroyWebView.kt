package com.yapp.breake.core.auth.kakao

import android.webkit.CookieManager
import android.webkit.WebView
import timber.log.Timber

internal fun WebView.destroySafely() {
	CookieManager.getInstance().removeAllCookies { success ->
		if (success) {
			Timber.d("모든 쿠키가 성공적으로 삭제됨")
		} else {
			Timber.w("쿠키 삭제 실패")
		}
	}

	stopLoading()
	clearHistory()
	clearCache(true)
	removeAllViews()
	destroy()
}
