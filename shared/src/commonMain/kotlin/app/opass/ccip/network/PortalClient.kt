/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network

import app.opass.ccip.network.models.event.Event
import app.opass.ccip.network.models.eventconfig.EventConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object PortalClient {

    private const val BASE_URL = "https://portal.opass.app"

    private val client = HttpClient {
        defaultRequest {
            url(BASE_URL)
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getEvents(): List<Event> {
        return client.get("/events/").body()
    }

    suspend fun getEventConfig(eventId: String): EventConfig {
        return client.get("/events/$eventId/").body()
    }
}
