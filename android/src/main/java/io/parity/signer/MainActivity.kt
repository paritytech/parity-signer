package io.parity.signer

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.parity.signer.components.BigButton
import io.parity.signer.components.BottomBar
import io.parity.signer.components.TopBar
import io.parity.signer.models.AlertState
import io.parity.signer.models.SignerDataModel
import io.parity.signer.models.navigate
import io.parity.signer.screens.LandingView
import io.parity.signer.screens.WaitingScreen
import io.parity.signer.ui.theme.SignerOldTheme
import io.parity.signer.ui.theme.Text600
import io.parity.signer.uniffi.ScreenData
import io.parity.signer.uniffi.initLogging

@ExperimentalMaterialApi
@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {
	init {
		initLogging("SIGNER_RUST_LOG")
	}

	// rust library is initialized inside data model
	private val signerDataModel by viewModels<SignerDataModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		signerDataModel.context = applicationContext
		signerDataModel.activity = this

		signerDataModel.lateInit()

		setContent {
			SignerApp(signerDataModel)
		}
	}
}

/**
 * Main app component - hosts navhost, Rust-based source of truth, etc.
 */
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun SignerApp(signerDataModel: SignerDataModel) {
	SignerOldTheme {
		val onBoardingDone = signerDataModel.onBoardingDone.observeAsState()
		val authenticated = signerDataModel.authenticated.observeAsState()
		val actionResult = signerDataModel.actionResult.observeAsState()
		val shieldAlert = signerDataModel.alertState.observeAsState()
		val progress = signerDataModel.progress.observeAsState()
		val captured = signerDataModel.captured.observeAsState()
		val total = signerDataModel.total.observeAsState()
		val localNavAction = signerDataModel.localNavAction.observeAsState()

		when (onBoardingDone.value) {
			OnBoardingState.Yes -> {

				if (authenticated.value == true) {
					BackHandler {
						signerDataModel.navigator.backAction()
					}
					// Structure to contain all app
					Scaffold(
						topBar = {
							TopBar(
								signerDataModel = signerDataModel,
								alertState = shieldAlert
							)
						},
						bottomBar = {
							if (actionResult.value?.footer == true) BottomBar(
								signerDataModel = signerDataModel
							)
						}
					) { innerPadding ->
						Box(modifier = Modifier.padding(innerPadding)) {
//								if (signerDataModel.localNavAction.value == LocalNavAction.None) {
							ScreenSelector(
								screenData = actionResult.value?.screenData
									?: ScreenData.Documents,//default fallback
								alertState = shieldAlert,
								progress = progress,
								captured = captured,
								total = total,
								button = signerDataModel::navigate,
								signerDataModel = signerDataModel
							)
							ModalSelector(
								modalData = actionResult.value?.modalData,
								alertState = shieldAlert,
								button = signerDataModel::navigate,
								signerDataModel = signerDataModel,
							)
							AlertSelector(
								alert = actionResult.value?.alertData,
								alertState = shieldAlert,
								button = signerDataModel::navigate,
								acknowledgeWarning = signerDataModel::acknowledgeWarning
							)
//								} else {
//									LocalNavSelectorFullScreen(
//										signerDataModel,
//										navAction = localNavAction.value
//									)
//								}
						}
					}
				} else {
					Column(verticalArrangement = Arrangement.Center) {
						Spacer(Modifier.weight(0.5f))
						BigButton(
							text = "Unlock app",
							action = {
								signerDataModel.authentication.authenticate(signerDataModel.activity) {
									signerDataModel.totalRefresh()
								}
							}
						)
						Spacer(Modifier.weight(0.5f))
					}
				}
				//todo dmitry put in the middle for bottom sheet
				BottomSheetSelector(
					signerDataModel = signerDataModel,
					navAction = localNavAction.value,
				)
			}
			OnBoardingState.No -> {
				if (shieldAlert.value == AlertState.None) {
					Scaffold { padding ->
						LandingView(
							signerDataModel::onBoard,
							modifier = Modifier.padding(padding)
						)
					}
				} else {
					Box(
						contentAlignment = Alignment.Center,
						modifier = Modifier.padding(12.dp)
					) {
						Text(
							"Please enable airplane mode",
							color = MaterialTheme.colors.Text600
						)
					}
				}
			}
			OnBoardingState.InProgress -> {
				if (authenticated.value == true) {
					WaitingScreen()
				} else {
					Column(verticalArrangement = Arrangement.Center) {
						Spacer(Modifier.weight(0.5f))
						BigButton(
							text = "Unlock app",
							action = {
								signerDataModel.lateInit()
							}
						)
						Spacer(Modifier.weight(0.5f))
					}
				}
			}
			null -> WaitingScreen()
		}
	}
}
