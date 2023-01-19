//
//  SeedPhraseView.swift
//  NativeSigner
//
//  Created by Krzysztof Rodak on 19/09/2022.
//

import SwiftUI

struct SeedPhraseElementModel: Equatable {
    let position: String
    let word: String
}

struct SeedPhraseViewModel: Equatable {
    let seeds: [SeedPhraseElementModel]

    init(seedPhrase: String) {
        seeds = seedPhrase.components(separatedBy: .whitespaces)
            .enumerated()
            .map { SeedPhraseElementModel(
                position: String($0.offset + 1),
                word: $0.element.trimmingCharacters(in: .whitespacesAndNewlines)
            )
            }
    }
}

/// Component to present seed phrase divided by words along with word position label
/// Layout is fixed to 3 columns
struct SeedPhraseView: View {
    private let viewModel: SeedPhraseViewModel
    private let columns = [
        GridItem(.flexible()),
        GridItem(.flexible()),
        GridItem(.flexible())
    ]
    private let reducedWidthColumn = [
        GridItem(.flexible()),
        GridItem(.flexible())
    ]

    init(
        viewModel: SeedPhraseViewModel
    ) {
        self.viewModel = viewModel
    }

    var body: some View {
        LazyVGrid(columns: layout(), alignment: .leading, spacing: 0) {
            ForEach(viewModel.seeds, id: \.position) { seedWord in
                HStack(alignment: .center, spacing: Spacing.extraExtraSmall) {
                    Text(seedWord.position)
                        .font(.robotoMonoRegular)
                        .foregroundColor(Asset.textAndIconsDisabled.swiftUIColor)
                        .frame(minWidth: Sizes.seedWordPositionWidth, alignment: .trailing)
                        .minimumScaleFactor(1)
                        .lineLimit(1)
                    Text(seedWord.word)
                        .font(.robotoMonoBold)
                        .foregroundColor(Asset.textAndIconsSecondary.swiftUIColor)
                        .minimumScaleFactor(1)
                        .lineLimit(1)
                }
                .frame(height: 24)
                .padding([.bottom, .top], Spacing.extraExtraSmall)
            }
        }
        .padding(Spacing.medium)
        .containerBackground(CornerRadius.small)
    }

    private func layout() -> [GridItem] {
        UIScreen.main.bounds.width == DeviceConstants.compactDeviceWidth ? reducedWidthColumn : columns
    }
}

#if DEBUG
    struct SeedPhraseView_Previews: PreviewProvider {
        static var previews: some View {
            VStack {
                Spacer()
                SeedPhraseView(
                    viewModel: PreviewData.seedPhraseViewModel
                )
                .padding(Spacing.medium)
                Spacer()
            }
            .background(Asset.backgroundSecondary.swiftUIColor)
            .frame(height: .infinity)
            .preferredColorScheme(.dark)
            .previewLayout(.sizeThatFits)
        }
    }
#endif
