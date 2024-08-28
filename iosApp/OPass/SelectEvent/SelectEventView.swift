//
//  SelectEventView.swift
//  OPass
//
//  Created by Brian Chang on 2024/8/25.
//  2024 OPass.
//

import Shared
import SwiftUI

struct SelectEventView: View {
    @State private var viewModel = SelectEventViewModel()
    @AppStorage("EventID") private var eventID = ""
    @Environment(\.colorScheme) private var colorScheme
    @Environment(\.dismiss) private var dismiss

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
                            Text(event.name.localized())
                                .foregroundStyle(colorScheme == .light ? .black : .white)
                        } icon: {
                            AsyncImage(url: URL(string: event.logoUrl)) { image in
                                image
                                    .renderingMode(.original)
                                    .resizable()
                                    .scaledToFit()
                                    .padding(5)
                                    .background(.blue)
                                    .clipShape(.rect(cornerRadius: 5))
                            } placeholder: {
                                ProgressView()
                            }
                            .frame(width: 100, height: 50)
                        }
                        .labelStyle(.titleAndIcon)
                    }
                }
            }
        }
        .sensoryFeedback(.success, trigger: eventID)
        .refreshable { await viewModel.loadEvents() }
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
                Button("Cancel") {
                    dismiss()
                }
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
