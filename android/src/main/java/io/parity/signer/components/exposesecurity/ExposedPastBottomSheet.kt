package io.parity.signer.components.exposesecurity

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.parity.signer.R
import io.parity.signer.components.base.PrimaryButtonBottomSheet
import io.parity.signer.components.base.SecondaryButtonBottomSheet
import io.parity.signer.models.Callback
import io.parity.signer.models.EmptyNavigator
import io.parity.signer.models.Navigator
import io.parity.signer.ui.theme.*
import io.parity.signer.uniffi.Action

@Composable
fun ExposedPastBottomSheet(
	navigator: Navigator,
	acknowledgeWarning: Callback
) {
	Column(
		modifier = Modifier
			.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {

		Image(
			painter = painterResource(id = R.drawable.ic_wifi_32),
			contentDescription = stringResource(R.string.description_shield_exposed_icon),
			colorFilter = ColorFilter.tint(MaterialTheme.colors.red400),
			modifier = Modifier
				.padding(vertical = 24.dp)
				.size(80.dp),
		)

		Text(
			text = stringResource(R.string.exposed_past_title),
			color = MaterialTheme.colors.primary,
			style = SignerTypeface.TitleL,
			modifier = Modifier
				.fillMaxWidth(1f)
				.padding(horizontal = 24.dp),
		)
		Spacer(modifier = Modifier.padding(top = 16.dp))
		Text(
			text = stringResource(R.string.exposed_past_message),
			color = MaterialTheme.colors.textSecondary,
			style = SignerTypeface.BodyL,
			modifier = Modifier
				.fillMaxWidth(1f)
				.padding(horizontal = 24.dp),

			)
		Spacer(modifier = Modifier.padding(top = 24.dp))
		PrimaryButtonBottomSheet(
			label = stringResource(R.string.exposed_before_cancel_button),
			modifier = Modifier.padding(horizontal = 32.dp),
		) {
			navigator.navigate(Action.GO_BACK)
		}
		Spacer(modifier = Modifier.padding(top = 8.dp))
		SecondaryButtonBottomSheet(
			label = stringResource(R.string.exposed_before_proceed_button),
			modifier = Modifier.padding(horizontal = 32.dp),
		) {
				acknowledgeWarning()
			navigator.navigate(Action.GO_BACK)
		}
		Spacer(modifier = Modifier.padding(top = 24.dp))
	}
}


@Preview(
	name = "light", group = "themes", uiMode = UI_MODE_NIGHT_NO,
	showBackground = true, backgroundColor = 0xFFFFFFFF,
)
@Preview(
	name = "dark", group = "themes", uiMode = UI_MODE_NIGHT_YES,
	showBackground = true, backgroundColor = 0xFF000000,
)
@Composable
private fun PreviewExposedBeforeBottomSheet() {
	SignerNewTheme {
		ExposedPastBottomSheet(
			EmptyNavigator(), {},
		)
	}
}

