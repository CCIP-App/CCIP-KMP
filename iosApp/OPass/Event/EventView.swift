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
        GeometryReader { proxy in
            let containerWidth = proxy.size.width

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
                    .padding(.top, containerWidth * 0.3 + 30)
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
                .frame(width: containerWidth * 0.78, height: containerWidth * 0.3)
                .frame(maxWidth: .infinity, alignment: .center)
                .padding(10)
                .background(BlurView(style: colorScheme == .dark ? .dark : .light))
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        }
        .onChange(of: eventID) { Task { await viewModel.loadEvent(reload: true) } }
        .toolbarBackground(.section, for: .navigationBar)
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .principal) {
                Text(config.name).bold()
            }
        }
    }

    @ViewBuilder
    private func featureButton(_ feature: Feature) -> some View {
        VStack {
            Button {
                switch feature.type {
                case .fastPass:
                    router.push(EventDestinations.fastpass)
                case .ticket:
                    router.push(EventDestinations.ticket)
                case .schedule:
                    router.push(EventDestinations.schedule)
                case .announcement:
                    router.push(EventDestinations.announcement)
                case .wifi:
                    if let wifi = feature.wifi {
                        if wifi.count == 1 {
                            NEHotspot.connect(wifi: wifi[0])
                        } else {
                            router.push(EventDestinations.wifi(wifi))
                        }
                    }
                case .telegram:
                    if let urlString = feature.url, let url = URL(string: urlString) {
                        UIApplication.shared.open(url)
                    }
                default:
                    if let urlString = feature.url, let url = URL(string: urlString) {
                        router.push(EventDestinations.webview(url, feature.label))
                    }
                }
            } label: {
                Rectangle()
                    .aspectRatio(0.8484848, contentMode: .fit)
                    .foregroundColor(.clear)
                    .overlay {
                        GeometryReader { geometry in
                            let width = geometry.size.width
                            CachedAsyncImage(url: URL(string: feature.iconUrl ?? "")) { phase in
                                switch phase {
                                case .success(let image):
                                    image
                                        .renderingMode(.template)
                                        .interpolation(.none)
                                        .resizable()
                                        .scaledToFill()
                                        .frame(width: width * 0.8, height: width * 0.8)
                                default:
                                    Image(systemName: feature.symbol)
                                        .resizable()
                                        .scaledToFill()
                                        .frame(width: width * 0.7, height: width * 0.65)
                                }
                            }
                            .frame(maxWidth: .infinity, maxHeight: .infinity)
                        }
                    }
                
            }
            .buttonStyle(.bordered)
            .tint(feature.color)
            .buttonBorderShape(.roundedRectangle(radius: 18))
            .glassEffect(in: .rect(cornerRadius: 18))

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
