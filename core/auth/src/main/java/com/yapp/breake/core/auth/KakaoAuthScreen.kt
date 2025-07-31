package com.yapp.breake.core.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.statusBarsPadding
import timber.log.Timber
import java.net.URISyntaxException

private const val KAKAO_AUTH_URL = "https://kauth.kakao.com" // 카카오 진입 URL
private const val KAKAO_ACCOUNT_URL = "https://accounts.kakao.com" // 카카오 로그인 URL
private const val KAKAO_MIDDLE_URL = "https://logins.daum.net" // 로그인 인증 중간 URL
private const val KAKAO_REDIRECT_URL = "https://www.yapp-dev/oauth" // 리다이렉트 URL
private const val KAKAO_BASE_URL = "$KAKAO_AUTH_URL/oauth/authorize" +
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

				// input 커서 디폴트: 가장 왼쪽에 고정
				view.evaluateJavascript(
					"""
					(function(){
					  document.querySelectorAll('input').forEach(input => {
						let beforeState = {
						  value: '',
						  selectionStart: 0,
						  selectionEnd: 0
						};

						// 입력 직전 상태 저장
						input.addEventListener('beforeinput', e => {
						  beforeState = {
							value: input.value,
							selectionStart: input.selectionStart,
							selectionEnd: input.selectionEnd
						  };
						});

						// 입력 후 커서 위치 조정
						input.addEventListener('input', e => {
						  const inputType = e.inputType;

						  if (inputType === 'insertText' || inputType === 'insertCompositionText') {
							const inserted = e.data || '';
							const newPos = beforeState.selectionStart + inserted.length;
							input.setSelectionRange(newPos, newPos);
						  }
						  else if (inputType === 'deleteContentBackward' || inputType === 'deleteContentForward') {
							const deletedLength = beforeState.value.length - input.value.length;
							let newPos;

							if (inputType === 'deleteContentBackward') {
							  newPos = beforeState.selectionStart - deletedLength;
							} else {
							  newPos = beforeState.selectionStart;
							}

							newPos = Math.max(0, Math.min(newPos, input.value.length));
							input.setSelectionRange(newPos, newPos);
						  }
						  else if (inputType === 'deleteByDrag' || inputType === 'deleteByCut') {
							const newPos = beforeState.selectionStart;
							input.setSelectionRange(newPos, newPos);
						  }
						  else if (inputType === 'insertFromPaste') {
							const pastedLength = input.value.length - beforeState.value.length + (beforeState.selectionEnd - beforeState.selectionStart);
							const newPos = beforeState.selectionStart + pastedLength;
							input.setSelectionRange(newPos, newPos);
						  }
						});

						// 롱 프레스로 단어 선택
						let longPressTimer = null;

						input.addEventListener('touchstart', e => {
						  longPressTimer = setTimeout(() => {
							const touch = e.touches[0];
							const rect = input.getBoundingClientRect();
							const x = touch.clientX - rect.left;

							// 터치 위치의 문자 인덱스 계산
							const canvas = document.createElement('canvas');
							const ctx = canvas.getContext('2d');
							const style = window.getComputedStyle(input);
							ctx.font = style.fontSize + ' ' + style.fontFamily;

							let charIndex = 0;
							let width = 0;
							for (let i = 0; i < input.value.length; i++) {
							  const charWidth = ctx.measureText(input.value[i]).width;
							  if (width + charWidth / 2 > x) break;
							  width += charWidth;
							  charIndex = i + 1;
							}

							selectWordAt(input, charIndex);
						  }, 500);
						});

						input.addEventListener('touchend', () => {
						  if (longPressTimer) {
							clearTimeout(longPressTimer);
							longPressTimer = null;
						  }
						});

						input.addEventListener('touchmove', () => {
						  if (longPressTimer) {
							clearTimeout(longPressTimer);
							longPressTimer = null;
						  }
						});

						// 단어 선택 함수
						function selectWordAt(input, pos) {
						  const text = input.value;

						  // 전체 텍스트 선택
						  if (text.length > 0) {
							input.setSelectionRange(0, text.length);
						  }
						}
					  });
					})();
					""".trimIndent(),
					null,
				)
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
