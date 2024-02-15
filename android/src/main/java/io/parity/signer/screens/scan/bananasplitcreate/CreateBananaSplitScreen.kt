package io.parity.signer.screens.scan.bananasplitcreate

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.parity.signer.domain.Callback
import io.parity.signer.ui.theme.SignerNewTheme


class  {
}
@Composable
fun CreateBananaSplitScreen(
	onClose: Callback,
	onCreate: Callback,
	modifier: Modifier = Modifier,
) {

}


@SuppressLint("UnrememberedMutableState")
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
private fun PreviewCreateBananaSplitScreen() {
	SignerNewTheme {
		CreateBananaSplitScreen(
			onClose = {},
			onCreate = {},
		)
	}
}
