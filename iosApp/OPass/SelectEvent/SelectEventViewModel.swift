//
//  SelectEventViewModel.swift
//  OPass
//
//  Created by Brian Chang on 2024/8/25.
//  2024 OPass.
//

import Shared
import OSLog
import SwiftUI

private let logger = Logger(subsystem: "OPassApp", category: "SelectEventViewModel")

@MainActor @Observable
class SelectEventViewModel {
    enum ViewState {
        case ready([Event_])
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
                let name = event.name
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
    private var events: [Event_]?

    func loadEvents() async {
        async let cache = portal.getEvents(forceReload: false)
        async let remote = portal.getEvents(forceReload: true)

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
