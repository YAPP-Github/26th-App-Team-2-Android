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
	data class App(
		val scrollIndex: Int = 0,
		val searchingText: String = "",
		override val groupId: Long,
		override val groupName: String,
		override val apps: PersistentList<AppModel>,
		override val selectedApps: PersistentList<SelectedAppModel>,
	) : RegistryUiState(groupId, groupName, apps, selectedApps)

	sealed interface Group {
		data class Initial(
			override val groupId: Long,
			override val groupName: String = "",
			override val apps: PersistentList<AppModel> = persistentListOf(),
			override val selectedApps: PersistentList<SelectedAppModel>,
		) : RegistryUiState(groupId, groupName, apps, selectedApps)

		data class Updated(
			override val groupId: Long,
			override val groupName: String,
			override val apps: PersistentList<AppModel>,
			override val selectedApps: PersistentList<SelectedAppModel>,
		) : RegistryUiState(groupId, groupName, apps, selectedApps)
	}
}
