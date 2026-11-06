package com.example.misrecordatorios


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaCreacion(
    titulo: String,
    descripcion: String,
    categoria: String,
    prioridad: Prioridad?,
    categorias: List<String>,
    enviando: Boolean,
    onTituloChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onPrioridadChange: (Prioridad) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tituloValido = titulo.trim().length >= 4
    val descripcionValida = descripcion.length <= 200
    val categoriaValida = categoria in categorias
    val formularioValido = tituloValido &&
            descripcionValida &&
            categoriaValida &&
            prioridad != null


    Scaffold(modifier = modifier, containerColor = androidx.compose.ui.graphics.Color(0xFFE8F5E9)) { innerPadding ->
    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Nuevo recordatorio",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = titulo,
                onValueChange = onTituloChange,
                label = { Text("Título") },
                isError = titulo.isNotEmpty() && !tituloValido,
                supportingText = {
                    if (titulo.isNotEmpty() && !tituloValido) {
                        Text("Ingresá al menos 4 caracteres")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = onDescripcionChange,
                label = { Text("Descripción") },
                isError = !descripcionValida,
                supportingText = {
                    Text("${descripcion.length}/200 caracteres")
                },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Categoría")

            categorias.forEach { opcion ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = categoria == opcion,
                        onClick = { onCategoriaChange(opcion) }
                    )
                    Text(opcion)
                }
            }

            Text("Prioridad")

            listOf(
                Prioridad.Alta,
                Prioridad.Media,
                Prioridad.Baja
            ).forEach { opcion ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = prioridad == opcion,
                        onClick = { onPrioridadChange(opcion) }
                    )
                    Text(
                        when (opcion) {
                            Prioridad.Alta -> "Alta"
                            Prioridad.Media -> "Media"
                            Prioridad.Baja -> "Baja"
                        }
                    )
                }
            }

            if (enviando) {
                CircularProgressIndicator()
                Text("Guardando recordatorio...")
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onGuardar,
                    enabled = formularioValido && !enviando
                ) {
                    Text("Guardar")
                }

                OutlinedButton(
                    onClick = onCancelar,
                    enabled = !enviando
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}
