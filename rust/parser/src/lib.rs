//! This crate is a transaction parser used by
//! [Signer](https://github.com/paritytech/parity-signer).
//!
//! Signer allows to sign only the transactions that were successfully parsed
//! and were approved by user after checking the transaction contents.
//!
//! Transactions are read by the Signer as QR codes having following structure:
//!
//! <table>
//!     <tr>
//!         <td>prelude</td>
//!         <td>public key</td>
//!         <td>SCALE-encoded call data</td>
//!         <td>SCALE-encoded extensions</td>
//!         <td>network genesis hash</td>
//!     </tr>
//! </table>

#![deny(unused_crate_dependencies)]

use frame_metadata::v14::RuntimeMetadataV14;
#[cfg(feature = "test")]
use frame_metadata::RuntimeMetadata;
use parity_scale_codec::{Decode, DecodeAll, Encode};
use printing_balance::AsBalance;
use sp_core::H256;
use sp_runtime::generic::Era;

#[cfg(feature = "test")]
use defaults::default_types_vec;
#[cfg(feature = "test")]
use definitions::metadata::info_from_metadata;
use definitions::{
    error_signer::{ParserDecodingError, ParserError, ParserMetadataError},
    network_specs::ShortSpecs,
    types::TypeEntry,
};

pub mod cards;
use cards::ParserCard;
mod decoding_older;
use decoding_older::process_as_call;
pub mod decoding_commons;
use decoding_commons::{get_compact, OutputCard};
mod decoding_sci;
use decoding_sci::decoding_sci_entry_point;
mod decoding_sci_ext;
use decoding_sci_ext::{decode_ext_attempt, Ext};
#[cfg(feature = "test")]
mod error;
#[cfg(feature = "test")]
use error::{ArgumentsError, Error};
pub mod method;
use method::OlderMeta;
#[cfg(feature = "test")]
#[cfg(test)]
mod tests;

/// Function intakes SCALE encoded method part of transaction as Vec<u8>,
/// network metadata and network specs.
///
pub fn parse_method(
    method_data: &mut Vec<u8>,
    metadata_bundle: &MetadataBundle,
    short_specs: &ShortSpecs,
) -> Result<Vec<OutputCard>, ParserError> {
    let start_indent = 0;
    let out = match metadata_bundle {
        MetadataBundle::Older {
            older_meta,
            types,
            network_version: _,
        } => process_as_call(method_data, older_meta, types, start_indent, short_specs)?,
        MetadataBundle::Sci {
            meta_v14,
            network_version: _,
        } => decoding_sci_entry_point(method_data, meta_v14, start_indent, short_specs)?,
    };
    if !method_data.is_empty() {
        return Err(ParserError::Decoding(
            ParserDecodingError::SomeDataNotUsedMethod,
        ));
    }
    Ok(out)
}

/// Struct to decode pre-determined extensions for transactions with V12 and V13 metadata
#[derive(Debug, Decode, Encode)]
struct ExtValues {
    era: Era,
    #[codec(compact)]
    nonce: u64,
    #[codec(compact)]
    tip: u128,
    metadata_version: u32,
    tx_version: u32,
    genesis_hash: H256,
    block_hash: H256,
}

pub fn parse_extensions(
    extensions_data: &mut Vec<u8>,
    metadata_bundle: &MetadataBundle,
    short_specs: &ShortSpecs,
    optional_mortal_flag: Option<bool>,
) -> Result<Vec<OutputCard>, ParserError> {
    let indent = 0;
    let (era, block_hash, cards) = match metadata_bundle {
        MetadataBundle::Older {
            older_meta: _,
            types: _,
            network_version,
        } => {
            let ext = match <ExtValues>::decode_all(&mut &extensions_data[..]) {
                Ok(a) => a,
                Err(_) => return Err(ParserError::Decoding(ParserDecodingError::ExtensionsOlder)),
            };
            if ext.genesis_hash != short_specs.genesis_hash {
                return Err(ParserError::Decoding(
                    ParserDecodingError::GenesisHashMismatch,
                ));
            }
            if network_version != &ext.metadata_version {
                return Err(ParserError::WrongNetworkVersion {
                    as_decoded: ext.metadata_version.to_string(),
                    in_metadata: network_version.to_owned(),
                });
            }
            let tip =
                <u128>::convert_balance_pretty(ext.tip, short_specs.decimals, &short_specs.unit);
            let cards = vec![
                OutputCard {
                    card: ParserCard::Era(ext.era),
                    indent,
                },
                OutputCard {
                    card: ParserCard::Nonce(ext.nonce.to_string()),
                    indent,
                },
                OutputCard {
                    card: ParserCard::Tip {
                        number: tip.number.to_string(),
                        units: tip.units,
                    },
                    indent,
                },
                OutputCard {
                    card: ParserCard::NetworkNameVersion {
                        name: short_specs.name.to_string(),
                        version: network_version.to_string(),
                    },
                    indent,
                },
                OutputCard {
                    card: ParserCard::TxVersion(ext.tx_version.to_string()),
                    indent,
                },
                OutputCard {
                    card: ParserCard::BlockHash(ext.block_hash),
                    indent,
                },
            ];
            (ext.era, ext.block_hash, cards)
        }
        MetadataBundle::Sci {
            meta_v14,
            network_version,
        } => {
            let mut ext = Ext::init();
            let extensions_decoded =
                decode_ext_attempt(extensions_data, &mut ext, meta_v14, indent, short_specs)?;
            if let Some(genesis_hash) = ext.found_ext.genesis_hash {
                if genesis_hash != short_specs.genesis_hash {
                    return Err(ParserError::Decoding(
                        ParserDecodingError::GenesisHashMismatch,
                    ));
                }
            }
            let block_hash = match ext.found_ext.block_hash {
                Some(a) => a,
                None => {
                    return Err(ParserError::FundamentallyBadV14Metadata(
                        ParserMetadataError::NoBlockHash,
                    ))
                }
            };
            let era = match ext.found_ext.era {
                Some(a) => a,
                None => {
                    return Err(ParserError::FundamentallyBadV14Metadata(
                        ParserMetadataError::NoEra,
                    ))
                }
            };
            match ext.found_ext.network_version_printed {
                Some(a) => {
                    if a != network_version.to_string() {
                        return Err(ParserError::WrongNetworkVersion {
                            as_decoded: a,
                            in_metadata: network_version.to_owned(),
                        });
                    }
                }
                None => {
                    return Err(ParserError::FundamentallyBadV14Metadata(
                        ParserMetadataError::NoVersionExt,
                    ))
                }
            }
            if !extensions_data.is_empty() {
                return Err(ParserError::Decoding(
                    ParserDecodingError::SomeDataNotUsedExtensions,
                ));
            }
            (era, block_hash, extensions_decoded)
        }
    };
    if let Era::Immortal = era {
        if short_specs.genesis_hash != block_hash {
            return Err(ParserError::Decoding(
                ParserDecodingError::ImmortalHashMismatch,
            ));
        }
        if let Some(true) = optional_mortal_flag {
            return Err(ParserError::Decoding(
                ParserDecodingError::UnexpectedImmortality,
            ));
        }
    }
    if let Era::Mortal(_, _) = era {
        if let Some(false) = optional_mortal_flag {
            return Err(ParserError::Decoding(
                ParserDecodingError::UnexpectedMortality,
            ));
        }
    }
    Ok(cards)
}

pub fn cut_method_extensions(data: &[u8]) -> Result<(Vec<u8>, Vec<u8>), ParserError> {
    let pre_method = get_compact::<u32>(data).map_err(|_| ParserError::SeparateMethodExtensions)?;
    let method_length = pre_method.compact_found as usize;
    match pre_method.start_next_unit {
        Some(start) => match data.get(start..start + method_length) {
            Some(a) => Ok((a.to_vec(), data[start + method_length..].to_vec())),
            None => Err(ParserError::SeparateMethodExtensions),
        },
        None => {
            if method_length != 0 {
                return Err(ParserError::SeparateMethodExtensions);
            }
            Ok((Vec::new(), data.to_vec()))
        }
    }
}

#[allow(clippy::type_complexity)]
pub fn parse_set(
    data: &[u8],
    metadata_bundle: &MetadataBundle,
    short_specs: &ShortSpecs,
    optional_mortal_flag: Option<bool>,
) -> Result<
    (
        Result<Vec<OutputCard>, ParserError>,
        Vec<OutputCard>,
        Vec<u8>,
        Vec<u8>,
    ),
    ParserError,
> {
    // if unable to separate method date and extensions, then some fundamental flaw is in transaction itself
    let (mut method_data, mut extensions_data) = cut_method_extensions(data)?;

    // try parsing extensions, if is works, the version and extensions are correct
    let extensions_cards = parse_extensions(
        &mut extensions_data,
        metadata_bundle,
        short_specs,
        optional_mortal_flag,
    )?;
    // try parsing method
    let method_cards_result = parse_method(&mut method_data, metadata_bundle, short_specs);
    Ok((
        method_cards_result,
        extensions_cards,
        method_data,
        extensions_data,
    ))
}

#[cfg(feature = "test")]
pub fn parse_and_display_set(
    data: &[u8],
    metadata: &RuntimeMetadata,
    short_specs: &ShortSpecs,
) -> Result<String, String> {
    let meta_info = match info_from_metadata(metadata) {
        Ok(x) => x,
        Err(e) => return Err(Error::Arguments(ArgumentsError::Metadata(e)).show()),
    };
    if meta_info.name != short_specs.name {
        return Err(Error::Arguments(ArgumentsError::NetworkNameMismatch {
            name_metadata: meta_info.name,
            name_network_specs: short_specs.name.to_string(),
        })
        .show());
    }
    let metadata_bundle = match metadata {
        RuntimeMetadata::V12(_) | RuntimeMetadata::V13(_) => {
            let older_meta = match metadata {
                RuntimeMetadata::V12(meta_v12) => OlderMeta::V12(meta_v12),
                RuntimeMetadata::V13(meta_v13) => OlderMeta::V13(meta_v13),
                _ => unreachable!(),
            };
            let types = match default_types_vec() {
                Ok(a) => {
                    if a.is_empty() {
                        return Err(Error::Arguments(ArgumentsError::NoTypes).show());
                    }
                    a
                }
                Err(_) => return Err(Error::Arguments(ArgumentsError::DefaultTypes).show()),
            };
            MetadataBundle::Older {
                older_meta,
                types,
                network_version: meta_info.version,
            }
        }
        RuntimeMetadata::V14(meta_v14) => MetadataBundle::Sci {
            meta_v14,
            network_version: meta_info.version,
        },
        _ => unreachable!(), // just checked in the info_from_metadata function if the metadata is acceptable one
    };
    match parse_set(data, &metadata_bundle, short_specs, None) {
        Ok((method_cards_result, extensions_cards, _, _)) => {
            let mut method = String::new();
            let mut extensions = String::new();
            match method_cards_result {
                Ok(method_cards) => {
                    for (i, x) in method_cards.iter().enumerate() {
                        if i > 0 {
                            method.push_str(",\n");
                        }
                        method.push_str(&x.card.show_no_docs(x.indent));
                    }
                }
                Err(e) => method = e.show(),
            }
            for (i, x) in extensions_cards.iter().enumerate() {
                if i > 0 {
                    extensions.push_str(",\n");
                }
                extensions.push_str(&x.card.show_no_docs(x.indent));
            }
            Ok(format!(
                "\nMethod:\n\n{}\n\n\nExtensions:\n\n{}",
                method, extensions
            ))
        }
        Err(e) => Err(Error::Parser(e).show()),
    }
}

pub enum MetadataBundle<'a> {
    Older {
        older_meta: OlderMeta<'a>,
        types: Vec<TypeEntry>,
        network_version: u32,
    },
    Sci {
        meta_v14: &'a RuntimeMetadataV14,
        network_version: u32,
    },
}
