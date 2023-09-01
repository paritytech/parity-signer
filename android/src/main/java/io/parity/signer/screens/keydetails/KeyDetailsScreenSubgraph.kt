package io.parity.signer.screens.keydetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.parity.signer.domain.Callback
import io.parity.signer.domain.backend.mapError
import io.parity.signer.domain.toKeyDetailsModel
import io.parity.signer.screens.keydetails.exportprivatekey.ConfirmExportPrivateKeyMenu
import io.parity.signer.screens.keydetails.exportprivatekey.PrivateKeyExportBottomSheet
import io.parity.signer.ui.BottomSheetWrapperRoot
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@Composable
fun KeyDetailsScreenSubgraph(
	navController: NavHostController,
	keyAddr: String,
	keySpec: String,
) {

	val vm = KeyDetailsScreenViewModel()
	//todo dmitry fix
	val model = remember {
		runBlocking {
			vm.fetchModel(keyAddr, keySpec)
		}.mapError()!!.toKeyDetailsModel()
	}
	val menuNavController = rememberNavController()

	Box(modifier = Modifier.statusBarsPadding()) {
		KeyDetailsPublicKeyScreen(
			model = model,
			onBack = { navController.popBackStack() },
			onMenu = {
				menuNavController.navigate(
					KeyPublicDetailsMenuSubgraph.keyMenuGeneral
				)
			},
		)
	}


	NavHost(
		navController = menuNavController,
		startDestination = KeyPublicDetailsMenuSubgraph.empty,
	) {
		val closeAction: Callback = {
			menuNavController.popBackStack()
		}
		composable(KeyPublicDetailsMenuSubgraph.empty) {}//no menu
		composable(KeyPublicDetailsMenuSubgraph.keyMenuGeneral) {
			BottomSheetWrapperRoot(onClosedAction = closeAction) {
				KeyDetailsGeneralMenu(
					closeMenu = closeAction,
					onExportPrivateKey = {
						navController.navigate(KeyPublicDetailsMenuSubgraph.keyMenuExportConfirmation) {
							popUpTo(KeyPublicDetailsMenuSubgraph.empty)
						}
					},
					onDelete = {
						navController.navigate(KeyPublicDetailsMenuSubgraph.keyMenuDelete) {
							popUpTo(KeyPublicDetailsMenuSubgraph.empty)
						}
					},
				)
			}
		}
		composable(KeyPublicDetailsMenuSubgraph.keyMenuDelete) {
			val context = rememberCoroutineScope()
			BottomSheetWrapperRoot(onClosedAction = closeAction) {
				KeyDetailsDeleteConfirmBottomSheet(
					onCancel = closeAction,
					onRemoveKey = {
						context.launch {
							val result = vm.removeDerivedKey(keyAddr, keySpec)
							//todo dmitry post toast success of not
						}
					},
				)
			}
		}
		composable(KeyPublicDetailsMenuSubgraph.keyMenuExportConfirmation) {
			BottomSheetWrapperRoot(onClosedAction = closeAction) {
				ConfirmExportPrivateKeyMenu(
					onExportPrivate = {
						navController.navigate(KeyPublicDetailsMenuSubgraph.keyMenuExportResult) {
							popUpTo(KeyPublicDetailsMenuSubgraph.empty)
						}
					},
					onClose = closeAction,
				)
			}
		}
		composable(KeyPublicDetailsMenuSubgraph.keyMenuExportResult) {
			val context = LocalContext.current
			val privateModel = remember {
				vm.getPrivateExportKey(model, context)
			}
			if (privateModel != null) {
				BottomSheetWrapperRoot(onClosedAction = closeAction) {
					PrivateKeyExportBottomSheet(
						model = privateModel,
						onClose = closeAction,
					)
				}
			} else {
				// #1533
				// navigate to KeyPublicDetailsMenuSubgraph.keyMenuPasswordForExport
			}
		}
		composable(KeyPublicDetailsMenuSubgraph.keyMenuPasswordForExport) {
			//todo handle keyMenuExportResult #1533 issue
		}
	}
}


private object KeyPublicDetailsMenuSubgraph {
	const val empty = "key_menu_empty"
	const val keyMenuGeneral = "key_menu_general"
	const val keyMenuDelete = "key_menu_delete"
	const val keyMenuExportConfirmation = "key_menu_export"
	const val keyMenuExportResult = "key_private_export_result"
	const val keyMenuPasswordForExport = "key_private_export_password"
}
