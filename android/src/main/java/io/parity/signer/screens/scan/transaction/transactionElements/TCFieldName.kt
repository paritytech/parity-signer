package io.parity.signer.screens.scan.transaction.transactionElements

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import io.parity.signer.ui.theme.Text600
import io.parity.signer.uniffi.MscFieldName

@Composable
fun TCFieldName(fieldName: MscFieldName) {
	//TODO: documentation button
	//todo dmitry redesign this and below
	Text(
		fieldName.name,
		style = MaterialTheme.typography.body2,
		color = MaterialTheme.colors.Text600
	)
}
