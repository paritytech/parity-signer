//
//  KeyDetailsView.swift
//  NativeSigner
//
//  Created by Krzysztof Rodak on 29/08/2022.
//

import SwiftUI

struct KeyDetailsView: View {
    @State private var shouldPresentRemoveConfirmationModal = false
    @State private var shouldPresentBackupModal = false
    @State private var isShowingActionSheet = false
    @State private var isShowingRemoveConfirmation: Bool = false
    @State private var isShowingBackupModal: Bool = false

    @ObservedObject private var navigation: NavigationCoordinator

    private let alertClosure: (() -> Void)?
    private let viewModel: KeyDetailsViewModel
    private let actionModel: KeyDetailsActionModel
    private let forgetKeyActionHandler: ForgetKeySetAction
    private let exportPrivateKeyService: PrivateKeyQRCodeService

    init(
        navigation: NavigationCoordinator,
        forgetKeyActionHandler: ForgetKeySetAction,
        viewModel: KeyDetailsViewModel,
        actionModel: KeyDetailsActionModel,
        exportPrivateKeyService: PrivateKeyQRCodeService,
        alertClosure: (() -> Void)? = nil
    ) {
        self.navigation = navigation
        self.forgetKeyActionHandler = forgetKeyActionHandler
        self.viewModel = viewModel
        self.actionModel = actionModel
        self.exportPrivateKeyService = exportPrivateKeyService
        self.alertClosure = alertClosure
    }

    var body: some View {
        ZStack(alignment: .bottom) {
            VStack(spacing: 0) {
                // Navigation bar
                NavigationBarView(
                    navigation: navigation,
                    viewModel: .init(
                        leftButton: .arrow,
                        rightButton: .more
                    ),
                    actionModel: .init(rightBarMenuAction: {
                        isShowingActionSheet.toggle()
                    })
                )
                // Main key cell
                HStack {
                    VStack(alignment: .leading, spacing: Spacing.extraExtraSmall) {
                        Text(viewModel.keySummary.keyName)
                            .foregroundColor(Asset.textAndIconsPrimary.swiftUIColor)
                            .font(Fontstyle.titleL.base)
                        Text(viewModel.keySummary.base58)
                            .foregroundColor(Asset.textAndIconsTertiary.swiftUIColor)
                            .font(Fontstyle.bodyM.base)
                            .lineLimit(1)
                    }
                    Spacer().frame(maxWidth: .infinity)
                    Asset.chevronRight.swiftUIImage
                        .foregroundColor(Asset.textAndIconsSecondary.swiftUIColor)
                }
                .padding(Padding.detailsCell)
                .contentShape(Rectangle())
                .onTapGesture {
                    navigation.perform(navigation: actionModel.addressKeyNavigation)
                }
                // Header
                HStack {
                    Localizable.KeyDetails.Label.derived.text
                        .font(Fontstyle.bodyM.base)
                    Spacer().frame(maxWidth: .infinity)
                    Button(
                        action: {
                            navigation.perform(navigation: .init(action: .networkSelector))
                        }, label: {
                            Asset.switches.swiftUIImage
                        }
                    )
                }
                .foregroundColor(Asset.textAndIconsTertiary.swiftUIColor)
                .padding(Padding.detailsCell)
                // List of derived keys
                List {
                    ForEach(
                        viewModel.derivedKeys,
                        id: \.viewModel.path
                    ) { deriveKey in
                        DerivedKeyRow(deriveKey.viewModel)
                            .listRowBackground(Asset.backgroundSystem.swiftUIColor)
                            .listRowSeparator(.hidden)
                            .listRowInsets(EdgeInsets())
                            .contentShape(Rectangle())
                            .onTapGesture {
                                navigation.perform(navigation: deriveKey.actionModel.tapAction)
                            }
                    }
                    Spacer()
                        .listRowBackground(Asset.backgroundSystem.swiftUIColor)
                        .listRowSeparator(.hidden)
                        .frame(height: Heights.actionButton + Spacing.large)
                }
                .listStyle(.plain)
                .hiddenScrollContent()
            }
            .background(Asset.backgroundSystem.swiftUIColor)
            // Main CTA
            PrimaryButton(
                action: {
                    if let alertClosure = actionModel.alertClosure {
                        alertClosure()
                    } else {
                        navigation.perform(navigation: actionModel.createDerivedKey)
                    }
                },
                text: Localizable.KeyDetails.Action.create.key
            )
            .padding(Spacing.large)
        }
        .fullScreenCover(
            isPresented: $isShowingActionSheet,
            onDismiss: {
                if shouldPresentRemoveConfirmationModal == true {
                    shouldPresentRemoveConfirmationModal.toggle()
                    isShowingRemoveConfirmation.toggle()
                }
                if shouldPresentBackupModal == true {
                    shouldPresentBackupModal.toggle()
                    isShowingBackupModal.toggle()
                }
            }
        ) {
            KeyDetailsActionsModal(
                isShowingActionSheet: $isShowingActionSheet,
                shouldPresentRemoveConfirmationModal: $shouldPresentRemoveConfirmationModal,
                shouldPresentBackupModal: $shouldPresentBackupModal,
                navigation: navigation
            )
            .clearModalBackground()
        }
        .fullScreenCover(isPresented: $isShowingRemoveConfirmation) {
            HorizontalActionsBottomModal(
                viewModel: .forgetKeySet,
                mainAction: forgetKeyActionHandler.forgetKeySet(actionModel.removeSeed),
                // We need to fake right button action here or Rust machine will break
                // In old UI, if you dismiss equivalent of this modal, underlying modal would still be there,
                // so we need to inform Rust we actually hid it
                dismissAction: navigation.perform(navigation: .init(action: .rightButtonAction), skipDebounce: true),
                isShowingBottomAlert: $isShowingRemoveConfirmation
            )
            .clearModalBackground()
        }
        .fullScreenCover(isPresented: $isShowingBackupModal) {
            if let viewModel = exportPrivateKeyService.backupViewModel() {
                BackupModal(
                    isShowingBackupModal: $isShowingBackupModal,
                    viewModel: viewModel
                )
                .clearModalBackground()
            } else {
                EmptyView()
            }
        }
    }
}

struct KeyDetailsView_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            KeyDetailsView(
                navigation: NavigationCoordinator(),
                forgetKeyActionHandler: .init(navigation: NavigationCoordinator()),
                viewModel: .init(
                    keySummary: KeySummaryViewModel(
                        keyName: "Parity",
                        base58: "15Gsc678...0HA04H0A"
                    ),
                    derivedKeys: [
                        DerivedKeyRowModel(
                            viewModel: DerivedKeyRowViewModel(
                                identicon: PreviewData.exampleIdenticon,
                                path: "// polkadot",
                                hasPassword: false,
                                base58: "15Gsc678654FDSG0HA04H0A"
                            ),
                            actionModel: DerivedKeyActionModel(
                                tapAction: .init(action: .rightButtonAction)
                            )
                        ),

                        DerivedKeyRowModel(
                            viewModel: DerivedKeyRowViewModel(
                                identicon: PreviewData.exampleIdenticon,
                                path: "// polkadot",
                                hasPassword: false,
                                base58: "15Gsc678654FDSG0HA04H0A"
                            ),
                            actionModel: DerivedKeyActionModel(
                                tapAction: .init(action: .rightButtonAction)
                            )
                        ),
                        DerivedKeyRowModel(
                            viewModel: DerivedKeyRowViewModel(
                                identicon: PreviewData.exampleIdenticon,
                                path: "//astar//verylongpathsolongitrequirestwolinesoftextormaybeevenmore",
                                hasPassword: true,
                                base58: "15Gsc678654FDSG0HA04H0A"
                            ),
                            actionModel: DerivedKeyActionModel(
                                tapAction: .init(action: .rightButtonAction)
                            )
                        ),
                        DerivedKeyRowModel(
                            viewModel: DerivedKeyRowViewModel(
                                identicon: PreviewData.exampleIdenticon,
                                path: "//verylongpathsolongitrequirestwolinesoftextormaybeevenmore",
                                hasPassword: false,
                                base58: "15Gsc678654FDSG0HA04H0A"
                            ),
                            actionModel: DerivedKeyActionModel(
                                tapAction: .init(action: .rightButtonAction)
                            )
                        ),
                        DerivedKeyRowModel(
                            viewModel: DerivedKeyRowViewModel(
                                identicon: PreviewData.exampleIdenticon,
                                path: "// acala",
                                hasPassword: true,
                                base58: "15Gsc678654FDSG0HA04H0A"
                            ),
                            actionModel: DerivedKeyActionModel(
                                tapAction: .init(action: .rightButtonAction)
                            )
                        ),
                        DerivedKeyRowModel(
                            viewModel: DerivedKeyRowViewModel(
                                identicon: PreviewData.exampleIdenticon,
                                path: "// moonbeam",
                                hasPassword: true,
                                base58: "15Gsc678654FDSG0HA04H0A"
                            ),
                            actionModel: DerivedKeyActionModel(
                                tapAction: .init(action: .rightButtonAction)
                            )
                        ),
                        DerivedKeyRowModel(
                            viewModel: DerivedKeyRowViewModel(
                                identicon: PreviewData.exampleIdenticon,
                                path: "// kilt",
                                hasPassword: true,
                                base58: "15Gsc6786546423FDSG0HA04H0A"
                            ),
                            actionModel: DerivedKeyActionModel(
                                tapAction: .init(action: .rightButtonAction)
                            )
                        )
                    ]
                ),
                actionModel: KeyDetailsActionModel(
                    addressKeyNavigation: .init(action: .goBack),
                    derivedKeysNavigation: [],
                    alertClosure: nil,
                    removeSeed: ""
                ),
                exportPrivateKeyService: PrivateKeyQRCodeService(
                    navigation: NavigationCoordinator(),
                    keys: MKeys(
                        set: [],
                        root: .init(
                            seedName: "",
                            identicon: [],
                            addressKey: "",
                            base58: "",
                            swiped: false,
                            multiselect: false,
                            secretExposed: false
                        ),
                        network: .init(title: "", logo: ""),
                        multiselectMode: false,
                        multiselectCount: ""
                    )
                )
            )
        }
        .preferredColorScheme(.dark)
        .previewLayout(.sizeThatFits)
    }
}
