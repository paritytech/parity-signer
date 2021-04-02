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

import { StackNavigationProp } from '@react-navigation/stack';
import React from 'react';
import { View, Text } from 'react-native';

// TODO use typescript 3.8's type import, Wait for prettier update.
import { AccountsStoreStateWithIdentity } from 'types/identityTypes';
import { NavigationAccountIdentityProps } from 'types/props';
import { RootStackParamList } from 'types/routes';
import { withCurrentIdentity } from 'utils/HOC';

interface Props {
	path: string;
	networkKey: string;
	navigation: StackNavigationProp<RootStackParamList, 'SendBalance'>;
	accountsStore: AccountsStoreStateWithIdentity;
}

function SendBalance({}: NavigationAccountIdentityProps<
	'SendBalance'
>): React.ReactElement {
	return (
		<View>
			<Text>To be implemented</Text>
		</View>
		// <PathDetailsView
		// 	accountsStore={accountsStore}
		// 	navigation={navigation}
		// 	path={path}
		// 	networkKey={networkKey}
		// />
	);
}

export default withCurrentIdentity(SendBalance);
