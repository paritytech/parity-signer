// Copyright 2015-2019 Parity Technologies (UK) Ltd.
// This file is part of Parity.

// Parity is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// Parity is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with Parity.  If not, see <http://www.gnu.org/licenses/>.

'use strict';

import React, { useState } from 'react';
import { ScrollView, StyleSheet, Text } from 'react-native';
import { withNavigation } from 'react-navigation';

import colors from '../colors';
import AccountCard from '../components/AccountCard';
import Button from '../components/Button';
import fonts from '../fonts';
import {
	NETWORK_LIST,
	UnknownNetworkKeys,
	SubstrateNetworkKeys,
	NetworkProtocols
} from '../constants';
import { navigateToPathsList, unlockSeed } from '../util/navigationHelpers';
import { withAccountStore } from '../util/HOC';
import { alertPathDerivationError } from '../util/alertUtils';
import { getAvailableNetworkKeys } from '../util/identitiesUtils';

function AccountNetworkChooser({ navigation, accounts }) {
	const isNew = navigation.getParam('isNew', false);
	const [shouldShowMoreNetworks, setShouldShowMoreNetworks] = useState(false);
	const excludedNetworks = [UnknownNetworkKeys.UNKNOWN];
	if (!__DEV__) {
		excludedNetworks.push(SubstrateNetworkKeys.SUBSTRATE_DEV);
		excludedNetworks.push(SubstrateNetworkKeys.KUSAMA_DEV);
	}

	const getNetworkKeys = ([networkKey]) => {
		const availableNetworks = getAvailableNetworkKeys(
			accounts.state.currentIdentity
		);
		if (excludedNetworks.includes(networkKey)) return false;
		if (isNew) return true;
		if (shouldShowMoreNetworks) {
			return !availableNetworks.includes(networkKey);
		}
		return availableNetworks.includes(networkKey);
	};

	const renderShowMoreButton = () => {
		if (isNew) return;
		if (!shouldShowMoreNetworks) {
			return (
				<>
					<Button
						title="Add Network Account"
						onPress={() => setShouldShowMoreNetworks(true)}
					/>
					<Button
						title="Scan"
						onPress={() => navigation.navigate('QrScanner')}
					/>
				</>
			);
		} else {
			return (
				<Button
					title="Show Existed Network Account"
					onPress={() => setShouldShowMoreNetworks(false)}
				/>
			);
		}
	};

	return (
		<ScrollView style={styles.body}>
			<Text style={styles.title}>
				{isNew ? 'CREATE YOUR FIRST KEYPAIR' : 'CHOOSE NETWORK'}{' '}
			</Text>
			{Object.entries(NETWORK_LIST)
				.filter(getNetworkKeys)
				.map(([networkKey, networkParams]) => (
					<AccountCard
						address={''}
						networkKey={networkKey}
						onPress={async () => {
							if (isNew) {
								const { prefix, pathId, protocol } = networkParams;
								const seed = await unlockSeed(navigation);
								let derivationSucceed;
								if (protocol === NetworkProtocols.SUBSTRATE) {
									derivationSucceed = await accounts.deriveNewPath(
										`//${pathId}//default`,
										seed,
										prefix,
										networkKey
									);
								} else {
									derivationSucceed = await accounts.deriveEthereumAccount(
										seed,
										networkKey
									);
								}
								if (derivationSucceed) {
									navigateToPathsList(navigation, networkKey);
								} else {
									alertPathDerivationError();
								}
							} else {
								navigation.navigate('PathsList', { networkKey });
							}
						}}
						title={networkParams.title}
					/>
				))}
			{renderShowMoreButton()}
		</ScrollView>
	);
}

export default withAccountStore(withNavigation(AccountNetworkChooser));

const styles = StyleSheet.create({
	body: {
		backgroundColor: colors.bg,
		flex: 1,
		flexDirection: 'column',
		overflow: 'hidden'
	},
	header: {
		alignItems: 'center',
		flexDirection: 'row',
		justifyContent: 'center'
	},
	title: {
		color: colors.bg_text_sec,
		flexDirection: 'column',
		fontFamily: fonts.bold,
		fontSize: 18,
		justifyContent: 'center',
		marginTop: 16,
		paddingLeft: 72
	}
});
