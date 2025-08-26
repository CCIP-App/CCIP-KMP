//
//  OPassApp.swift
//  OPass
//
//  Created by Brian Chang on 2024/8/18.
//
//  SPDX-FileCopyrightText: 2024-2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import Shared
import OSLog
import SwiftUI
import FirebaseCore
import FirebaseAppCheck
import FirebaseAnalytics
import FirebasePerformance
import FirebaseCrashlytics
import OneSignalFramework

@main
struct OPassApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) private var appDelegate
    @AppStorage("DarkMode") private var darkMode: DarkMode = .system

    var body: some Scene {
        WindowGroup {
            ContentView()
                .preferredColorScheme(darkMode.colorScheme)
        }
    }
}

class AppDelegate: NSObject, UIApplicationDelegate {
    private let logger = Logger(subsystem: "OPassApp", category: "AppDelegate")

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        // Firebase
        let providerFactory = OPassAppCheckProviderFactory()
        AppCheck.setAppCheckProviderFactory(providerFactory)
        FirebaseApp.configure()
        Analytics.setAnalyticsCollectionEnabled(true)
      
        // OneSignal
        OneSignal.initialize("b6213f49-e356-4b48-aa9d-7cf10ce1904d", withLaunchOptions: launchOptions)
        OneSignal.Notifications.requestPermission({ accepted in
            self.logger.info("User accepted notifications: \(accepted)")
        }, fallbackToSettings: false)
      
       return true
    }
}

private class OPassAppCheckProviderFactory: NSObject, AppCheckProviderFactory {
    func createProvider(with app: FirebaseApp) -> AppCheckProvider? {
#if targetEnvironment(simulator)
        return AppCheckDebugProvider(app: app)
#else
        return AppAttestProvider(app: app)
#endif
    }
}

enum DarkMode: Int, Identifiable {
    case system, enable, disable
    internal var id: Self { self }
    var colorScheme: ColorScheme? {
        switch self {
        case .system: return nil
        case .enable: return .dark
        case .disable: return .light
        }
    }
}
