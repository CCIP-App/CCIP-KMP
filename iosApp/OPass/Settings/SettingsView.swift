//
//  SettingsView.swift
//  OPass
//
//  Created by Brian Chang on 2024/8/20.
//  SPDX-FileCopyrightText: 2024 OPass
//  SPDX-License-Identifier: GPL-3.0-only
//

import SwiftUI

struct SettingsView: View {
    // MARK: - Variable
    private let websiteURL = URL(string: "https://opass.app")!
    private let gitHubURL = URL(string: "https://github.com/CCIP-App/CCIP-KMP")!
    private let policyURL = URL(string: "https://opass.app/privacy-policy.html")!

    @Environment(\.colorScheme) private var colorScheme

    @AppStorage("HapticFeedback") private var hapticFeedback = true

    @State private var safariUrl = URL(string: "https://opass.app")!
    @State private var safariPresented = false

    // MARK: - View
    var body: some View {
        Form {
            introductionSection()

            generalSection()

            aboutSection()

            bottomText()
        }
        .safariViewSheet(url: safariUrl, isPresented: $safariPresented)
        .analyticsScreen(name: "SettingsView")
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle("Settings")
        .listSectionSpacing(0)
    }

    @ViewBuilder
    private func introductionSection() -> some View {
        VStack(spacing: 5) {
            Image(.opassIcon)
                //.interpolation(.high)
                .resizable()
                .scaledToFit()
                .frame(width: 70)
                .clipShape(.rect(cornerRadius: 15.6)) // radius = width * 2/9

            Text("OPass")
                .font(.title2)
                .bold()

            Text("Open Pass & All Pass - A Community Checkin with Interactivity Project for iOS")
                .multilineTextAlignment(.center)
                .padding(.horizontal, 5)
        }
        .frame(maxWidth: .infinity, alignment: .center)
        .padding(.vertical, 5)
    }

    @ViewBuilder
    private func generalSection() -> some View {
        Section("GENERAL") {
            NavigationLink {
                GeneralSettingsView()
            } label: {
                Label {
                    Text("General")
                } icon: {
                    Image(systemName: "gear")
                        .resizable()
                        .scaledToFit()
                        .foregroundStyle(.gray)
                }
                .labelStyle(CenterLabelStyle())
            }

            NavigationLink {
                AppearanceSettingsView()
            } label: {
                Label {
                    Text("Appearance")
                } icon: {
                    Image(systemName: "sun.max.fill")
                        .resizable()
                        .scaledToFit()
                        .foregroundStyle(.yellow)
                }
                .labelStyle(CenterLabelStyle())
            }
        }
    }

    @ViewBuilder
    private func aboutSection() -> some View {
        Section("ABOUT") {
            Button {
                safariUrl = websiteURL
                safariPresented.toggle()
            } label: {
                Label {
                    VStack(alignment: .leading) {
                        Text("Official Website")
                            .foregroundStyle(colorScheme == .light ? .black : .white)
                        Text(websiteURL.absoluteString)
                            .foregroundStyle(.gray)
                            .font(.subheadline)
                    }
                    Spacer()
                    Image(.externalLink)
                        .renderingMode(.template)
                        .resizable()
                        .scaledToFit()
                        .foregroundStyle(.gray.opacity(0.7))
                        .frame(width: 18)
                } icon: {
                    Image(systemName: "safari")
                        .resizable()
                        .scaledToFit()
                        .symbolRenderingMode(.hierarchical)
                }
            }
            .labelStyle(CenterLabelStyle())

            Button {
                safariUrl = gitHubURL
                safariPresented.toggle()
            } label: {
                Label {
                    VStack(alignment: .leading) {
                        Text("Source Code")
                            .foregroundStyle(colorScheme == .light ? .black : .white)
                        Text(gitHubURL.absoluteString)
                            .foregroundStyle(.gray)
                            .font(.subheadline)
                    }
                    Spacer()
                    Image(.externalLink)
                        .renderingMode(.template)
                        .resizable()
                        .scaledToFit()
                        .foregroundStyle(.gray.opacity(0.7))
                        .frame(width: 18)
                } icon: {
                    Image(.githubMark)
                        .renderingMode(.template)
                        .resizable()
                        .scaledToFit()
                        .foregroundStyle(colorScheme == .light ? .black : .white)
                }
                .labelStyle(CenterLabelStyle())
            }

            Button {
                safariUrl = policyURL
                safariPresented.toggle()
            } label: {
                Label {
                    VStack(alignment: .leading, spacing: 0) {
                        Text("Privacy Policy")
                            .foregroundStyle(colorScheme == .light ? .black : .white)
                        Text(policyURL.absoluteString)
                            .foregroundStyle(.gray)
                            .font(.subheadline)
                    }
                    Spacer()
                    Image(.externalLink)
                        .renderingMode(.template)
                        .resizable()
                        .scaledToFit()
                        .foregroundStyle(.gray.opacity(0.7))
                        .frame(width: 18)
                } icon: {
                    Image(systemName: "doc.plaintext")
                        .resizable()
                        .scaledToFit()
                        .foregroundStyle(.gray)
                }
            }
            .labelStyle(CenterLabelStyle())
        }
        .sensoryFeedback(.selection, trigger: safariPresented) { $1 && hapticFeedback }
    }

    @ViewBuilder
    private func bottomText() -> some View {
        VStack {
            Text("Version \(Bundle.main.releaseVersionNumber ?? "") (\(Bundle.main.buildVersionNumber ?? ""))")
                .foregroundStyle(.gray)
                .font(.footnote)
            Text("Made with Love")
                .foregroundStyle(.gray)
                .font(.caption)
                .bold()
        }
        .frame(maxWidth: .infinity, alignment: .center)
        .listRowBackground(Color.clear)
    }
}

private extension Bundle {
    var releaseVersionNumber: String? {
        return infoDictionary?["CFBundleShortVersionString"] as? String
    }
    var buildVersionNumber: String? {
        return infoDictionary?["CFBundleVersion"] as? String
    }
}

#Preview {
    SettingsView()
}
