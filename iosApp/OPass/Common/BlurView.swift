//
//  BlurView.swift
//  OPass
//
//  Created by Brian Chang on 2024/11/7.
//  2024 OPass.
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
