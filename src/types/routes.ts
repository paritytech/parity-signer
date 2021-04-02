export type RootStackParamList = {
	Main: undefined;
	AddNetwork: { isNew: boolean } | undefined;
	ShowRecoveryPhrase: { isNew: true } | { isNew: false; seedPhrase: string };
	RenameWallet: { identity };
	DeleteWallet: { identity };
	CreateWallet: { isRecover: boolean } | undefined;
	Settings: undefined;
	MessageDetails: undefined;
	Empty: undefined;
	ReceiveBalance: { path: string };
	SendBalance: { path: string };
	SignTx:
		| undefined
		| {
				isScanningNetworkSpec: true;
		  };
	Security: undefined;
	SignedTx: undefined;
	TxDetails: undefined;
};
