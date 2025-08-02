package com.yapp.breake.presentation.registry

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yapp.breake.core.appscanner.InstalledAppScanner
import com.yapp.breake.core.model.app.App
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.core.navigation.route.SubRoute
import com.yapp.breake.core.ui.UiString
import com.yapp.breake.core.util.toByteArray
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.usecase.CreateNewGroupUseCase
import com.yapp.breake.domain.usecase.DeleteGroupUseCase
import com.yapp.breake.presentation.home.R
import com.yapp.breake.presentation.registry.model.AppModel.Companion.initialAppsMapper
import com.yapp.breake.presentation.registry.model.RegistryModalState
import com.yapp.breake.presentation.registry.model.RegistryNavState
import com.yapp.breake.presentation.registry.model.RegistrySnackBarState
import com.yapp.breake.presentation.registry.model.RegistryUiState
import com.yapp.breake.presentation.registry.model.SelectedAppModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegistryViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	appScanner: InstalledAppScanner,
	private val appGroupRepository: AppGroupRepository,
	private val createNewGroupUseCase: CreateNewGroupUseCase,
	private val deleteGroupUseCase: DeleteGroupUseCase,
) : ViewModel() {
	// TODO: 그룹 아이디가 없을 시(새 그룹) 추후 데이터베이스 연동 후 가장 작고 사용 가능한 그룹 넘버 부여
	private val appGroupId = savedStateHandle.toRoute<SubRoute.Registry>().groupId ?: 10L
	private val targetAppGroup: AppGroup? = runBlocking {
		appGroupRepository.getAppGroupById(appGroupId)
	}
	private val selectedApps: PersistentList<SelectedAppModel> =
		persistentListOf<SelectedAppModel>().builder().apply {
			targetAppGroup?.let {
				it.apps.forEachIndexed { index, appModel ->
					add(
						SelectedAppModel(
							index = index,
							name = appModel.name,
							packageName = appModel.packageName,
							icon = appScanner.getIconDrawable(appModel.packageName),
						),
					)
				}
			}
		}.build()
	private val cachedAppsDeferred = viewModelScope.async(Dispatchers.IO) {
		appScanner.getInstalledAppsMetaData().map(initialAppsMapper).toPersistentList()
	}

	private val _registryUiState: MutableStateFlow<RegistryUiState> =
		MutableStateFlow<RegistryUiState>(
			RegistryUiState.Group.Initial(
				groupId = appGroupId,
				groupName = targetAppGroup?.name ?: "",
				selectedApps = selectedApps,
			),
		)
	val registryUiState = _registryUiState.asStateFlow()

	private val _snackBarFlow = MutableSharedFlow<RegistrySnackBarState>()
	val snackBarFlow = _snackBarFlow.asSharedFlow()

	private val _navigationFlow = MutableSharedFlow<RegistryNavState>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	private val _modalFlow = MutableStateFlow<RegistryModalState>(RegistryModalState.Idle)
	val registryModalFlow = _modalFlow.asStateFlow()

	private val _lazyColumnIndexFlow = MutableSharedFlow<Int>()
	val lazyColumnIndexFlow = _lazyColumnIndexFlow.asSharedFlow()

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
									element = cachedApps[index].copy(isSelected = true),
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
									),
								)
							}
					}
				}.build(),
			)
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
	}

	fun tryRemoveGroup() {
		_modalFlow.value = RegistryModalState.ShowGroupDeletionWarning
	}

	fun createNewGroup() {
		viewModelScope.launch {
			val currentUiState = _registryUiState.value
			createNewGroupUseCase(
				onError = { throwable ->
					_snackBarFlow.emit(
						RegistrySnackBarState.Error(
							UiString.ResourceString(R.string.registry_snackbar_group_creation_error),
						),
					)
				},
				group = currentUiState.let {
					AppGroup(
						id = 1,
						name = it.groupName,
						appGroupState = AppGroupState.NeedSetting,
						apps = it.selectedApps.map { selectedApp ->
							App(
								packageName = selectedApp.packageName,
								name = selectedApp.name,
								icon = selectedApp.icon.toByteArray(),
								// TODO: 카테고리 추후 추가 필요
								category = "기타",
							)
						},
						snoozes = emptyList(),
						endTime = null,
					)
				},
			)
			_snackBarFlow.emit(
				RegistrySnackBarState.Success(
					UiString.ResourceString(R.string.registry_snackbar_group_creation_successful),
				),
			)
			_navigationFlow.emit(RegistryNavState.NavigateToHome)
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
						SelectedAppModel(
							index = index,
							name = appModel.name,
							packageName = appModel.packageName,
							icon = appModel.icon,
						)
					} else {
						null
					}
				}.toPersistentList(),
			)
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
		}
	}

	// ------------- Modal State -------------
	fun dismissModal() {
		_modalFlow.value = RegistryModalState.Idle
	}

	fun removeGroup() {
		viewModelScope.launch {
			deleteGroupUseCase(
				onError = {
					_snackBarFlow.emit(
						RegistrySnackBarState.Error(
							UiString.ResourceString(R.string.registry_snackbar_group_deletion_error),
						),
					)
				},
				groupId = registryUiState.value.groupId,
			)
			_modalFlow.value = RegistryModalState.Idle
			_snackBarFlow.emit(
				RegistrySnackBarState.Success(
					UiString.ResourceString(R.string.registry_snackbar_group_deletion_successful),
				),
			)
			_navigationFlow.emit(RegistryNavState.NavigateToHome)
		}
	}
}
