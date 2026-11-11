package com.mateju.frontend.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AuthRepository(
    private val client: HttpClient,
    private val baseUrl: String,
) {
    suspend fun login(username: String, password: String): Result<LoginResponse> = runCatching {
        val response = client.post("$baseUrl/api/v1/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username = username.trim(), password = password))
        }

        if (response.status != HttpStatusCode.OK) {
            val error = runCatching { response.body<ApiError>() }.getOrNull()
            error?.message ?: "No fue posible iniciar sesión."
        } else {
            response.body<LoginResponse>()
        }
    }.fold(
        onSuccess = { value ->
            when (value) {
                is LoginResponse -> Result.success(value)
                is String -> Result.failure(IllegalStateException(value))
                else -> Result.failure(IllegalStateException("Respuesta inesperada del servidor."))
            }
        },
        onFailure = { Result.failure(it) },
    )
}
