//
//  EventView.swift
//  OPass
//
//  Created by Brian Chang on 2024/9/1.
//  2024 OPass.
//

import SwiftUI

@MainActor
struct EventView: View {
    // MARK: - Variable
    @State private var viewModel = EventViewModel()

    // MARK: - View
    var body: some View {
        Group {
            if let config = viewModel.config {
                VStack {
                    Text(config.name)
                    Text(config.logoUrl)
                }
            } else {
                ProgressView("Loading")
                    .task { await viewModel.loadEvent() }
            }
        }
        .analyticsScreen(name: "EventView")
    }
}

#Preview {
    NavigationStack {
        EventView()
    }
}
