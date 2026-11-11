package com.mateju.frontend.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mateju.frontend.data.LoginResponse

private val Green = Color(0xFF168A4A)
private val GreenDark = Color(0xFF0E6B39)
private val PageBackground = Color(0xFFF4FAF6)

private data class Stat(val emoji: String, val label: String, val value: String, val tint: Color)

private data class Item(
    val emoji: String,
    val title: String,
    val detail: String,
    val progress: Float? = null,
)

// ---------------------------------------------------------------- Dashboards por rol

@Composable
fun StudentDashboard(session: LoginResponse, onLogout: () -> Unit) {
    DashboardScaffold(
        session = session,
        roleLabel = "Estudiante",
        greeting = "¡Hola, ${session.displayName}!",
        subtitle = "Tu siguiente paso está listo. ¡Vas muy bien esta semana!",
        stats = listOf(
            Stat("🔥", "Racha", "4 días", Color(0xFFFFF1D6)),
            Stat("⭐", "XP semanal", "120 / 200", Color(0xFFEAF3FF)),
            Stat("🏆", "Nivel", "Nivel 3", Color(0xFFE5F7EC)),
        ),
        extra = { StreakRow(daysDone = 4) },
        sectionTitle = "Tu ruta de hoy",
        items = listOf(
            Item("📘", "Probabilidad y estadística", "Lección 3: distribuciones y muestras", 0.6f),
            Item("🧮", "Álgebra lineal", "Práctica: matrices y determinantes", 0.35f),
            Item("📝", "Quiz pendiente", "Cálculo diferencial · vence mañana"),
        ),
        actions = listOf("Continuar lección", "Ver mi progreso"),
        onLogout = onLogout,
    )
}

@Composable
fun ProfessorDashboard(session: LoginResponse, onLogout: () -> Unit) {
    DashboardScaffold(
        session = session,
        roleLabel = "Docente",
        greeting = "Hola, ${session.displayName}",
        subtitle = "Revisa la actividad de tus grupos.",
        stats = listOf(
            Stat("📚", "Cursos activos", "3", Color(0xFFEAF3FF)),
            Stat("✅", "Prácticas hoy", "28", Color(0xFFE5F7EC)),
            Stat("✍️", "Por revisar", "8", Color(0xFFFFF1D6)),
        ),
        sectionTitle = "Mis grupos",
        items = listOf(
            Item("👥", "Probabilidad · Grupo A", "32 estudiantes · avance promedio 72%", 0.72f),
            Item("👥", "Estadística · Grupo B", "29 estudiantes · avance promedio 58%", 0.58f),
            Item("👥", "Álgebra · Grupo C", "35 estudiantes · avance promedio 81%", 0.81f),
        ),
        actions = listOf("Ver grupos", "Crear actividad"),
        onLogout = onLogout,
    )
}

@Composable
fun AdminDashboard(session: LoginResponse, onLogout: () -> Unit) {
    DashboardScaffold(
        session = session,
        roleLabel = "Administrador",
        greeting = "Bienvenida, ${session.displayName}",
        subtitle = "Resumen operativo de Mateju.",
        stats = listOf(
            Stat("👤", "Usuarios", "146", Color(0xFFE5F7EC)),
            Stat("🗂️", "Materias", "12", Color(0xFFEAF3FF)),
            Stat("📬", "Solicitudes", "3", Color(0xFFFFF1D6)),
        ),
        sectionTitle = "Pendientes de revisión",
        items = listOf(
            Item("🆕", "Alta de docente", "Solicitud de acceso · hace 2 horas"),
            Item("📖", "Nueva materia", "Propuesta: Estadística aplicada"),
            Item("🔐", "Restablecer contraseña", "Un usuario solicitó ayuda con su cuenta"),
        ),
        actions = listOf("Gestionar catálogo", "Administrar usuarios"),
        onLogout = onLogout,
    )
}

// ---------------------------------------------------------------- Estructura común

@Composable
private fun DashboardScaffold(
    session: LoginResponse,
    roleLabel: String,
    greeting: String,
    subtitle: String,
    stats: List<Stat>,
    sectionTitle: String,
    items: List<Item>,
    actions: List<String>,
    onLogout: () -> Unit,
    extra: @Composable () -> Unit = {},
) {
    var confirmLogout by remember { mutableStateOf(false) }
    var notice by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(PageBackground)) {
        TopBar(session, roleLabel, onLogoutClick = { confirmLogout = true })

        // Contenido con scroll: el botón de cerrar sesión ya NO queda fuera de la pantalla
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier.widthIn(max = 900.dp).fillMaxWidth().padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(greeting, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = Color(0xFF51635A))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    stats.forEach { StatCard(it, Modifier.weight(1f)) }
                }

                extra()

                Text(sectionTitle, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                items.forEach { ItemCard(it) }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    actions.forEachIndexed { index, label ->
                        if (index == 0) {
                            Button(onClick = { notice = label }) { Text(label) }
                        } else {
                            OutlinedButton(onClick = { notice = label }) { Text(label) }
                        }
                    }
                }
            }
        }
    }

    if (confirmLogout) {
        AlertDialog(
            onDismissRequest = { confirmLogout = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Quieres salir de tu cuenta?") },
            confirmButton = {
                TextButton(onClick = { confirmLogout = false; onLogout() }) { Text("Sí, salir") }
            },
            dismissButton = {
                TextButton(onClick = { confirmLogout = false }) { Text("Cancelar") }
            },
        )
    }

    notice?.let { label ->
        AlertDialog(
            onDismissRequest = { notice = null },
            title = { Text(label) },
            text = { Text("Esta sección estará disponible en la siguiente entrega.") },
            confirmButton = { TextButton(onClick = { notice = null }) { Text("Entendido") } },
        )
    }
}

@Composable
private fun TopBar(session: LoginResponse, roleLabel: String, onLogoutClick: () -> Unit) {
    val initials = session.displayName
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(Green, GreenDark)))
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("mateju", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier.size(40.dp).background(Color.White, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(initials, color = Green, fontWeight = FontWeight.Bold)
        }
        Column {
            Text(session.displayName, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(roleLabel, color = Color(0xFFCDEED9), style = MaterialTheme.typography.labelMedium)
        }
        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Green),
        ) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
private fun StatCard(stat: Stat, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = stat.tint),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(stat.emoji, fontSize = 26.sp)
            Text(stat.value, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            Text(stat.label, style = MaterialTheme.typography.labelLarge, color = Color(0xFF51635A))
        }
    }
}

@Composable
private fun ItemCard(item: Item) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier.size(48.dp).background(Color(0xFFE5F7EC), CircleShape),
                contentAlignment = Alignment.Center,
            ) { Text(item.emoji, fontSize = 22.sp) }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(item.detail, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF51635A))
                item.progress?.let { ProgressBar(it) }
            }
        }
    }
}

@Composable
private fun ProgressBar(progress: Float) {
    Box(
        modifier = Modifier.fillMaxWidth().height(10.dp)
            .background(Color(0xFFE3EAE5), RoundedCornerShape(50)),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(progress.coerceIn(0f, 1f)).fillMaxHeight()
                .background(Green, RoundedCornerShape(50)),
        )
    }
}

@Composable
private fun StreakRow(daysDone: Int) {
    val days = listOf("L", "M", "X", "J", "V", "S", "D")
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Tu semana", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                days.forEachIndexed { index, day ->
                    val done = index < daysDone
                    Box(
                        modifier = Modifier.size(38.dp)
                            .background(if (done) Green else Color(0xFFE3EAE5), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(day, color = if (done) Color.White else Color(0xFF51635A), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
