//
//  SelectEventView.swift
//  OPass
//
//  Created by Brian Chang on 2024/8/25.
//  SPDX-FileCopyrightText: 2024 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import Shared
import SwiftUI

@MainActor
struct SelectEventView: View {
    // MARK: - Variable
    @State private var viewModel = SelectEventViewModel()

    @Environment(\.colorScheme) private var colorScheme
    @Environment(\.dismiss) private var dismiss

    @AppStorage("EventID") private var eventID = ""
    @AppStorage("HapticFeedback") private var hapticFeedback = true

    // MARK: - View
    var body: some View {
        NavigationStack {
            Group {
                switch viewModel.viewState {
                case .ready(let events):
                    eventList(events)
                case .loading:
                    loadingView()
                case .error(let error):
                    errorView(error)
                }
            }
            .navigationBarTitleDisplayMode(.inline)
            .navigationTitle("Select Event")
            .toolbar { toolbar() }
        }
        .interactiveDismissDisabled(eventID.isEmpty)
        .analyticsScreen(name: "SelectEventView")
    }

    @ViewBuilder
    private func eventList(_ events: [Event]) -> some View {
        Form {
            if events.isEmpty {
                ContentUnavailableView.search(text: viewModel.searchText)
            } else {
                ForEach(events, id: \.id) { event in
                    Button {
                        eventID = event.id
                        dismiss()
                    } label: {
                        Label {
                            Text(" " + event.name)
                                .foregroundStyle(colorScheme == .light ? .black : .white)
                                .font(.system(.title3, design: .rounded, weight: .medium))
                        } icon: {
                            AsyncImage(url: URL(string: event.logoUrl), transaction: .init(animation: .easeInOut)) { phase in
                                switch phase {
                                case .empty:
                                    ProgressView()
                                case .success(let image):
                                    image
                                        .resizable()
                                        .scaledToFit()
                                default:
                                    Image(systemName: "exclamationmark.triangle.fill")
                                        .foregroundStyle(colorScheme == .light ? .white : .gray)
                                }
                            }
                            .padding(5)
                            .frame(width: 80, height: 50)
                            .background(.gray, in: .rect(cornerRadius: 5))
                        }
                        .labelStyle(.titleAndIcon)
                    }
                }
            }
        }
        .animation(events.isEmpty ? nil : .default, value: events)
        .scrollDismissesKeyboard(.interactively)
        .sensoryFeedback(.success, trigger: eventID) { _, _ in hapticFeedback }
        .searchable(text: $viewModel.searchText,
                    placement: .navigationBarDrawer,
                    prompt: "Search Event")
    }

    @ViewBuilder
    private func loadingView() -> some View {
        ProgressView("Loading")
            .task { await viewModel.loadEvents() }
    }

    @ViewBuilder
    private func errorView(_ error: Error) -> some View {
        ContentUnavailableView("Something went wrong",
                               systemImage: "exclamationmark.triangle",
                               description: .init(error.localizedDescription))
    }

    @ToolbarContentBuilder
    private func toolbar() -> some ToolbarContent {
        if !eventID.isEmpty {
            ToolbarItem(placement: .topBarLeading) {
                Button("Cancel") { dismiss() }
            }
        }
    }
}

#Preview {
    ProgressView()
        .sheet(isPresented: .constant(true)) {
            SelectEventView()
        }
}
