//
//  Seeds.swift
//  NativeSigner
//
//  Created by Alexander Slesarev on 13.9.2021.
//

import Foundation

import Security //for keyring

/**
 * Apple's own crypto boilerplate
 */
enum KeychainError: Error {
    case noPassword
    case unexpectedPasswordData
    case unhandledError(status: OSStatus)
}

/**
 * Seeds management operations - these mostly rely on secure enclave
 * However, some rustnative operations happen here as well (default keys creation and associated keys removal)
 *
 *  Seeds are stored in keyring - it has SQL-like api but is backed by secure enclave
 *  IMPORTANT! The keys from keyring are not removed on app uninstall!
 *  Remember to wipe the app with wipe button in settings.
 */
extension SignerDataModel {
    
    func refreshSeeds() {
        var item: CFTypeRef?
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecMatchLimit as String: kSecMatchLimitAll,
            kSecReturnAttributes as String: true,
            kSecReturnData as String: false
        ]
        print("starting seed names query")
        let status = SecItemCopyMatching(query as CFDictionary, &item)
        print("refresh seeds")
        print(status)
        print(SecCopyErrorMessageString(status, nil) ?? "Success")
        guard let itemFound = item as? [[String : Any]]
        else {
            print("no seeds available")
            self.seedNames = []
            update_seed_names(nil, seedNames.joined(separator: ","))
            return
        }
        print("some seeds fetched")
        print(itemFound)
        print(kSecAttrAccount)
        let seedNames = itemFound.map{item -> String in
            guard let seedName = item[kSecAttrAccount as String] as? String
            else {
                print("seed name decoding error")
                return "error!"
            }
            return seedName
        }
        print(seedNames)
        self.seedNames = seedNames.sorted()
        update_seed_names(nil, self.seedNames.joined(separator: ","))
    }
    
    func addSeed(seedName: String, seedLength: Int32) {
        var err = ExternError()
        guard let accessFlags = SecAccessControlCreateWithFlags(kCFAllocatorDefault, kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly, .devicePasscode, &error) else {
            print("Access flags could not be allocated")
            print(error ?? "no error code")
            self.lastError = "iOS key manager error, report a bug"
            return
        }
        print(accessFlags)
        if checkSeedCollision(seedName: seedName) {
            print("Key collision")
            self.lastError = "Seed with this name already exists"
            return
        }
        if checkSeedPhraseCollision(seedPhrase: "") {
            print("Key collision")
            self.lastError = "Seed with this name already exists"
            return
        }
        withUnsafeMutablePointer(to: &err) {err_ptr in
            let res = try_create_seed(err_ptr, seedName, seedLength, dbName)
            if err_ptr.pointee.code != 0 {
                self.lastError = String(cString: err_ptr.pointee.message)
                print("Rust returned error")
                print(self.lastError)
                signer_destroy_string(err_ptr.pointee.message)
                return
            }
            let finalSeedPhraseString = String(cString: res!)
            guard let finalSeedPhrase = finalSeedPhraseString.data(using: .utf8) else {
                print("could not encode seed phrase")
                self.lastError = "Seed phrase contains non-0unicode symbols"
                return
            }
            signer_destroy_string(res)
            print(finalSeedPhrase)
            let query: [String: Any] = [
                kSecClass as String: kSecClassGenericPassword,
                kSecAttrAccessControl as String: accessFlags,
                kSecAttrAccount as String: seedName,
                kSecValueData as String: finalSeedPhrase,
                kSecReturnData as String: true
            ]
            var resultAdd: AnyObject?
            let status = SecItemAdd(query as CFDictionary, &resultAdd)
            guard status == errSecSuccess else {
                print("key add failure")
                print(status)
                self.lastError = SecCopyErrorMessageString(status, nil)! as String
                return
            }
            self.refreshSeeds()
            self.seedBackup = finalSeedPhraseString
            self.pushButton(buttonID: .BackupSeed, details: seedName)
            //TODO
        }
    }
    
    func restoreSeed(seedName: String, seedPhrase: String) {
        var err = ExternError()
        guard let accessFlags = SecAccessControlCreateWithFlags(kCFAllocatorDefault, kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly, .devicePasscode, &error) else {
            print("Access flags could not be allocated")
            print(error ?? "no error code")
            self.lastError = "iOS key manager error, report a bug"
            return
        }
        if checkSeedPhraseCollision(seedPhrase: seedPhrase) {
            print("Key collision")
            self.lastError = "Seed with this name already exists"
            return
        }
        withUnsafeMutablePointer(to: &err) {err_ptr in
            let res = try_restore_seed(err_ptr, seedName, seedPhrase, dbName)
            if err_ptr.pointee.code != 0 {
                self.lastError = String(cString: err_ptr.pointee.message)
                print("Rust returned error")
                print(self.lastError)
                signer_destroy_string(err_ptr.pointee.message)
                return
            }
            let finalSeedPhraseString = String(cString: res!)
            guard let finalSeedPhrase = finalSeedPhraseString.data(using: .utf8) else {
                print("could not encode seed phrase")
                self.lastError = "Seed phrase contains non-0unicode symbols"
                return
            }
            signer_destroy_string(res)
            print(finalSeedPhrase)
            let query: [String: Any] = [
                kSecClass as String: kSecClassGenericPassword,
                kSecAttrAccessControl as String: accessFlags,
                kSecAttrAccount as String: seedName,
                kSecValueData as String: finalSeedPhrase,
                kSecReturnData as String: true
            ]
            var resultAdd: AnyObject?
            let status = SecItemAdd(query as CFDictionary, &resultAdd)
            guard status == errSecSuccess else {
                print("key add failure")
                print(status)
                self.lastError = SecCopyErrorMessageString(status, nil)! as String
                return
            }
            self.refreshSeeds()
            self.pushButton(buttonID: .GoForward)
            //TODO
        }
    }
    
    /**
     * Each seed name should be unique, obviously. We do not want to overwrite old seeds.
     */
    func checkSeedCollision(seedName: String) -> Bool {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecValueData as String: seedName,
            kSecMatchLimit as String: kSecMatchLimitOne
        ]
        let status = SecItemCopyMatching(query as CFDictionary, nil)
        return status == errSecSuccess
    }
    
    /**
     * Check if proposed seed phrase is already saved. But mostly require auth on seed creation.
     */
    func checkSeedPhraseCollision(seedPhrase: String) -> Bool {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: seedPhrase,
            kSecMatchLimit as String: kSecMatchLimitOne
        ]
        let status = SecItemCopyMatching(query as CFDictionary, nil)
        return status == errSecSuccess
    }
    
    /**
     * Selects seed and updates the model accordingly
     */
    func selectSeed(seedName: String) {
        self.selectedSeed = seedName
        //TODO: this all should be part of backend event
        //self.fetchKeys()
    }
    
    /**
     * This is simple explicit "get" for showing plaintext seedBackup value after it was fetched
     */
    func getRememberedSeedPhrase() -> String {
        if self.seedBackup == "" {
            self.seedBackup = getSeed(seedName: self.selectedSeed, backup: true)
        }
        if self.seedBackup == "" {
            //TODO: improve this
            pushButton(buttonID: .GoBack)
        }
        return self.seedBackup
    }
    
    /**
     * Gets seed by seedName from keyring
     * Calls auth screen automatically; no need to call it specially or wrap
     */
    func getSeed(seedName: String, backup: Bool = false) -> String {
        var err = ExternError()
        var item: CFTypeRef?
        var logSuccess = true
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: seedName,
            kSecMatchLimit as String: kSecMatchLimitOne,
            kSecReturnData as String: true
        ]
        let status = SecItemCopyMatching(query as CFDictionary, &item)
        if status == errSecSuccess {
            if backup {
                withUnsafeMutablePointer(to: &err) {err_ptr in
                    seed_name_was_shown(err_ptr, seedName, self.dbName)
                    if err_ptr.pointee.code != 0 {
                        print("Seed access logging error! This system is broken and should not be used anymore.")
                        self.lastError = String(cString: err_ptr.pointee.message)
                        print(self.lastError)
                        signer_destroy_string(err_ptr.pointee.message)
                        //Attempt to log this anyway one last time;
                        //if this fails too - complain to joulu pukki
                        history_entry_system(nil, "Seed access logging failed!", self.dbName)
                        logSuccess = false
                    }
                }
                return logSuccess ? String(data: (item as! CFData) as Data, encoding: .utf8) ?? "" : ""
            }
            return String(data: (item as! CFData) as Data, encoding: .utf8) ?? ""
        } else {
            self.lastError = SecCopyErrorMessageString(status, nil)! as String
            return ""
        }
    }
    
    
    /**
     * Removes seed and all derived keys
     */
    func removeSeed(seedName: String) {
        let query = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: seedName
        ] as CFDictionary
        let status = SecItemDelete(query)
        print(status.description)
        if status == errSecSuccess {
            pushButton(buttonID: .RemoveSeed)
            refreshSeeds()
        } else {
            print(seedName)
            self.lastError = SecCopyErrorMessageString(status, nil)! as String
            print("remove seed from secure storage error: " + self.lastError)
        }
    }
     
    
    /*
     * Guess possible seed word(s) from user input
     */
    func guessWord(word: String) -> [String] {
        let res = guess_word(nil, word)
        if let wordsJSON = String(cString: res!).data(using: .utf8) {
            guard let words = try? JSONDecoder().decode([String].self, from: wordsJSON)
            else { return [] }
            return words
        } else {
            return []
        }
    }
}
