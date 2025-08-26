//
//  BlurView.swift
//  OPass
//
//  Created by Brian Chang on 2024/11/7.
//
//  SPDX-FileCopyrightText: 2024-2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import SwiftUI


struct BlurView: UIViewRepresentable {
    var style: UIBlurEffect.Style = .dark

    func makeUIView(context: Context) -> UIVisualEffectView {
        return UIVisualEffectView(effect: UIBlurEffect(style: style))
    }

    func updateUIView(_ uiView: UIVisualEffectView, context: Context) {
        uiView.effect = UIBlurEffect(style: style)
    }
}
