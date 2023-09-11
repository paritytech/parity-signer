package io.parity.signer.screens.settings.general

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.parity.signer.components.exposesecurity.ExposedAlert
import io.parity.signer.domain.Callback
import io.parity.signer.screens.settings.SettingsScreenSubgraph
import io.parity.signer.ui.BottomSheetWrapperRoot


@Composable
internal fun SettingsGeneralNavSubgraph(
	parentNavController: NavController,
) {
	val context = LocalContext.current
	val vm: SettingsGeneralViewModel = viewModel()

	val appVersion = rememberSaveable { vm.getAppVersion(context)	}
	val shieldState = vm.networkState.collectAsStateWithLifecycle()

	val menuNavController = rememberNavController()

	Box(modifier = Modifier.statusBarsPadding()) {
		SettingsScreenGeneralView(
			navController = parentNavController,
			onWipeData = { menuNavController.navigate(SettingsGeneralMenu.wipe_factory) },
			onOpenLogs = { parentNavController.navigate(SettingsScreenSubgraph.logs) },
			onShowTerms = { parentNavController.navigate(SettingsScreenSubgraph.terms) },
			onShowPrivacyPolicy = {
				parentNavController.navigate(SettingsScreenSubgraph.privacyPolicy)
			},
			onBackup = { parentNavController.navigate(SettingsScreenSubgraph.backup) },
			onManageNetworks = {
				parentNavController.navigate(SettingsScreenSubgraph.networkList)
			},
			onGeneralVerifier = {
				parentNavController.navigate(SettingsScreenSubgraph.generalVerifier)
			},
			onExposedClicked = { menuNavController.navigate(SettingsGeneralMenu.exposed_shield_alert) },
			isStrongBoxProtected = vm.isStrongBoxProtected,
			appVersion = appVersion,
			networkState = shieldState,
		)
	}

	NavHost(
		navController = menuNavController,
		startDestination = SettingsGeneralMenu.empty,
	) {
		val closeAction: Callback = {
			menuNavController.popBackStack()
		}
		composable(SettingsGeneralMenu.empty) {}//no menu
		composable(SettingsGeneralMenu.wipe_factory) {
			BottomSheetWrapperRoot(onClosedAction = closeAction) {
				ConfirmFactorySettingsBottomSheet(
					onCancel = closeAction,
					onFactoryReset = vm::wipeToFactory
				)
			}
		}
		composable(SettingsGeneralMenu.exposed_shield_alert) {
			ExposedAlert(navigateBack = closeAction)
		}
	}
}


private object SettingsGeneralMenu {
	const val empty = "settings_menu_empty"
	const val wipe_factory = "settings_confirm_reset"
	const val exposed_shield_alert = "settings_exposed"
}
