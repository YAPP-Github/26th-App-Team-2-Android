package com.teambrake.brake.presentation.registry

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.amplitude.android.Amplitude
import com.amplitude.core.events.Identify
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.teambrake.brake.core.amplitude.AmplitudeEventHelper
import com.teambrake.brake.core.appscanner.InstalledAppScanner
import com.teambrake.brake.core.model.app.App
import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.core.navigation.route.SubRoute
import com.teambrake.brake.core.ui.UiString
import com.teambrake.brake.core.util.toByteArray
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.usecase.CreateNewGroupUseCase
import com.teambrake.brake.domain.usecase.DeleteGroupUseCase
import com.teambrake.brake.domain.usecase.GrantNewGroupIdUseCase
import com.teambrake.brake.presentation.home.R
import com.teambrake.brake.presentation.registry.model.AppModel.Companion.initialAppsMapper
import com.teambrake.brake.presentation.registry.model.RegistryModalState
import com.teambrake.brake.presentation.registry.model.RegistryNavState
import com.teambrake.brake.presentation.registry.model.RegistrySnackBarState
import com.teambrake.brake.presentation.registry.model.RegistryUiState
import com.teambrake.brake.presentation.registry.model.SelectedAppModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistryViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	appScanner: InstalledAppScanner,
	private val appGroupRepository: AppGroupRepository,
	private val createNewGroupUseCase: CreateNewGroupUseCase,
	private val deleteGroupUseCase: DeleteGroupUseCase,
	grantNewGroupIdUseCase: GrantNewGroupIdUseCase,
	private val firebaseAnalytics: FirebaseAnalytics,
	private val amplitude: Amplitude,
) : ViewModel() {
	private val _registryUiState: MutableStateFlow<RegistryUiState> = MutableStateFlow(
		RegistryUiState.Group.Initial(
			groupId = 0L,
			groupName = "",
			selectedApps = persistentListOf(),
		),
	)
	val registryUiState = _registryUiState.asStateFlow()

	// 그룹 생성/수정 구분을 위한 플래그
	private var isEditingExistingGroup = false

	init {
		viewModelScope.launch {
			val groupId = savedStateHandle.toRoute<SubRoute.Registry>().groupId
				?: when (val result = grantNewGroupIdUseCase()) {
					is BrakeResult.Success -> result.data
					is BrakeResult.Error -> {
						_snackBarFlow.emit(
							RegistrySnackBarState.Error(
								UiString.ResourceString(R.string.registry_snackbar_group_id_fetch_error),
							),
						)
						return@launch
					}
				}

			val targetAppGroup = appGroupRepository.getAppGroupById(groupId)

			// 기존 그룹을 수정하는 경우
			isEditingExistingGroup = targetAppGroup != null

			val selectedApps =
				persistentListOf<SelectedAppModel>().builder().apply {
					targetAppGroup?.let { group ->
						group.apps.forEachIndexed { index, appModel ->
							add(
								SelectedAppModel(
									id = appModel.id,
									index = index,
									name = appModel.name,
									packageName = appModel.packageName,
									icon = appScanner.getIconDrawable(appModel.packageName),
								),
							)
						}
					}
				}.build()

			_registryUiState.value = RegistryUiState.Group.Initial(
				groupId = groupId,
				groupName = targetAppGroup?.name.orEmpty(),
				selectedApps = selectedApps,
			)
		}
	}

	private val _snackBarFlow = MutableSharedFlow<RegistrySnackBarState>()
	val snackBarFlow = _snackBarFlow.asSharedFlow()

	private val _navigationFlow = MutableSharedFlow<RegistryNavState>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	private val _modalFlow = MutableStateFlow<RegistryModalState>(RegistryModalState.Idle)
	val modalFlow = _modalFlow.asStateFlow()

	private val _lazyColumnIndexFlow = MutableSharedFlow<Int>()
	val lazyColumnIndexFlow = _lazyColumnIndexFlow.asSharedFlow()

	private val cachedAppsDeferred = viewModelScope.async(Dispatchers.IO) {
		appScanner.getInstalledAppsMetaData().map(initialAppsMapper).toPersistentList()
	}

	// ------------- Group Registry -------------
	fun updateGroupName(groupName: String) {
		val currentUiState = _registryUiState.value
		_registryUiState.value = currentUiState.let {
			RegistryUiState.Group.Updated(
				groupId = it.groupId,
				groupName = groupName,
				apps = it.apps,
				selectedApps = it.selectedApps,
			)
		}
	}

	fun startSelectingApps() {
		viewModelScope.launch {
			val currentUiState = _registryUiState.value
			val cachedApps = cachedAppsDeferred.await()
			_registryUiState.value = RegistryUiState.App(
				groupId = currentUiState.groupId,
				groupName = currentUiState.groupName,
				apps = cachedApps.builder().apply {
					currentUiState.selectedApps.forEach { selectedApp ->
						cachedApps.indexOfFirst { it.packageName == selectedApp.packageName }
							.takeIf { it >= 0 }?.let { index ->
								set(
									index = index,
									element = cachedApps[index].copy(
										isSelected = true,
									),
								)
							}
					}
				}.build(),
				selectedApps = persistentListOf<SelectedAppModel>().builder().apply {
					currentUiState.selectedApps.forEach { selectedApp ->
						cachedApps.indexOfFirst { it.packageName == selectedApp.packageName }
							.takeIf { it >= 0 }?.let { index ->
								add(
									SelectedAppModel(
										index = index,
										name = selectedApp.name,
										packageName = selectedApp.packageName,
										icon = selectedApp.icon,
										id = selectedApp.id,
									),
								)
							}
					}
				}.build(),
			)
		}
		firebaseAnalytics.logEvent("start_selecting_app") {
			param("group_id", registryUiState.value.groupId)
		}
	}

	fun removeSelectedApp(selectedIndex: Int) {
		val currentUiState = _registryUiState.value
		_registryUiState.value = currentUiState.let {
			RegistryUiState.Group.Updated(
				groupId = it.groupId,
				groupName = it.groupName,
				apps = it.apps,
				selectedApps = it.selectedApps.removeAt(selectedIndex),
			)
		}
	}

	fun cancelCreatingNewGroup() {
		viewModelScope.launch {
			_navigationFlow.emit(RegistryNavState.NavigateToHome)
		}
		firebaseAnalytics.logEvent("cancel_creating_modifying_group") {
			param("group_id", registryUiState.value.groupId)
		}
	}

	fun tryRemoveGroup() {
		_modalFlow.value = RegistryModalState.ShowGroupDeletionWarning
	}

	fun createNewGroup() {
		viewModelScope.launch {
			val currentUiState = _registryUiState.value
			val result = createNewGroupUseCase(
				group = currentUiState.let {
					AppGroup(
						id = it.groupId,
						name = it.groupName,
						appGroupState = AppGroupState.NeedSetting,
						apps = it.selectedApps.map { selectedApp ->
							App(
								packageName = selectedApp.packageName,
								id = selectedApp.id,
								name = selectedApp.name,
								icon = selectedApp.icon.toByteArray(),
								category = "기타",
							)
						},
						snoozes = emptyList(),
						goalMinutes = null,
						sessionStartTime = null,
						startTime = null,
						endTime = null,
					)
				},
			)

			when (result) {
				is BrakeResult.Error -> {
					_snackBarFlow.emit(
						RegistrySnackBarState.Error(
							UiString.ResourceString(R.string.registry_snackbar_group_creation_error),
						),
					)
					return@launch
				}

				is BrakeResult.Success -> {
					_snackBarFlow.emit(
						RegistrySnackBarState.Success(
							UiString.ResourceString(R.string.registry_snackbar_group_creation_successful),
						),
					)
					launch(Dispatchers.IO) {
						// Amplitude 이벤트 전송 (6번: create_app_group, 7번: edit_app_group)
						val event = if (isEditingExistingGroup) {
							AmplitudeEventHelper.editAppGroupEvent(
								groupId = currentUiState.groupId.toString(),
								groupName = currentUiState.groupName,
								groupAppCount = currentUiState.selectedApps.size,
							)
						} else {
							AmplitudeEventHelper.createAppGroupEvent(
								groupId = currentUiState.groupId.toString(),
								groupName = currentUiState.groupName,
								groupAppCount = currentUiState.selectedApps.size,
							)
						}
						amplitude.track(event.getEventName(), event.toEventProperties())

						// 17. total_group_count User Property 업데이트
						val totalGroupCount = appGroupRepository.observeAppGroup().first().size
						val userProperty = AmplitudeEventHelper.setTotalGroupCount(count = totalGroupCount)
						amplitude.identify(
							Identify().apply {
								userProperty.toUserProperties().forEach { (key, value) ->
									set(key, value)
								}
							},
						)

						firebaseAnalytics.logEvent("create_modify_group") {
							param("group_id", currentUiState.groupId)
							param("group_name", currentUiState.groupName)
							for (selectedApp in currentUiState.selectedApps) {
								param("app_name", selectedApp.name)
							}
						}
					}
					_navigationFlow.emit(RegistryNavState.NavigateToHome)
				}
			}
		}
	}

	// ------------- App Registry -------------
	fun updateSearchingText(searchingText: String) {
		val currentUiState = _registryUiState.value as RegistryUiState.App
		_registryUiState.value = currentUiState.copy(
			searchingText = searchingText,
		)
		searchApp(shouldShowNotFound = false)
	}

	fun searchApp(shouldShowNotFound: Boolean) {
		val currentUiState = _registryUiState.value as RegistryUiState.App
		val query = currentUiState.searchingText

		val matchedIndex = currentUiState.apps
			.withIndex()
			.minByOrNull { (_, app) ->
				val pos = app.name.indexOf(query, ignoreCase = true)
				if (pos >= 0) pos else Int.MAX_VALUE
			}?.takeIf { it.value.name.contains(query, ignoreCase = true) }?.index ?: -1

		// 결과가 없으면 상태 갱신하지 않음
		if (matchedIndex == -1) {
			// ime 에서 완료 버튼을 통해 검색을 시도했을 때만 에러 스낵바 표시
			if (shouldShowNotFound) {
				viewModelScope.launch {
					_snackBarFlow.emit(
						RegistrySnackBarState.Error(
							UiString.ResourceString(R.string.registry_app_error_message, query),
						),
					)
				}
			}
			return
		}
		viewModelScope.launch {
			_lazyColumnIndexFlow.emit(matchedIndex)
		}
	}

	fun selectApp(selectedIndex: Int) {
		val currentUiState = _registryUiState.value as RegistryUiState.App
		_registryUiState.value = currentUiState.let {
			it.copy(
				apps = it.apps.set(
					index = selectedIndex,
					element = it.apps[selectedIndex].copy(
						isSelected = !it.apps[selectedIndex].isSelected,
					),
				),
			)
		}
	}

	fun completeSelectingApps() {
		val currentUiState = _registryUiState.value
		_registryUiState.value = currentUiState.let {
			RegistryUiState.Group.Updated(
				groupId = it.groupId,
				groupName = it.groupName,
				apps = it.apps,
				selectedApps = it.apps.mapIndexedNotNull { index, appModel ->
					if (appModel.isSelected) {
						val existingApp = it.selectedApps.find { selectedApp ->
							selectedApp.packageName == appModel.packageName
						}

						SelectedAppModel(
							index = index,
							name = appModel.name,
							packageName = appModel.packageName,
							icon = appModel.icon,
							id = existingApp?.id,
						)
					} else {
						null
					}
				}.toPersistentList(),
			)
		}
		firebaseAnalytics.logEvent("complete_selecting_apps") {
			param("group_id", currentUiState.groupId)
		}
	}

	// 선택한 앱을 그룹에 포함시키지 않고 그룹 화면으로 돌아가기
	fun cancelSelectingApps() {
		viewModelScope.launch {
			val currentUiState = _registryUiState.value
			val cachedApps = cachedAppsDeferred.await()
			_registryUiState.value = RegistryUiState.Group.Updated(
				groupId = currentUiState.groupId,
				groupName = currentUiState.groupName,
				apps = cachedApps.builder().apply {
					currentUiState.selectedApps.forEach { selectedApp ->
						set(
							index = selectedApp.index,
							element = cachedApps[selectedApp.index].copy(isSelected = true),
						)
					}
				}.build(),
				selectedApps = currentUiState.selectedApps,
			)
			firebaseAnalytics.logEvent("cancel_selecting_apps") {
				param("group_id", currentUiState.groupId)
			}
		}
	}

	// ------------- Modal State -------------
	fun dismissModal() {
		_modalFlow.value = RegistryModalState.Idle
	}

	fun removeGroup() {
		viewModelScope.launch {
			val result = deleteGroupUseCase(groupId = registryUiState.value.groupId)
			when (result) {
				is BrakeResult.Error -> {
					_snackBarFlow.emit(
						RegistrySnackBarState.Error(
							UiString.ResourceString(R.string.registry_snackbar_group_deletion_error),
						),
					)
					return@launch
				}

				is BrakeResult.Success -> {

					_modalFlow.value = RegistryModalState.Idle
					_snackBarFlow.emit(
						RegistrySnackBarState.Success(
							UiString.ResourceString(R.string.registry_snackbar_group_deletion_successful),
						),
					)
					launch(Dispatchers.IO) {
						// 8번: delete_app_group 이벤트 전송
						val deleteEvent = AmplitudeEventHelper.deleteAppGroupEvent(
							groupId = currentUiState.groupId.toString(),
							groupName = currentUiState.groupName,
							groupAppCount = currentUiState.selectedApps.size,
						)
						amplitude.track(deleteEvent.getEventName(), deleteEvent.toEventProperties())

						// 17. total_group_count User Property 업데이트
						val totalGroupCount = appGroupRepository.observeAppGroup().first().size
						val userProperty = AmplitudeEventHelper.setTotalGroupCount(count = totalGroupCount)
						amplitude.identify(
							Identify().apply {
								userProperty.toUserProperties().forEach { (key, value) ->
									set(key, value)
								}
							},
						)

						firebaseAnalytics.logEvent("delete_group") {
							param("group_id", currentUiState.groupId)
							param("group_name", currentUiState.groupName)
							for (selectedApp in currentUiState.selectedApps) {
								param("app_name", selectedApp.name)
							}
						}
					}
					_navigationFlow.emit(RegistryNavState.NavigateToHome)
				}
			}
		}
	}
}
