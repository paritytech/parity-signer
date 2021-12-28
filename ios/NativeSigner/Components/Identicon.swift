//
//  Identicon.swift
//  NativeSigner
//
//  Created by Alexander Slesarev on 28.12.2021.
//

import SwiftUI

struct Identicon: View {
    let identicon: String
    let rowHeight: CGFloat = 28
    var body: some View {
        Image(uiImage: UIImage(data: Data(fromHexEncodedString: identicon) ?? Data()) ?? UIImage())
            .resizable(resizingMode: .stretch)
            .frame(width: rowHeight, height: rowHeight)
    }
}

/*
struct Identicon_Previews: PreviewProvider {
    static var previews: some View {
        Identicon()
    }
}
 */
