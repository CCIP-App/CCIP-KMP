//
//  ScheduleViewModel.swift
//  OPass
//
//  Created by Brian Chang on 2025/8/25.
//
//  SPDX-FileCopyrightText: 2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import Shared
import SwiftUI
import Algorithms
import OrderedCollections

@Observable
class ScheduleViewModel {
    @ObservationIgnored 
    @AppStorage("EventID") private var eventID = ""
    
    private(set) var schedule: ScheduleData?
    private(set) var error: Error?
    
    var viewState: ScheduleViewState {
        if let schedule { return .ready(schedule) }
        if let error { return .failed(error) }
        return .loading
    }
    
    func loadSchedule() async {
        enum Source { case cache, remote }
        await withTaskGroup(of: (Source, Result<Schedule?, Error>).self) { group in
            // Fetch from remote
            group.addTask {
                do {
                    let sched = try await PortalHelper.shared.getSchedule(eventId: self.eventID, forceReload: true)
                    return (.remote, .success(sched))
                } catch {
                    return (.remote, .failure(error))
                }
            }
            
            // Fetch from cache
            group.addTask {
                let sched = try? await PortalHelper.shared.getSchedule(eventId: self.eventID, forceReload: false)
                return (.cache, .success(sched))
            }
             
            while let (source, result) = await group.next() {
                switch result {
                case .success(let schedule):
                    if let schedule {
                        await processSchedule(schedule)
                    }
                    
                    if source == .remote {
                        group.cancelAll()
                    }
                case .failure(let error):
                    self.error = error
                    group.cancelAll()
                }
            }
        }
    }
    
    func processSchedule(_ schedule: Schedule) async  {
        let sessions = schedule.sessions
            .chunked(on: { $0.startDate.dateTruncated(from: .hour)! })
            .sorted(by: { $0.0 < $1.0 })
            .map {($0.0, $0.1.chunked(on: { $0.startDate.dateTruncated(from: .second)! }))}
        
        let speakers = OrderedDictionary(schedule.speakers.map({ ($0.id, $0) }), uniquingKeysWith: { $1 })
        let types = OrderedDictionary(schedule.sessionTypes.map({ ($0.id, $0) }), uniquingKeysWith: { $1 })
        let rooms = OrderedDictionary(schedule.rooms.map({ ($0.id, $0) }), uniquingKeysWith: { $1 })
        let tags = OrderedDictionary(schedule.tags.map({ ($0.id, $0) }), uniquingKeysWith: { $1 })
        
        return self.schedule = .init(
            sessions: sessions,
            speakers: speakers,
            types: types,
            rooms: rooms,
            tags: tags
        )
    }
}
