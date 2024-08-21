//
//  SettingsView.swift
//  OPass
//
//  Created by Brian Chang on 2024/8/20.
//  2024 OPass.
//

import SwiftUI

struct SettingsView: View {
    private let websiteURL = URL(string: "https://opass.app")!
    private let gitHubURL = URL(string: "https://github.com/CCIP-App")!
    private let policyURL = URL(string: "https://opass.app/privacy-policy.html")!
    @Environment(\.colorScheme) private var colorScheme

    var body: some View {
        Form {
            generalSection()

            aboutSection()

            bottomText()
        }
        .analyticsScreen(name: "SettingsView")
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle("Settings")
        .listSectionSpacing(0)
    }

    @ViewBuilder
    private func generalSection() -> some View {
        Section("GENERAL") {
            NavigationLink {
                AppearanceSettingsView()
            } label: {
                Label {
                    Text("Appearance")
                } icon: {
                    Image(systemName: "sun.max.fill")
                        .foregroundStyle(.yellow)
                }
            }
        }
    }

    @ViewBuilder
    private func aboutSection() -> some View {
        Section("ABOUT") {
            Button {
                return
            } label: {
                Label {
                    Text("Official Website")
                    Spacer()
                    Image(systemName: "arrow.up.right.square")
                        .symbolRenderingMode(.hierarchical)
                } icon: {
                    Image(systemName: "safari")
                        .symbolRenderingMode(.hierarchical)
                }
            }
            .buttonStyle(.plain)

            Button {
                return
            } label: {
                Label {
                    Text("Source Code")
                    Spacer()
                    Image(systemName: "arrow.up.right.square")
                        .symbolRenderingMode(.hierarchical)
                } icon: {
                    Image(.githubMark)
                        .renderingMode(.template)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 23)
                        .foregroundStyle(colorScheme == .light ? .black : .white)
                }
            }
            .buttonStyle(.plain)

            Button {
                return
            } label: {
                Label {
                    Text("Privacy Policy")
                    Spacer()
                    Image(systemName: "arrow.up.right.square")
                        .symbolRenderingMode(.hierarchical)
                } icon: {
                    Image(systemName: "doc.plaintext")
                        .foregroundStyle(.gray)
                }
            }
            .buttonStyle(.plain)
        }
    }

    @ViewBuilder
    private func bottomText() -> some View {
        Section {
            HStack {
                Spacer()
                VStack {
                    Text("Version \(Bundle.main.releaseVersionNumber ?? "") (\(Bundle.main.buildVersionNumber ?? ""))")
                        .foregroundStyle(.gray)
                        .font(.footnote)
                    Text("Made with Love")
                        .foregroundStyle(.gray)
                        .font(.caption)
                        .bold()
                }
                Spacer()
            }
        }
        .listRowBackground(Color.clear)
        .padding(.bottom, 5)
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
