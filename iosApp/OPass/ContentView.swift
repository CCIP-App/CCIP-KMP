//
//  ContentView.swift
//  OPass
//
//  Created by Brian Chang on 2024/8/18.
//
//  SPDX-FileCopyrightText: 2024-2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import Shared
import SwiftUI

struct ContentView: View {
    // MARK: - Variable
    @AppStorage("EventID") private var eventID = ""
    @AppStorage("HapticFeedback") private var hapticFeedback = true

    @State private var selectEventSheetPresented = false

    // MARK: - View
    var body: some View {
        NavigationStack {
            Group {
                if eventID.isEmpty {
                    loadingView
                } else {
                    EventView()
                }
            }
            .sensoryFeedback(.selection, trigger: selectEventSheetPresented) { $1 && hapticFeedback }
            .sheet(isPresented: $selectEventSheetPresented) { SelectEventView() }
            .toolbar { toolbar() }
        }
        .analyticsScreen(name: "ContentView")
    }

    @ViewBuilder
    private var loadingView: some View {
        ProgressView("Loading")
            .onAppear { selectEventSheetPresented.toggle() }
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
