package com.mateju.frontend.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mateju.frontend.sensors.rememberAccelerometer
import kotlin.math.roundToInt
import kotlin.math.sqrt

private fun Float.dosDecimales() = (this * 100).roundToInt() / 100.0

@Composable
fun SensorCard(modifier: Modifier = Modifier) {
    val acc = rememberAccelerometer()

    Card(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Sensor de movimiento", style = MaterialTheme.typography.titleMedium)

            if (acc == null) {
                Text("Acelerómetro no disponible en esta plataforma.")
            } else {
                Text("X: ${acc.x.dosDecimales()}   Y: ${acc.y.dosDecimales()}   Z: ${acc.z.dosDecimales()}")

                // En reposo la magnitud es ≈ 9.8 (gravedad); si sube mucho, el dispositivo se está agitando
                val magnitud = sqrt(acc.x * acc.x + acc.y * acc.y + acc.z * acc.z)
                Text(
                    if (magnitud > 13f) "¡Movimiento detectado!" else "Dispositivo en reposo",
                    color = if (magnitud > 13f) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}