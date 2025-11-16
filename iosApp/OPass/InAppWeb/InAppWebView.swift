//
//  InAppWebView.swift
//  OPass
//
//  Created by Brian Chang on 2025/11/16.
//
//  SPDX-FileCopyrightText: 2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import SwiftUI
import WebKit

struct InAppWebView: View {
    let url: URL
    let title: String?
    @State private var model: InAppWebViewModel
    @Environment(\.colorScheme) private var colorScheme
    
    init(url: URL, title: String? = nil) {
        self.url = url
        self.title = title
        self.model = .init(url: url)
    }
    
    var body: some View {
        WebView(model.page)
//            .scrollBounceBehavior(.basedOnSize, axes: [.horizontal, .vertical])
//            .ignoresSafeArea(.all, edges: .bottom)
            .onAppear { model.load() }
            .toolbar {
                ToolbarItem(placement: .principal) {
                    VStack(spacing: 0) {
                        // TODO: Color Problem, but it seem to work just fine in the beta version, so I don't know.
                        Text(title ?? model.page.title)
                            .font(.headline)
                        
                        if let text = model.page.url?.host() {
                            Text(text)
                                .font(.caption)
                                .foregroundColor(.gray)
                        }
                        
                        if model.page.isLoading {
                            ProgressView(value: model.page.estimatedProgress)
                                .padding(.top, 2)
                        }
                    }
                    .animation(.linear, value: model.page.isLoading)
                }
                
                ToolbarItemGroup(placement: .primaryAction) {
                    Menu("More", systemImage: "ellipsis") {
                        Button("Reload", systemImage: "arrow.clockwise") {
                            model.page.reload()
                        }
                        
                        ShareLink(item: url) {
                            Label("Share", systemImage: "square.and.arrow.up")
                        }
                        
                        Button("Open in Safari", systemImage: "safari") {
                            UIApplication.shared.open(url)
                        }
                    }
                }
            }
            .webViewContentBackground(.hidden)
            .background(.section)
    }
}

#Preview {
    NavigationStack {
        InAppWebView(url: URL(string: "https://sitcon.org")!)
    }
}

