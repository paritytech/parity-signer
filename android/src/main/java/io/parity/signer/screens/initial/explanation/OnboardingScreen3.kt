package io.parity.signer.screens.initial.explanation

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.parity.signer.R
import io.parity.signer.components.base.PageIndicatorLine
import io.parity.signer.domain.Callback
import io.parity.signer.ui.theme.*


@Composable
internal fun OnboardingScreen3(onSkip: Callback) {
	OnboardingScreenGeneric(
		page = 3,
		showSkip = true,
		title = stringResource(R.string.onboarding_header_3),
		image = painterResource(id = R.drawable.onboarding_3),
		onSkip = onSkip,
	)
}


@Preview(
	name = "dark", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_YES,
	showBackground = true, backgroundColor = 0xFF000000,
)
@Composable
private fun PreviewOnboarding3Small() {
	SignerNewTheme {
		Box(modifier = Modifier.size(320.dp, 568.dp)) {
			OnboardingScreen3({})
		}
	}
}

@Preview(
	name = "dark", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_YES,
	showBackground = true, backgroundColor = 0xFF000000,
)
@Composable
private fun PreviewOnboarding3Big() {
	SignerNewTheme {
		Box(modifier = Modifier.fillMaxSize(1f)) {
			OnboardingScreen3({})
		}
	}
}

