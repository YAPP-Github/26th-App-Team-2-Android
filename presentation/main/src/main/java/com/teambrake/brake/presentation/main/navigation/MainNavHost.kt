package com.teambrake.brake.presentation.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.teambrake.brake.presentation.feeback.inquiry.navEntry.inquiryNavEntry
import com.teambrake.brake.presentation.feeback.opinion.navEntry.opinionNavEntry
import com.teambrake.brake.presentation.home.navEntry.homeNavEntry
import com.teambrake.brake.presentation.legal.navEntry.legalNavEntries
import com.teambrake.brake.presentation.login.navEntry.loginNavEntry
import com.teambrake.brake.presentation.nickname.navEntry.nicknameNavEntry
import com.teambrake.brake.presentation.onboarding.navEntry.onboardingNavEntries
import com.teambrake.brake.presentation.permission.navEntry.permissionNavEntry
import com.teambrake.brake.presentation.registry.navEntry.registryNavEntry
import com.teambrake.brake.presentation.report.navEntry.reportNavEntry
import com.teambrake.brake.presentation.setting.navEntry.settingNavEntry
import com.teambrake.brake.presentation.signup.navEntry.signupNavEntry

@Composable
internal fun MainNavHost(
	backStack: List<NavKey>,
	padding: PaddingValues,
	onChangeDarkTheme: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
) {
	val entryProvider = entryProvider {
		loginNavEntry()
		signupNavEntry()
		onboardingNavEntries()
		legalNavEntries()
		permissionNavEntry()
		reportNavEntry(padding = padding)
		homeNavEntry(padding = padding)
		registryNavEntry()
		settingNavEntry(
			padding = padding,
			onChangeDarkTheme = onChangeDarkTheme,
		)
		nicknameNavEntry()
		inquiryNavEntry()
		opinionNavEntry()
	}

	NavDisplay(
		backStack = backStack,
		entryProvider = entryProvider,
		entryDecorators = listOf(
			rememberSaveableStateHolderNavEntryDecorator(),
			rememberViewModelStoreNavEntryDecorator(),
		),
		modifier = modifier,
	)
}
