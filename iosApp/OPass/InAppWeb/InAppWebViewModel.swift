//
//  InAppWebViewModel.swift
//  OPass
//
//  Created by Brian Chang on 2025/11/16.
//
//  SPDX-FileCopyrightText: 2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import SwiftUI
import WebKit

@Observable @MainActor
class InAppWebViewModel {
    let url: URL
    var page: WebPage
    private var navigationDecider = NavigationDecider()
    
    init(url: URL) {
        self.url = url
        self.page = WebPage() // Any better solution?
        self.page = WebPage(
            configuration: .init(),
            navigationDecider: navigationDecider
        )
        self.navigationDecider.owner = self
    }
    
    func load() {
        var request = URLRequest(url: url)
        request.attribution = .user
        page.load(request)
    }
}

private class NavigationDecider: WebPage.NavigationDeciding {
    weak var owner: InAppWebViewModel?
    
    func decidePolicy(
        for action: WebPage.NavigationAction,
        preferences: inout WebPage.NavigationPreferences
    ) async -> WKNavigationActionPolicy {
        if action.target == nil {
            owner?.page.load(action.request)
            return .cancel
        }
        return .allow
    }
}
