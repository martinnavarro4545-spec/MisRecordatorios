package com.example.misrecordatorios


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaDetalle(
    recordatorio: Recordatorio?,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(modifier = modifier, containerColor = androidx.compose.ui.graphics.Color(0xFFF3E8FF)) { innerPadding ->
    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Detalle del recordatorio",
                style = MaterialTheme.typography.headlineSmall
            )

            if (recordatorio != null) {
                Text("Título: ${recordatorio.titulo}")
                Text("Categoría: ${recordatorio.categoria}")
                Text("Prioridad: ${recordatorio.prioridad::class.simpleName}")
                Text("Estado: ${recordatorio.estado::class.simpleName}")
                Text(
                    "Descripción: ${recordatorio.descripcion ?: "Sin descripción"}"
                )
            } else {
                Text("No se encontró el recordatorio")
            }

            Button(onClick = onVolver) {
                Text("Volver")
            }
        }
    }
}
