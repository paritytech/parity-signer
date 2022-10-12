package io.parity.signer.screens

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.parity.signer.R
import io.parity.signer.components.items.KeySetItem
import io.parity.signer.components.base.PrimaryButtonBottomSheet
import io.parity.signer.components.panels.BottomBar2
import io.parity.signer.components.panels.BottomBar2State
import io.parity.signer.models.EmptyNavigator
import io.parity.signer.models.Navigator
import io.parity.signer.ui.helpers.PreviewData
import io.parity.signer.ui.theme.SignerNewTheme
import io.parity.signer.ui.theme.TypefaceNew
import io.parity.signer.uniffi.*

/**
 * Default main screen with list Seeds/root keys
 */
@Composable
fun KeySetsScreen(
	model: KeySetsSelectViewModel,
	navigator: Navigator,
) {
	Column(Modifier.background(MaterialTheme.colors.background)) {
		ScreenHeaderTitle(R.string.key_sets_screem_title)
		LazyColumn(
			contentPadding = PaddingValues(horizontal = 12.dp),
			verticalArrangement = Arrangement.spacedBy(10.dp),
			modifier = Modifier.weight(1f),
		) {
			val cards = model.keys
			items(cards.size) { i ->
				KeySetItem(model = cards[i]) {
					navigator.navigate(Action.SELECT_SEED, cards[i].seedName)
				}
			}
		}
		PrimaryButtonBottomSheet(
			label = stringResource(R.string.key_sets_screem_add_key_button),
			modifier = Modifier.padding(24.dp),
		) {
			navigator.navigate(Action.RIGHT_BUTTON_ACTION) //new seed for this state
		}
		BottomBar2(navigator, BottomBar2State.KEYS)
	}
}

@Composable
private fun ScreenHeaderTitle(@StringRes stringId: Int) {
	Box(
		modifier = Modifier
			.fillMaxWidth(1f)
			.defaultMinSize(minHeight = 56.dp)
	) {
		Text(
			text = stringResource(stringId),
			color = MaterialTheme.colors.primary,
			style = TypefaceNew.TitleS,
			textAlign = TextAlign.Center,
			modifier = Modifier.align(Alignment.Center)
		)
	}
}


/**
 * Local copy of shared [MSeeds] class
 */
data class KeySetsSelectViewModel(val keys: List<KeySetViewModel>)

fun MSeeds.toKeySetsSelectViewModel() = KeySetsSelectViewModel(
	seedNameCards.map { it.toSeedViewModel() }
)

/**
 * Local copy of shared [SeedNameCard] class
 */
data class KeySetViewModel(
	val seedName: String,
	val identicon: List<UByte>,
	val derivedKeysCount: UInt
)

fun SeedNameCard.toSeedViewModel() =
	KeySetViewModel(seedName, identicon, derivedKeysCount)


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
private fun PreviewKeySetsSelectScreen() {
	val keys = mutableListOf(
		KeySetViewModel(
			"first seed name",
			PreviewData.exampleIdenticon,
			1.toUInt()
		),
		KeySetViewModel(
			"second seed name",
			PreviewData.exampleIdenticon,
			3.toUInt()
		),
	)
	repeat(30) {
		keys.add(
			KeySetViewModel(
				"second seed name",
				PreviewData.exampleIdenticon,
				3.toUInt()
			)
		)
	}
	val mockModel = KeySetsSelectViewModel(keys)
	SignerNewTheme {
		Box(modifier = Modifier.size(350.dp, 550.dp)) {
			KeySetsScreen(mockModel, EmptyNavigator())
		}
	}
}
