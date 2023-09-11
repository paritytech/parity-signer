package io.parity.signer.domain.storage

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import io.parity.signer.dependencygraph.ServiceLocator
import io.parity.signer.domain.SharedViewModel
import io.parity.signer.uniffi.Action
import io.parity.signer.uniffi.updateSeedNames
import kotlinx.coroutines.*

/**
 * Refresh seed names list
 * should be called within authentication envelope
 * authentication.authenticate(activity) {refreshSeedNames()}
 * which is somewhat asynchronous
 * todo dmitry remove
 */
internal fun SharedViewModel.tellRustSeedNames() {
	val seedStorage = ServiceLocator.seedStorage
	val allNames = seedStorage.getSeedNames()
	updateSeedNames(allNames.toList())
}


/**
 * Fetch seed from strongbox; must be in unlocked scope
 */
@Deprecated("Use SeedStorage or better SeedRepository")
internal fun SharedViewModel.getSeed(
	seedName: String,
	showInLogs: Boolean = false
): String {
	val seedStorage = ServiceLocator.seedStorage
	return try {
		seedStorage.getSeed(seedName, showInLogs)
	} catch (e: java.lang.Exception) {
		Log.d("get seed failure", e.toString())
		Toast.makeText(context, "get seed failure: $e", Toast.LENGTH_LONG).show()
		""
	}
}

/**
 * All logic required to remove seed from memory
 *
 * 1. Remover encrypted storage item
 * 2. Synchronizes list of seeds with rust
 * 3. Calls rust remove seed logic
 */
@Deprecated("Use SeedStorage or better SeedRepository")
fun SharedViewModel.removeSeed(seedName: String) {
	val seedStorage = ServiceLocator.seedStorage
	ServiceLocator.authentication.authenticate(activity) {
		try {
			seedStorage.removeSeed(seedName)
			tellRustSeedNames()
			navigator.navigate(Action.REMOVE_SEED)
			//as remove_seed navigating us to old Log screen
			//this will open keyset list
			navigator.navigate(Action.START)
		} catch (e: java.lang.Exception) {
			Log.d("remove seed error", e.toString())
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
		}
	}
}

