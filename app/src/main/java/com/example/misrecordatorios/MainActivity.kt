package com.example.misrecordatorios

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.misrecordatorios.ui.theme.MisRecordatoriosTheme



data class  Recordatorio(
    val titulo: String,
    val categoria: String,
    val prioridad: Prioridad,
    val estado: Estado,

    val descripcion: String? = null,
    val id: Int = 0

)
sealed class Prioridad {
    object Alta : Prioridad()
    object Media : Prioridad()
    object Baja : Prioridad()
}

    sealed class Estado {
        object Pendiente : Estado()
        object Completado: Estado()
    }

val recordatoriosSimulados = listOf(
    Recordatorio(
        id = 1,
            titulo = "Comprar lana",
    categoria = "Compras",
    prioridad = Prioridad.Media,
    estado= Estado.Pendiente
    ),
    Recordatorio(
        id = 2,
        titulo = "Ir al gimnasio",
        categoria = "Personal",
        prioridad = Prioridad.Alta,
        estado = Estado.Pendiente
    )
)
val categorias = recordatoriosSimulados.map { it.categoria }.toSet ()
val coloresCategorias = mapOf(
    "Compras" to "Verde",
        "Personal" to "Azul"
)
val recordatoriosPendientes = recordatoriosSimulados.filter { it.estado == Estado.Pendiente
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MisRecordatoriosTheme {
                Navegacion()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val context =
            LocalContext.current
        var recordatorio by remember { mutableStateOf("") }
        val listaRecordatorios = remember { mutableStateListOf<String>() }
        val listaSimulada = recordatoriosSimulados
        OutlinedTextField(
        value = recordatorio,
            onValueChange = { recordatorio = it }
        )
        Text(
            text = "Bienvenida a Mis Recordatorios"
        )
        Button(onClick ={ listaRecordatorios.add(recordatorio)
        }) {
        Text("Agregar recordatorio") }
        listaRecordatorios.forEach { item ->
            Text(text = item)
        }
        listaSimulada.forEach { item ->
            Text(text = item.titulo)
            Text(text = "Categoria: ${item.categoria}")

            Text(
                text = "Prioridad: ${item.prioridad::class.simpleName}",
                color = when (item.prioridad) {
                    Prioridad.Alta -> androidx.compose.ui.graphics.Color.Red
                    Prioridad.Media -> androidx.compose.ui.graphics.Color(0xFFB36B00)
                    Prioridad.Baja -> androidx.compose.ui.graphics.Color(0xFF2E7D32)
                }
            )

            Text(text = "Estado:${item.estado::class.simpleName}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MisRecordatoriosTheme {
        Greeting("Android")
    }
}