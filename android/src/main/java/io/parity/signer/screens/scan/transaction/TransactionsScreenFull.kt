package io.parity.signer.screens.scan.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.parity.signer.R
import io.parity.signer.components.base.PrimaryButtonWide
import io.parity.signer.components.base.ScreenHeader
import io.parity.signer.components.base.SecondaryButtonWide
import io.parity.signer.components.qrcode.AnimatedQrKeysInfo
import io.parity.signer.components.qrcode.EmptyQrCodeProvider
import io.parity.signer.models.*
import io.parity.signer.screens.scan.elements.TransactionErrors
import io.parity.signer.screens.scan.transaction.components.TransactionElementSelector
import io.parity.signer.screens.scan.transaction.components.TransactionSummaryView
import io.parity.signer.screens.scan.transaction.components.toSigningTransactionModels
import io.parity.signer.ui.theme.SignerTypeface
import io.parity.signer.uniffi.MSignatureReady
import io.parity.signer.uniffi.MTransaction
import io.parity.signer.uniffi.TransactionType


/**
 * Old UI screen edited to work on new screens
 */
@Composable
fun TransactionsScreenFull(
	transactions: List<MTransaction>,
	title: String,
	signature: MSignatureReady?,
	modifier: Modifier = Modifier,
	onBack: Callback,
	onFinish: Callback, //todo scan this leading to general state moving to Scan and it's crashing in selector
) {
	val detailedTransaction = remember {
		mutableStateOf<MTransaction?>(null)
	}
	detailedTransaction.value?.let { detaitTransac ->
		//detailes mode
		TransactionDetailsScreen(transaction = detaitTransac,
			modifier = modifier,
			onBack = { detailedTransaction.value = null })
	} ?: run {
		//default view
		TransactionsScreenFull(
			modifier,
			title,
			onBack,
			transactions,
			detailedTransaction,
			signature,
			onFinish
		)
	}
}

@Composable
private fun TransactionsScreenFull(
	modifier: Modifier,
	title: String,
	onBack: Callback,
	transactions: List<MTransaction>,
	detailedTransaction: MutableState<MTransaction?>,
	signature: MSignatureReady?,
	onFinish: Callback
) {
	Column(modifier.fillMaxSize(1f)) {
		ScreenHeader(title = title, onBack = onBack)
		Column(
			Modifier.verticalScroll(rememberScrollState())
		) {
			transactions.forEach {
				TransactionIssues(it)
			}
			//new transaction summary
			ShowTransactionsPreview(transactions, detailedTransaction)
			signature?.let {
				QrSignatureData(it)
			}
			Spacer(modifier = Modifier.weight(1f))
			ActionButtons(
				transactions,
				onBack,
				onFinish
			)
		}
	}
}

@Composable
private fun ShowTransactionsPreview(
	transactions: List<MTransaction>,
	detailedTransactionPreview: MutableState<MTransaction?>
) {
	transactions.withIndex()
		.filter { it.value.shouldShowAsSummaryTransaction() }
		.toSigningTransactionModels().forEach { transactionModel ->
			TransactionSummaryView(transactionModel) { index ->
				try {
					val transaction = transactions[index]
					detailedTransactionPreview.value = transaction
				} catch (e: Exception) {
					submitErrorState("wrong index of clicked transaction - sync issue? $e")
				}
			}
		}
	//old separate transactions
	transactions.filter { !it.shouldShowAsSummaryTransaction() }.forEach {
		Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
			it.sortedValueCards.forEach {
				TransactionElementSelector(it)
			}
		}
//	transaction.authorInfo?.let {
//		KeyCardOld(identity = it)
//	}
//	transaction.networkInfo?.let {
//		NetworkCard(NetworkCardModel(it.networkTitle, it.networkLogo))
//	}
	}
}

@Composable
private fun QrSignatureData(signature: MSignatureReady) {
	Text(
		text = stringResource(R.string.transaction_qr_header),
		color = MaterialTheme.colors.primary,
		style = SignerTypeface.TitleS,
		modifier = Modifier.padding(horizontal = 8.dp, vertical = 14.dp),
		maxLines = 1,
	)
	AnimatedQrKeysInfo<List<List<UByte>>>(
		input = signature.signatures.map { it.getData() },
		provider = EmptyQrCodeProvider(),
		modifier = Modifier.fillMaxWidth(1f)
	)
}

@Composable
private fun ActionButtons(
	transactions: List<MTransaction>,
	onBack: Callback,
	onFinish: Callback
) {
	val action = transactions.first().ttype
	when (action) {
		TransactionType.SIGN -> {
			AddLogElement()

			SecondaryButtonWide(
				label = stringResource(R.string.transaction_action_done),
				withBackground = true,
				modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
				onClicked = onBack,
			)
		}
		TransactionType.DONE -> {
			SecondaryButtonWide(
				label = stringResource(R.string.transaction_action_done),
				withBackground = true,
				modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
				onClicked = onBack,
			)
		}
		TransactionType.STUB -> {
			PrimaryButtonWide(
				label = stringResource(R.string.transaction_action_approve),
				modifier = Modifier
					.padding(horizontal = 24.dp)
					.padding(top = 32.dp, bottom = 8.dp),
				onClicked = onFinish,
			)
			SecondaryButtonWide(
				label = stringResource(R.string.transaction_action_decline),
				modifier = Modifier
					.padding(horizontal = 24.dp)
					.padding(bottom = 32.dp),
				onClicked = onBack,
			)
		}
		TransactionType.READ -> {
			SecondaryButtonWide(
				label = stringResource(R.string.transaction_action_back),
				withBackground = true,
				modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
				onClicked = onBack,
			)
		}
		TransactionType.IMPORT_DERIVATIONS -> {
			PrimaryButtonWide(
				label = stringResource(R.string.transaction_action_select_seed),
				modifier = Modifier
					.padding(horizontal = 24.dp)
					.padding(top = 32.dp, bottom = 8.dp),
				onClicked = onFinish,
			)
			SecondaryButtonWide(
				label = stringResource(R.string.transaction_action_decline),
				modifier = Modifier
					.padding(horizontal = 24.dp)
					.padding(bottom = 32.dp),
				onClicked = onBack,
			)
		}
	}
}

@Composable
private fun AddLogElement() {
	//already signed and we show qr code in this screen now
	// , so we cannot add log there
//	val comment = remember { mutableStateOf("") }
//	Text(
//		"LOG NOTE",
//		style = MaterialTheme.typography.overline,
//		color = MaterialTheme.colors.Text400
//	)
//
//	val focusManager = LocalFocusManager.current
//	val focusRequester = remember { FocusRequester() }
//	SingleTextInput(
//		content = comment,
//		update = { comment.value = it },
//		onDone = { },
//		focusManager = focusManager,
//		focusRequester = focusRequester
//	)
//	Text(
//		"visible only on this device",
//		style = MaterialTheme.typography.subtitle1,
//		color = MaterialTheme.colors.Text400
//	)
}

@Composable
internal fun TransactionIssues(transaction: MTransaction) {
	transaction.transactionIssues().let {
		if (it.isNotEmpty()) {
			TransactionErrors(
				errors = it,
				modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
			)
		}
	}
}

private fun MTransaction.shouldShowAsSummaryTransaction(): Boolean {
	return when (ttype) {
		// Rounded corner summary card like new transaction to send tokens
		TransactionType.SIGN,
		TransactionType.READ -> {
			true
		}
		// Used when new network is being added
		// User when network metadata is being added
		TransactionType.STUB,
		TransactionType.DONE,
		TransactionType.IMPORT_DERIVATIONS -> {
			return false
		}
	}
}