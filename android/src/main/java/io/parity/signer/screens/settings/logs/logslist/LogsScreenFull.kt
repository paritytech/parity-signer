package io.parity.signer.screens.settings.logs.logslist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.parity.signer.domain.Navigator
import io.parity.signer.screens.settings.logs.LogsScreenModel
import io.parity.signer.screens.settings.logs.LogsSubgraph


@Composable
fun LogsScreenFull(
	rootNavigator: Navigator,
	navController: NavController,
) {
	val subNavController = rememberNavController()
	Box(Modifier.statusBarsPadding()) {
		LogsScreen(
			model = LogsScreenModel(),
			rootNavigator = rootNavigator,
			onMenu = { subNavController.navigate(LogsMenuSubgraph.logs_menu) },
			onLogClicked = { logId -> navController.navigate(LogsSubgraph.logs_details + "/" + logId) },
		)
	}
	//bottom sheets
	NavHost(
		navController = subNavController,
		startDestination = LogsMenuSubgraph.logs_empty,
	) {
		composable(LogsMenuSubgraph.logs_empty) {}
		composable(LogsMenuSubgraph.logs_menu) {
//					BottomSheetWrapperRoot(onClosedAction = closeAction) {
//						NetworkSelectionBottomSheet(
//							networks = deriveViewModel.allNetworks,
//							currentlySelectedNetwork = selectedNetwork.value,
//							onClose = closeAction,
//							onSelect = { newNetwork ->
//								deriveViewModel.updateSelectedNetwork(newNetwork)
//								closeAction()
//							},
//						)
//					}
		}
		composable(LogsMenuSubgraph.logs_menu_delete_confirm) {

		}
	}
}

private object LogsMenuSubgraph {
	const val logs_empty = "logs_menu_empty"
	const val logs_menu = "logs_menu"
	const val logs_menu_delete_confirm = "logs_menu_delete_confirm"
}
