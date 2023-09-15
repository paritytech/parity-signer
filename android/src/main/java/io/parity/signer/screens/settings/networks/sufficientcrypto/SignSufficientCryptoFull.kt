package io.parity.signer.screens.settings.networks.sufficientcrypto

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.parity.signer.screens.settings.networks.sufficientcrypto.view.SufficientCryptoReadyBottomSheet
import io.parity.signer.bottomsheets.password.EnterPassword
import io.parity.signer.domain.Callback
import io.parity.signer.screens.settings.networks.sufficientcrypto.view.SignSufficientCryptoScreen
import io.parity.signer.ui.BottomSheetWrapperRoot
import io.parity.signer.uniffi.MSignSufficientCrypto


//todo dmitry implement
//				as SignSufficientCryptoInteractor done
//				navstate.rs:830 it's Sign sufficient crypto


//		SignSufficientCrypto(
//			screenData.f,
//			sharedViewModel::signSufficientCrypto
//		)
// end of action here calling go forward and it's in navstate.rs:427
//			todo dmitry handle password here on action
//			io/parity/signer/domain/storage/TransactionOld.kt:8 ^^

//todo dmitry get this model like in
// ios/PolkadotVault/Backend/NavigationServices/ManageNetworkDetailsService.swift:10


@Composable
fun SignSufficientCryptoFull(
	sc: MSignSufficientCrypto,
	onBack: Callback
) {
	val vm: SignSufficientCryptoViewModel = viewModel()

	val passwordState = vm.password.collectAsStateWithLifecycle()
	val signatureState = vm.signature.collectAsStateWithLifecycle()

	val backAction = {
		val wasState = vm.isHasStateThenClear()
		if (!wasState) onBack()
	}
	BackHandler(onBack = backAction)

	SignSufficientCryptoScreen(
		model = sc,
		signSufficientCrypto = vm::onSignSufficientCrypto,
	)

	passwordState.value?.let { enterPasswordModel ->
		BottomSheetWrapperRoot(onClosedAction = vm::clearState) {
			EnterPassword(
				data = enterPasswordModel,
				proceed = { password ->
					vm.passwordAttempt(password)
				},
				onClose = vm::clearState,
			)
		}
	} ?: signatureState.value?.let { signature ->
		BottomSheetWrapperRoot(onClosedAction = vm::clearState) {
			SufficientCryptoReadyBottomSheet(
				sufficientCrypto = signature,
			)
		}
	}

}

