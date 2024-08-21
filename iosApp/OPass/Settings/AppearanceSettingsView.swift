//
//  AppearanceSettingsView.swift
//  OPass
//
//  Created by Brian Chang on 2024/8/21.
//  2024 OPass.
//

import SwiftUI

struct AppearanceSettingsView: View {
    @AppStorage("DarkMode") private var darkMode: DarkMode = .system
    @Environment(\.colorScheme) private var colorScheme

    var body: some View {
        Form {
            Section {
                Picker(selection: self.$darkMode) {
                    Text("System").tag(DarkMode.system)
                    Text("Enable").tag(DarkMode.enable)
                    Text("Disable").tag(DarkMode.disable)
                } label: {
                    Label {
                        Text("Dark Mode")
                    } icon: {
                        Image(systemName: self.colorScheme == .light ? "sun.max.fill" : "moon.fill")
                            .foregroundStyle(self.colorScheme == .light ? .yellow : .indigo)
                            .symbolEffect(.bounce, value: self.darkMode)
                    }
                }
            }
        }
        .analyticsScreen(name: "AppearanceSettingsView")
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle("Appearance")
    }
}

#Preview {
    AppearanceSettingsView()
}
