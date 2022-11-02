package io.parity.signer.screens.keydetails

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.parity.signer.R
import io.parity.signer.components.base.PrimaryButtonBottomSheet
import io.parity.signer.components.base.ScreenHeader
import io.parity.signer.components.base.ScreenHeaderClose
import io.parity.signer.components.exposesecurity.ExposedIcon
import io.parity.signer.components.panels.BottomBar2
import io.parity.signer.components.panels.BottomBar2State
import io.parity.signer.components.sharedcomponents.KeyCard
import io.parity.signer.components.sharedcomponents.KeySeedCard
import io.parity.signer.models.*
import io.parity.signer.ui.helpers.PreviewData
import io.parity.signer.ui.theme.SignerNewTheme
import io.parity.signer.ui.theme.appliedStroke
import io.parity.signer.ui.theme.fill6
import io.parity.signer.uniffi.Action

/**
 * Default main screen with list Seeds/root keys
 */
@Composable
fun KeyDetailsPublicKeyScreen(
	model: KeyDetailsModel,
	rootNavigator: Navigator,
) {
	Column(Modifier.background(MaterialTheme.colors.background)) {
		ScreenHeaderClose(
			stringResource(id = R.string.key_details_public_export_title),
			if (model.isRootKey) {
				null
			} else {
				stringResource(id = R.string.key_details_public_export_derived_subtitle)
			},
			onClose = { rootNavigator.backAction() },
			onMenu = { rootNavigator.navigate(Action.RIGHT_BUTTON_ACTION) }
		)
		Box(modifier = Modifier.weight(1f)) {
			Column(
				modifier = Modifier.verticalScroll(rememberScrollState())
			) {
				val qrRounding = dimensionResource(id = R.dimen.qrShapeCornerRadius)
				val plateShape =
					RoundedCornerShape(qrRounding, qrRounding, qrRounding, qrRounding)
				Column(
					modifier = Modifier
						.padding(horizontal = 24.dp, vertical = 50.dp)
						.clip(plateShape)
						.border(
							BorderStroke(1.dp, MaterialTheme.colors.appliedStroke),
							plateShape
						)
						.background(MaterialTheme.colors.fill6, plateShape)
				) {
					Box(
						modifier = Modifier
							.fillMaxWidth(1f)
							.aspectRatio(1.1f)
							.background(
								Color.White,
								RoundedCornerShape(qrRounding)
							),
						contentAlignment = Alignment.Center,
					) {
						Image(
							bitmap = model.qr.intoImageBitmap(),
							contentDescription = stringResource(R.string.qr_with_address_to_scan_description),
							contentScale = ContentScale.Fit,
							modifier = Modifier.size(264.dp)
						)
					}
					if (model.isRootKey) {
						KeySeedCard(
							seedTitle = model.address.seedName,
							base58 = model.address.base58
						)
					} else {
						KeyCard(model.address)
					}
				}
			}
		}
	}
}


@Preview(
	name = "light", group = "general", uiMode = Configuration.UI_MODE_NIGHT_NO,
	showBackground = true, backgroundColor = 0xFFFFFFFF,
)
@Preview(
	name = "dark", group = "general",
	uiMode = Configuration.UI_MODE_NIGHT_YES,
	showBackground = true, backgroundColor = 0xFF000000,
)
@Composable
private fun PreviewKeyDetailsScreenDerived() {
	val state = remember { mutableStateOf(AlertState.Past) }
	val mockModel = KeyDetailsModel.createStubDerived()
	SignerNewTheme {
		Box(modifier = Modifier.size(350.dp, 550.dp)) {
			KeyDetailsPublicKeyScreen(mockModel, EmptyNavigator(), )
		}
	}
}

@Preview(
	name = "light", group = "general", uiMode = Configuration.UI_MODE_NIGHT_NO,
	showBackground = true, backgroundColor = 0xFFFFFFFF,
)
@Preview(
	name = "dark", group = "general",
	uiMode = Configuration.UI_MODE_NIGHT_YES,
	showBackground = true, backgroundColor = 0xFF000000,
)
@Composable
private fun PreviewKeyDetailsScreenRoot() {
	val state = remember { mutableStateOf(AlertState.Past) }
	val mockModel = KeyDetailsModel.createStubRoot()
	SignerNewTheme {
		Box(modifier = Modifier.size(350.dp, 550.dp)) {
			KeyDetailsPublicKeyScreen(mockModel, EmptyNavigator(), )
		}
	}
}
