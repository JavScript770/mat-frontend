package com.mateju.frontend.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberAccelerometer(): AccelerometerData? {
    val context = LocalContext.current
    var data by remember { mutableStateOf<AccelerometerData?>(null) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                data = AccelerometerData(event.values[0], event.values[1], event.values[2])
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        if (accelerometer != null) {
            sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        }

        // Se libera el sensor al salir de la pantalla para no gastar batería
        onDispose { sensorManager.unregisterListener(listener) }
    }
    return data
}