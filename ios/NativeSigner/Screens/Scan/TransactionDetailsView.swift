//
//  TransactionDetailsView.swift
//  NativeSigner
//
//  Created by Krzysztof Rodak on 20/11/2022.
//

import SwiftUI

struct TransactionDetailsView: View {
    @StateObject var viewModel: ViewModel
    @EnvironmentObject private var navigation: NavigationCoordinator
    @EnvironmentObject private var data: SignerDataModel

    var body: some View {
        VStack {
            NavigationBarView(
                viewModel: .init(title: Localizable.TransactionPreview.Label.title.string, leftButton: .xmark),
                actionModel: .init(
                    leftBarMenuAction: { viewModel.onBackButtonTap() },
                    rightBarMenuAction: {}
                )
            )
            ScrollView {
                VStack {
                    ForEach(viewModel.transaction.content.content.asSortedCards(), id: \.index) { card in
                        TransactionCardView(card: card)
                    }
                }
                .padding(.horizontal, Spacing.large)
            }
            .padding(.top, Spacing.medium)
        }
        .onAppear {
            viewModel.use(navigation: navigation)
        }
        .background(Asset.backgroundPrimary.swiftUIColor)
    }
}

extension TransactionDetailsView {
    final class ViewModel: ObservableObject {
        @Binding var isPresented: Bool
        private weak var navigation: NavigationCoordinator!

        let transaction: TransactionWrapper

        init(
            isPresented: Binding<Bool>,
            transaction: TransactionWrapper
        ) {
            _isPresented = isPresented
            self.transaction = transaction
        }

        func use(navigation: NavigationCoordinator) {
            self.navigation = navigation
        }

        func onBackButtonTap() {
            isPresented.toggle()
        }
    }
}

// struct TransactionPreview_Previews: PreviewProvider {
// static var previews: some View {
//     TransactionDetailsView(viewModel: .init(isPresented: Binding<Bool>.constant(true), transaction: .init(content: MTransaction(content: .init(author: nil, error: nil, extensions: <#T##[TransactionCard]?#>, importingDerivations: <#T##[TransactionCard]?#>, message: <#T##[TransactionCard]?#>, meta: <#T##[TransactionCard]?#>, method: [.init(index: 1, indent: 1, card:.met)], newSpecs: <#T##[TransactionCard]?#>, verifier: <#T##[TransactionCard]?#>, warning: <#T##[TransactionCard]?#>, typesInfo: <#T##[TransactionCard]?#>), ttype: .sign, authorInfo: nil, networkInfo: nil))))
// }
// }
