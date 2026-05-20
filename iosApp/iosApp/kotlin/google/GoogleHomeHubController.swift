//
//  GoogleHomeHubController.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 20/05/2026.
//

import ComposeApp
import Matter
import SharedCode
import GoogleHomeSDK

class GoogleHubControllerImpl : GoogleHubController {
    
    func getHubs() async throws -> [GoogleHub] {
        let controller = GoogleHomeController.instance()
        return try await controller.getHubs()
    }

    func activateHub(hub: GoogleHub) async throws {
        let controller = GoogleHomeController.instance()
        try await controller.activateHub(hub)
    }
}
