//
//  ScheduleModel.swift
//  OPass
//
//  Created by Brian Chang on 2025/8/29.
//
//  SPDX-FileCopyrightText: 2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import Shared
import SwiftDate
import OrderedCollections

struct ScheduleData {
    var sessions: [(DateInRegion, [(DateInRegion, ArraySlice<Session>)])]
    var speakers: OrderedDictionary<String, Speaker>
    var types: OrderedDictionary<String, LocalizedObject>
    var rooms: OrderedDictionary<String, LocalizedObject>
    var tags: OrderedDictionary<String, LocalizedObject>
}

enum ScheduleViewState {
    case ready(ScheduleData)
    case failed(Error)
    case loading
}
