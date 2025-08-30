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
import SwiftUI
import SwiftDate
import OrderedCollections

enum ScheduleViewState {
    case ready(ScheduleData)
    case failed(Error)
    case loading
}

struct ScheduleData {
    let sessions: [(DateInRegion, [(DateInRegion, ArraySlice<Session>)])]
    let speakers: OrderedDictionary<String, Speaker>
    let types: OrderedDictionary<String, LocalizedObject>
    let rooms: OrderedDictionary<String, LocalizedObject>
    let tags: OrderedDictionary<String, LocalizedObject>
}

struct ScheduleFilter: Hashable {
    var speaker: Set<String> = []
    var type: Set<String> = []
    var room: Set<String> = []
    var tag: Set<String> = []
    var liked = false
    var count = 0
}

struct OffsetKey: PreferenceKey {
    static var defaultValue: CGFloat = .zero
    static func reduce(value: inout CGFloat, nextValue: () -> CGFloat) {
        value = nextValue()
    }
}
