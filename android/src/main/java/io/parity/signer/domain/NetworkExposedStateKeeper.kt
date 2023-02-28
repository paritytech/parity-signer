package io.parity.signer.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.provider.Settings
import io.parity.signer.backend.UniffiInteractor
import io.parity.signer.uniffi.historyAcknowledgeWarnings
import io.parity.signer.uniffi.historyGetWarnings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class NetworkExposedStateKeeper(
	private val appContext: Context,
	private val rustInteractor: UniffiInteractor
) {

	private val _airplaneModeEnabled: MutableStateFlow<Boolean?> =
		MutableStateFlow(null)
	val airPlaneMode: StateFlow<Boolean?> = _airplaneModeEnabled

	private val _wifiDisabledState: MutableStateFlow<Boolean?> =
		MutableStateFlow(null)
	val wifiDisabledState: StateFlow<Boolean?> = _wifiDisabledState

	private val _airGapModeState: MutableStateFlow<NetworkState> =
		MutableStateFlow(NetworkState.None)
	val airGapModeState: StateFlow<NetworkState> = _airGapModeState

	init {
		registerAirplaneBroadcastReceiver()
		registerWifiBroadcastReceiver()
		reactOnAirplaneMode()
		reactOnWifiAwareState()
	}

	/**
	 * Expects that rust nav machine is initialized that should always be the case
	 * as it's required to show UI calling this function
	 */
	fun acknowledgeWarning() {
		if (airGapModeState.value == NetworkState.Past) {
			historyAcknowledgeWarnings()
			_airGapModeState.value = NetworkState.None
		}
	}

	private fun registerAirplaneBroadcastReceiver() {
		val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
		val receiver: BroadcastReceiver = object : BroadcastReceiver() {
			override fun onReceive(context: Context, intent: Intent) {
				reactOnAirplaneMode()
			}
		}
		appContext.registerReceiver(receiver, intentFilter)
	}

	private fun updateGeneralAirgap(isBreached: Boolean) {
		if (isBreached) {
			if (airGapModeState.value != NetworkState.Active) {
				_airGapModeState.value = NetworkState.Active
				if (appContext.isDbCreatedAndOnboardingPassed()) {
					rustInteractor.historyDeviceWasOnline()
				}
			}
		} else {
			if (airGapModeState.value == NetworkState.Active) {
				_airGapModeState.value =
					if (appContext.isDbCreatedAndOnboardingPassed())
						NetworkState.Past else NetworkState.None
			}
		}
	}

	private fun reactOnAirplaneMode() {
		val airplaneModeOff = Settings.Global.getInt(
			appContext.contentResolver,
			Settings.Global.AIRPLANE_MODE_ON,
			0
		) != 0
		_airplaneModeEnabled.value = !airplaneModeOff
		updateGeneralAirgap(airplaneModeOff)
	}


	private fun registerWifiBroadcastReceiver() {
		val intentFilter = IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION)
		val receiver: BroadcastReceiver = object : BroadcastReceiver() {
			override fun onReceive(context: Context, intent: Intent) {
				reactOnWifiAwareState()
			}
		}
		appContext.registerReceiver(receiver, intentFilter)
	}

	private fun reactOnWifiAwareState() {
		val wifi =
			appContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
		if (wifi != null) {
			_wifiDisabledState.value = !wifi.isWifiEnabled
			updateGeneralAirgap(wifi.isWifiEnabled)
		} else {
			_wifiDisabledState.value = true
		}
	}

	/**
	 * Can't do initially as navigation should be initialized before we check rust.
	 */
	fun updateAlertStateFromHistory() {
		_airGapModeState.value = if (historyGetWarnings()) {
			if (airGapModeState.value == NetworkState.Active) NetworkState.Active else NetworkState.Past
		} else {
			NetworkState.None
		}
	}
}

/**
 * Describes current state of network detection alertness
 */
enum class NetworkState {
	None,
	Active,
	Past
}
