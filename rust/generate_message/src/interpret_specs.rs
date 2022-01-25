use serde_json::map::Map;
use jsonrpsee_types::JsonValue;
use std::convert::TryInto;
use definitions::{error::SpecsError, network_specs::NetworkProperties};

/// Function to interpret network properties fetched via rpc call

pub fn interpret_properties (x: &Map<String, JsonValue>, optional_prefix_from_meta: Option<u16>) -> Result<NetworkProperties, SpecsError> {
    let base58prefix: u16 = match x.get("ss58Format") {
        Some(a) => {
            match a {
                JsonValue::Number(b) => {
                    match b.as_u64() {
                        Some(c) => {
                            match c.try_into() {
                                Ok(d) => {
                                    match optional_prefix_from_meta {
                                        Some(prefix_from_meta) => {
                                            if prefix_from_meta == d {d}
                                            else {return Err(SpecsError::Base58PrefixMismatch{specs: d, meta: prefix_from_meta})}
                                        },
                                        None => d,
                                    }
                                },
                                Err(_) => return Err(SpecsError::Base58PrefixFormatNotSupported{value: a.to_string()}),
                            }
                        },
                        None => return Err(SpecsError::Base58PrefixFormatNotSupported{value: a.to_string()}),
                    }
                },
                _ => return Err(SpecsError::Base58PrefixFormatNotSupported{value: a.to_string()}),
            }
        },
        None => {
            match optional_prefix_from_meta {
                Some(prefix_from_meta) => prefix_from_meta,
                None => return Err(SpecsError::NoBase58Prefix),
            }
        },
    };
    let decimals: u8 = match x.get("tokenDecimals") {
        Some(a) => {
            match a {
                JsonValue::Number(b) => {
                    match b.as_u64() {
                        Some(c) => {
                            match c.try_into() {
                                Ok(d) => d,
                                Err(_) => return Err(SpecsError::DecimalsFormatNotSupported{value: a.to_string()}),
                            }
                        },
                        None => return Err(SpecsError::DecimalsFormatNotSupported{value: a.to_string()}),
                    }
                },
                _ => return Err(SpecsError::DecimalsFormatNotSupported{value: a.to_string()}),
            }
        },
        None => return Err(SpecsError::NoDecimals),
    };
    let unit = match x.get("tokenSymbol") {
        Some(a) => {
            match a {
                JsonValue::String(b) => b.to_string(),
                _ => return Err(SpecsError::UnitFormatNotSupported{value: a.to_string()}),
            }
        },
        None => return Err(SpecsError::NoUnit),
    };
    Ok(NetworkProperties {
        base58prefix,
        decimals,
        unit,
    })
}

