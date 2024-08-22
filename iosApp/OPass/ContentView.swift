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
    var body: some View {
        NavigationStack {
            VStack {
                Image(systemName: "globe")
                    .imageScale(.large)
                    .foregroundStyle(.tint)
                Text(Greeting().greet())
            }
            .toolbar { toolbar() }
        }
    }

    @ToolbarContentBuilder
    private func toolbar() -> some ToolbarContent {
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
