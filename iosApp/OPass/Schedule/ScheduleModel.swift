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
    let sessions: [(DateInRegion, [(DateInRegion, ArraySlice<Session>)])]
    let speakers: OrderedDictionary<String, Speaker>
    let types: OrderedDictionary<String, LocalizedObject>
    let rooms: OrderedDictionary<String, LocalizedObject>
    let tags: OrderedDictionary<String, LocalizedObject>
}

enum ScheduleViewState {
    case ready(ScheduleData)
    case failed(Error)
    case loading
}
