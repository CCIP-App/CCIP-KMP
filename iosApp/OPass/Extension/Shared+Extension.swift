//
//  Shared+Extension.swift
//  OPass
//
//  Created by Brian Chang on 2024/9/2.
//  2024 OPass.
//

import Shared
import SwiftUI

extension Shared.PortalHelper {
    static let shared = PortalHelper()
}

extension Shared.Feature {
    var symbol: String {
        switch self.type {
        case .fastPass: return "wallet.pass"
        case .schedule: return "scroll"
        case .announcement: return "megaphone"
        case .puzzle: return "puzzlepiece.extension"
        case .ticket: return "ticket"
        case .telegram: return "paperplane"
        case .im: return "bubble.right"
        case .sponsors: return "banknote"
        case .staffs: return "person.2"
        case .venue: return "map"
        case .wifi: return "wifi"
        default: return "shippingbox"
        }
    }

    var color: Color {
        switch self.type {
        case .fastPass: return .blue
        case .schedule: return .green
        case .announcement: return .orange
        case .puzzle: return .blue
        case .ticket: return .purple
        case .telegram: return .init(red: 89/255, green: 196/255, blue: 189/255)
        case .im: return .init(red: 86/255, green: 89/255, blue: 207/255)
        case .sponsors: return .yellow
        case .staffs: return .gray
        case .venue: return .init(red: 87/255, green: 172/255, blue: 225/255)
        case .wifi: return .brown
        default: return .purple
        }
    }
}
