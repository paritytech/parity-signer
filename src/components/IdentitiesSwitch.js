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
import { FlatList, Modal, View, TouchableWithoutFeedback } from 'react-native';
import { withNavigation, ScrollView } from 'react-navigation';

import ButtonIcon from './ButtonIcon';
import colors from '../colors';
import fontStyles from '../fontStyles';
import Separator from './Separator';
import { withAccountStore } from '../util/HOC';
import { getIdentityName } from '../util/identitiesUtils';
import testIDs from '../../e2e/testIDs';

function IdentitiesSwitch({ navigation, accounts }) {
	const defaultVisible = navigation.getParam('isSwitchOpen', false);
	const [visible, setVisible] = useState(defaultVisible);
	const { currentIdentity, identities } = accounts.state;

	const onIdentitySelected = async identity => {
		setVisible(false);
		await accounts.selectIdentity(identity);
		navigation.navigate('AccountNetworkChooser');
	};

	const renderIdentityOptions = identity => {
		return (
			<>
				<ButtonIcon
					title="Manage Identity"
					onPress={async () => {
						setVisible(false);
						await accounts.selectIdentity(identity);
						navigation.navigate('IdentityManagement');
					}}
					iconBgStyle={styles.i_arrowBg}
					iconType="antdesign"
					iconName="arrowright"
					iconSize={18}
					testID={testIDs.IdentitiesSwitch.manageIdentityButton}
					textStyle={fontStyles.t_regular}
					style={styles.i_arrowStyle}
				/>
				<ButtonIcon
					title="Show Recovery Phrase"
					onPress={async () => {
						setVisible(false);
						await accounts.selectIdentity(identity);
						navigation.navigate('IdentityBackup', { isNew: false });
					}}
					iconBgStyle={styles.i_arrowBg}
					iconType="antdesign"
					iconName="arrowright"
					iconSize={18}
					textStyle={fontStyles.t_regular}
					style={styles.i_arrowStyle}
				/>
			</>
		);
	};

	const renderCurrentIdentityCard = () => {
		if (!currentIdentity) return;

		const currentIdentityTitle = getIdentityName(currentIdentity, identities);

		return (
			<>
				<ButtonIcon
					title={currentIdentityTitle}
					onPress={() => onIdentitySelected(currentIdentity)}
					iconType="antdesign"
					iconName="user"
					iconSize={40}
					style={{ paddingLeft: 8 * 2 }}
					textStyle={fontStyles.h1}
				/>
				{renderIdentityOptions(currentIdentity)}
				<Separator style={{ marginBottom: 0 }} />
			</>
		);
	};

	const renderSettings = () => {
		return (
			<>
				<ButtonIcon
					title="About"
					onPress={() => {
						setVisible(false);
						navigation.navigate('About');
						// go to Settings;
					}}
					iconType="antdesign"
					iconName="info"
					iconSize={24}
					textStyle={fontStyles.t_big}
					style={{ paddingLeft: 8 * 4 }}
				/>
				<ButtonIcon
					title="Terms and Conditions"
					onPress={() => {
						setVisible(false);
						navigation.navigate('TermsAndConditions', { disableButtons: true });
					}}
					iconBgStyle={styles.i_arrowBg}
					iconType="antdesign"
					iconName="arrowright"
					iconSize={18}
					textStyle={fontStyles.t_regular}
					style={styles.i_arrowStyle}
				/>
				<ButtonIcon
					title="Privacy Policy"
					onPress={() => {
						setVisible(false);
						navigation.navigate('PrivacyPolicy');
					}}
					iconBgStyle={styles.i_arrowBg}
					iconType="antdesign"
					iconName="arrowright"
					iconSize={18}
					textStyle={fontStyles.t_regular}
					style={styles.i_arrowStyle}
				/>
			</>
		);
	};

	const renderNonSelectedIdentity = ({ item, index }) => {
		const identity = item;
		const title = identity.name || `identity_${index.toString()}`;

		return (
			<ButtonIcon
				dropdown={false}
				renderDropdownElement={() => renderIdentityOptions(identity)}
				title={title}
				onPress={() => onIdentitySelected(identity)}
				iconType="antdesign"
				iconName="user"
				iconSize={24}
				style={{ paddingLeft: 8 * 4 }}
				textStyle={fontStyles.h2}
			/>
		);
	};

	const renderIdentities = () => {
		// if no identity or the only one we have is the selected one

		if (!identities.length || (identities.length === 1 && currentIdentity))
			return;

		const identitiesToShow = currentIdentity
			? identities.filter(
					identity => identity.encryptedSeed !== currentIdentity.encryptedSeed
			  )
			: identities;

		return (
			<>
				<ScrollView style={{ maxHeight: 180 }}>
					<FlatList
						data={identitiesToShow}
						renderItem={renderNonSelectedIdentity}
						keyExtractor={item => item.encryptedSeed}
						style={{ paddingVertical: identities.length > 5 ? 8 : 0 }}
					/>
				</ScrollView>
				{identities.length > 5 && (
					<Separator
						shadow={true}
						style={{ backgroundColor: 'transparent', marginTop: 0 }}
						shadowStyle={{ opacity: 0.9 }}
					/>
				)}
			</>
		);
	};

	return (
		<View>
			<ButtonIcon
				onPress={() => setVisible(!visible)}
				iconName="user"
				iconType="antdesign"
				iconBgStyle={{ backgroundColor: 'transparent' }}
				testID={testIDs.IdentitiesSwitch.toggleButton}
			/>

			<Modal
				animationType="fade"
				visible={visible}
				transparent={true}
				onRequestClose={() => setVisible(false)}
			>
				<TouchableWithoutFeedback
					style={{ flex: 1 }}
					onPressIn={() => setVisible(false)}
				>
					<View
						testID={testIDs.IdentitiesSwitch.modal}
						style={styles.container}
						onPress={() => {
							setVisible(false);
						}}
					>
						<View style={styles.card}>
							{renderCurrentIdentityCard()}
							{renderIdentities()}
							{accounts.getAccounts().size > 0 && (
								<>
									<ButtonIcon
										title="Legacy Accounts"
										onPress={() => {
											setVisible(false);
											navigation.navigate('LegacyAccountList');
										}}
										iconName="solution1"
										iconType="antdesign"
										iconSize={24}
										textStyle={fontStyles.t_big}
										style={{ paddingLeft: 8 * 4 }}
									/>
									<Separator />
								</>
							)}

							{__DEV__ && (
								<ButtonIcon
									title="Add Identity"
									testID={testIDs.IdentitiesSwitch.addIdentityButton}
									onPress={() => {
										setVisible(false);
										navigation.navigate('IdentityNew');
									}}
									iconName="plus"
									iconType="antdesign"
									iconSize={24}
									textStyle={fontStyles.t_big}
									style={{ paddingLeft: 8 * 4 }}
								/>
							)}
							<Separator />
							<ButtonIcon
								title="Add legacy account"
								onPress={() => {
									setVisible(false);
									navigation.navigate('AccountNew');
								}}
								iconName="plus"
								iconType="antdesign"
								iconSize={24}
								textStyle={fontStyles.t_big}
								style={{ paddingLeft: 8 * 4 }}
							/>
							<Separator />
							{renderSettings()}
						</View>
					</View>
				</TouchableWithoutFeedback>
			</Modal>
		</View>
	);
}

const styles = {
	card: {
		backgroundColor: colors.bg,
		borderRadius: 5,
		paddingBottom: 16,
		paddingTop: 8
	},
	container: {
		backgroundColor: 'rgba(0,0,0,0.8)',
		flex: 1,
		justifyContent: 'center',
		marginTop: -24,
		paddingLeft: 16,
		paddingRight: 16
	},
	i_arrowBg: {
		backgroundColor: 'rgba(0,0,0,0)',
		width: 12
	},
	i_arrowStyle: {
		opacity: 0.7,
		paddingBottom: 6,
		paddingLeft: 8 * 8,
		paddingTop: 0
	}
};

export default withAccountStore(withNavigation(IdentitiesSwitch));
