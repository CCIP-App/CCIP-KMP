//
//  SafariView.swift
//  OPass
//
//  Created by Brian Chang on 2024/8/22.
//  2024 OPass.
//

import SwiftUI
import SafariServices

extension View {
    func safariViewSheet(url: URL, isPresented: Binding<Bool>, onDismiss: (() -> Void)? = nil) -> some View {
        self.sheet(isPresented: isPresented, onDismiss: onDismiss) {
            SFSafariViewWrapper(url: url)
                .analyticsScreen(name: "SFSafariView")
                .ignoresSafeArea()
        }
    }
}

private struct SFSafariViewWrapper: UIViewControllerRepresentable {
    let url: URL
    func makeUIViewController(context: UIViewControllerRepresentableContext<Self>) -> SFSafariViewController {
        return SFSafariViewController(url: url)
    }
    func updateUIViewController(_ uiViewController: SFSafariViewController, context: UIViewControllerRepresentableContext<SFSafariViewWrapper>) {
        return
    }
}
