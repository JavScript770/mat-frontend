import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.compose.runtime:runtime:1.12.1")
            implementation("org.jetbrains.compose.foundation:foundation:1.12.1")
            implementation("org.jetbrains.compose.material3:material3:1.12.0-alpha03")
            implementation("org.jetbrains.compose.ui:ui:1.12.1")
            implementation("org.jetbrains.compose.components:components-ui-tooling-preview:1.12.1")
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
        }
        getByName("desktopMain").dependencies {
            implementation("org.jetbrains.compose.desktop:desktop-jvm-windows-x64:1.12.1")
            implementation(libs.ktor.client.cio)
        }
    }
}

android {
    namespace = "com.mateju.frontend"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.mateju.frontend"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "0.1.0"
    }
}

compose.desktop {
    application {
        mainClass = "com.mateju.frontend.MainKt"
    }
}
