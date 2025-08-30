//
//  AppearanceSettingsView.swift
//  OPass
//
//  Created by Brian Chang on 2024/8/21.
//
//  SPDX-FileCopyrightText: 2024-2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import SwiftUI

struct AppearanceSettingsView: View {
    @AppStorage("HapticFeedback") private var hapticFeedback = true
    @AppStorage("DarkMode") private var darkMode: DarkMode = .system

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
