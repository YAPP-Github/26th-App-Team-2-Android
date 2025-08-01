package com.yapp.breake.presentation.login.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.presentation.login.R

@Composable
fun LoginNoticeText(
	modifier: Modifier = Modifier,
	onPrivacyClick: () -> Unit,
	onTermsClick: () -> Unit,
) {
	val notice = stringResource(R.string.login_notice_full_message)
	val privacy = stringResource(R.string.privacy_policy)
	val terms = stringResource(R.string.terms_of_service)

	val pStart = notice.indexOf(privacy).takeIf { it >= 0 } ?: return
	val pEnd = pStart + privacy.length
	val tStart = notice.indexOf(terms).takeIf { it >= 0 } ?: return
	val tEnd = tStart + terms.length

	// 특정 단어에 withLink 적용하여 해당 부분에 밑줄 효과와 이벤트 핸들러 추가
	val annotated = buildAnnotatedString {
		append(notice.substring(0, pStart))

		withLink(LinkAnnotation.Clickable("PRIVACY") { onPrivacyClick() }) {
			withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
				append(privacy)
			}
		}

		append(notice.substring(pEnd, tStart))

		withLink(LinkAnnotation.Clickable("TERMS") { onTermsClick() }) {
			withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
				append(terms)
			}
		}

		append(notice.substring(tEnd))
	}

	Text(
		text = annotated,
		style = BrakeTheme.typography.body12M,
		modifier = modifier,
		textAlign = TextAlign.Center,
	)
}
