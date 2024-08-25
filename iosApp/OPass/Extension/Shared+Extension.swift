//
//  Shared+Extension.swift
//  OPass
//
//  Created by Brian Chang on 2024/8/25.
//  2024 OPass.
//

import Shared

extension Shared.LocalizedString {
    func localized() -> String {
        switch Locale.current.language.languageCode?.identifier {
        case "zh": return self.zh
        default: return self.en
        }
    }
}
