//
//  SettingsView.swift
//  OPass
//
//  Created by Brian Chang on 2024/8/20.
//  2024 OPass.
//

import SwiftUI

struct SettingsView: View {
    var body: some View {
        Form {
            Section {
                NavigationLink {
                    AppearanceSettingsView()
                } label: {
                    Label("Appearance", systemImage: "sun.max.fill")
                }
            }

            bottomText()
        }
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle("Settings")
        .listSectionSpacing(0)
    }

    @ViewBuilder
        private func bottomText() -> some View {
            Section {
                HStack {
                    Spacer()
                    VStack {
                        Text("Version \(Bundle.main.releaseVersionNumber ?? "") (\(Bundle.main.buildVersionNumber ?? ""))")
                            .foregroundStyle(.gray)
                            .font(.footnote)
                        Text("Made with Love")
                            .foregroundStyle(.gray)
                            .font(.caption)
                            .bold()
                    }
                    Spacer()
                }
            }
            .listRowBackground(Color.clear)
            .padding(.bottom, 5)
        }
}

private struct AppearanceSettingsView: View {
    @AppStorage("DarkMode") private var darkMode: DarkMode = .system

    var body: some View {
        Form {
            Section {
                Picker(
                    "Dark Mode",
                    systemImage: self.darkMode.colorScheme == .light ? "sun.max.fill" : "moon.fill",
                    selection: self.$darkMode
                ) {
                    Text("System").tag(DarkMode.system)
                    Text("Enable").tag(DarkMode.enable)
                    Text("Disable").tag(DarkMode.disable)
                }
                .symbolEffect(.bounce, value: self.darkMode)
                .symbolRenderingMode(.palette)
            }
        }
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle("Appearance")
    }
}

extension Bundle {
    var releaseVersionNumber: String? {
        return infoDictionary?["CFBundleShortVersionString"] as? String
    }
    var buildVersionNumber: String? {
        return infoDictionary?["CFBundleVersion"] as? String
    }
}

#Preview {
    SettingsView()
}
