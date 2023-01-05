package io.parity.signer.screens.keyderivation

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.parity.signer.R
import io.parity.signer.components.base.ScreenHeaderClose
import io.parity.signer.models.Callback
import io.parity.signer.ui.theme.*


@Composable
fun DeriveKeyBaseScreen(onClose: Callback) {
	val onNetworkSelect = {}
	val onDerivationHelpClicked = {}
	val onPathClicked = {}
	val path = remember {
		mutableStateOf<String>("")
	}

	Column() {
		ScreenHeaderClose(
			title = "Create Derived Key",
			onClose = { /*TODO*/ },
			onMenu = {},
			differentMenuIcon = Icons.Filled.HelpOutline
		)

		Text(
			text = "Choose Network",
			color = MaterialTheme.colors.primary,
			style = SignerTypeface.TitleS,
		)
		Row(
			modifier = Modifier
				.fillMaxWidth(1f)
				.clickable(onClick = onNetworkSelect)
				.background(
					MaterialTheme.colors.fill6,
					RoundedCornerShape(dimensionResource(id = R.dimen.innerFramesCornerRadius))
				)
				.padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 4.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = "Network",
				color = MaterialTheme.colors.primary,
				style = SignerTypeface.TitleS,
			)
			Spacer(modifier = Modifier.weight(1f))
			NetworkLabel(networkName = "Polkadot") //todo pass
			ChevronRight()
		}

		Row {
			Text(
				text = "Derivation Path ",
				color = MaterialTheme.colors.primary,
				style = SignerTypeface.TitleS,
			)
			Image(
				imageVector = Icons.Filled.Help,
				contentDescription = "Derivation help clicked",
				colorFilter = ColorFilter.tint(MaterialTheme.colors.pink300),
				modifier = Modifier
					.padding(horizontal = 8.dp)
					.clickable(onClick = onDerivationHelpClicked)
					.size(18.dp)
					.align(Alignment.CenterVertically)
			)
		}
		Row(
			modifier = Modifier
				.fillMaxWidth(1f)
				.clickable(onClick = onPathClicked)
				.background(
					MaterialTheme.colors.fill6,
					RoundedCornerShape(dimensionResource(id = R.dimen.innerFramesCornerRadius))
				)
				.padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 4.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = path.value.ifEmpty { "// Add Path Name" },
				color = if (path.value.isEmpty()) {
					MaterialTheme.colors.textTertiary
				} else {
					MaterialTheme.colors.primary
				},
				style = SignerTypeface.TitleS,
			)
			Spacer(modifier = Modifier.weight(1f))
			ChevronRight()
		}
		Text(
			text = "Keep the path name written legebly and stored securely. Derived key can be backed up only by entering derivation path.",
			color = MaterialTheme.colors.textTertiary,
			style = SignerTypeface.CaptionM,
		)
	}
}

@Composable
private fun NetworkLabel(networkName: String) {
	Box(
		modifier = Modifier
			.background(
				MaterialTheme.colors.fill12,
				RoundedCornerShape(12.dp)
			)
			.padding(horizontal = 8.dp, vertical = 2.dp),
		contentAlignment = Alignment.Center,
	) {
		Text(
			networkName,
			color = MaterialTheme.colors.textSecondary,
			style = SignerTypeface.BodyM,
		)
	}
}

@Composable
private fun ChevronRight() {
	Image(
		imageVector = Icons.Filled.ChevronRight,
		contentDescription = null,
		colorFilter = ColorFilter.tint(MaterialTheme.colors.textDisabled),
		modifier = Modifier
			.size(28.dp)
	)
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
private fun PreviewDeriveKeyBaseScreen() {
	SignerNewTheme {
		DeriveKeyBaseScreen({})
	}
}
