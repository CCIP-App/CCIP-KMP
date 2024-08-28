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

@Observable
class SelectEventViewModel {
    enum ViewState {
        case ready([Event])
        case loading
        case error(Error)
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
                let name = event.name.localized().lowercased()
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
        do {
            let events = try await PortalClient().getEvents()
            await MainActor.run { self.events = events }
        } catch {
            logger.error("\(error)")
            await MainActor.run { self.error = error }
        }
    }
}
