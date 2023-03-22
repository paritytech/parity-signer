//
//  AuthenticatedScreenContainer.swift
//  Polkadot Vault
//
//  Created by Krzysztof Rodak on 05/08/2022.
//

import SwiftUI

struct AuthenticatedScreenContainer: View {
    @EnvironmentObject private var connectivityMediator: ConnectivityMediator
    @EnvironmentObject private var navigation: NavigationCoordinator
    @StateObject var viewModel: ViewModel
    @StateObject var snackBarPresentation = ServiceLocator.bottomSnackbarPresentation
    @GestureState private var dragOffset = CGSize.zero
    @State private var isShowingQRScanner: Bool = false

    var body: some View {
        VStack(spacing: 0) {
            ZStack {
                VStack(spacing: 0) {
                    viewModel.mainScreenFactory.screen(for: navigation.actionResult.screenData)
                }
                viewModel.modalFactory.modal(for: navigation.actionResult.modalData)
            }
            .gesture(
                DragGesture().updating($dragOffset, body: { value, _, _ in
                    if value.startLocation.x < 20, value.translation.width > 100, !navigation.disableSwipeToBack {
                        navigation.perform(navigation: .init(action: .goBack))
                    }
                })
            )
            if navigation.actionResult.footer, navigation.selectedTab != .keys {
                TabBarView(
                    selectedTab: $navigation.selectedTab
                )
            }
        }
        .bottomEdgeOverlay(
            overlayView: CameraView(
                viewModel: .init(
                    isPresented: $navigation.shouldPresentQRScanner
                )
            ),
            isPresented: $isShowingQRScanner
        )
        .onReceive(navigation.$shouldPresentQRScanner) { shouldPresent in
            withAnimation {
                if shouldPresent {
                    // Pretend to go to QR Scanner tab, to be able to display transaction later
                    navigation.performFake(navigation: .init(action: .navbarScan))
                    isShowingQRScanner = true
                } else {
                    if let overrideNavigation = navigation.overrideQRScannerDismissalNavigation {
                        // Override default tab navigation when we want to end on specific screen after camera
                        // dismissal
                        navigation.perform(navigation: overrideNavigation)
                        navigation.overrideQRScannerDismissalNavigation = nil
                    } else if let action = navigation.selectedTab.action {
                        // "Pretend" to go back to previous tab (as we don't change `selectedTab` when showing QR screen
                        // now), but do
                        // it for real before dismissing QR code scanner to have dataset to display as we might have
                        // ventured
                        // into transaction details
                        navigation.perform(navigation: .init(action: action))
                    }
                    isShowingQRScanner = false
                }
            }
        }
        .gesture(
            DragGesture().onEnded { drag in
                if drag.translation.width < -20 {
                    navigation.perform(navigation: .init(action: .goBack))
                }
            }
        )
        .environmentObject(snackBarPresentation)
        .bottomSnackbar(snackBarPresentation.viewModel, isPresented: $snackBarPresentation.isSnackbarPresented)
        .fullScreenCover(
            isPresented: $navigation.genericError.isPresented
        ) {
            ErrorBottomModal(
                viewModel: .alertError(message: navigation.genericError.errorMessage),
                dismissAction: navigation.perform(navigation: .init(action: .goBack)),
                isShowingBottomAlert: $navigation.genericError.isPresented
            )
            .clearModalBackground()
        }
    }
}

extension AuthenticatedScreenContainer {
    final class ViewModel: ObservableObject {
        private let navigation: NavigationCoordinator
        let modalFactory: ModalFactory
        let mainScreenFactory: MainScreensFactory

        init(
            navigation: NavigationCoordinator = NavigationCoordinator(),
            modalFactory: ModalFactory = ModalFactory(),
            mainScreenFactory: MainScreensFactory = MainScreensFactory()
        ) {
            self.navigation = navigation
            self.modalFactory = modalFactory
            self.mainScreenFactory = mainScreenFactory
            navigation.perform(navigation: .init(action: .start))
        }
    }
}
