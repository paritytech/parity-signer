package io.parity.signer.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.parity.signer.components.documents.PpScreen
import io.parity.signer.components.documents.TosScreen
import io.parity.signer.domain.Callback
import io.parity.signer.domain.Navigator
import io.parity.signer.domain.NetworkState
import io.parity.signer.screens.settings.backup.SeedBackupIntegratedScreen
import io.parity.signer.screens.settings.general.ConfirmFactorySettingsBottomSheet
import io.parity.signer.screens.settings.general.SettingsGeneralNavSubgraph
import io.parity.signer.screens.settings.logs.logsNavigationSubgraph
import io.parity.signer.ui.BottomSheetWrapperRoot
import io.parity.signer.uniffi.Action

/**
 * Settings screen; General purpose stuff like legal info, networks management
 * and history should be here. This is final point in navigation:
 * all subsequent interactions should be in modals or drop-down menus
 */
@Composable
fun SettingsScreenSubgraph(
	rootNavigator: Navigator,
	isStrongBoxProtected: Boolean,
	appVersion: String,
	wipeToFactory: Callback,
	networkState: State<NetworkState?>
) {
	val navController = rememberNavController()
	NavHost(
		navController = navController,
		startDestination = SettingsScreenSubgraph.home,
	) {

		composable(SettingsScreenSubgraph.home) {
			SettingsGeneralNavSubgraph(
				rootNavigator = rootNavigator,
				parentNavController = navController,
				isStrongBoxProtected = isStrongBoxProtected,
				appVersion = appVersion,
				wipeToFactory = wipeToFactory,
				networkState = networkState,
			)
		}
		composable(SettingsScreenSubgraph.terms) {
			Box(modifier = Modifier.statusBarsPadding()) {
				TosScreen(onBack = {
					navController.popBackStack(SettingsScreenSubgraph.home, false)
				})
			}
		}
		composable(SettingsScreenSubgraph.privacyPolicy) {
			Box(modifier = Modifier.statusBarsPadding()) {
				PpScreen(onBack = {
					navController.popBackStack(SettingsScreenSubgraph.home, false)
				})
			}
		}
		composable(SettingsScreenSubgraph.backup) {
			SeedBackupIntegratedScreen(rootNavigator) {
				navController.popBackStack(SettingsScreenSubgraph.home, false)
			}
		}
		logsNavigationSubgraph(
			SettingsScreenSubgraph.logs,
			rootNavigator,
			navController
		)
	}
}

internal object SettingsScreenSubgraph {
	const val home = "settings_home"
	const val terms = "settings_terms_of_service"
	const val privacyPolicy = "settings_privacy_polcy"
	const val backup = "settings_backup"
	const val logs = "settings_logs"

}

