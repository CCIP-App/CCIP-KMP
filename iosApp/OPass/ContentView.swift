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
    @AppStorage("HapticFeedback") private var hapticFeedback = true
    @AppStorage("EventID") private var eventID = ""

    @State private var router = Router()
    @State private var selectEventSheetPresented = false

    var body: some View {
        NavigationStack(path: $router.path) {
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
        .environment(router)
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

@Observable
class Router {
    var path = NavigationPath()
    
    func push(_ route: any Hashable) { path.append(route) }
    func pop() { path.removeLast() }
    func popToRoot() { path.removeLast(path.count) }
}

#Preview {
    ContentView()
}
