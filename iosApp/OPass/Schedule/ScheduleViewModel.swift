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

@MainActor @Observable
class ScheduleViewModel {
    @ObservationIgnored @AppStorage("EventID") private var eventID = ""
    
    private(set) var schedule: Schedule?
    var sessions: [Session]?
    var error: Error?
    
    enum ViewState {
        case loading
        case ready(Schedule)
        case failed(Error)
    }
    
    var viewState: ViewState {
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
                    self.schedule = schedule
                    
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
    
    func processSchedule(_ schedule: Schedule) {
        self.sessions = schedule.sessions
    }
}
