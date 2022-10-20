package io.parity.signer.screens.keysets.export

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import io.parity.signer.models.Callback
import io.parity.signer.screens.keysets.KeySetsSelectViewModel
import io.parity.signer.ui.BottomSheetWrapperContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun KeySetsExportScreenFull(
	model: KeySetsSelectViewModel,
	onClose: Callback,
) {
	val modalBottomSheetState =
		rememberModalBottomSheetState(
			ModalBottomSheetValue.Hidden,
			confirmStateChange = {
				it != ModalBottomSheetValue.HalfExpanded
			}
		)
	val scope = rememberCoroutineScope()
	BottomSheetWrapperContent(
		bottomSheetState = modalBottomSheetState,
		bottomSheetContent = {
			KeySetExportResultBottomSheet(
				seeds = model.keys.toSet(), //todo dmitry
				onClose = { scope.launch { modalBottomSheetState.hide() }},
			)
		},
		mainContent = {
			KeySetsSelectExportScreenContent(
				model = model,
				onClose = onClose,
				onExportSelected = {
					scope.launch {
						modalBottomSheetState.animateTo(
							ModalBottomSheetValue.Expanded
						)
					}
				}, //todo dmitry
				onExportAll = {
					scope.launch {
						modalBottomSheetState.animateTo(
							ModalBottomSheetValue.Expanded
						)
					}
				},//todo dmitry
			)
		},
	)
}
