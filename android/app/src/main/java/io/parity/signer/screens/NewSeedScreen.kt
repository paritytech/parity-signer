package io.parity.signer.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import io.parity.signer.ButtonID
import io.parity.signer.components.BigButton
import io.parity.signer.components.HeadingOverline
import io.parity.signer.components.SingleTextInput
import io.parity.signer.models.SignerDataModel

@Composable
fun NewSeedScreen(
	button: (button: ButtonID, details: String) -> Unit,
	signerDataModel: SignerDataModel
) {
	val seedName = remember { mutableStateOf("") }
	val lastError = signerDataModel.lastError.observeAsState()
	val focusManager = LocalFocusManager.current
	val focusRequester = remember { FocusRequester() }

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Top,
		modifier = Modifier.padding(20.dp).fillMaxSize()
	) {
		Row {
			HeadingOverline("DISPLAY NAME")
			Spacer(Modifier.weight(1f))
		}
		SingleTextInput(
			content = seedName,
			update = {
				seedName.value = it
				signerDataModel.clearError()
			},
			onDone = {
				if (seedName.value.isNotBlank() && !seedName.value.contains(",")) {
					button(ButtonID.GoForward, seedName.value)
				}
			},
			isCrypto = true,
			focusManager = focusManager,
			focusRequester = focusRequester
		)

		Text("Display name visible only on this device")
		BigButton(
			text = "Generate seed phrase",
			action = {
				focusManager.clearFocus()
				button(ButtonID.GoForward, seedName.value)
			},
			isDisabled = seedName.value.isBlank() || seedName.value.contains(",")
		)
	}
	DisposableEffect(Unit) {
		if (signerDataModel.screenData.value?.optBoolean("keyboard") == true) {
			focusRequester.requestFocus()
		}
		seedName.value =
			signerDataModel.screenData.value?.optString("seed_name") ?: ""
		onDispose { focusManager.clearFocus() }
	}
}
