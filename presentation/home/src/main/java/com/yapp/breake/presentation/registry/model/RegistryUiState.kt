package com.yapp.breake.presentation.registry.model

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Stable
sealed class RegistryUiState(
	open val groupId: Long,
	open val groupName: String,
	open val apps: PersistentList<AppModel>,
	open val selectedApps: PersistentList<SelectedAppModel>,
) {
	@Stable
	data class App(
		val searchingText: String = "",
		override val groupId: Long,
		override val groupName: String,
		override val apps: PersistentList<AppModel>,
		override val selectedApps: PersistentList<SelectedAppModel>,
	) : RegistryUiState(groupId, groupName, apps, selectedApps)

	sealed interface Group {
		@Stable
		data class Initial(
			override val groupId: Long,
			override val groupName: String = "",
			override val apps: PersistentList<AppModel> = persistentListOf(),
			override val selectedApps: PersistentList<SelectedAppModel>,
		) : RegistryUiState(groupId, groupName, apps, selectedApps)

		@Stable
		data class Updated(
			override val groupId: Long,
			override val groupName: String,
			override val apps: PersistentList<AppModel>,
			override val selectedApps: PersistentList<SelectedAppModel>,
		) : RegistryUiState(groupId, groupName, apps, selectedApps)
	}
}
