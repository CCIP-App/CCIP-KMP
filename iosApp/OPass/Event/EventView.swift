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
        .background(.section)
    }

    @ViewBuilder
    private func eventView(_ config: EventConfig) -> some View {
        VStack {
            CachedAsyncImage(url: URL(string: config.logoUrl)) { phase in
                switch phase {
                case .empty:
                    ProgressView()
                case .success(let image):
                    image.resizable().scaledToFit()
                default:
                    Text(config.name)
                        .font(.system(.largeTitle, design: .rounded))
                        .fontWeight(.medium)
                        .fixedSize(horizontal: false, vertical: true)
                }
            }
            .frame(width: UIScreen.main.bounds.width * 0.78, height: UIScreen.main.bounds.width * 0.4)

            FeatureGrid(config: config)
        }
        .onChange(of: eventID) { Task { await viewModel.loadEvent(reload: true) } }
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .principal) {
                Text(config.name)
                    .bold()
            }
        }
    }

    @ViewBuilder
    private func loadingView() -> some View {
        ProgressView("Loading")
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .task { await viewModel.loadEvent() }
    }
}

#Preview {
    NavigationStack {
        EventView()
    }
}
