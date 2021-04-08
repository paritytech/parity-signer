// Copyright 2015-2020 Parity Technologies (UK) Ltd.
// Copyright 2021 Commonwealth Labs, Inc.
// This file is part of Layer Wallet.

// Layer Wallet is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// Layer Wallet is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with Layer Wallet. If not, see <http://www.gnu.org/licenses/>.

import React, { useContext, useState } from 'react';
import { View } from 'react-native';
import { showMessage } from 'react-native-flash-message';

import { components } from 'styles';
import { AccountsContext } from 'stores/AccountsContext';
import Button from 'components/Button';
import TextInput from 'components/TextInput';
import { NavigationAccountIdentityProps } from 'types/props';

type Props = NavigationAccountIdentityProps<'RenameWallet'>;

function RenameWallet({ navigation, route }: Props): React.ReactElement {
	const accountsStore = useContext(AccountsContext);
	const { identity } = route.params;
	const [newIdentityName, setNewIdentityName] = useState(identity?.name || '');

	if (!identity) return <View />;

	const onChangeIdentity = async (name: string): Promise<void> => {
		setNewIdentityName(name);
	};

	const onSaveIdentity = async (): Promise<void> => {
		try {
			accountsStore.updateIdentityName(newIdentityName);
			showMessage('Wallet renamed.');
			navigation.goBack();
		} catch (err) {
			showMessage(`Could not rename: ${err.message}`);
		}
	};

	return (
		<View style={components.page}>
			<TextInput
				label="Display Name"
				onChangeText={onChangeIdentity}
				value={newIdentityName}
				placeholder="Enter a new wallet name"
				autofocus
			/>
			<Button title="Save" onPress={onSaveIdentity} fluid={true} />
		</View>
	);
}

export default RenameWallet;
