//
//  EventViewModel.swift
//  OPass
//
//  Created by Brian Chang on 2024/9/1.
//  SPDX-FileCopyrightText: 2024 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import Shared
import OSLog
import SwiftUI

private let logger = Logger(subsystem: "OPassApp", category: "EventViewModel")

@MainActor @Observable
class EventViewModel {
    @ObservationIgnored
    @AppStorage("EventID") private var eventID = ""

    private(set) var config: EventConfig?

    private var error: Error?

    func loadEvent(reload: Bool = false) async {
        if reload { config = nil }
        async let cache = PortalHelper.shared.getEventConfig(eventId: eventID, forceReload: false)
        async let remote = PortalHelper.shared.getEventConfig(eventId: eventID, forceReload: true)

        do {
            config = try? await cache
            config = try await remote
        } catch {
            if config == nil {
                logger.error("\(error)")
                self.error = error
            } else {
                logger.info("Faild with remote data: \(error)")
            }
        }
    }

    func reset() {
        config = nil
    }
}
