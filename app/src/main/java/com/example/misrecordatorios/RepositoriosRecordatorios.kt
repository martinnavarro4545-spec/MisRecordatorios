package com.example.misrecordatorios

interface RepositorioRecordatorios {
    fun obtenerTodos(): List<Recordatorio>
    fun obtenerPorId(id: Int): Recordatorio?
    fun guardar(recordatorio: Recordatorio): Recordatorio
}

class RepositorioEnMemoria : RepositorioRecordatorios {

    private val recordatorios =
        recordatoriosSimulados.toMutableList()

    override fun obtenerTodos(): List<Recordatorio> {
        return recordatorios.toList()
    }

    override fun obtenerPorId(id: Int): Recordatorio? {
        return recordatorios.find { it.id == id }
    }

    override fun guardar(recordatorio: Recordatorio): Recordatorio {
        val nuevoId = (recordatorios.maxOfOrNull { it.id } ?: 0) + 1
        val nuevo = recordatorio.copy(id = nuevoId)
        recordatorios.add(nuevo)
        return nuevo
    }
}

