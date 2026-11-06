package com.example.misrecordatorios

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navegacion(
    vm: RecordatoriosViewModel = viewModel()
) {
    val navController = rememberNavController()
    val listado by vm.listadoUiState.collectAsStateWithLifecycle()
    val formulario by vm.formularioUiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = "listado"
    ) {
        composable("listado") {
            when (val estado = listado) {
                ListadoUiState.Cargando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                ListadoUiState.Vacio -> {
                    MensajeEstado(
                        mensaje = "Todavía no hay recordatorios",
                        textoBoton = "Crear recordatorio",
                        onAccion = {
                            vm.limpiarFormulario()
                            navController.navigate("crear")
                        }
                    )
                }

                is ListadoUiState.Error -> {
                    MensajeEstado(
                        mensaje = estado.mensaje,
                        textoBoton = "Reintentar",
                        onAccion = { vm.cargarRecordatorios() }
                    )
                }

                is ListadoUiState.ConDatos -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row {
                            TextButton(
                                onClick = { vm.simularEstadoVacio() }
                            ) {
                                Text("Probar vacío")
                            }
                            TextButton(
                                onClick = { vm.simularEstadoError() }
                            ) {
                                Text("Probar error")
                            }
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            PantallaListado(
                                recordatorios = estado.recordatorios,
                                onCrearRecordatorio = {
                                    vm.limpiarFormulario()
                                    navController.navigate("crear")
                                },
                                onRecordatorioClick = { id ->
                                    navController.navigate("detalle/$id")
                                }
                            )
                        }
                    }
                }
            }
        }

        composable("crear") {
            LaunchedEffect(formulario.guardadoExitoso) {
                if (formulario.guardadoExitoso) {
                    navController.popBackStack()
                    vm.limpiarFormulario()
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                if (formulario.errorGuardado != null) {
                    Text(
                        text = formulario.errorGuardado!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    PantallaCreacion(
                        titulo = formulario.titulo,
                        descripcion = formulario.descripcion,
                        categoria = formulario.categoria,
                        prioridad = formulario.prioridad,
                        categorias = vm.categorias,
                        enviando = formulario.enviando,
                        onTituloChange = vm::cambiarTitulo,
                        onDescripcionChange = vm::cambiarDescripcion,
                        onCategoriaChange = vm::cambiarCategoria,
                        onPrioridadChange = vm::cambiarPrioridad,
                        onGuardar = vm::guardarRecordatorio,
                        onCancelar = {
                            navController.popBackStack()
                            vm.limpiarFormulario()
                        }
                    )
                }
            }
        }

        composable(
            route = "detalle/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: -1

            PantallaDetalle(
                recordatorio = vm.obtenerPorId(id),
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun MensajeEstado(
    mensaje: String,
    textoBoton: String,
    onAccion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(mensaje)
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onAccion) {
            Text(textoBoton)
        }
    }
}

