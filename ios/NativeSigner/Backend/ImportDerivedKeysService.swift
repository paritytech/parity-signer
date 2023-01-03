//
//  ImportDerivedKeysService.swift
//  NativeSigner
//
//  Created by Krzysztof Rodak on 02/01/2023.
//

import Foundation

final class ImportDerivedKeysService {
    private let databaseMediator: DatabaseMediating
    private let seedsMediator: SeedsMediating
    private let callQueue: Dispatching
    private let callbackQueue: Dispatching

    init(
        databaseMediator: DatabaseMediating = DatabaseMediator(),
        seedsMediator: SeedsMediating = ServiceLocator.seedsMediator,
        callQueue: Dispatching = DispatchQueue(label: "ImportDerivedKeysService", qos: .userInitiated),
        callbackQueue: Dispatching = DispatchQueue.main
    ) {
        self.databaseMediator = databaseMediator
        self.seedsMediator = seedsMediator
        self.callQueue = callQueue
        self.callbackQueue = callbackQueue
    }

    func importDerivedKeys(
        _ seedPreviews: [SeedKeysPreview],
        _ completion: @escaping (Result<Void, ServiceError>) -> Void
    ) {
        callQueue.async {
            let result: Result<Void, ServiceError>
            do {
                try importDerivations(dbname: self.databaseMediator.databaseName, seedDerivedKeys: seedPreviews)
                result = .success(())
            } catch {
                result = .failure(.unknown)
            }
            self.callbackQueue.async {
                completion(result)
            }
        }
    }

    func updateWithSeeds(
        _ seedPreviews: [SeedKeysPreview],
        completion: @escaping (Result<[SeedKeysPreview], ServiceError>) -> Void
    ) {
        let seeds = seedsMediator.getAllSeeds()
        let result: Result<[SeedKeysPreview], ServiceError>
        do {
            let filledInSeedPreviews = try populateDerivationsHasPwd(
                seeds: seeds,
                seedDerivedKeys: seedPreviews
            )
            result = .success(filledInSeedPreviews)
        } catch {
            result = .failure(.unknown)
        }
        self.callbackQueue.async {
            completion(result)
        }
    }
}
