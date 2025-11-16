//
//  EventDestinations.swift
//  OPass
//
//  Created by Brian Chang on 2025/8/30.
//
//  SPDX-FileCopyrightText: 2024-2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import Shared
import SwiftUI

enum EventDestinations: Hashable {
    case fastpass
    case ticket
    case schedule
    case announcement
    case wifi([WiFi])
    case webview(URL, String?)
}

extension View {
    func eventDestinations() -> some View {
        self.navigationDestination(for: EventDestinations.self) { destination in
            switch destination {
            case .fastpass:
                ProgressView("FastPass Place Holder")
            case .ticket:
                ProgressView("Ticket Place Holder")
            case .schedule:
                ScheduleView()
            case .announcement:
                ProgressView("Announcement Place Holder")
            case .wifi(let wifi):
                ProgressView("Wifi Place Holder")
            case .webview(let url, let title):
                InAppWebView(url: url, title: title)
            }
        }
    }
}
