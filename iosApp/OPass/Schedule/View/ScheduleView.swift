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
import SwiftDate

struct ScheduleView: View {
    @State private var viewModel = ScheduleViewModel()
    
    @State var tabProgress: CGFloat = 0
    
    var body: some View {
        VStack {
            switch viewModel.viewState {
            case .ready(let schedule):
                if schedule.sessions.count == 1 {
                    sessionList(schedule.sessions[0].1)
                } else {
                    scheduleList(schedule.sessions)
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
    
    private func scheduleList(_ schedule: [(DateInRegion, [(DateInRegion, ArraySlice<Session>)])]) -> some View {
        GeometryReader {
            let size = $0.size
            ScrollView(.horizontal) {
                LazyHStack(spacing: 0) {
                    ForEach(schedule, id:\.0) {
                        sessionList($0.1)
                    }
                }
                .scrollTargetLayout()
                .overlay {
                    GeometryReader {
                        Color.clear
                            .preference(
                                key: OffsetKey.self,
                                value: $0.frame(in: .scrollView(axis: .horizontal)).minX
                            )
                            .onPreferenceChange(OffsetKey.self) { value in
                                tabProgress = max(min((-value / (size.width * CGFloat(schedule.count - 1))), 1), 0)
                            }
                    }
                }
            }
            .scrollPosition(id: $viewModel.selectedDay)
            .scrollTargetBehavior(.paging)
            .scrollIndicators(.never)
            .ignoresSafeArea(.all, edges: .bottom)
//            .background(.listBackground)
        }
    }
    
    private func sessionList(_ sessions: [(DateInRegion, ArraySlice<Session>)]) -> some View {
            ScrollView(.vertical) {
                LazyVStack {
                    ForEach(sessions, id: \.0) { (_, sectionSessions) in
                        LazyVStack(alignment: .leading, spacing: 0) {
                            ForEach(sectionSessions, id: \.id) {
                                Text($0.title)
                            }
                        }
                    }
                }
                .padding()

            }
            .containerRelativeFrame(.horizontal)
            .scrollIndicators(.automatic)
//            .overlay {
//                if sessions.isEmpty {
//                    ContentUnavailableView {
//                        Label("No Sessions Found", systemImage: "text.badge.xmark")
//                    } description: {
//                        Text("Use fewer filters or reset all filters.")
//                    } actions: {
//                        Button("Reset Filters") {
//                            self.filters = .init()
//                        }
//                        .bold()
//                    }
//                }
//            }
        }
}
