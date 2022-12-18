package io.parity.signer.models

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


/**
 * Storing seed phrase in secure storage
 * All functions requiring user to be authenticated, properties do not.
 *
 * This is not safe class to use directly. Use wrappers that checks for authenticated.
 */
class SeedStorage {

	private val _lastKnownSeedNames = MutableStateFlow(arrayOf<String>())
	val lastKnownSeedNames: StateFlow<Array<String>> = _lastKnownSeedNames
	val isStrongBoxProtected: Boolean
		get() = masterKey.isStrongBoxBacked


	private lateinit var masterKey: MasterKey
	private var hasStrongbox: Boolean = false
	private val keyStore = "AndroidKeyStore"
	private lateinit var sharedPreferences: SharedPreferences

	/**
	 * @throws UserNotAuthenticatedException
	 */
	fun init(appContext: Context) {
		hasStrongbox = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			appContext
				.packageManager
				.hasSystemFeature(PackageManager.FEATURE_STRONGBOX_KEYSTORE)
		} else {
			false
		}

		Log.d("strongbox available:", hasStrongbox.toString())

		// Init crypto for seeds:
		// https://developer.android.com/training/articles/keystore
		masterKey = if (hasStrongbox) {
			MasterKey.Builder(appContext)
				.setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
				.setRequestStrongBoxBacked(true)
				.setUserAuthenticationRequired(true)
				.build()
		} else {
			MasterKey.Builder(appContext)
				.setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
				.setUserAuthenticationRequired(true)
				.build()
		}

		Log.e("ENCRY", "$appContext $keyStore $masterKey")
		//we need to be authenticated for this
		sharedPreferences =
			if (FeatureFlags.isEnabled(FeatureOption.SKIP_UNLOCK_FOR_DEVELOPMENT)) {
				appContext.getSharedPreferences(
					"FeatureOption.SKIP_UNLOCK_FOR_DEVELOPMENT",
					Context.MODE_PRIVATE
				)
			} else {
				EncryptedSharedPreferences(
					appContext,
					keyStore,
					masterKey,
					EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
					EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
				)
			}
	}


	/**
	 * @throws UserNotAuthenticatedException
	 */
	fun getSeedNames(): Array<String> =
		sharedPreferences.all.keys.sorted().toTypedArray().also {
			_lastKnownSeedNames.value = it
		}

	/**
	 * Add seed, encrypt it, and create default accounts
	 *
	 * @throws UserNotAuthenticatedException
	 */
	fun addSeed(
		seedName: String,
		seedPhrase: String,
	) {

		// First check for seed collision
		if (sharedPreferences.all.values.contains(seedPhrase)) {
			error("This seed phrase already exists")
		}

		// Encrypt and save seed
		with(sharedPreferences.edit()) {
			putString(seedName, seedPhrase)
			apply()
		}
	}

	/**
	 * @throws UserNotAuthenticatedException
	 */
	fun getSeed(
		seedName: String,
	): String {
		val seedPhrase = sharedPreferences.getString(seedName, "") ?: ""
		return if (seedPhrase.isBlank()) {
			""
		} else {
			seedPhrase
		}
	}

	/**
	 * @throws [UserNotAuthenticatedException]
	 */
	fun removeSeed(seedName: String) {
		sharedPreferences.edit().remove(seedName).apply()
	}

	/**
	 * @throws UserNotAuthenticatedException
	 */
	fun wipe() {
		sharedPreferences.edit().clear().commit() // No, not apply(), do it now!
	}

}
