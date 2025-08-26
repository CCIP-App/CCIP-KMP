//
//  Session.swift
//  OPass
//
//  Created by Brian Chang on 2025/8/26.
//
//  SPDX-FileCopyrightText: 2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import Shared
import SwiftDate

extension Shared.Session {
    var startDate: DateInRegion {
        return start.toISODate(region: .current)!
    }
    
    var endDate: DateInRegion {
        return end.toISODate(region: .current)!
    }
}
