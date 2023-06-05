package io.parity.signer.screens.keysets.create

import SignerCheckbox
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.parity.signer.R
import io.parity.signer.components.base.NotificationFrameText
import io.parity.signer.components.base.PrimaryButtonWide
import io.parity.signer.components.base.ScreenHeader
import io.parity.signer.components.base.SignerDivider
import io.parity.signer.components.networkicon.NetworkIcon
import io.parity.signer.domain.Callback
import io.parity.signer.domain.NetworkModel
import io.parity.signer.ui.theme.SignerNewTheme
import io.parity.signer.ui.theme.SignerTypeface
import io.parity.signer.ui.theme.fill6

//todo dmitry add this
@Composable
fun NewKeySetSelectNetworkScreen(
	networks: List<NetworkModel>,
	selected: MutableState<Set<NetworkModel>>,
	onNetworkSelect: (NetworkModel) -> Unit, //todo dmitry
	onProceed: () -> Unit, // todo dmitry onProceed: (List<NetworkModel>) -> Unit,
	onAddAll: Callback,
	onBack: Callback
) {
	//io/parity/signer/screens/keysetdetails/items/NetworkKeysExpandableMultiselect.kt:57
	//todo dmitry example as it was done
	Column(
		modifier = Modifier
            .fillMaxSize(1f)
            .background(MaterialTheme.colors.background)
            .verticalScroll(rememberScrollState()),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		ScreenHeader(
			title = stringResource(R.string.keyset_create_keys_title), //todo dmitry
			onBack = onBack,
		)
		Text(
			text = stringResource(R.string.keyset_create_keys_subtitle), //todo dmitry
			color = MaterialTheme.colors.primary,
			style = SignerTypeface.BodyL,
			modifier = Modifier
				.padding(horizontal = 24.dp)
		)
		Column(
			modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .background(
                    MaterialTheme.colors.fill6,
                    RoundedCornerShape(dimensionResource(id = R.dimen.plateDefaultCornerRadius))
                )
		) {
			networks.forEach { network ->
				NetworkItemMultiselect(
					network = network,
					isSelected = selected.value.contains(network)
				) { network ->
					onNetworkSelect(network)
				}
				SignerDivider()
			}
			NetworkItemMultiselectAll(onAddAll)
		}
		NotificationFrameText(
			message = stringResource(R.string.keyset_create_keys_notification_text),
			modifier = Modifier
				.padding(horizontal = 16.dp)
		)
		Spacer(modifier = Modifier.weight(1f))

		PrimaryButtonWide(
			label = stringResource(R.string.keyset_create_keys_cta),
			modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp),
			onClicked = onProceed,
		)
	}
}


@Composable
private fun NetworkItemMultiselect(
	network: NetworkModel,
	isSelected: Boolean,
	onClick: (NetworkModel) -> Unit,
) {
	Row(
		modifier = Modifier.clickable { onClick(network) },
		verticalAlignment = Alignment.CenterVertically
	) {
		NetworkIcon(
			networkLogoName = network.logo,
			modifier = Modifier
                .padding(
                    top = 16.dp,
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 12.dp
                )
                .size(36.dp),
		)
		Text(
			text = network.title,
			color = MaterialTheme.colors.primary,
			style = SignerTypeface.TitleS,
		)
		Spacer(modifier = Modifier.weight(1f))
		SignerCheckbox(
			isChecked = isSelected,
			modifier = Modifier.padding(end = 8.dp),
			uncheckedColor = MaterialTheme.colors.primary,
		) {
			onClick(network)
		}
	}
}


@Composable
private fun NetworkItemMultiselectAll(
	onClick: Callback,
) {
	Row(
		modifier = Modifier.clickable(onClick = onClick)
			.height(68.dp)
			.padding(horizontal = 16.dp)
			.fillMaxWidth(1f),
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = "Select All", //todo dmitry
			color = MaterialTheme.colors.primary,
			style = SignerTypeface.TitleS,
		)
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
private fun PreviewNewKeySetSelectNetwork() {
	val networks = listOf(
		NetworkModel(
			key = "0",
			logo = "polkadot",
			title = "Polkadot",
		),
		NetworkModel(
			key = "1",
			logo = "Kusama",
			title = "Kusama",
		),
		NetworkModel(
			key = "2",
			logo = "Wastend",
			title = "Wastend",
		),
	)
	val selected = remember<MutableState<Set<NetworkModel>>> {
		mutableStateOf(setOf(networks[1]))
	}
	SignerNewTheme {
		NewKeySetSelectNetworkScreen(networks, selected, {}, {}, {}, {})
	}
}
