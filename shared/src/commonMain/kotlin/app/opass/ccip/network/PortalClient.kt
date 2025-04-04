/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network

import app.opass.ccip.network.models.event.Event
import app.opass.ccip.network.models.eventconfig.EventConfig
import app.opass.ccip.network.models.fastpass.Announcement
import app.opass.ccip.network.models.fastpass.Attendee
import app.opass.ccip.network.models.schedule.Schedule
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class PortalClient {

    private val BASE_URL = "https://portal.opass.app"
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val client = HttpClient {
        defaultRequest { url(BASE_URL) }
        install(ContentNegotiation) { json(json) }
    }
    private val universalClient = HttpClient {
        install(ContentNegotiation) {
            // Register content type as any as some events have wrong content type
            json(json = json, contentType = ContentType.Any)
        }
    }

    suspend fun getEvents(): List<Event> {
        return client.get("/events/").body()
    }

    suspend fun getEventConfig(eventId: String): EventConfig {
        return client.get("/events/$eventId/").body()
    }

    suspend fun getEventSchedule(url: String): Schedule {
        return universalClient.get(url).body()
    }

    suspend fun getFastPassStatus(url: String, token: String): Attendee {
        return universalClient.get {
            url("$url/status")
            parameter("token", token)
        }.body()
    }

    suspend fun getAnnouncements(url: String, token: String? = null): List<Announcement> {
        return universalClient.get {
            url("$url/announcement")
            if (token != null) parameter("token", token)
        }.body()
    }

    suspend fun useScenario(url: String, token: String, scenarioId: String): Attendee {
        return universalClient.get {
            url("$url/use/$scenarioId")
            parameter("token", token)
        }.body()
    }
}
