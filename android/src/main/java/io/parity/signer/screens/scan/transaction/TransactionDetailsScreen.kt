package io.parity.signer.screens.scan.transaction

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.parity.signer.R
import io.parity.signer.components.base.ScreenHeader
import io.parity.signer.models.Callback
import io.parity.signer.screens.scan.transaction.components.TransactionElementSelector
import io.parity.signer.uniffi.MTransaction


@Composable
internal fun TransactionDetailsScreen(
	transaction: MTransaction,
	modifier: Modifier = Modifier,
	onBack: Callback
) {
	BackHandler(onBack = onBack)

	Column(modifier.fillMaxSize(1f)) {
		ScreenHeader(
			title = stringResource(R.string.transaction_details_screen_header),
			onBack = onBack
		)
		Column(
			Modifier.verticalScroll(rememberScrollState())
		) {
			TransactionIssues(transaction)
			transaction.sortedValueCards.forEach {
				TransactionElementSelector(it)
			}
		}
	}
}
