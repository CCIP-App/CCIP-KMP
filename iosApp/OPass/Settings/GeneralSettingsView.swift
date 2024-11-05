//
//  GeneralSettingsView.swift
//  OPass
//
//  Created by Brian Chang on 2024/9/7.
//  2024 OPass.
//

import SwiftUI

struct GeneralSettingsView: View {
    // MARK: - Variable
    @AppStorage("HapticFeedback") private var hapticFeedback = true

    // MARK: - View
    var body: some View {
        Form {
            Section {
                Toggle("Haptic Feedback", systemImage: "hand.tap", isOn: $hapticFeedback)
            }
        }
        .analyticsScreen(name: "GeneralSettingsView")
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle("General")
        .listSectionSpacing(.compact)
    }
}

#Preview {
    GeneralSettingsView()
}
