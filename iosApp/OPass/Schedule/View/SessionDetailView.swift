//
//  SessionDetailView.swift
//  OPass
//
//  Created by Brian Chang on 2025/8/26.
//
//  SPDX-FileCopyrightText: 2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import Shared
import SwiftUI


struct SessionDetailView: View {
    let viewModel: ScheduleViewModel
    let session: Session
    
    @Environment(\.colorScheme) private var colorScheme
    
    var body: some View {
        Form {
            if let tags = session.tags {
                tagSection(for: tags)
            }
            
            titleSection()
            
            if session.hasAnyFeature {
                featureSection()
            }
        }
        .listSectionSpacing(0)
        .contentMargins(.top, 15)
    }
    
    @ViewBuilder
    private func tagSection(for tags: [String]) -> some View {
        Section {
            ScrollView(.horizontal) {
                HStack {
                    ForEach(tags, id: \.self) { tag in
                        if let tagName = viewModel.getTag(tag)?.name {
                            Button(tagName) {
                                // TODO: What to do here? Open a new list of sessions with this tag??
                            }
                            .buttonStyle(.bordered)
                            .foregroundStyle(colorScheme == .light ? .black : .white)
                        }
                    }
                }
            }
            .scrollIndicators(.hidden)
        }
        .listRowInsets(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
        .listRowBackground(Color.clear)
    }
    
    @ViewBuilder
    private func titleSection() -> some View {
        Section {
            Text(session.title)
                .font(.title)
                .bold()
        }
        .listRowInsets(EdgeInsets(top: 5, leading: 5, bottom: 5, trailing: 5))
        .listRowBackground(Color.clear)
    }
    
    @ViewBuilder
    private func featureSection() -> some View {
        Section {
            ScrollView(.horizontal) {
                HStack {
                    if let qa = session.qa, let url = URL(string: qa) {
                        VStack {
                            Button {
                                // TODO:
                            } label: {
                                Image(systemName: "questionmark")
                                    .font(.system(size: 23, weight: .semibold, design: .rounded))
                                    .foregroundStyle(colorScheme == .dark ? .gray : Color(red: 72/255, green: 72/255, blue: 74/255))
                                    .frame(width: 60, height: 60)
                                    .background(.section)
                                    .clipShape(.rect(cornerRadius: 15))
                            }
                            
                            Text("QA")
                                .font(.caption2)
                        }
                    }
                    if let record = session.record, let url = URL(string: record) {
                        VStack {
                            Button {
                                // TODO:
                            } label: {
                                Image(systemName: "play")
                                    .font(.system(size: 23, weight: .semibold, design: .rounded))
                                    .foregroundStyle(colorScheme == .dark ? .gray : Color(red: 72/255, green: 72/255, blue: 74/255))
                                    .frame(width: 60, height: 60)
                                    .background(.section)
                                    .clipShape(.rect(cornerRadius: 15))
                            }
                            
                            Text("Record")
                                .font(.caption2)
                        }
                    }
                    if let slide = session.slide, let url = URL(string: slide) {
                        VStack {
                            Button {
                                // TODO:
                            } label: {
                                Image(systemName: "paperclip")
                                    .font(.system(size: 23, weight: .semibold, design: .rounded))
                                    .foregroundStyle(colorScheme == .dark ? .gray : Color(red: 72/255, green: 72/255, blue: 74/255))
                                    .frame(width: 60, height: 60)
                                    .background(.section)
                                    .clipShape(.rect(cornerRadius: 15))
                            }
                            
                            Text("Slide")
                                .font(.caption2)
                        }
                    }
                    if let liveUrl = session.liveUrl, let url = URL(string: liveUrl) {
                        VStack {
                            Button {
                                // TODO:
                            } label: {
                                Image(systemName: "video")
                                    .font(.system(size: 23, weight: .semibold, design: .rounded))
                                    .foregroundStyle(colorScheme == .dark ? .gray : Color(red: 72/255, green: 72/255, blue: 74/255))
                                    .frame(width: 60, height: 60)
                                    .background(.section)
                                    .clipShape(.rect(cornerRadius: 15))
                            }
                            
                            Text("Live")
                                .font(.caption2)
                        }
                    }
                    if let coWriteUrl = session.coWriteUrl, let url = URL(string: coWriteUrl) {
                        VStack {
                            Button {
                                // TODO:
                            } label: {
                                Image(systemName: "keyboard")
                                    .font(.system(size: 23, weight: .semibold, design: .rounded))
                                    .foregroundStyle(colorScheme == .dark ? .gray : Color(red: 72/255, green: 72/255, blue: 74/255))
                                    .frame(width: 60, height: 60)
                                    .background(.section)
                                    .clipShape(.rect(cornerRadius: 15))
                            }
                            
                            Text("Co-Write")
                                .font(.caption2)
                        }
                    }
                }
            }
        }
        .listRowInsets(EdgeInsets(top: 5, leading: 5, bottom: 5, trailing: 5))
        .listRowBackground(Color.clear)
    }
}
