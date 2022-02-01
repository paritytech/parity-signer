package io.parity.signer.screens

import android.content.Context
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import io.parity.signer.ButtonID
import io.parity.signer.components.SettingsCardTemplate
import io.parity.signer.models.SignerDataModel
import io.parity.signer.models.pushButton

/**
 * Settings screen; General purpose stuff like legal info, networks management
 * and history should be here. This is final point in navigation:
 * all subsequent interactions should be in modals or drop-down menus
 */
@Composable
fun SettingsScreen(signerDataModel: SignerDataModel) {

	Column {
		Row(Modifier.clickable { signerDataModel.pushButton(ButtonID.ManageNetworks) }) {
			SettingsCardTemplate(text = "Networks")
		}
		Row(Modifier.clickable { signerDataModel.pushButton(ButtonID.BackupSeed) }) {
			SettingsCardTemplate(text = "Backup keys")
		}
		Column(Modifier.clickable { signerDataModel.pushButton(ButtonID.ViewGeneralVerifier) }) {
			Row{
				Text("Verifier certificate")
				Spacer(Modifier.weight(1f))
			}
			//VerifierCard
		}
		Row(
			Modifier.clickable {
				signerDataModel.wipe()
				signerDataModel.totalRefresh()
			}
		) { SettingsCardTemplate(text = "Wipe signer", danger = true) }
		Row(Modifier.clickable { signerDataModel.pushButton(ButtonID.ShowDocuments) }) {
			SettingsCardTemplate(text = "About")
		}
		SettingsCardTemplate(
			"Hardware seed protection: " + signerDataModel.isStrongBoxProtected()
				.toString(), withIcon = false
		)
		SettingsCardTemplate("Version: " + signerDataModel.getAppVersion(), withIcon = false)
	}
}
