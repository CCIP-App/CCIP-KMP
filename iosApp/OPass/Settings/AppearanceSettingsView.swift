//
//  AppearanceSettingsView.swift
//  OPass
//
//  Created by Brian Chang on 2024/8/21.
//  2024 OPass.
//

import SwiftUI

struct AppearanceSettingsView: View {
    // MARK: - Variable
    @AppStorage("HapticFeedback") private var hapticFeedback = true
    @AppStorage("DarkMode") private var darkMode: DarkMode = .system

    // MARK: - View
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
                        Image(systemName: "moon.fill")
                            .resizable()
                            .scaledToFit()
                            .foregroundStyle(.indigo)
                            .symbolEffect(.bounce, value: darkMode)
                    }
                    .labelStyle(CenterLabelStyle())
                }
                .sensoryFeedback(.success, trigger: darkMode) { _, _ in hapticFeedback }
            }

            Button("Reset Appearance") {
                darkMode = .system
            }
            .foregroundStyle(.red)
        }
        .analyticsScreen(name: "AppearanceSettingsView")
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle("Appearance")
        .listSectionSpacing(.compact)
    }
}

#Preview {
    AppearanceSettingsView()
}
