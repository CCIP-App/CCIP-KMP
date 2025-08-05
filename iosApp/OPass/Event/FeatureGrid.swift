//
//  FeatureGrid.swift
//  OPass
//
//  Created by Brian Chang on 2024/11/6.
//  SPDX-FileCopyrightText: 2024 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import Shared
import SwiftUI

struct FeatureGrid: View {
    // MARK: - Variables
    let config: EventConfig

    // MARK: - Views
    var body: some View {
        ScrollView {
            LazyVGrid(columns: .init(
                repeating: .init(spacing: 30, alignment: .top),
                count: 4
            )) {
                ForEach(config.features, id: \.self) { feature in
                    featureGridItem(feature)
                        .padding(.bottom, 5)
                }
            }
        }
        .padding(.horizontal)
    }

    @ViewBuilder
    private func featureGridItem(_ feature: Feature) -> some View {
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
                            .scaledToFit()
                    default:
                        Image(systemName: feature.symbol)
                            .resizable()
                            .scaledToFit()
                            .padding(3)
                    }
                }
            }
            .buttonStyle(.bordered)
            .tint(feature.color)

            Text(feature.label)
                .font(.custom("RobotoCondensed-Regular", size: 11, relativeTo: .caption2))
                .fixedSize(horizontal: false, vertical: true)
                .multilineTextAlignment(.center)
        }
    }
}
