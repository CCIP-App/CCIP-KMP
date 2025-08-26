//
//  GeneralSettingsView.swift
//  OPass
//
//  Created by Brian Chang on 2024/9/7.
//
//  SPDX-FileCopyrightText: 2024-2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import SwiftUI

struct GeneralSettingsView: View {
    // MARK: - Variable
    @AppStorage("HapticFeedback") private var hapticFeedback = true
    @Environment(\.colorScheme) private var colorScheme

    @State private var cleaningCache = false

    // MARK: - View
    var body: some View {
        Form {
            Section {
                Toggle("Haptic Feedback", systemImage: "hand.rays.fill", isOn: $hapticFeedback)
                    .symbolRenderingMode(.hierarchical)
            }

            Section {
                Label {
                    Text("Cache Usage")
                    Spacer()
                    Text(formatBytes(URLCache.shared.currentDiskUsage))
                        .foregroundStyle(.gray)
                } icon: {
                    Image(systemName: "chart.bar.fill")
                        .resizable()
                        .scaledToFit()
                        .foregroundStyle(.gray)
                }
                .labelStyle(CenterLabelStyle())

                Button {
                    Task {
                        cleaningCache = true
                        URLCache.shared.removeAllCachedResponses()
                        try? await Task.sleep(nanoseconds: 1000000000)
                        URLCache.shared.removeAllCachedResponses()
                        cleaningCache = false
                    }
                } label: {
                    HStack {
                        Text("Clean Cache")
                            .foregroundStyle(.red)
                        Spacer()
                        if (cleaningCache) {
                            ProgressView()
                        }
                    }
                }
            } footer: {
                Text("Clean cache will only remove unnecessary cache to free up space. Some essential cache required for the app to operate will remain intact.")
            }
        }
        .analyticsScreen(name: "GeneralSettingsView")
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle("General")
    }

    // MARK: Functions
    func formatBytes(_ bytes: Int) -> String {
        let units = ["B", "KB", "MB", "GB", "TB", "PB", "EB"]
        if bytes == 0 {
            return "0 B"
        }
        let bytes = Double(bytes)
        let level = bytes > 0 ? Int(floor(log(bytes) / log(1024))) : 0
        let safeLevel = min(level, units.count - 1)
        let value = bytes / pow(1024, Double(safeLevel))
        let formattedValue: String
        if value < 10 {
            formattedValue = String(format: "%.2f", value)
        } else if value < 100 {
            formattedValue = String(format: "%.1f", value)
        } else {
            formattedValue = String(format: "%.0f", value)
        }
        return "\(formattedValue) \(units[safeLevel])"
    }
}

#Preview {
    GeneralSettingsView()
}
