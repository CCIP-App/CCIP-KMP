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
                Group {
                    if schedule.sessions.count == 1 {
                        sessionList(schedule.sessions[0].1)
                    } else {
                        scheduleList(schedule.sessions)
                    }
                }
                .searchable(text: .constant(""))
            case .failed(let error):
                ContentUnavailableView(
                    "Something Went Wrong",
                    systemImage: "",
                    description: .init("\(error.localizedDescription)")
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
        }
    }
    
    private func sessionList(_ sessions: [(DateInRegion, ArraySlice<Session>)]) -> some View {
            Form {
                ForEach(sessions, id: \.0) { (sectionTime, sectionSessions) in
                    Section {
                        ForEach(sectionSessions, id: \.id) { session in
                            NavigationLink {
                                SessionDetailView(viewModel: viewModel, session: session)
                            } label: {
                                VStack(alignment: .leading) {
                                    HStack {
                                        Button(viewModel.getRoom(session.room)?.name ?? session.room) {
                                            // TODO: Click it to have it filtered.
                                        }
                                        .buttonStyle(.borderedProminent)
                                        .controlSize(.mini)
                                        .font(.caption)
                                        
                                        
                                        let start = session.startDate, end = session.endDate
                                        Text(String(format: "%d:%02d ~ %d:%02d",
                                                    start.hour, start.minute,
                                                    end.hour, end.minute))
                                        .foregroundStyle(.gray)
                                        .font(.footnote)
                                    }
                                    
                                    Text(session.title)
                                    
                                    if let tags = session.tags {
                                        ScrollView(.horizontal) {
                                            HStack {
                                                ForEach(tags, id: \.self) { tag in
                                                    if let tagName = viewModel.getTag(tag)?.name {
                                                        Button(tagName) {
                                                            // TODO: Click it to have it filtered.
                                                        }
                                                        .buttonStyle(.bordered)
                                                        .controlSize(.mini)
                                                        .font(.caption)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } header: {
                        HStack {
                            Text(String(format: "%d:%02d", sectionTime.hour, sectionTime.minute))
                            VStack {
                                Divider()
                            }
                        }
                    }
                }
                
                Rectangle()
                    .frame(height: 50)
                    .foregroundStyle(.clear)
                    .listRowBackground(Color.clear)
            }
            .contentMargins(.top, 15)
            .listSectionSpacing(0)
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
