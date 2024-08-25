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
    @Environment(\.colorScheme) private var colorScheme

    var body: some View {
        NavigationStack {
            Group {
                if let events = viewModel.listEvents {
                    Form {
                        ForEach(events, id: \.id) { event in
                            Button {
                                return
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
                    .searchable(text: $viewModel.searchText,
                                placement: .navigationBarDrawer,
                                prompt: "Search Event")
                } else if let error = viewModel.error {
                    ContentUnavailableView(
                        "Something went wrong",
                        systemImage: "exclamationmark.triangle",
                        description: Text(error.localizedDescription)
                    )
                } else {
                    ProgressView("Loading")
                        .task { await viewModel.loadEvents() }
                }
            }
            .navigationBarTitleDisplayMode(.inline)
            .navigationTitle("Select Event")
        }
    }
}

#Preview {
    ProgressView()
        .sheet(isPresented: .constant(true)) {
            SelectEventView()
        }
}
