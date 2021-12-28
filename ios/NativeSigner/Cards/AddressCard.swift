//
//  IdentityCard.swift
//  NativeSigner
//
//  Created by Alexander Slesarev on 3.8.2021.
//

import SwiftUI

/**
 * Card for showing any address.
 * Accepts Address object
 */
struct AddressCard: View {
    @EnvironmentObject var data: SignerDataModel
    var address: Address
    @GestureState private var dragOffset = CGSize.zero
    let rowHeight: CGFloat = 28
    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 4).foregroundColor(Color("Bg200")).frame(height: 44)
            HStack {
                Identicon(identicon: address.identicon)
                VStack (alignment: .leading) {
                    HStack {
                        Text(address.seed_name)
                            Text(address.path)
                        if address.has_pwd {
                            Text("///").foregroundColor(Color("Crypto400"))
                                .font(FCrypto(style: .body2))
                            Image(systemName: "lock").foregroundColor(Color("Crypto400"))
                                .font(FCrypto(style: .body2))
                        }
                    }.foregroundColor(Color("Crypto400"))
                        .font(FCrypto(style: .body2))
                    //Here we could have shortened base58 address when buttons are shown, but we don't need to
                    Text(address.base58.truncateMiddle(length: 8)).foregroundColor(Color("Text400"))
                        .font(FCrypto(style: .body1))
                }
                Spacer()
            }.padding(.horizontal, 8)
        }
    }
}
