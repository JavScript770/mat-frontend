# Mateju frontend

Frontend de la primera entrega de Mateju, construido con Kotlin Multiplatform y Compose Multiplatform.

## Incluye

- Pantalla de inicio de sesión.
- Navegación por roles: `STUDENT`, `PROFESSOR` y `ADMIN`.
- Cliente Ktor para consumir el backend local.
- Código compartido para Android y escritorio en `composeApp/src/commonMain`.

## Ejecutar en escritorio

Con el backend iniciado en `http://localhost:8080`, ejecutar desde esta carpeta:

```powershell
.\gradlew.bat :composeApp:run
```

Para Android, abrir esta carpeta con Android Studio y ejecutar la configuración `composeApp`. El emulador se conecta al backend de la computadora mediante `http://10.0.2.2:8080`.

Las instrucciones completas, cuentas de demostración y enlaces de entrega se encuentran en el documento de entrega compartido por el equipo.
