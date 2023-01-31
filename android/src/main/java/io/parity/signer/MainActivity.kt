package io.parity.signer

import android.graphics.Color
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import io.parity.signer.dependencygraph.ServiceLocator
import io.parity.signer.domain.MainFlowViewModel
import io.parity.signer.ui.SignerNavHost
import io.parity.signer.ui.theme.SignerNewTheme

@ExperimentalMaterialApi
@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {

	// rust library is initialized inside data model
	private val mainFlowViewModel by viewModels<MainFlowViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		ServiceLocator.initActivityDependencies(this)

		if (savedInstanceState == null) {
			mainFlowViewModel.lateInit() //todo onboarding init only when main flow started
		}

		//remove automatic insets so bottom sheet can dimm status bar, other views will add their paddings if needed.
		WindowCompat.setDecorFitsSystemWindows(window, false)
		window.statusBarColor = Color.TRANSPARENT;

		setContent {
			SignerNewTheme {
				SignerNavHost(navController = rememberNavController())
			}
		}
	}

	override fun onDestroy() {
		ServiceLocator.deinitActivityDependencies()
		super.onDestroy()
	}
}



