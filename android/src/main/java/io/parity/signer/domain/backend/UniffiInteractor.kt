package io.parity.signer.domain.backend

import android.content.Context
import io.parity.signer.R
import io.parity.signer.domain.KeySetsSelectModel
import io.parity.signer.domain.NavigationError
import io.parity.signer.domain.NetworkModel
import io.parity.signer.domain.submitErrorState
import io.parity.signer.domain.toKeySetsSelectModel
import io.parity.signer.domain.toNetworkModel
import io.parity.signer.screens.keydetails.exportprivatekey.PrivateKeyExportModel
import io.parity.signer.screens.keydetails.exportprivatekey.toPrivateKeyExportModel
import io.parity.signer.screens.scan.errors.TransactionError
import io.parity.signer.screens.scan.errors.findErrorDisplayed
import io.parity.signer.screens.scan.errors.toTransactionError
import io.parity.signer.uniffi.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest

/**
 * Wrapper for uniffi calls into rust. Made for centralized handling errors
 * and to have those functions scoped in specific namespace
 */
class UniffiInteractor(val appContext: Context) {

	/**
	 * Rust db is initializing only when main screen is shown
	 * and we need to provide seeds for it, so we cannot do it rightaway.
	 */
	val wasRustInitialized = MutableStateFlow(false)

	private val suspendedTasksContext: CoroutineScope =
		CoroutineScope(Dispatchers.IO)

	suspend fun navigate(
		action: Action,
		details: String = "",
		seedPhrase: String = "",
	): OperationResult<ActionResult, NavigationError> =
		withContext(Dispatchers.IO) {
			try {
				OperationResult.Ok(backendAction(action, details, seedPhrase))
			} catch (e: ErrorDisplayed) {
				OperationResult.Err(
					NavigationError(
						appContext.getString(
							R.string.navigation_error_general_message,
							e.findErrorDisplayed()?.message ?: e.message
						)
					)
				)
			}
		}

	suspend fun performTransaction(payload: String): OperationResult<ActionResult, TransactionError> =
		withContext(Dispatchers.IO) {
			try {
				OperationResult.Ok(
					backendAction(
						Action.TRANSACTION_FETCHED,
						payload,
						""
					)
				)
			} catch (e: ErrorDisplayed) {
				OperationResult.Err(e.toTransactionError())
			} catch (e: Throwable) {
				OperationResult.Err(
					TransactionError.Generic(
						appContext.getString(
							R.string.navigation_error_general_message,
							e.findErrorDisplayed()?.message ?: e.message
						)
					)
				)
			}
		}

	fun historyDeviceWasOnline() {
		if (wasRustInitialized.value) {
			io.parity.signer.uniffi.historyDeviceWasOnline()
		} else {
			suspendedTasksContext.launch {
				wasRustInitialized.collectLatest {
					if (it) {
						io.parity.signer.uniffi.historyDeviceWasOnline()
						return@collectLatest
					}
				}
			}
		}
	}

	suspend fun exportSeedWithKeys(
		seed: String, derivedKeyAddr: List<String>
	): UniffiResult<MKeysInfoExport> = withContext(Dispatchers.IO) {
		try {
			val keys = keysBySeedName(seed)
			val pathAndNetworks = derivedKeyAddr.map { keyAddr ->
				val key = keys.set.find { it.key.addressKey == keyAddr }!!
				PathAndNetwork(
					key.key.address.path, key.network.networkSpecsKey
				)
			}
			val keyInfo = io.parity.signer.uniffi.exportKeyInfo(
				seedName = seed,
				exportedSet = ExportedSet.Selected(pathAndNetworks),
			)
			UniffiResult.Success(keyInfo)
		} catch (e: ErrorDisplayed) {
			UniffiResult.Error(e)
		}
	}

	suspend fun encodeToQrImages(binaryData: List<List<UByte>>): UniffiResult<List<List<UByte>>> =
		withContext(Dispatchers.IO) {
			try {
				val images = binaryData.map {
					async(Dispatchers.IO) {
						io.parity.signer.uniffi.encodeToQr(it, false)
					}
				}.map { it.await() }
				UniffiResult.Success(images)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun getAllNetworks(): UniffiResult<List<NetworkModel>> =
		withContext(Dispatchers.IO) {
			try {
				val networks =
					io.parity.signer.uniffi.getManagedNetworks().networks.map { it.toNetworkModel() }
				UniffiResult.Success(networks)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun validateDerivationPath(
		path: String,
		seed: String,
		selectedNetworkSpecs: String
	): UniffiResult<DerivationCheck> =
		withContext(Dispatchers.IO) {
			try {
				val validationResult = substratePathCheck(
					seedName = seed,
					path = path,
					network = selectedNetworkSpecs
				)
				UniffiResult.Success(validationResult)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun getLogs(): UniffiResult<MLog> =
		withContext(Dispatchers.IO) {
			try {
				val validationResult = io.parity.signer.uniffi.getLogs()
				UniffiResult.Success(validationResult)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun getLogDetails(logIndex: UInt): UniffiResult<MLogDetails> =
		withContext(Dispatchers.IO) {
			try {
				val validationResult = io.parity.signer.uniffi.getLogDetails(logIndex)
				UniffiResult.Success(validationResult)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun clearLogHistory(): UniffiResult<Unit> =
		withContext(Dispatchers.IO) {
			try {
				val validationResult = io.parity.signer.uniffi.clearLogHistory()
				UniffiResult.Success(validationResult)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun addCommentToLogs(userComment: String): UniffiResult<Unit> =
		withContext(Dispatchers.IO) {
			try {
				val validationResult =
					io.parity.signer.uniffi.handleLogComment(userComment)
				UniffiResult.Success(validationResult)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun previewDynamicDerivations(
		seeds: Map<String, String>,
		payload: String
	): UniffiResult<DdPreview> =
		withContext(Dispatchers.IO) {
			try {
				val validationResult =
					io.parity.signer.uniffi.previewDynamicDerivations(seeds, payload)
				UniffiResult.Success(validationResult)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun signDynamicDerivationsTransactions(
		seeds: Map<String, String>,
		payload: List<String>
	): UniffiResult<MSignedTransaction> =
		withContext(Dispatchers.IO) {
			try {
				val transactionResult =
					io.parity.signer.uniffi.signDdTransaction(payload, seeds)
				UniffiResult.Success(transactionResult)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun createNewSeedPhrase(
	): UniffiResult<String> =
		withContext(Dispatchers.IO) {
			try {
				val transactionResult = io.parity.signer.uniffi.printNewSeed("")
				UniffiResult.Success(transactionResult.seedPhrase)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun generateSecretKeyQr(
		publicKey: String,
		expectedSeedName: String,
		networkSpecsKey: String,
		seedPhrase: String,
		keyPassword: String?,
	): UniffiResult<PrivateKeyExportModel> =
		withContext(Dispatchers.IO) {
			try {
				val transactionResult =
					io.parity.signer.uniffi.generateSecretKeyQr(
						publicKey = publicKey,
						expectedSeedName = expectedSeedName,
						networkSpecsKey = networkSpecsKey,
						seedPhrase = seedPhrase,
						keyPassword = keyPassword,
					).toPrivateKeyExportModel()
				UniffiResult.Success(transactionResult)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun getKeySets(
		seedNames: List<String>
	): UniffiResult<KeySetsSelectModel> =
		withContext(Dispatchers.IO) {
			try {
				val transactionResult =
					io.parity.signer.uniffi.getSeeds(seedNames).toKeySetsSelectModel()
				UniffiResult.Success(transactionResult)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun getKeyPublicKey(
		addressKey: String,
		networkSpecsKey: String
	): UniffiResult<MKeyDetails> =
		withContext(Dispatchers.IO) {
			try {
				val transactionResult =
					io.parity.signer.uniffi.getKeySetPublicKey(
						addressKey,
						networkSpecsKey
					)
				UniffiResult.Success(transactionResult)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun removeKeySet(
		addressKey: String
	): UniffiResult<Unit> =
		withContext(Dispatchers.IO) {
			try {
				val transactionResult = io.parity.signer.uniffi.removeKeySet(addressKey)
				UniffiResult.Success(transactionResult)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun getManagedNetworkDetails(
		networkKey: String
	): UniffiResult<MNetworkDetails> =
		withContext(Dispatchers.IO) {
			try {
				val transactionResult =
					io.parity.signer.uniffi.getManagedNetworkDetails(networkKey)
				UniffiResult.Success(transactionResult)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun removeMetadataManagedNetwork(
		networkKey: String,
		metadataSpecsVersion: String
	): UniffiResult<Unit> =
		withContext(Dispatchers.IO) {
			try {
				val transactionResult =
					io.parity.signer.uniffi.removeMetadataOnManagedNetwork(
						networkKey,
						metadataSpecsVersion
					)
				UniffiResult.Success(transactionResult)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun seedPhraseGuessWords(
		userInput: String
	): UniffiResult<List<String>> =
		withContext(Dispatchers.IO) {
			try {
				val transactionResult =
					io.parity.signer.uniffi.seedPhraseGuessWords(userInput)
				UniffiResult.Success(transactionResult)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}

	suspend fun removedDerivedKey(
		addressKey: String,
		networkSpecsKey: String,
	): UniffiResult<Unit> =
		withContext(Dispatchers.IO) {
			try {
				val transactionResult =
					io.parity.signer.uniffi.removeDerivedKey(addressKey, networkSpecsKey)
				UniffiResult.Success(transactionResult)
			} catch (e: ErrorDisplayed) {
				UniffiResult.Error(e)
			}
		}


}

sealed class UniffiResult<T> {
	data class Success<T>(val result: T) : UniffiResult<T>()
	data class Error<Any>(val error: ErrorDisplayed) : UniffiResult<Any>()
}

sealed class OperationResult<out T, out E> {
	data class Ok<out T>(val result: T) : OperationResult<T, Nothing>()
	data class Err<out E>(val error: E) : OperationResult<Nothing, E>()
}

sealed class CompletableResult<out T, out E> {
	data class Ok<out T>(val result: T) : CompletableResult<T, Nothing>()
	data class Err<out E>(val error: E) : CompletableResult<Nothing, E>()
	object InProgress : CompletableResult<Nothing, Nothing>()
}

fun <T> UniffiResult<T>.mapError(): T? {
	return when (this) {
		is UniffiResult.Error -> {
			submitErrorState("uniffi interaction exception $error")
			null
		}

		is UniffiResult.Success -> {
			result
		}
	}
}

@Deprecated("Handle error state")
fun <T, V> OperationResult<T, V>.mapError(): T? {
	return when (this) {
		is OperationResult.Err -> {
			submitErrorState("uniffi interaction exception $error")
			null
		}

		is OperationResult.Ok -> {
			result
		}
	}
}
