package com.mateju.frontend.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mateju.frontend.data.AuthRepository
import com.mateju.frontend.data.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class LoginPresenter(
    private val repository: AuthRepository,
    private val scope: CoroutineScope,
    private val onAuthenticated: (LoginResponse) -> Unit,
) {
    var state by mutableStateOf(LoginUiState())
        private set

    fun updateUsername(value: String) {
        state = state.copy(username = value, errorMessage = null)
    }

    fun updatePassword(value: String) {
        state = state.copy(password = value, errorMessage = null)
    }

    /** Limpia el formulario (se usa al cerrar sesión para no dejar la contraseña escrita). */
    fun reset() {
        state = LoginUiState()
    }

    fun login() {
        if (state.username.isBlank() || state.password.isBlank()) {
            state = state.copy(errorMessage = "Escribe tu usuario y contraseña.")
            return
        }
        if (state.isLoading) return

        state = state.copy(isLoading = true, errorMessage = null)
        scope.launch {
            repository.login(state.username, state.password)
                .onSuccess { session ->
                    state = state.copy(isLoading = false)
                    onAuthenticated(session)
                }
                .onFailure { error ->
                    state = state.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No fue posible conectar con Mateju.",
                    )
                }
        }
    }
}
