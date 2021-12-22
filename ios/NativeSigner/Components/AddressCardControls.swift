//
//  AddressCardControls.swift
//  NativeSigner
//
//  Created by Alexander Slesarev on 15.10.2021.
//

import SwiftUI

struct AddressCardControls: View {
    @EnvironmentObject var data: SignerDataModel
    var address: Address
    var rowHeight: CGFloat
    @State private var delete = false
    var body: some View {
        Text("multicontrol")
        /*if data.getMultiSelectionMode() {
            if data.multiSelected.contains(address) {
                Image(systemName: "checkmark.circle.fill").imageScale(.large)
            } else {
                Image(systemName: "circle").imageScale(.large)
            }
        } else {
            if (data.selectedAddress == address) {
                Button(action: {
                    data.selectSeed(seedName: data.selectedAddress!.seed_name)
                    data.proposeIncrement()
                    data.createAddress(password: "")
                }) {
                    ZStack {
                        RoundedRectangle(cornerRadius: 6)
                        Text("N+1").font(.system(size: 12, design: .monospaced))
                    }.frame(width: rowHeight, height: rowHeight)
                }
                Button(action: {
                    delete = true
                }) {
                    ZStack {
                        RoundedRectangle(cornerRadius: 6))
                        Image(systemName: "trash.slash"))
                    }
                    .frame(width: rowHeight, height: rowHeight)
                    .alert(isPresented: $delete, content: {
                        Alert(
                            title: Text("Delete key?"),
                            message: Text("You are about to delete key " + data.selectedAddress!.path),
                            primaryButton: .cancel(),
                            secondaryButton: .destructive(
                                Text("Delete"),
                                action: { data.deleteSelectedAddress()
                                }
                            )
                        )
                    })
                }
            }
        }
         */
    }
}

/*
struct AddressCardControls_Previews: PreviewProvider {
    static var previews: some View {
        AddressCardControls()
    }
}
*/
