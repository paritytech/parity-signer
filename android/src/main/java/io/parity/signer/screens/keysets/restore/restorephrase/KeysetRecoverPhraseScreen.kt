package io.parity.signer.screens.keysets.restore.restorephrase

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.parity.signer.R
import io.parity.signer.components.base.ScreenHeaderWithButton
import io.parity.signer.domain.Callback
import io.parity.signer.screens.keysets.restore.KeysetRecoverModel
import io.parity.signer.screens.keysets.restore.KeysetRecoverViewModel
import io.parity.signer.ui.theme.SignerNewTheme
import io.parity.signer.ui.theme.SignerTypeface
import kotlinx.coroutines.Dispatchers


@Composable
fun KeysetRecoverPhraseScreen(
	onContinue: Callback,
	onBack: Callback,
	initialRecoverSeedPhrase: KeysetRecoverModel,
) {
	val viewModel: KeysetRecoverViewModel = viewModel()
	//Dispatchers.Main.immediate because it used in TextField to workaround bug
	//https://issuetracker.google.com/issues/160257648
	val state = viewModel.recoverState.collectAsState(Dispatchers.Main.immediate)

//	todo dmitry pass this state above
	LaunchedEffect(key1 = Unit) {
		viewModel.initValue(initialRecoverSeedPhrase)
	}

	state.value?.let { state ->
		KeysetRecoverPhraseScreenView(
			model = state,
			backAction = {
				viewModel.resetState()
				onBack()
			},
			onNewInput = { newInput -> viewModel.onTextEntry(newInput) },
			onAddSuggestedWord = { suggestedWord -> viewModel.addWord(suggestedWord) },
			onDone = {
				state.readySeed?.let { seedFinal ->
					onContinue()
				}
			}
		)
	}
}


@Composable
private fun KeysetRecoverPhraseScreenView(
	model: KeysetRecoverModel,
	backAction: Callback,
	onNewInput: (input: String) -> Unit,
	onAddSuggestedWord: (input: String) -> Unit,
	onDone: Callback,
) {
	Column(
		Modifier
			.fillMaxSize(1f)
			.background(MaterialTheme.colors.background)
			.verticalScroll(rememberScrollState()),
	) {
		ScreenHeaderWithButton(
			canProceed = model.readySeed != null,
			title = stringResource(R.string.recovert_key_set_title),
			btnText = stringResource(R.string.button_next),
			onDone = onDone,
			onClose = backAction,
			backNotClose = true,
		)
		Text(
			text = stringResource(R.string.recover_key_set_subtitle),
			color = MaterialTheme.colors.primary,
			style = SignerTypeface.BodyL,
			modifier = Modifier
				.padding(horizontal = 24.dp)
				.padding(top = 8.dp, bottom = 2.dp),
		)
		EnterSeedPhraseBox(
			enteredWords = model.draft,
			userInput = model.userInput,
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.padding(top = 8.dp, bottom = 12.dp),
			onEnteredChange = onNewInput,
		)
		RestoreSeedPhraseSuggest(
			guessWord = model.suggestedWords,
			onClicked = onAddSuggestedWord,
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
private fun PreviewKeysetRecoverPhraseScreenView() {
	SignerNewTheme {
		KeysetRecoverPhraseScreenView(
			model = KeysetRecoverModel.stub(),
			backAction = {},
			onNewInput = { _ -> },
			onAddSuggestedWord = { _ -> },
			onDone = {}
		)
	}
}
