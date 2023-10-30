//
//  ExportPrivateKeyService.swift
//  Polkadot Vault
//
//  Created by Krzysztof Rodak on 12/09/2022.
//

import Foundation

final class ExportPrivateKeyService {
    private let databaseMediator: DatabaseMediating
    private let seedsMediator: SeedsMediating
    private let backendService: BackendService

    init(
        backendService: BackendService = BackendService(),
        databaseMediator: DatabaseMediating = DatabaseMediator(),
        seedsMediator: SeedsMediating = ServiceLocator.seedsMediator
    ) {
        self.backendService = backendService
        self.databaseMediator = databaseMediator
        self.seedsMediator = seedsMediator
    }

    func exportPrivateKey(
        _ keyDetails: MKeyDetails,
        completion: @escaping (Result<ExportPrivateKeyViewModel, ServiceError>) -> Void
    ) {
        backendService.performCall({
            try generateSecretKeyQr(
                publicKey: keyDetails.pubkey,
                expectedSeedName: keyDetails.address.seedName,
                networkSpecsKey: keyDetails.networkInfo.networkSpecsKey,
                seedPhrase: self.seedsMediator.getSeed(seedName: keyDetails.address.seedName),
                keyPassword: nil
            )
        }, completion: { (result: Result<MKeyDetails, ErrorDisplayed>) in
            switch result {
            case let .success(keyDetails):
                completion(.success(ExportPrivateKeyViewModel(
                    qrCode: keyDetails.qr,
                    addressFooter: .init(
                        identicon: keyDetails.address.identicon,
                        networkLogo: keyDetails.networkInfo.networkLogo,
                        base58: keyDetails.base58
                    )
                )))
            case let .failure(error):
                completion(.failure(.init(message: error.backendDisplayError)))
            }
        })
    }
}
