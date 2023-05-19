package io.parity.signer.screens.keysets.restore.restorephrase

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.parity.signer.R
import io.parity.signer.components.base.ScreenHeaderWithButton
import io.parity.signer.domain.Callback
import io.parity.signer.screens.keysets.restore.KeysetRecoverModel
import io.parity.signer.ui.theme.SignerNewTheme
import io.parity.signer.ui.theme.SignerTypeface

@Composable
fun KeysetRecoverPhraseScreenView(
	model: KeysetRecoverModel,
	backAction: Callback,
	onNewInput: (input: String) -> Unit,
	onAddSuggestedWord: (input: String) -> Unit,
//	addSeed: ( //todo check old
//		seedName: String,
//		seedPhrase: String,
//	) -> Unit,
) {

	Column(
        Modifier
            .fillMaxSize(1f)
            .background(MaterialTheme.colors.background),
	) {

		ScreenHeaderWithButton(
			canProceed = model.readySeed != null,
			title = stringResource(R.string.recovert_key_set_title),
			btnText = stringResource(R.string.generic_done),
			onDone = {
//				posted.value = true
//				onDone(note.value)
			},
			onClose = backAction,
			backNotClose = true,
		)
		Text(
			text = "Secret Recovery Phrase",//todo dmitry fix
			color = MaterialTheme.colors.primary,
			style = SignerTypeface.BodyL,
			modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 8.dp, bottom = 20.dp),
		)

		EnterSeedPhraseBox(
			enteredWords = model.draft,
			progressWord = model.userInput,
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
			KeysetRecoverModel.stub(),
			{},
			{ _ -> },
			{ _ -> })
	}
}
