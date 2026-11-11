package com.mateju.frontend.data

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient

/**
 * Desktop reaches the local server directly; Android uses the emulator host alias.
 */
expect fun backendBaseUrl(): String
