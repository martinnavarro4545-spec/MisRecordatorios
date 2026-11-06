package com.example.misrecordatorios
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun PantallaListado(
    recordatorios: List<Recordatorio>,
    onCrearRecordatorio: () -> Unit,
    onRecordatorioClick: (Int) -> Unit
) {
    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color(0xFFBBDEFB),
        floatingActionButton = {
            FloatingActionButton(onClick = onCrearRecordatorio) {
                Text("+")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text("Mis recordatorios")
            }

            items(recordatorios, key = { it.id }) { recordatorio ->
                TarjetaRecordatorio(
                    recordatorio = recordatorio,
                    onClick = {
                        onRecordatorioClick(recordatorio.id)
                    }
                )
            }
        }
    }
}

@Composable
fun TarjetaRecordatorio(
    recordatorio: Recordatorio,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val colorTarjeta = when (recordatorio.prioridad) {
        Prioridad.Alta -> Color(0xFFFFCDD2)
        Prioridad.Media -> Color(0xFFFFF9C4)
        Prioridad.Baja -> Color(0xFFC8E6C9)
    }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorTarjeta)
    ) {

    Column(modifier = Modifier.padding(16.dp)) {
            Text(recordatorio.titulo)
            Text("Categoría: ${recordatorio.categoria}")
            Text("Prioridad: ${recordatorio.prioridad::class.simpleName}")
            Text("Estado: ${recordatorio.estado::class.simpleName}")
        }
    }
}
