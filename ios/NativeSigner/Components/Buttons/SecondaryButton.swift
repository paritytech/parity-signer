//
//  SecondaryButton.swift
//  NativeSigner
//
//  Created by Krzysztof Rodak on 19/08/2022.
//

import SwiftUI

struct SecondaryButton: View {
    var action: () -> Void
    var text: LocalizedStringKey
    @State var isDisabled: Bool = false

    var body: some View {
        ActionButton(
            action: action,
            text: text,
            style: ActionButtonStyle(
                backgroundColor: Asset.fill18.swiftUIColor,
                foregroundColor: (isDisabled ? Asset.textAndIconsDisabled : Asset.textAndIconsPrimary).swiftUIColor
            ),
            isDisabled: isDisabled
        )
    }
}

struct SecondaryButton_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            Text("<< Enabled >>")
            SecondaryButton(
                action: {},
                text: "Short Title"
            )
            SecondaryButton(
                action: {},
                text: "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
            )
            Text("<< Disabled >>")
            SecondaryButton(
                action: {},
                text: "Short Title",
                isDisabled: true
            )
            SecondaryButton(
                action: {},
                text: "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                isDisabled: true
            )
        }
        .preferredColorScheme(.dark)
        .previewLayout(.sizeThatFits)
    }
}
