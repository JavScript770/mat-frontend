package com.mateju.frontend.sensors

import androidx.compose.runtime.Composable

/** Lectura del acelerómetro en m/s² sobre cada eje. */
data class AccelerometerData(val x: Float, val y: Float, val z: Float)

/** Devuelve la lectura actual del acelerómetro, o null si la plataforma no tiene sensor. */
@Composable
expect fun rememberAccelerometer(): AccelerometerData?