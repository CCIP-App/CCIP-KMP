//
//  EventView.swift
//  OPass
//
//  Created by Brian Chang on 2024/9/1.
//
//  SPDX-FileCopyrightText: 2024-2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import Shared
import SwiftUI

struct EventView: View {
    @Environment(\.colorScheme) private var colorScheme
    @Environment(Router.self) private var router
    
    @AppStorage("EventID") private var eventID = ""
    
    @State private var viewModel = EventViewModel()

    var body: some View {
        Group {
            if let config = viewModel.config {
                eventView(config)
            } else {
                loadingView
            }
        }
        .analyticsScreen(name: "EventView")
        .background(.section)
        .eventDestinations()
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
            .background(BlurView(style: colorScheme == .dark ? .dark : .light))
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
                switch feature.type {
                case .fastPass:
                    print("###")
                    print("##\(router.path)")
                    router.push(EventDestinations.fastpass)
                    print("##\(router.path)")
                case .ticket: router.push(EventDestinations.ticket)
                case .schedule: router.push(EventDestinations.schedule)
                case .announcement: router.push(EventDestinations.announcement)
                case .wifi:
                    if let wifi = feature.wifi {
                        if wifi.count == 1 {
                            NEHotspot.connect(wifi: wifi[0])
                        } else {
                            router.push(EventDestinations.wifi(wifi))
                        }
                    }
                    
                case .telegram:
                    router.push(EventDestinations.fastpass)
                default:
                    router.push(EventDestinations.webview)
                }
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
    private var loadingView: some View {
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
