#!/usr/bin/env bash
set -e

keystore="$(readlink -f "$1")"
version="$2"

# Get latest android-sdk-linux version for apksigner path
ANDROID_BUILD_TOOLS_PATH=$(find /opt/android-sdk-linux/build-tools/ -maxdepth 1 -type d | sort -V | tail -n 1)

pushd "$(dirname "${0}")"/../android
  echo "[+] Signing bundle"
  "$ANDROID_BUILD_TOOLS_PATH/apksigner" sign --ks "$keystore" ../android/build/outputs/apk/release/android-release-unsigned.apk
  cp ../android/build/outputs/apk/release/android-release-unsigned.apk ../signer-"$version"-build.apk
popd
