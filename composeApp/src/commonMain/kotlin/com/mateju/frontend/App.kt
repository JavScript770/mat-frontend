package com.mateju.frontend

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.mateju.frontend.data.AuthRepository
import com.mateju.frontend.data.LoginResponse
import com.mateju.frontend.data.UserRole
import com.mateju.frontend.data.backendBaseUrl
import com.mateju.frontend.data.createHttpClient
import com.mateju.frontend.ui.AdminDashboard
import com.mateju.frontend.ui.LoginPresenter
import com.mateju.frontend.ui.LoginScreen
import com.mateju.frontend.ui.ProfessorDashboard
import com.mateju.frontend.ui.StudentDashboard

@Composable
fun MatejuApp() {
    val scope = rememberCoroutineScope()
    var session by remember { mutableStateOf<LoginResponse?>(null) }
    val presenter = remember {
        LoginPresenter(
            repository = AuthRepository(createHttpClient(), backendBaseUrl()),
            scope = scope,
            onAuthenticated = { session = it },
        )
    }

    // Cerrar sesión: limpia el formulario y vuelve a la pantalla de login
    val logout = {
        presenter.reset()
        session = null
    }

    MaterialTheme {
        when (val activeSession = session) {
            null -> LoginScreen(
                state = presenter.state,
                onUsernameChange = presenter::updateUsername,
                onPasswordChange = presenter::updatePassword,
                onLogin = presenter::login,
            )
            else -> when (activeSession.role) {
                UserRole.STUDENT -> StudentDashboard(activeSession, logout)
                UserRole.PROFESSOR -> ProfessorDashboard(activeSession, logout)
                UserRole.ADMIN -> AdminDashboard(activeSession, logout)
            }
        }
    }
}
