package io.parity.signer.components.panels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.parity.signer.components.NavbarShield
import io.parity.signer.domain.NetworkState
import io.parity.signer.domain.MainFlowViewModel
import io.parity.signer.domain.navigate
import io.parity.signer.ui.theme.Action400
import io.parity.signer.ui.theme.Bg100
import io.parity.signer.ui.theme.Text400
import io.parity.signer.uniffi.Action
import io.parity.signer.uniffi.RightButton
import io.parity.signer.uniffi.ScreenNameType

@Composable
fun TopBar(
	mainFlowViewModel: MainFlowViewModel,
	networkState: State<NetworkState?>
) {
	val actionResult = mainFlowViewModel.actionResult.collectAsState()
	// val screenName = signerDataModel.screenLabel.collectAsState()
	// val screenNameType = signerDataModel.screenNameType.collectAsState()
	// val rightButton = signerDataModel.rightButton.collectAsState()

	TopAppBar(
		backgroundColor = MaterialTheme.colors.Bg100
	) {
		Row(
			horizontalArrangement = Arrangement.Start,
			modifier = Modifier
				.weight(0.2f, fill = true)
				.width(72.dp)
		) {
			if (actionResult.value.back == true) {
				Button(
					colors = buttonColors(
						contentColor = MaterialTheme.colors.Action400,
						backgroundColor = MaterialTheme.colors.Bg100
					),
					onClick = {
						mainFlowViewModel.navigate(Action.GO_BACK)
					}
				) {
					if (actionResult.value.rightButton == RightButton.MULTI_SELECT) {
						Icon(
							Icons.Default.Close,
							"go back",
							tint = MaterialTheme.colors.Text400
						)
					} else {
						Icon(
							Icons.Default.ArrowBack,
							"go back",
							tint = MaterialTheme.colors.Text400
						)
					}
				}
			}
		}
		Row(
			horizontalArrangement = Arrangement.Center,
			modifier = Modifier.weight(0.6f, fill = true)
		) {
			Text(
				actionResult.value.screenLabel ?: "",
				style = if (actionResult.value.screenNameType == ScreenNameType.H1) {
					MaterialTheme.typography.h2
				} else {
					MaterialTheme.typography.h4
				}
			)
		}
		Row(
			horizontalArrangement = Arrangement.End,
			modifier = Modifier
				.weight(0.2f, fill = true)
				.width(72.dp)
		) {
			IconButton(onClick = { mainFlowViewModel.navigate(Action.RIGHT_BUTTON_ACTION) }) {
				when (actionResult.value.rightButton) {
					RightButton.NEW_SEED -> {
						Icon(
							Icons.Default.AddCircleOutline,
							"New Seed",
							tint = MaterialTheme.colors.Action400
						)
					}
					RightButton.BACKUP -> {
						Icon(
							Icons.Default.MoreVert,
							"Seed backup",
							tint = MaterialTheme.colors.Action400
						)
					}
					RightButton.LOG_RIGHT -> {
						Icon(
							Icons.Default.MoreVert,
							"Log menu",
							tint = MaterialTheme.colors.Action400
						)
					}
					RightButton.MULTI_SELECT -> {
					}
					null -> {}
					else -> {
						Icon(
							Icons.Default.MoreVert,
							"Menu",
							tint = MaterialTheme.colors.Action400
						)
					}
				}
			}
			IconButton(onClick = { mainFlowViewModel.navigate(Action.SHIELD) }) {
				NavbarShield(networkState = networkState)
			}
		}
	}
}
