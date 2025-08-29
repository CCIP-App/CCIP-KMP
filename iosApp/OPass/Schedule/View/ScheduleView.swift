//
//  ScheduleView.swift
//  OPass
//
//  Created by Brian Chang on 2025/8/25.
//
//  SPDX-FileCopyrightText: 2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import Shared
import SwiftUI

struct ScheduleView: View {
    @State private var viewModel = ScheduleViewModel()
    
    var body: some View {
        VStack {
            switch viewModel.viewState {
            case .ready(let schedule):
                VStack {
                    Text("Loaded")
                }
            case .failed(let error):
                ContentUnavailableView(
                    "Something Went Wrong",
                    systemImage: "",
                    description: .init("\(error.localizedDescription) (\(error)")
                )
            case .loading:
                ProgressView("Loading Schedule")
                    .task { await viewModel.loadSchedule() }
            }
        }
        .analyticsScreen(name: "ScheduleView")
        .environment(viewModel)
    }
}
