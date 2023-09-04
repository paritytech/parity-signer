package io.parity.signer.ui.mainnavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.parity.signer.domain.Navigator
import io.parity.signer.domain.SharedViewModel
import io.parity.signer.domain.storage.removeSeed
import io.parity.signer.domain.submitErrorState
import io.parity.signer.domain.toKeySetDetailsModel
import io.parity.signer.screens.createderivation.DerivationCreateSubgraph
import io.parity.signer.screens.keydetails.KeyDetailsScreenSubgraph
import io.parity.signer.screens.keysetdetails.KeySetDetailsScreenSubgraph
import io.parity.signer.screens.keysets.KeySetsScreenSubgraph
import io.parity.signer.screens.keysets.create.NewKeysetStepSubgraph
import io.parity.signer.screens.scan.ScanNavSubgraph
import io.parity.signer.screens.settings.SettingsScreenSubgraph
import io.parity.signer.uniffi.ErrorDisplayed
import io.parity.signer.uniffi.keysBySeedName

@Composable
fun CoreUnlockedNavSubgraph(
	rootNavigator: Navigator,
	singleton: SharedViewModel,
) {

	val navController = rememberNavController()
	NavHost(
		navController = navController,
		startDestination = CoreUnlockedNavSubgraph.keySetList,
	) {
		composable(CoreUnlockedNavSubgraph.keySetList) {
			Box(modifier = Modifier.statusBarsPadding()) {
				KeySetsScreenSubgraph(
					rootNavigator = rootNavigator,
					navController = navController,
				)
			}
		}
		composable(
			route = CoreUnlockedNavSubgraph.KeySetDetails.route,
			arguments = listOf(
				navArgument(CoreUnlockedNavSubgraph.KeySetDetails.seedNameArg) {
					type = NavType.StringType
				}
			)
		) {
			val seedName =
				it.arguments?.getString(CoreUnlockedNavSubgraph.KeySetDetails.seedNameArg)
			val model = remember {
				try {
					//todo dmitry export this to vm and handle errors - open default for example
					keysBySeedName(seedName!!).toKeySetDetailsModel()
				} catch (e: ErrorDisplayed) {
					rootNavigator.backAction()
					submitErrorState("unexpected error in keysBySeedName $e")
					null
				}
			}
			model?.let {
				KeySetDetailsScreenSubgraph(
					fullModel = model,
					navController = navController,
					onBack = { rootNavigator.backAction() },
					onRemoveKeySet = {
						val root = model.root
						if (root != null) {
							singleton.removeSeed(root.seedName)
						} else {
							//todo key details check if this functions should be disabled in a first place
							submitErrorState("came to remove key set but root key is not available")
						}
					},
				)
			}
		}
		composable(CoreUnlockedNavSubgraph.newKeySet) {
			NewKeysetStepSubgraph(
				navController = navController,
			)
		}
		composable(CoreUnlockedNavSubgraph.recoverKeySet) {
			//todo dmitry implement
		}
		composable(
			route = CoreUnlockedNavSubgraph.KeyDetails.route,
			arguments = listOf(
				navArgument(CoreUnlockedNavSubgraph.KeyDetails.argKeyAddr) {
					type = NavType.StringType
				},
				navArgument(CoreUnlockedNavSubgraph.KeyDetails.argKeySpec) {
					type = NavType.StringType
				},
			)
		) {
			val keyAddr =
				it.arguments?.getString(CoreUnlockedNavSubgraph.KeyDetails.argKeyAddr)!!
			val keySpec =
				it.arguments?.getString(CoreUnlockedNavSubgraph.KeyDetails.argKeySpec)!!
			KeyDetailsScreenSubgraph(
				navController = navController,
				keyAddr = keyAddr,
				keySpec = keySpec
			)
		}
		composable(CoreUnlockedNavSubgraph.camera) {
			ScanNavSubgraph(
				onCloseCamera = {
					navController.popBackStack()
				},
				openKeySet = { seedName ->
					navController.navigate(
						CoreUnlockedNavSubgraph.KeySetDetails.destination(
							seedName = seedName
						)
					)
				}
			)
		}
		composable(CoreUnlockedNavSubgraph.newDerivationKey) {
			//todo dmitry implement
//			Box(
//				modifier = Modifier
//					.background(MaterialTheme.colors.background)
//			) {
//				DerivationCreateSubgraph(
//					rootNavigator, screenData.f.seedName,
//				)
//			}
		}
		composable(CoreUnlockedNavSubgraph.settings) {
			//todo dmitry implement
//			SettingsScreenSubgraph(
//				rootNavigator = rootNavigator,
//				isStrongBoxProtected = sharedViewModel.seedStorage.isStrongBoxProtected,
//				appVersion = sharedViewModel.getAppVersion(),
//				wipeToFactory = sharedViewModel::wipeToFactory,
//				networkState = networkState
//			)
		}
	}
}

internal object CoreUnlockedNavSubgraph {
	const val keySetList = "core_keyset_list"
	const val newKeySet = "core_new_keyset"
	const val newDerivationKey = "core_new_derivation"
	const val recoverKeySet = "keyset_recover_flow"
	const val camera = "unlocked_camera"

	object KeySetDetails {
		internal const val seedNameArg = "title"
		private const val baseRoute = "core_keyset_details_home"
		const val route = "$baseRoute/{$seedNameArg}"
		fun destination(seedName: String) = "$baseRoute/${seedName}"
	}

	object KeyDetails {
		internal const val argKeyAddr = "key_addr"
		internal const val argKeySpec = "key_spec"
		private const val baseRoute = "core_key_details_home"
		const val route = "$baseRoute/{$argKeyAddr}/{$argKeySpec}"
		fun destination(keyAddr: String, keySpec: String) =
			"$baseRoute/$keyAddr/$keySpec"
	}
	const val settings = "core_settings_flow"
}
