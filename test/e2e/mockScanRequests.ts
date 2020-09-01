// Copyright 2015-2020 Parity Technologies (UK) Ltd.
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

export enum ScanTestRequest {
	SetRemarkExtrinsicKusama,
	TransferExtrinsicKusama,
	SetRemarkMultiPartKusama,
	SetRemarkHashKusama,
	SetRemarkExtrinsicPolkadot,
	TransferExtrinsicPolkadot,
	SetRemarkMultiPartPolkadot,
	EthereumTransaction,
	EthereumMessage,
	PasswordedAccountExtrinsic,
	AddNewNetwork
}

export const scanRequestDataMap = {
	[ScanTestRequest.SetRemarkExtrinsicKusama]:
		'49500000100005301025a4a03f84a19cf8ebda40e62358c592870691a9cf456138bb4829969d10fe96910000104114501440026040000b0a8d493285c2df73290dfb7e61f870f17b41801197a149ca93654499ea3dafe345f53c073281fc382d20758aee06ceae3014fd53df734d3e94d54642a56dd51b0a8d493285c2df73290dfb7e61f870f17b41801197a149ca93654499ea3dafe0ec11ec',
	[ScanTestRequest.TransferExtrinsicKusama]:
		'400b900000100005301025a4a03f84a19cf8ebda40e62358c592870691a9cf456138bb4829969d10fe969a00400ba9b70c65836c513128befe1a0b2cafa358c8168cde56a9731f8cab772428a77070010a5d4e8a503440026040000b0a8d493285c2df73290dfb7e61f870f17b41801197a149ca93654499ea3dafe3ea846400c95ce536f8ba122643fad07068b4d0777dc6ec39b6f169681b8121ab0a8d493285c2df73290dfb7e61f870f17b41801197a149ca93654499ea3dafe0ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11',
	[ScanTestRequest.SetRemarkMultiPartKusama]: [
		'4040500000200005301025a4a03f84a19cf8ebda40e62358c592870691a9cf456138bb4829969d10fe969ed100001dd10696d706f7274202a2061732052656163742066726f6d20277265616374273b0a696d706f7274207b20506c6174666f726d2c205374796c6553686565742c20546578742c2056696577207d2066726f6d202772656163742d6e6174697665273b0a696d706f7274207b204e61746976654d6f64756c6573207d2066726f6d202772656163742d6e6174697665273b0a0a636f6e737420696e737472756374696f6e73203d20506c6174666f726d2e73656c656374287b0a2020696f733a2060507265737320436d642b5220746f2072656c6f61642c5c6e436d642b44206f72207368616b6520666f7220646576206d656e75602c0a2020616e64726f69643a2060446f75626c65207461702052206f6e20796f7572206b6579626f61726420746f2072656c6f61642c5c6e5368616b65206f72207072657373206d656e7520627574746f6e20666f7220646576206d656e75602c0a7d293b0a0a6578706f72742064656661756c742066756e6374696f6e204170702829207b0a0a2020636f6e7374207b205375627374726174655369676e207d203d204e61746976654d6f64756c6573207c7c207b7d3b0a2020636f6e736f6c652e6c6f672827737562737472617465207369676e206973272c205375627374726174655369676e293b0a0a202072657475726e20280a202020203c56696577207374796c653d7b7374796c65732e636f6e7461696e65727d3e0a2020202020203c54657874207374796c653d7b7374796c65732e77656c636f6d657d3e57656c636f6d6520746f205265616374204e6174697665213c2f546578743e0a2020202020203c54657874207374796c653d7b7374796c65732e696e737472756374696f6e737d3e546f2067657420737461727465642c2065646974204170702e6a733c2f546578743e0a2020202020203c54657874207374796c653d7b7374796c65732e696e737472756374696f6e737d3e7b696e737472756374696f6e737d3c2f546578743e0a202020203c2f566965773e0a2020293b0a7d0a0a636f6e7374207374796c6573203d205374796c6553686565742e637265617465287b0a2020636f6e7461696e65723a207b0a20202020666c65783a20312c0a202020206a757374696679436f6e74656e743a202763656e746572272c0a20202020616c69676e4974656d733a202763656e746572272c0a202020206261636b67726f756e64436f6c6f723a202723463546434646272c0a20207d2c0a202077656c636f6d653a207b0a20202020666f6e7453697a653a2032302c0a2020202074657874416c69676e3a202763656e746572272c0a202020206d617267696e3a2031302c0a20207d2c0ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11',
		'400cd00000200010a2020696e737472756374696f6e733a207b0a2020202074657874416c69676e3a202763656e746572272c0a20202020636f6c6f723a202723333333333333272c0a202020206d617267696e426f74746f6d3a20352c0a20207d2c0a7d293b0a5500440026040000b0a8d493285c2df73290dfb7e61f870f17b41801197a149ca93654499ea3dafe5e1efdd919bb098a956954a74f230c9466af21bd3911d2f876c9a0564b1b00e1b0a8d493285c2df73290dfb7e61f870f17b41801197a149ca93654499ea3dafe0ec11ec11ec11ec11'
	],
	[ScanTestRequest.SetRemarkHashKusama]:
		'46800000100005301015a4a03f84a19cf8ebda40e62358c592870691a9cf456138bb4829969d10fe96985b8101f780a0e5a087df2243ef1b2aa85e40907eb7be740598e515c025776a1b0a8d493285c2df73290dfb7e61f870f17b41801197a149ca93654499ea3dafe0ec11',
	[ScanTestRequest.SetRemarkExtrinsicPolkadot]:
		'4990000010000530102ae8673f5391fa714f18f070edfc7db1c6ccd9ee923d0f9b061f28c5d5978162a1000010411d5010000000000000000000091b171bb158e2d3848fa23a9f1c25182fb8e20313b2c1eb49219da7a70ce90c3547562cde4345701ab5ff4721f720c837e2ab7eace195c141c6ff026871fc24f91b171bb158e2d3848fa23a9f1c25182fb8e20313b2c1eb49219da7a70ce90c30ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec',
	[ScanTestRequest.TransferExtrinsicPolkadot]:
		'400bd0000010000530102ae8673f5391fa714f18f070edfc7db1c6ccd9ee923d0f9b061f28c5d5978162aa0050052f375f446eb485845cef8b99f2eac811304bed326e15e7af23585bb81d9752e070010a5d4e825020000000000000000000091b171bb158e2d3848fa23a9f1c25182fb8e20313b2c1eb49219da7a70ce90c324f94af637ccb3a44abc152a86e02add0989f8a7e1a840c2920b9cd7ba7fe09091b171bb158e2d3848fa23a9f1c25182fb8e20313b2c1eb49219da7a70ce90c30ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11',
	[ScanTestRequest.SetRemarkMultiPartPolkadot]: [
		'404050000020000530102ae8673f5391fa714f18f070edfc7db1c6ccd9ee923d0f9b061f28c5d5978162aed100003dd10696d706f7274202a2061732052656163742066726f6d20277265616374273b0a696d706f7274207b20506c6174666f726d2c205374796c6553686565742c20546578742c2056696577207d2066726f6d202772656163742d6e6174697665273b0a696d706f7274207b204e61746976654d6f64756c6573207d2066726f6d202772656163742d6e6174697665273b0a0a636f6e737420696e737472756374696f6e73203d20506c6174666f726d2e73656c656374287b0a2020696f733a2060507265737320436d642b5220746f2072656c6f61642c5c6e436d642b44206f72207368616b6520666f7220646576206d656e75602c0a2020616e64726f69643a2060446f75626c65207461702052206f6e20796f7572206b6579626f61726420746f2072656c6f61642c5c6e5368616b65206f72207072657373206d656e7520627574746f6e20666f7220646576206d656e75602c0a7d293b0a0a6578706f72742064656661756c742066756e6374696f6e204170702829207b0a0a2020636f6e7374207b205375627374726174655369676e207d203d204e61746976654d6f64756c6573207c7c207b7d3b0a2020636f6e736f6c652e6c6f672827737562737472617465207369676e206973272c205375627374726174655369676e293b0a0a202072657475726e20280a202020203c56696577207374796c653d7b7374796c65732e636f6e7461696e65727d3e0a2020202020203c54657874207374796c653d7b7374796c65732e77656c636f6d657d3e57656c636f6d6520746f205265616374204e6174697665213c2f546578743e0a2020202020203c54657874207374796c653d7b7374796c65732e696e737472756374696f6e737d3e546f2067657420737461727465642c2065646974204170702e6a733c2f546578743e0a2020202020203c54657874207374796c653d7b7374796c65732e696e737472756374696f6e737d3e7b696e737472756374696f6e737d3c2f546578743e0a202020203c2f566965773e0a2020293b0a7d0a0a636f6e7374207374796c6573203d205374796c6553686565742e637265617465287b0a2020636f6e7461696e65723a207b0a20202020666c65783a20312c0a202020206a757374696679436f6e74656e743a202763656e746572272c0a20202020616c69676e4974656d733a202763656e746572272c0a202020206261636b67726f756e64436f6c6f723a202723463546434646272c0a20207d2c0a202077656c636f6d653a207b0a20202020666f6e7453697a653a2032302c0a2020202074657874416c69676e3a202763656e746572272c0a202020206d617267696e3a2031302c0a20207d2c0ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11',
		'400d100000200010a2020696e737472756374696f6e733a207b0a2020202074657874416c69676e3a202763656e746572272c0a20202020636f6c6f723a202723333333333333272c0a202020206d617267696e426f74746f6d3a20352c0a20207d2c0a7d293b0a85020000000000000000000091b171bb158e2d3848fa23a9f1c25182fb8e20313b2c1eb49219da7a70ce90c3b70a4c547cfa0e53aae485b9250dbd84d357681a0cc355e5dc3c237bca54439e91b171bb158e2d3848fa23a9f1c25182fb8e20313b2c1eb49219da7a70ce90c30ec11ec11'
	],
	[ScanTestRequest.EthereumTransaction]: {
		data:
			'{"action":"signTransaction","data":{"account":"311a6D0334141597828999cc89cFeb738d06Fdc2","rlp":"eb808504a817c80082520894486e7833587284ccde66b31144589f92cb16632887038d7ea4c68000802a8080"}}',
		rawData:
			'400bb7b22616374696f6e223a227369676e5472616e73616374696f6e222c2264617461223a7b226163636f756e74223a2233313161364430333334313431353937383238393939636338396346656237333864303646646332222c22726c70223a2265623830383530346138313763383030383235323038393434383665373833333538373238346363646536366233313134343538396639326362313636333238383730333864376561346336383030303830326138303830227d7d0ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11'
	},
	[ScanTestRequest.EthereumMessage]: {
		data:
			'{"action":"signData","data":{"account":"311a6D0334141597828999cc89cFeb738d06Fdc2","data":"426565722069732074686572617079"}}',
		rawData:
			'47b7b22616374696f6e223a227369676e44617461222c2264617461223a7b226163636f756e74223a2233313161364430333334313431353937383238393939636338396346656237333864303646646332222c2264617461223a22343236353635373232303639373332303734363836353732363137303739227d7d0ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec11ec'
	},
	[ScanTestRequest.PasswordedAccountExtrinsic]:
		'49500000100005301028a476a30a7fd07657fa2e3648ec74b76aea0e9c032772ed108b78924539ce61f10000104112501000026040000b0a8d493285c2df73290dfb7e61f870f17b41801197a149ca93654499ea3dafe3ca87fad296dc36e2070c536142e2e9a74fe221f0b9dbce0e9f85921bd0c5e77b0a8d493285c2df73290dfb7e61f870f17b41801197a149ca93654499ea3dafe0ec11ec',
	[ScanTestRequest.AddNewNetwork]:
		'{"color":"#98F789","decimals":18,"genesisHash":"0x783c78945a4e4a3118190bcf93002bb2d2903192bed10040eb52d54500aade36","prefix":0,"title":"Acala Mandala TC4","unit":"ACA"}'
};
