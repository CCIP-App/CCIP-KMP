//
//  ContentView.swift
//  OPass
//
//  Created by Brian Chang on 2024/8/18.
//  2024 OPass.
//

import Shared
import SwiftUI

struct ContentView: View {
    @AppStorage("EventID") private var eventID = ""
    @State private var selectEventSheetPresented = false

    var body: some View {
        NavigationStack {
            Group {
                if eventID.isEmpty {
                    ProgressView("Loading")
                        .onAppear { selectEventSheetPresented.toggle() }
                } else {
                    Text(eventID)
                }
            }
            .toolbar { toolbar() }
            .sensoryFeedback(.selection, trigger: selectEventSheetPresented) { $1 }
            .sheet(isPresented: $selectEventSheetPresented) {
                SelectEventView()
            }
        }
        .analyticsScreen(name: "ContentView")
    }

    @ToolbarContentBuilder
    private func toolbar() -> some ToolbarContent {
        ToolbarItem(placement: .topBarLeading) {
            Button {
                selectEventSheetPresented.toggle()
            } label: {
                Image(systemName: "rectangle.stack")
            }
        }

        ToolbarItem(placement: .topBarTrailing) {
            NavigationLink {
                SettingsView()
            } label: {
                Image(systemName: "gearshape")
            }
        }
    }
}

#Preview {
    ContentView()
}
