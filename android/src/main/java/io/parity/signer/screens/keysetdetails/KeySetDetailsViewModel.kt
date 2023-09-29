package io.parity.signer.screens.keysetdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.parity.signer.dependencygraph.ServiceLocator
import io.parity.signer.domain.KeyModel
import io.parity.signer.domain.KeySetDetailsModel
import io.parity.signer.domain.NetworkModel
import io.parity.signer.domain.NetworkState
import io.parity.signer.domain.backend.BackupInteractor
import io.parity.signer.domain.backend.OperationResult
import io.parity.signer.domain.backend.mapInner
import io.parity.signer.domain.backend.toOperationResult
import io.parity.signer.domain.storage.RepoResult
import io.parity.signer.domain.usecases.AllNetworksUseCase
import io.parity.signer.uniffi.ErrorDisplayed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class KeySetDetailsViewModel : ViewModel() {
	private val preferencesRepository = ServiceLocator.preferencesRepository
	private val uniffiInteractor = ServiceLocator.uniffiInteractor
	private val backupInteractor = BackupInteractor()
	private val allNetworksUseCase = AllNetworksUseCase(uniffiInteractor)
	private val networkExposedStateKeeper =
		ServiceLocator.networkExposedStateKeeper
	private val seedRepository = ServiceLocator.activityScope!!.seedRepository

	val filters: StateFlow<Set<String>> =
		preferencesRepository.networksFilter.stateIn(
			viewModelScope,
			SharingStarted.WhileSubscribed(5_000),
			initialValue = emptySet(),
		)
	val networkState: StateFlow<NetworkState> =
		networkExposedStateKeeper.airGapModeState

	private val fullScreenState =
		MutableStateFlow<OperationResult<KeySetDetailsScreenState, ErrorDisplayed>>(
			OperationResult.Ok(KeySetDetailsScreenState.LoadingState)
		)

	val filteredScreenState: StateFlow<OperationResult<KeySetDetailsScreenState, ErrorDisplayed>> =
		fullScreenState.combine(filters) { fullState, filter ->
			when (fullState) {
				is OperationResult.Err -> fullState
				is OperationResult.Ok -> {
					if (filter.isEmpty()) fullState else {
						val value = fullState.result
						val result: KeySetDetailsScreenState =
							when (value) {
								is KeySetDetailsScreenState.Data -> {
									KeySetDetailsScreenState.Data(
										filteredModel = value.filteredModel.copy(keysAndNetwork = value.filteredModel.keysAndNetwork.filter {
											filter.contains(
												it.network.networkSpecsKey
											)
										}),
										wasEmptyKeyset = value.wasEmptyKeyset,
									)
								}

								KeySetDetailsScreenState.NoKeySets,
								KeySetDetailsScreenState.LoadingState -> {
									value
								}
							}
						OperationResult.Ok(result)
					}
				}
			}
		}.stateIn(
			viewModelScope,
			SharingStarted.WhileSubscribed(1_000),
			initialValue = fullScreenState.value,
		)


	private suspend fun getKeySetDetails(requestedSeedName: String?): OperationResult<KeySetDetailsScreenState, ErrorDisplayed> {
		if (requestedSeedName != null) {
			preferencesRepository.setLastSelectedSeed(requestedSeedName)
		}
		val seedName =
			requestedSeedName ?: preferencesRepository.getLastSelectedSeed()
			?: seedRepository.getLastKnownSeedNames().firstOrNull()
			?: return OperationResult.Ok(KeySetDetailsScreenState.NoKeySets)

		return uniffiInteractor.keySetBySeedName(seedName).toOperationResult()
			.mapInner {
				KeySetDetailsScreenState.Data(
					filteredModel = it,
					wasEmptyKeyset = it.keysAndNetwork.isEmpty()
				)
			}
	}

	suspend fun feedModelForSeed(seedName: String?) {
		val result = getKeySetDetails(requestedSeedName = seedName)
		fullScreenState.value = result
	}

	fun getAllNetworks(): List<NetworkModel> {
		return allNetworksUseCase.getAllNetworks()
	}

	fun setFilters(networksToFilter: Set<NetworkModel>) {
		viewModelScope.launch {
			preferencesRepository.setNetworksFilter(networksToFilter.map { it.key }
				.toSet())
		}
	}

	suspend fun removeSeed(root: KeyModel): OperationResult<Unit, Exception> {
		return seedRepository.removeKeySet(root.seedName)
	}

	suspend fun getSeedPhrase(seedName: String): String? {
		return when (val result = seedRepository.getSeedPhraseForceAuth(seedName)) {
			is RepoResult.Failure -> {
				null
			}

			is RepoResult.Success -> {
				backupInteractor.notifyRustSeedWasShown(seedName)
				result.result
			}
		}
	}
}

sealed class KeySetDetailsScreenState {

	object NoKeySets : KeySetDetailsScreenState()
	object LoadingState : KeySetDetailsScreenState()

	data class Data(
		val filteredModel: KeySetDetailsModel,
		val wasEmptyKeyset: Boolean
	) : KeySetDetailsScreenState()
}
