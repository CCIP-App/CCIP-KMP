//
//  EventView.swift
//  OPass
//
//  Created by Brian Chang on 2024/9/1.
//  2024 OPass.
//

import Shared
import SwiftUI

@MainActor
struct EventView: View {
    // MARK: - Variable
    @State private var viewModel = EventViewModel()

    @AppStorage("EventID") private var eventID = ""

    // MARK: - View
    var body: some View {
        Group {
            if let config = viewModel.config {
                eventView(config)
            } else {
                loadingView()
            }
        }
        .analyticsScreen(name: "EventView")
    }

    @ViewBuilder
    private func eventView(_ config: EventConfig) -> some View {
        VStack {
            Text(config.name)
            Text(config.logoUrl)
        }
        .onChange(of: eventID) { viewModel.reset() }
    }

    @ViewBuilder
    private func loadingView() -> some View {
        ProgressView("Loading")
            .task { await viewModel.loadEvent() }
    }
}

#Preview {
    NavigationStack {
        EventView()
    }
}
