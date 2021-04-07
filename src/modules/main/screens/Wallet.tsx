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

import React, { ReactElement, useContext, useMemo } from 'react';
import { BackHandler, FlatList, FlatListProps } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialIcons';
import { useFocusEffect } from '@react-navigation/native';

import { NetworkCard } from '../components/NetworkCard';
import OnBoardingView from '../components/OnBoarding';
import NoCurrentIdentity from '../components/NoCurrentIdentity';

import { colors } from 'styles';
import {
	SubstrateNetworkKeys,
	UnknownNetworkKeys
} from 'constants/networkSpecs';
import { NetworksContext } from 'stores/NetworkContext';
import { AccountsContext } from 'stores/AccountsContext';
import testIDs from 'e2e/testIDs';
import {
	isEthereumNetworkParams,
	isSubstrateNetworkParams,
	NetworkParams
} from 'types/networkTypes';
import { NavigationProps } from 'types/props';
import { getExistedNetworkKeys, getIdentityName } from 'utils/identitiesUtils';
import { navigateToReceiveBalance } from 'utils/navigationHelpers';
import TouchableItem from 'components/TouchableItem';
import { SafeAreaViewContainer } from 'components/SafeAreaContainer';
import AccountPrefixedTitle from 'components/AccountPrefixedTitle';
import ScreenHeading from 'components/ScreenHeading';
import NavigationTab from 'components/NavigationTab';

const filterNetworks = (
	networkList: Map<string, NetworkParams>,
	extraFilter?: (networkKey: string, shouldExclude: boolean) => boolean
): Array<[string, NetworkParams]> => {
	const excludedNetworks = [UnknownNetworkKeys.UNKNOWN];
	const filterNetworkKeys = ([networkKey]: [string, any]): boolean => {
		const shouldExclude = excludedNetworks.includes(networkKey);
		if (extraFilter !== undefined)
			return extraFilter(networkKey, shouldExclude);
		return !shouldExclude;
	};
	return Array.from(networkList.entries())
		.filter(filterNetworkKeys)
		.sort((a, b) => a[1].order - b[1].order);
};

function Wallet({ navigation }: NavigationProps<'Wallet'>): React.ReactElement {
	const accountsStore = useContext(AccountsContext);
	const { identities, currentIdentity, loaded } = accountsStore.state;
	const networkContextState = useContext(NetworksContext);
	const { allNetworks } = networkContextState;
	// catch android back button and prevent exiting the app
	useFocusEffect(
		React.useCallback((): any => {
			const handleBackButton = (): boolean => true;
			const backHandler = BackHandler.addEventListener(
				'hardwareBackPress',
				handleBackButton
			);
			return (): void => backHandler.remove();
		}, [])
	);

	const availableNetworks = useMemo(
		() =>
			currentIdentity
				? getExistedNetworkKeys(currentIdentity, networkContextState)
				: [],
		[currentIdentity, networkContextState]
	);

	const networkList = useMemo(
		() =>
			filterNetworks(allNetworks, networkKey => {
				return availableNetworks.includes(networkKey);
			}),
		[availableNetworks, allNetworks]
	);

	if (!loaded) return <SafeAreaViewContainer />;
	if (identities.length === 0) return <OnBoardingView />;
	if (currentIdentity === null) return <NoCurrentIdentity />;

	const getListOptions = (): Partial<FlatListProps<any>> => {
		return {
			ListFooterComponent: (
				<>
					<TouchableItem
						onPress={(): void => navigation.navigate('SignTransaction')}
						style={{
							display: 'flex',
							flexDirection: 'row'
						}}
					>
						<Icon name="add" color={colors.text.main} size={30} />
						<AccountPrefixedTitle title="Sign a polkadot-js transaction" />
					</TouchableItem>
					<TouchableItem
						onPress={(): void => navigation.navigate('AddNetwork')}
						style={{
							display: 'flex',
							flexDirection: 'row'
						}}
					>
						<Icon name="add" color={colors.text.main} size={30} />
						<AccountPrefixedTitle title="Add a network" />
					</TouchableItem>
				</>
			)
		};
	};

	const onNetworkChosen = async (
		networkKey: string,
		networkParams: NetworkParams
	): Promise<void> => {
		if (isSubstrateNetworkParams(networkParams)) {
			// navigate to substrate account
			const { pathId } = networkParams;
			const fullPath = `//${pathId}`;
			navigateToReceiveBalance(navigation, networkKey, fullPath);
		} else {
			// navigate to ethereum account
			navigateToReceiveBalance(navigation, networkKey, networkKey);
		}
	};

	const renderNetwork = ({
		item
	}: {
		item: [string, NetworkParams];
	}): ReactElement => {
		const [networkKey, networkParams] = item;
		const networkIndexSuffix = isEthereumNetworkParams(networkParams)
			? networkParams.ethereumChainId
			: networkParams.pathId;
		return (
			<NetworkCard
				key={networkKey}
				testID={testIDs.Wallet.networkButton + networkIndexSuffix}
				networkKey={networkKey}
				onPress={(): Promise<void> =>
					onNetworkChosen(networkKey, networkParams)
				}
				title={networkParams.title}
			/>
		);
	};

	return (
		<SafeAreaViewContainer>
			<ScreenHeading title={getIdentityName(currentIdentity, identities)} />
			<FlatList
				data={networkList}
				keyExtractor={(item: [string, NetworkParams]): string => item[0]}
				renderItem={renderNetwork}
				testID={testIDs.Wallet.chooserScreen}
				{...getListOptions()}
			/>
			<NavigationTab />
		</SafeAreaViewContainer>
	);
}

export default Wallet;
