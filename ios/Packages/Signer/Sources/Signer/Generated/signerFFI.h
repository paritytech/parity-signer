// This file was autogenerated by some hot garbage in the `uniffi` crate.
// Trust me, you don't want to mess with it!

#pragma once

#include <stdbool.h>
#include <stdint.h>

// The following structs are used to implement the lowest level
// of the FFI, and thus useful to multiple uniffied crates.
// We ensure they are declared exactly once, with a header guard, UNIFFI_SHARED_H.
#ifdef UNIFFI_SHARED_H
    // We also try to prevent mixing versions of shared uniffi header structs.
    // If you add anything to the #else block, you must increment the version suffix in UNIFFI_SHARED_HEADER_V4
    #ifndef UNIFFI_SHARED_HEADER_V4
        #error Combining helper code from multiple versions of uniffi is not supported
    #endif // ndef UNIFFI_SHARED_HEADER_V4
#else
#define UNIFFI_SHARED_H
#define UNIFFI_SHARED_HEADER_V4
// ⚠️ Attention: If you change this #else block (ending in `#endif // def UNIFFI_SHARED_H`) you *must* ⚠️
// ⚠️ increment the version suffix in all instances of UNIFFI_SHARED_HEADER_V4 in this file.           ⚠️

typedef struct RustBuffer
{
    int32_t capacity;
    int32_t len;
    uint8_t *_Nullable data;
} RustBuffer;

typedef int32_t (*ForeignCallback)(uint64_t, int32_t, RustBuffer, RustBuffer *_Nonnull);

typedef struct ForeignBytes
{
    int32_t len;
    const uint8_t *_Nullable data;
} ForeignBytes;

// Error definitions
typedef struct RustCallStatus {
    int8_t code;
    RustBuffer errorBuf;
} RustCallStatus;

// ⚠️ Attention: If you change this #else block (ending in `#endif // def UNIFFI_SHARED_H`) you *must* ⚠️
// ⚠️ increment the version suffix in all instances of UNIFFI_SHARED_HEADER_V4 in this file.           ⚠️
#endif // def UNIFFI_SHARED_H

RustBuffer signer_23f1_action_get_name(
      RustBuffer action,
    RustCallStatus *_Nonnull out_status
    );
void signer_23f1_init_navigation(
      RustBuffer dbname,RustBuffer seed_names,
    RustCallStatus *_Nonnull out_status
    );
RustBuffer signer_23f1_backend_action(
      RustBuffer action,RustBuffer details,RustBuffer seed_phrase,
    RustCallStatus *_Nonnull out_status
    );
void signer_23f1_update_seed_names(
      RustBuffer seed_names,
    RustCallStatus *_Nonnull out_status
    );
uint32_t signer_23f1_qrparser_get_packets_total(
      RustBuffer data,int8_t cleaned,
    RustCallStatus *_Nonnull out_status
    );
RustBuffer signer_23f1_qrparser_try_decode_qr_sequence(
      RustBuffer data,RustBuffer password,int8_t cleaned,
    RustCallStatus *_Nonnull out_status
    );
RustBuffer signer_23f1_generate_secret_key_qr(
      RustBuffer public_key,RustBuffer expected_seed_name,RustBuffer network_specs_key,RustBuffer seed_phrase,RustBuffer key_password,
    RustCallStatus *_Nonnull out_status
    );
void signer_23f1_import_derivations(
      RustBuffer seed_derived_keys,
    RustCallStatus *_Nonnull out_status
    );
RustBuffer signer_23f1_populate_derivations_has_pwd(
      RustBuffer seeds,RustBuffer seed_derived_keys,
    RustCallStatus *_Nonnull out_status
    );
RustBuffer signer_23f1_substrate_path_check(
      RustBuffer seed_name,RustBuffer path,RustBuffer network,
    RustCallStatus *_Nonnull out_status
    );
void signer_23f1_try_create_address(
      RustBuffer seed_name,RustBuffer seed_phrase,RustBuffer path,RustBuffer network,
    RustCallStatus *_Nonnull out_status
    );
void signer_23f1_history_init_history_with_cert(
      
    RustCallStatus *_Nonnull out_status
    );
void signer_23f1_history_init_history_no_cert(
      
    RustCallStatus *_Nonnull out_status
    );
void signer_23f1_history_device_was_online(
      
    RustCallStatus *_Nonnull out_status
    );
int8_t signer_23f1_history_get_warnings(
      
    RustCallStatus *_Nonnull out_status
    );
void signer_23f1_history_acknowledge_warnings(
      
    RustCallStatus *_Nonnull out_status
    );
void signer_23f1_history_entry_system(
      RustBuffer event,
    RustCallStatus *_Nonnull out_status
    );
void signer_23f1_history_seed_name_was_shown(
      RustBuffer seed_name,
    RustCallStatus *_Nonnull out_status
    );
RustBuffer signer_23f1_export_key_info(
      RustBuffer selected_names,
    RustCallStatus *_Nonnull out_status
    );
RustBuffer signer_23f1_keys_by_seed_name(
      RustBuffer seed_name,
    RustCallStatus *_Nonnull out_status
    );
RustBuffer signer_23f1_encode_to_qr(
      RustBuffer payload,int8_t is_danger,
    RustCallStatus *_Nonnull out_status
    );
RustBuffer signer_23f1_get_all_networks(
      
    RustCallStatus *_Nonnull out_status
    );
void signer_23f1_init_logging(
      RustBuffer tag,
    RustCallStatus *_Nonnull out_status
    );
RustBuffer signer_23f1_get_logs(
      
    RustCallStatus *_Nonnull out_status
    );
RustBuffer signer_23f1_get_log_details(
      uint32_t order,
    RustCallStatus *_Nonnull out_status
    );
void signer_23f1_clear_log_history(
      
    RustCallStatus *_Nonnull out_status
    );
void signer_23f1_handle_log_comment(
      RustBuffer user_input,
    RustCallStatus *_Nonnull out_status
    );
RustBuffer ffi_signer_23f1_rustbuffer_alloc(
      int32_t size,
    RustCallStatus *_Nonnull out_status
    );
RustBuffer ffi_signer_23f1_rustbuffer_from_bytes(
      ForeignBytes bytes,
    RustCallStatus *_Nonnull out_status
    );
void ffi_signer_23f1_rustbuffer_free(
      RustBuffer buf,
    RustCallStatus *_Nonnull out_status
    );
RustBuffer ffi_signer_23f1_rustbuffer_reserve(
      RustBuffer buf,int32_t additional,
    RustCallStatus *_Nonnull out_status
    );
