package com.mateju.frontend.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
)

@Serializable
data class LoginResponse(
    val id: String,
    val displayName: String,
    val role: UserRole,
)

@Serializable
data class ApiError(
    val message: String,
)

@Serializable
enum class UserRole {
    STUDENT,
    PROFESSOR,
    ADMIN,
}
