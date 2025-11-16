//
//  SelectEventViewModel.swift
//  OPass
//
//  Created by Brian Chang on 2024/8/25.
//
//  SPDX-FileCopyrightText: 2024-2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import Shared
import OSLog
import SwiftUI

private let logger = Logger(subsystem: "OPassApp", category: "SelectEventViewModel")

@Observable
class SelectEventViewModel {
    enum ViewState {
        case ready([Event])
        case error(Error)
        case loading
    }

    var searchText = ""
    var viewState: ViewState {
        guard error == nil else { return .error(error!) }
        guard events != nil else { return .loading }
        return .ready({
            if searchText.isEmpty { return events! }

            let components = searchText
                .trimmingCharacters(in: .whitespacesAndNewlines)
                .components(separatedBy: " ")
                .compactMap {
                    let component = $0.trimmingCharacters(in: .whitespaces)
                    return component.isEmpty ? nil : component.lowercased()
                }

            return events!.filter { event in
                let name = event.name.lowercased()
                for component in components {
                    guard name.contains(component) else {
                        return false
                    }
                }
                return true
            }
        }())
    }

    private var error: Error?
    private var events: [Event]?

    func loadEvents() async {
        async let cache = PortalHelper.shared.getEvents(forceReload: false)
        async let remote = PortalHelper.shared.getEvents(forceReload: true)

        do {
            events = try? await cache
            events = try await remote
        } catch {
            if events == nil {
                logger.error("\(error)")
                self.error = error
            } else {
                logger.info("Faild with remote data: \(error)")
            }
        }
    }
}
