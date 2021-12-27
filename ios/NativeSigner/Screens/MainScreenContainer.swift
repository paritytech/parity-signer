//
//  ContentView.swift
//  NativeSigner
//
//  Created by Alexander Slesarev on 19.7.2021.
//

import SwiftUI


struct MainScreenContainer: View {
    @EnvironmentObject var data: SignerDataModel
    @GestureState private var dragOffset = CGSize.zero
    var body: some View {
        if data.onboardingDone {
            if data.authenticated {
            VStack {
                Header()
                ZStack {
                    VStack {
                        ScreenSelector()
                        Spacer()
                    }
                    ModalSelector()
                    AlertSelector()
                }
                .gesture(
                    DragGesture().updating($dragOffset, body: { (value, state, transaction) in
                        if value.startLocation.x < 20 && value.translation.width > 100 {
                            data.pushButton(buttonID: .GoBack)
                        }
                    })
                )
                //Certain places are better off without footer
                if data.actionResult.footer {
                    Footer()
                        .padding(.horizontal)
                        .padding(.vertical, 8)
                        .background(Color("Bg000"))
                }
            }
            .gesture(
                DragGesture().onEnded{drag in
                    if drag.translation.width < -20 {
                        data.pushButton(buttonID: .GoBack)
                    }
                }
            )
            .alert("Navigation error", isPresented: $data.parsingAlert, actions: {})
            } else {
                Button(action: {data.refreshSeeds()}) {
                    BigButton(
                        text: "Unlock app",
                        action: {
                            data.refreshSeeds()
                            data.totalRefresh()
                        }
                    )
                }
            }
        } else {
            if (data.protected) {
                if (data.canaryDead) {
                    Text("Please enable airplane mode, turn off bluetooth and wifi connection and disconnect all cables!")
                } else {
                    LandingView()
                }
            } else {
                Text("Please protect device with pin or password!")
            }
        }
    }
}

/*
 struct MainButtonScreen_Previews: PreviewProvider {
 static var previews: some View {
 MainScreenContainer()
 }
 }*/
