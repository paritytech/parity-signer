//!List of all screens
use sp_runtime::MultiSigner;
use zeroize::Zeroize;

use db_handling::interface_signer::{addresses_set_seed_name_network, first_network};
use definitions::{error::{AddressKeySource, ErrorSigner, ExtraAddressKeySourceSigner, InterfaceSigner, Signer}, helpers::{multisigner_to_public, make_identicon_from_multisigner}, keyring::{AddressKey, NetworkSpecsKey}, users::AddressDetails};
use transaction_parsing;
use transaction_signing;

const MAX_COUNT_SET: u8 = 1;

///All screens
#[derive(Debug, Clone)]
pub enum Screen {
    Log,
    LogDetails,
    Scan,
    Transaction(TransactionState),
    SeedSelector,
    Keys(KeysState),
    KeyDetails(AddressState),
    NewSeed,
    RecoverSeedName,
    RecoverSeedPhrase(String),
    DeriveKey(DeriveState),
    Settings,
    Verifier,
    ManageNetworks,
    NetworkDetails(NetworkSpecsKey),
    SelectSeedForBackup,
    SignSufficientCrypto(SufficientCryptoState),
    Nowhere,
}

///State of keys screen
#[derive(Debug, Clone)]
pub struct KeysState {
    seed_name: String,
    network: NetworkSpecsKey,
}

///State of screen with 1 key
///
///More general KeysState could always be determined as subset of this
#[derive(Debug, Clone)]
pub struct AddressState {
    keys_state: KeysState,
    selected: usize,
    set: Vec<MultiSigner>,
}

///State of derive key screen
#[derive(Debug, Clone)]
pub struct DeriveState {
    entered_info: EnteredInfo,
    keys_state: KeysState,
}

///State of transaction screen
#[derive(Debug, Clone)]
pub struct TransactionState {
    entered_info: EnteredInfo,
    action: transaction_parsing::Action,
    comment: String,
    counter: u8,
}

///State of screen generating sufficient crypto
#[derive(Debug, Clone)]
pub struct SufficientCryptoState {
    key_selected: Option<(MultiSigner, AddressDetails, String)>,
    entered_info: EnteredInfo,
    content: transaction_signing::SufficientContent,
    counter: u8,
}

///EnteredInfo, path+pwd entered by the user, zeroizeable
#[derive(Debug, Clone, Zeroize)]
#[zeroize(drop)]
pub struct EnteredInfo (pub String);

impl KeysState {
    pub fn new(seed_name: &str, database_name: &str) -> Result<Self, ErrorSigner> {
        let network_specs = first_network(database_name)?;
        Ok(Self {
            seed_name: seed_name.to_string(),
            network: NetworkSpecsKey::from_parts(&network_specs.genesis_hash.to_vec(), &network_specs.encryption),
        })
    }
    pub fn change_network(&self, network_specs_key: &NetworkSpecsKey) -> Self {
        Self {
            seed_name: self.seed_name(),
            network: network_specs_key.to_owned(),
        }
    }
    pub fn seed_name(&self) -> String {
        self.seed_name.to_owned()
    }
    pub fn network_specs_key(&self) -> NetworkSpecsKey {
        self.network.to_owned()
    }
}

impl AddressState {
    pub fn new(hex_address_key: &str, keys_state: &KeysState, database_name: &str) -> Result<Self, ErrorSigner> {
        let address_key = AddressKey::from_hex(hex_address_key)?;
        let multisigner = address_key.multi_signer::<Signer>(AddressKeySource::Extra(ExtraAddressKeySourceSigner::Interface))?;
        let seed_name = keys_state.seed_name();
        let mut whole_set = addresses_set_seed_name_network(database_name, &seed_name, &keys_state.network_specs_key())?;
        whole_set.sort_by(|(_, a), (_, b)| a.path.cmp(&b.path));
        let set: Vec<MultiSigner> = whole_set.into_iter().map(|(multisigner, _)| multisigner).collect();
        let selected = match set.iter().position(|a| a == &multisigner) {
            Some(a) => a,
            None => return Err(ErrorSigner::Interface(InterfaceSigner::AddressKeyNotInSet{address_key: address_key, seed_name}))
        };
        Ok(Self {
            keys_state: keys_state.to_owned(),
            selected,
            set
        })
    }
    pub fn get_keys_state(&self) -> KeysState {
        self.keys_state.to_owned()
    }
    pub fn seed_name(&self) -> String {
        self.keys_state.seed_name()
    }
    pub fn network_specs_key(&self) -> NetworkSpecsKey {
        self.keys_state.network_specs_key()
    }
    pub fn multisigner(&self) -> MultiSigner {
        self.set[self.selected].to_owned()
    }
    pub fn set(&self) -> Vec<MultiSigner> {
        self.set.to_owned()
    }
    pub fn next(&self) -> Self {
        let selected = {
            if self.selected+1 == self.set.len() {0}
            else {self.selected+1}
        };
        Self {
            keys_state: self.keys_state.to_owned(),
            selected,
            set: self.set.to_owned(),
        }
    }
    pub fn previous(&self) -> Self {
        let selected = {
            if self.selected == 0 {self.set.len()-1}
            else {self.selected-1}
        };
        Self {
            keys_state: self.keys_state.to_owned(),
            selected,
            set: self.set.to_owned(),
        }
    }
}

impl DeriveState {
    pub fn new (entered_string: &str, keys_state: &KeysState) -> Self {
        Self {
            entered_info: EnteredInfo(entered_string.to_string()),
            keys_state: keys_state.to_owned(),
        }
    }
    pub fn get_keys_state(&self) -> KeysState {
        self.keys_state.to_owned()
    }
    pub fn seed_name(&self) -> String {
        self.keys_state.seed_name()
    }
    pub fn network_specs_key(&self) -> NetworkSpecsKey {
        self.keys_state.network_specs_key()
    }
    pub fn path(&self) -> String {
        self.entered_info.0.to_owned()
    }
    pub fn update(&self, new_secret_string: &str) -> Self {
        Self {
            entered_info: EnteredInfo(new_secret_string.to_string()),
            keys_state: self.get_keys_state(),
        }
    }
}

impl TransactionState {
    pub fn new(details_str: &str, dbname: &str) -> Self {
        Self {
            entered_info: EnteredInfo("".to_string()),
            action: transaction_parsing::produce_output(details_str, dbname),
            comment: "".to_string(),
            counter: 0,
        }
    }
    pub fn update_seed(&self, new_secret_string: &str) -> Self {
        Self {
            entered_info: EnteredInfo(new_secret_string.to_string()),
            action: self.action(),
            comment: self.comment.to_string(),
            counter: self.counter,
        }
    }
    pub fn add_comment(&self, comment: &str) -> Self {
        Self {
            entered_info: self.entered_info.to_owned(),
            action: self.action(),
            comment: comment.to_string(),
            counter: self.counter,
        }
    }
    pub fn action(&self) -> transaction_parsing::Action {
        self.action.to_owned()
    }
    pub fn seed(&self) -> String {
        self.entered_info.0.to_string()
    }
    pub fn get_comment(&self) -> String {
        self.comment.to_owned()
    }
    pub fn plus_one(&self) -> Self {
        Self {
            entered_info: self.entered_info.to_owned(),
            action: self.action(),
            comment: self.get_comment(),
            counter: self.counter + 1,
        }
    }
    pub fn ok(&self) -> bool {
        self.counter < MAX_COUNT_SET
    }
    pub fn counter(&self) -> u8 {
        self.counter
    }
}

impl SufficientCryptoState {
    pub fn init(content: transaction_signing::SufficientContent) -> Self {
        Self {
            key_selected: None,
            entered_info: EnteredInfo("".to_string()),
            content,
            counter: 0,
        }
    }
    pub fn content(&self) -> transaction_signing::SufficientContent {
        self.content.to_owned()
    }
    pub fn key_selected(&self) -> Option<(MultiSigner, AddressDetails, String)> {
        self.key_selected.to_owned()
    }
    pub fn update(&self, multisigner: &MultiSigner, address_details: &AddressDetails, new_secret_string: &str) -> Self {
        let hex_identicon = match make_identicon_from_multisigner(&multisigner) {
            Ok(a) => hex::encode(a),
            Err(_) => String::new(),
        };
        let author_info = format!("\"base58\":\"{}\",\"identicon\":\"{}\",\"seed\":\"{}\",\"derivation_path\":\"{}\"", hex::encode(multisigner_to_public(&multisigner)), hex_identicon, address_details.seed_name, address_details.path);
        Self{
            key_selected: Some((multisigner.to_owned(), address_details.to_owned(), author_info)),
            entered_info: EnteredInfo(new_secret_string.to_string()),
            content: self.content(),
            counter: self.counter,
        }
    }
    pub fn seed(&self) -> String {
        self.entered_info.0.to_string()
    }
    pub fn plus_one(&self) -> Self {
        Self {
            key_selected: self.key_selected(),
            entered_info: self.entered_info.to_owned(),
            content: self.content(),
            counter: self.counter + 1,
        }
    }
    pub fn ok(&self) -> bool {
        self.counter < MAX_COUNT_SET
    }
    pub fn counter(&self) -> u8 {
        self.counter
    }
}

impl Screen {
    ///Encode screen name into string for UI
    pub fn get_name(&self) -> Option<String> {
        match self {
            Screen::Log => Some(String::from("Log")),
            Screen::LogDetails => Some(String::from("LogDetails")),
            Screen::Scan => Some(String::from("Scan")),
            Screen::Transaction(_) => Some(String::from("Transaction")),
            Screen::SeedSelector => Some(String::from("SeedSelector")),
            Screen::Keys(_) => Some(String::from("Keys")),
            Screen::KeyDetails(_) => Some(String::from("KeyDetails")),
            Screen::NewSeed => Some(String::from("NewSeed")),
            Screen::RecoverSeedName => Some(String::from("RecoverSeedName")),
            Screen::RecoverSeedPhrase(_) => Some(String::from("RecoverSeedPhrase")),
            Screen::DeriveKey(_) => Some(String::from("DeriveKey")),
            Screen::Settings => Some(String::from("Settings")),
            Screen::Verifier => Some(String::from("Verifier")),
            Screen::ManageNetworks => Some(String::from("ManageNetworks")),
            Screen::NetworkDetails(_) => Some(String::from("NetworkDetails")),
            Screen::SelectSeedForBackup => Some(String::from("SelectSeedForBackup")),
            Screen::SignSufficientCrypto(_) => Some(String::from("SignSufficientCrypto")),
            Screen::Nowhere => None,
        }
    }

    pub fn get_default_label(&self) -> String {
        match self {
            Screen::Log => "",
            Screen::LogDetails => "Event details",
            Screen::Scan => "",
            Screen::Transaction(_) => "",
            Screen::SeedSelector => "Select seed",
            Screen::Keys(_) => "",
            Screen::KeyDetails(_) => "Key",
            Screen::NewSeed => "New Seed",
            Screen::RecoverSeedName => "Recover Seed",
            Screen::RecoverSeedPhrase(_) => "Recover Seed",
            Screen::DeriveKey(_) => "",
            Screen::Settings => "",
            Screen::Verifier => "VERIFIER CERTIFICATE",
            Screen::ManageNetworks => "MANAGE NETWORKS",
            Screen::NetworkDetails(_) => "Network details",
            Screen::SelectSeedForBackup => "Select seed",
            Screen::SignSufficientCrypto(_) => "Sign SufficientCrypto",
            Screen::Nowhere => "",
        }.to_string()
    }

    pub fn has_back(&self) -> bool {
        match self {
            Screen::Log => false,
            Screen::LogDetails => true,
            Screen::Scan => false,
            Screen::Transaction(_) => true,
            Screen::SeedSelector => false,
            Screen::Keys(_) => true,
            Screen::KeyDetails(_) => true,
            Screen::NewSeed => true,
            Screen::RecoverSeedName => true,
            Screen::RecoverSeedPhrase(_) => true,
            Screen::DeriveKey(_) => true,
            Screen::Settings => false,
            Screen::Verifier => true,
            Screen::ManageNetworks => true,
            Screen::NetworkDetails(_) => true,
            Screen::SelectSeedForBackup => true,
            Screen::SignSufficientCrypto(_) => true,
            Screen::Nowhere => false,
        }
    }
}

#[cfg(test)]
mod tests {
//    use super::*;
}
