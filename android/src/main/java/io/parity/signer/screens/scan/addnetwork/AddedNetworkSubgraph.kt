package io.parity.signer.screens.scan.addnetwork

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.parity.signer.domain.NetworkModel


@Composable
fun AddedNetworkSubgraph(networkAdded: NetworkModel) {
	val viewModel: AddedNetworkViewModel = viewModel()

	Box(
		modifier = Modifier
			.fillMaxSize(1f)
			.statusBarsPadding()
			.background(MaterialTheme.colors.background)
	)

	val navController = rememberNavController()
	NavHost(
		navController = navController,
		startDestination = AddedNetworkNavigationSubgraph.AddedNetworkNavigationQuestion,
	) {
		composable(AddedNetworkNavigationSubgraph.AddedNetworkNavigationQuestion) {

		}
		composable(AddedNetworkNavigationSubgraph.AddedNetworkNavigationAllKeysets) {

		}
	}
}

private object AddedNetworkNavigationSubgraph {
	const val AddedNetworkNavigationQuestion = "added_network_question"
	const val AddedNetworkNavigationAllKeysets = "added_network_all_networks"
}
