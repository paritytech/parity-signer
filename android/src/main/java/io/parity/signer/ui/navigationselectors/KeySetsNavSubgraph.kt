package io.parity.signer.ui.navigationselectors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.parity.signer.models.AlertState
import io.parity.signer.models.Navigator
import io.parity.signer.screens.keysets.KeySetsMenuBottomSheet
import io.parity.signer.screens.keysets.KeySetsScreen
import io.parity.signer.screens.keysets.KeySetsSelectViewModel
import io.parity.signer.screens.keysets.export.KeySetExportBottomSheet
import io.parity.signer.screens.keysets.export.KeySetsSelectExportScreen
import io.parity.signer.ui.BottomSheetWrapper
//todo dmitry check that bottomsheet doesn't cover title
/**
 * Navigation Subgraph with compose nav controller for those Key Set screens which are not part of general
 * Rust-controlled navigation
 */
@Composable
fun KeySetsNavSubgraph(
	model: KeySetsSelectViewModel,
	rootNavigator: Navigator,
	alertState: State<AlertState?>, //for shield icon
) {
	val navController = rememberNavController()
	NavHost(
		navController = navController,
		startDestination = KeySetsNavSubgraph.home,
	) {

		composable(KeySetsNavSubgraph.home) {
			KeySetsScreen(
				model = model,
				rootNavigator = rootNavigator,
				localNavigator = navController,
				alertState = alertState,
			)
		}
		composable(KeySetsNavSubgraph.homeMenu) {
			KeySetsScreen(
				model = model,
				rootNavigator = rootNavigator,
				localNavigator = navController,
				alertState = alertState,
			)
			BottomSheetWrapper(onClosedAction = {
				navController.navigate(
					KeySetsNavSubgraph.home
				)
			}) {
				KeySetsMenuBottomSheet(navigator = navController)
			}
		}
		composable(KeySetsNavSubgraph.exportSelect) {
			KeySetsSelectExportScreen(
				model = model,
				navigator = navController,
			)
		}
		composable(KeySetsNavSubgraph.exportResult) {
			KeySetsSelectExportScreen(
				model = model,
				navigator = navController,
			)
			BottomSheetWrapper(onClosedAction = {
				navController.navigate(
					KeySetsNavSubgraph.home
				)
			}) {
				KeySetExportBottomSheet(
					model.keys.toSet() //todo dmitry
				)
			}
		}
	}
}

object KeySetsNavSubgraph {
	const val home = "keysets_home"
	const val homeMenu = "keysets_menu"
	const val exportSelect = "keysets_export_multiselect"
	const val exportResult = "keysets_export_results"
}
