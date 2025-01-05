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

    @Environment(\.colorScheme) private var colorScheme
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
        ZStack(alignment: .top) {
            ScrollView {
                LazyVGrid(columns: .init(
                    repeating: .init(spacing: 30, alignment: .top),
                    count: 4
                )) {
                    ForEach(config.features, id: \.self) { feature in
                        featureButton(feature)
                            .padding(.bottom, 5)
                    }
                }
                .padding(.top, UIScreen.main.bounds.width * 0.4 + 40)
            }
            .padding(.horizontal)

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
            .frame(maxWidth: .infinity, alignment: .center)
            .padding(10)
            .background(BlurView(style: colorScheme == .dark ? .dark : .light))//
        }
        .onChange(of: eventID) { Task { await viewModel.loadEvent(reload: true) } }
        .toolbarBackground(.section, for: .navigationBar)
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .principal) {
                Text(config.name)
                    .bold()
            }
        }
    }

    @ViewBuilder
    private func featureButton(_ feature: Feature) -> some View {
        VStack {
            Button {

            } label: {
                CachedAsyncImage(url: URL(string: feature.iconUrl ?? "")) { phase in
                    switch phase {
                    case .success(let image):
                        image
                            .renderingMode(.template)
                            .interpolation(.none)
                            .resizable()
                            .scaledToFill()
                    default:
                        Image(systemName: feature.symbol)
                            .resizable()
                            .scaledToFill()
                            .padding(3)
                    }
                }
            }
            .buttonStyle(.bordered)
            .tint(feature.color)
            .frame(width: 50, height: 50)

            Text(feature.label)
                .font(.custom("RobotoCondensed-Regular", size: 11, relativeTo: .caption2))
                .fixedSize(horizontal: false, vertical: true)
                .multilineTextAlignment(.center)
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
