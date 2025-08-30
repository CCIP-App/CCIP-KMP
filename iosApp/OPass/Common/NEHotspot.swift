//
//  NEHotspot.swift
//  OPass
//
//  Created by Brian Chang on 2025/8/30.
//
//  SPDX-FileCopyrightText: 2025 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import NetworkExtension
import Shared
import OSLog

class NEHotspot {
    private static let logger = Logger(subsystem: "app.opass.ccip", category: "NEHotspot")
    
    static func connect(wifi: WiFi) {
#if targetEnvironment(simulator)
        logger.debug("In Simulator, NEHotspot not working")
#else
        if !wifi.SSID.isEmpty {
            logger.info("NEHotspot association with SSID: \(wifi.SSID).");
            
            let config: NEHotspotConfiguration = wifi.password.isEmpty
                ? .init(ssid: wifi.SSID)
                : .init(ssid: wifi.SSID, passphrase: wifi.password, isWEP: false)
            
            config.joinOnce = false
            config.lifeTimeInDays = 7
            
            NEHotspotConfigurationManager.shared.apply(config) { error in
                if let error {
                    logger.error("NEHotspot faild: \(error)")
                }
            }
        }
#endif
    }
}
