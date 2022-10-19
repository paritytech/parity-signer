package io.parity.signer.screens.keysets.export

import io.parity.signer.screens.keysets.KeySetViewModel
import io.parity.signer.ui.helpers.PreviewData


class KeySetsExportService {
	suspend fun getQrCodesList(keySets: List<KeySetViewModel>): List<List<UByte>> {
		//todo dmitry
		return listOf(
			PreviewData.exampleQRCode,
			PreviewData.exampleQRCode.map{(it + 10.toUByte()).toUByte()}
		)
	}
}
