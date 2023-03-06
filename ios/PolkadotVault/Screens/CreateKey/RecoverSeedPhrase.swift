//
//  RecoverSeedPhrase.swift
//  Polkadot Vault
//
//  Created by Alexander Slesarev on 8.12.2021.
//

import SwiftUI

struct RecoverSeedPhrase: View {
    @StateObject var viewModel: ViewModel
    @EnvironmentObject var navigation: NavigationCoordinator
    @FocusState private var focus: Bool

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            NavigationBarView(
                viewModel: .init(
                    title: Localizable.RecoverSeedPhrase.Label.title.string,
                    leftButtons: [
                        .init(
                            type: .arrow,
                            action: viewModel.onBackTap
                        )
                    ],
                    rightButtons: [.init(
                        type: .activeAction(
                            Localizable.RecoverSeedPhrase.Action.done.key,
                            .constant(viewModel.content.readySeed == nil)
                        ),
                        action: viewModel.onDoneTap
                    )]
                )
            )
            ScrollView(showsIndicators: false) {
                VStack(alignment: .leading, spacing: 0) {
                    Localizable.RecoverSeedPhrase.Label.header.text
                        .foregroundColor(Asset.textAndIconsPrimary.swiftUIColor)
                        .font(PrimaryFont.bodyL.font)
                        .padding(.leading, Spacing.large)
                        .padding(.top, Spacing.large)
                        .padding(.bottom, Spacing.small)
                    VStack(alignment: .leading, spacing: 0) {
                        WrappingHStack(models: viewModel.seedPhraseGrid) { gridElement in
                            switch gridElement {
                            case let .seedPhraseElement(element):
                                HStack(alignment: .center, spacing: Spacing.extraExtraSmall) {
                                    Text(element.position)
                                        .foregroundColor(Asset.textAndIconsDisabled.swiftUIColor)
                                        .frame(minWidth: Sizes.seedWordPositionWidth, alignment: .trailing)
                                        .lineLimit(1)
                                        .padding(.leading, Spacing.extraSmall)
                                    Text(element.word)
                                        .foregroundColor(Asset.textAndIconsPrimary.swiftUIColor)
                                        .lineLimit(1)
                                        .fixedSize(horizontal: false, vertical: true)
                                        .padding(.trailing, Spacing.small)
                                }
                                .font(PrimaryFont.labelS.font)
                                .frame(height: Heights.seedPhraseCapsuleHeight)
                                .containerBackground()
                                .padding(.bottom, Spacing.extraExtraSmall)
                                .padding(.trailing, Spacing.extraExtraSmall)
                            case .input:
                                TextField(
                                    "",
                                    text: $viewModel.userInput
                                )
                                .focused($focus)
                                .inlineTextFieldStyle(text: $viewModel.userInput)
                                .onChange(of: viewModel.userInput, perform: { word in
                                    viewModel.onUserInput(word)
                                })
                                .frame(minWidth: 50, maxWidth: 100)
                                .onAppear {
                                    focus = true
                                }
                                .padding(.bottom, Spacing.extraExtraSmall)
                                .padding(.trailing, Spacing.extraExtraSmall)
                            }
                        }
                        .padding(.leading, Spacing.extraSmall)
                        .padding(.top, Spacing.extraSmall)
                        .padding(.trailing, Spacing.extraExtraSmall)
                        .padding(.bottom, Spacing.extraExtraSmall)
                        Spacer()
                    }
                    .frame(minHeight: 156)
                    .containerBackground(CornerRadius.small)
                    .padding(.horizontal, Spacing.large)
                    .padding(.bottom, Spacing.small)
                    ScrollView(.horizontal, showsIndicators: false) {
                        LazyHStack(alignment: .top, spacing: 0) {
                            Spacer()
                                .frame(width: Spacing.large, height: Spacing.large)
                            ForEach(viewModel.content.guessSet, id: \.self) { guess in
                                Text(guess)
                                    .foregroundColor(Asset.accentPink300.swiftUIColor)
                                    .font(PrimaryFont.labelS.font)
                                    .padding([.top, .bottom], Spacing.extraSmall)
                                    .padding([.leading, .trailing], Spacing.small)
                                    .background(Asset.accentPink12.swiftUIColor)
                                    .clipShape(Capsule())
                                    .onTapGesture {
                                        viewModel.onGuessTap(guess)
                                    }
                                    .padding(.trailing, Spacing.extraExtraSmall)
                            }
                            Spacer()
                                .frame(width: Spacing.large - Spacing.extraExtraSmall, height: Spacing.large)
                        }
                    }
                    .frame(height: 36)
                }
            }
            .onAppear {
                viewModel.use(navigation: navigation)
                focus = true
            }
        }
    }
}

extension RecoverSeedPhrase {
    struct SeedPhraseElement: Equatable, Identifiable, Hashable {
        let id = UUID()
        let position: String
        let word: String
    }

    struct TextInput: Equatable, Identifiable, Hashable {
        let id = UUID()
    }

    enum GridElement: Identifiable, Hashable {
        case seedPhraseElement(SeedPhraseElement)
        case input(TextInput)

        var id: UUID {
            switch self {
            case let .seedPhraseElement(element):
                return element.id
            case let .input(input):
                return input.id
            }
        }
    }
}

extension RecoverSeedPhrase {
    final class ViewModel: ObservableObject {
        @Published var seedPhraseGrid: [GridElement] = []
        var content: MRecoverSeedPhrase {
            didSet {
                regenerateGrid()
            }
        }

        private let seedsMediator: SeedsMediating
        private let textInput = TextInput()
        private var shouldSkipUpdate = false
        @Published var userInput: String = " "
        @Published var previousUserInput: String = " "

        weak var navigation: NavigationCoordinator!

        init(
            content: MRecoverSeedPhrase,
            seedsMediator: SeedsMediating = ServiceLocator.seedsMediator
        ) {
            self.content = content
            self.seedsMediator = seedsMediator
            regenerateGrid()
        }

        func use(navigation: NavigationCoordinator) {
            self.navigation = navigation
        }

        func onBackTap() {
            navigation.perform(navigation: .init(action: .goBack))
        }

        func onGuessTap(_ guess: String) {
            let result = navigation.performFake(navigation: .init(action: .pushWord, details: guess))
            guard case let .recoverSeedPhrase(updatedContent) = result.screenData else { return }
            content = updatedContent
            userInput = " "
        }

        func onUserInput(_ word: String) {
            guard !shouldSkipUpdate else { return }
            shouldSkipUpdate = true
            let wordToSend = word.isEmpty && !previousUserInput.isEmpty ? " " : word
            let result = navigation.performFake(navigation: .init(action: .textEntry, details: wordToSend))
            guard case let .recoverSeedPhrase(updatedContent) = result.screenData else { return }
            content = updatedContent
            if content.userInput != userInput {
                userInput = content.userInput
            }
            previousUserInput = userInput
            if userInput.isEmpty, content.userInput.isEmpty {
                userInput = " "
            }
            shouldSkipUpdate = false
        }

        func onDoneTap() {
            seedsMediator.restoreSeed(
                seedName: content.seedName,
                seedPhrase: content.readySeed ?? "",
                navigate: true
            )
        }

        private func regenerateGrid() {
            var updatedGrid: [GridElement] = content.draft.enumerated()
                .map { .seedPhraseElement(.init(position: String($0.offset + 1), word: $0.element)) }
            if updatedGrid.count < 24 {
                updatedGrid.append(.input(textInput))
            }
            seedPhraseGrid = updatedGrid
        }
    }
}

private extension MRecoverSeedPhrase {
    func draftPhrase() -> String {
        draft.joined(separator: " ")
    }
}
