package com.example.misrecordatorios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ListadoUiState {
    data object Cargando : ListadoUiState
    data object Vacio : ListadoUiState
    data class ConDatos(
        val recordatorios: List<Recordatorio>
    ) : ListadoUiState
    data class Error(val mensaje: String) : ListadoUiState
}

data class FormularioUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val prioridad: Prioridad? = null,
    val enviando: Boolean = false,
    val guardadoExitoso: Boolean = false,
    val errorGuardado: String? = null
)

class RecordatoriosViewModel(
    private val repositorio: RepositorioRecordatorios =
        RepositorioEnMemoria()
) : ViewModel() {

    val categorias = listOf("Compras", "Personal")

    private val _listadoUiState =
        MutableStateFlow<ListadoUiState>(ListadoUiState.Cargando)

    val listadoUiState: StateFlow<ListadoUiState> =
        _listadoUiState

    private val _formularioUiState =
        MutableStateFlow(FormularioUiState())

    val formularioUiState: StateFlow<FormularioUiState> =
        _formularioUiState

    init {
        cargarRecordatorios()
    }

    fun cargarRecordatorios() {
        viewModelScope.launch {
            _listadoUiState.value = ListadoUiState.Cargando
            delay(500)

            try {
                val datos = repositorio.obtenerTodos()

                _listadoUiState.value = if (datos.isEmpty()) {
                    ListadoUiState.Vacio
                } else {
                    ListadoUiState.ConDatos(datos)
                }
            } catch (e: Exception) {
                _listadoUiState.value =
                    ListadoUiState.Error("No se pudieron cargar los recordatorios")
            }
        }
    }

    fun simularEstadoVacio() {
        _listadoUiState.value = ListadoUiState.Vacio
    }

    fun simularEstadoError() {
        _listadoUiState.value =
            ListadoUiState.Error("Error al cargar los recordatorios")
    }

    fun obtenerPorId(id: Int): Recordatorio? {
        return repositorio.obtenerPorId(id)
    }

    fun cambiarTitulo(valor: String) {
        _formularioUiState.update {
            it.copy(titulo = valor)
        }
    }

    fun cambiarDescripcion(valor: String) {
        _formularioUiState.update {
            it.copy(descripcion = valor)
        }
    }

    fun cambiarCategoria(valor: String) {
        _formularioUiState.update {
            it.copy(categoria = valor)
        }
    }

    fun cambiarPrioridad(valor: Prioridad) {
        _formularioUiState.update {
            it.copy(prioridad = valor)
        }
    }

    fun guardarRecordatorio() {
        val datos = _formularioUiState.value

        if (datos.titulo.trim().length < 4 ||
            datos.descripcion.length > 200 ||
            datos.categoria !in categorias ||
            datos.prioridad == null ||
            datos.enviando
        ) {
            _formularioUiState.update {
                it.copy(errorGuardado = "Revisá los datos ingresados")
            }
            return
        }

        viewModelScope.launch {
            _formularioUiState.update {
                it.copy(enviando = true, errorGuardado = null)
            }

            delay(500)

            try {
                repositorio.guardar(
                    Recordatorio(
                        titulo = datos.titulo.trim(),
                        categoria = datos.categoria,
                        prioridad = datos.prioridad,
                        estado = Estado.Pendiente,
                        descripcion = datos.descripcion
                    )
                )

                _formularioUiState.update {
                    it.copy(
                        enviando = false,
                        guardadoExitoso = true
                    )
                }

                cargarRecordatorios()
            } catch (e: Exception) {
                _formularioUiState.update {
                    it.copy(
                        enviando = false,
                        errorGuardado = "No se pudo guardar"
                    )
                }
            }
        }
    }

    fun limpiarFormulario() {
        _formularioUiState.value = FormularioUiState()
    }
}

