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
    @State private var selectEventSheetPresented = false
    var body: some View {
        NavigationStack {
            VStack {
                Image(systemName: "globe")
                    .imageScale(.large)
                    .foregroundStyle(.tint)
            }
            .toolbar { toolbar() }
            .sheet(isPresented: $selectEventSheetPresented) {
                SelectEventView()
            }
        }
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
