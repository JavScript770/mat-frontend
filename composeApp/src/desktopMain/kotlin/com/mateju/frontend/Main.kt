package com.mateju.frontend

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Mateju",
        // Tamaño inicial cómodo para que se vea completo el dashboard
        state = rememberWindowState(size = DpSize(1000.dp, 760.dp)),
    ) {
        MatejuApp()
    }
}
