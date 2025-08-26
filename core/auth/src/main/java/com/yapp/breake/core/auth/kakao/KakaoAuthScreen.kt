package com.yapp.breake.core.auth.kakao

import android.annotation.SuppressLint
import android.content.Intent
import android.view.ViewGroup.LayoutParams
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.yapp.breake.core.auth.BuildConfig
import com.yapp.breake.core.auth.R
import timber.log.Timber
import java.net.URISyntaxException

private const val KAKAO_AUTH_URL = "https://kauth.kakao.com" // 카카오 진입 URL
private const val KAKAO_ACCOUNT_URL = "https://accounts.kakao.com" // 카카오 로그인 URL
private const val KAKAO_MIDDLE_URL = "https://logins.daum.net" // 로그인 인증 중간 URL
private val KAKAO_REDIRECT_URL
	get() = if (BuildConfig.DEBUG) {
		"https://www.yapp-dev/oauth"
	} else {
		"https://www.brake/oauth"
	}

private val KAKAO_BASE_URL = "$KAKAO_AUTH_URL/oauth/authorize" +
	"?response_type=code" +
	"&client_id=${BuildConfig.KAKAO_REST_API_KEY}" +
	"&redirect_uri=$KAKAO_REDIRECT_URL" +
	"&prompt=login"
private val allowedPrefixes = listOf(
	KAKAO_AUTH_URL,
	KAKAO_ACCOUNT_URL,
	KAKAO_REDIRECT_URL,
	KAKAO_MIDDLE_URL,
	"intent",
)

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun KakaoScreen(
	onBack: () -> Unit,
	onAuthSuccess: (String) -> Unit,
	onAuthError: (String) -> Unit,
) {
	val context = LocalContext.current
	val packageManager = context.packageManager
	var inAppLoaded by remember { mutableStateOf(false) }
	var webViewRef by remember { mutableStateOf<WebView?>(null) }

	// 디바이스 뒤로가기 버튼 처리
	BackHandler {
		// 웹뷰 내에서 뒤로 갈 수 있으면 웹뷰 내에서 뒤로가기
		if (webViewRef?.canGoBack() == true) {
			webViewRef?.goBack()
		} else {
			// 웹뷰 내에서 뒤로 갈 수 없으면 웹뷰 닫기
			webViewRef?.destroySafely()
			onBack()
		}
	}

	val webView = WebView(context).apply {
		webViewRef = this

		settings.apply {
			javaScriptEnabled = true
			domStorageEnabled = true
			javaScriptCanOpenWindowsAutomatically = true
			setSupportMultipleWindows(true)
			layoutParams = LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT,
			)
		}

		webViewClient = object : WebViewClient() {
			override fun shouldOverrideUrlLoading(
				view: WebView?,
				request: WebResourceRequest?,
			): Boolean {
				Timber.d("카카오 인증: URL 로딩 -> ${request?.url}")
				if (request?.url?.scheme == "intent" && !inAppLoaded) {
					try {
						// Intent 생성
						val intent =
							Intent.parseUri(request.url.toString(), Intent.URI_INTENT_SCHEME)

						// 실행 가능한 앱이 있으면 앱 실행
						if (intent.resolveActivity(packageManager) != null) {
							context.startActivity(intent).also {
								inAppLoaded = true
							}
							return true
						}

						// Fallback URL이 있으면 현재 웹뷰에 로딩
						val fallbackUrl = intent.getStringExtra("browser_fallback_url")
						if (fallbackUrl != null) {
							view?.loadUrl(fallbackUrl)
							return true
						}
					} catch (_: URISyntaxException) {
						return false
					}
				}

				val url = request?.url.toString()
				Timber.d("카카오 인증: URL 이동 시도 -> $url")

				// 허용된 URL만 통과
				if (allowedPrefixes.none { url.startsWith(it) }) {
					onAuthError(context.getString(R.string.auth_not_allowed_url_error))
					return true
				}

				// 리디렉트 처리
				if (url.startsWith(KAKAO_REDIRECT_URL)) {
					val uri = url.toUri()
					val code = uri.getQueryParameter("code")
					val errorP = uri.getQueryParameter("error")

					when {
						errorP != null -> onAuthError(context.getString(R.string.auth_error_failure_kakao_login))
						code != null -> {
							onAuthSuccess(code)
						}

						else -> onAuthError(context.getString(R.string.auth_error_not_earned_auth_code))
					}
					return true
				}
				return false
			}

			override fun onPageFinished(view: WebView, url: String) {
				super.onPageFinished(view, url)

				if (url.startsWith(KAKAO_ACCOUNT_URL) && isKakaoInstalled(context) && !inAppLoaded) {
					// JS SDK 로드
					view.evaluateJavascript(
						"""
						(function(){
							if (!window.Kakao) {
								var script = document.createElement('script');
								script.src = 'https://t1.kakaocdn.net/kakao_js_sdk/2.7.5/kakao.min.js';
								script.integrity = 'sha384-dok87au0gKqJdxs7msEdBPNnKSRT+/mhTVzq+qOhcL464zXwvcrpjeWvyj1kCdq6';
								script.crossOrigin = 'anonymous';
								script.onload = function(){ Kakao.init('${BuildConfig.KAKAO_JS_KEY}'); };
								document.head.appendChild(script);
							}
						})();
						""".trimIndent(),
						null,
					)

					val js = "Kakao.Auth.authorize({redirectUri:'$KAKAO_REDIRECT_URL'});"
					view.evaluateJavascript(js, null)
				}
			}

			override fun onReceivedError(
				view: WebView?,
				request: WebResourceRequest?,
				error: WebResourceError?,
			) {
				super.onReceivedError(view, request, error)
				onAuthError(context.getString(R.string.auth_error_loading_webview))
			}
		}

		loadUrl(KAKAO_BASE_URL)
	}

	AndroidView(
		factory = { webView },
		modifier = Modifier
			.fillMaxSize()
			.statusBarsPadding(),
		onRelease = { webView ->
			webView.destroySafely()
		},
	)
}
