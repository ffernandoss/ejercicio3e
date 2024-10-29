// DatosActivity.kt
package com.example.ejercicio3e

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ejercicio3e.ui.theme.Ejercicio3eTheme

// Actividad que muestra los detalles de una tarea
class DatosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtiene los datos de la tarea desde el Intent
        val nombre = intent.getStringExtra("NOMBRE_TAREA") ?: ""
        val descripcion = intent.getStringExtra("DESCRIPCION_TAREA") ?: ""
        val fecha = intent.getStringExtra("FECHA_TAREA") ?: ""
        val coste = intent.getDoubleExtra("COSTE_TAREA", 0.0)
        val prioridad = intent.getBooleanExtra("PRIORIDAD_TAREA", false)

        // Establece el contenido de la actividad usando Jetpack Compose
        setContent {
            Ejercicio3eTheme {
                // Llama a la función composable para mostrar los detalles de la tarea
                DatosTareaScreen(
                    nombre = nombre,
                    descripcion = descripcion,
                    fecha = fecha,
                    coste = coste,
                    prioridad = prioridad,
                    onBackClick = {
                        // Acción al hacer clic en el botón de volver
                        val intent = Intent(this, TareasEjercicio3Activity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

// Función composable que muestra los detalles de una tarea
@Composable
fun DatosTareaScreen(
    nombre: String,
    descripcion: String,
    fecha: String,
    coste: Double,
    prioridad: Boolean,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Muestra los detalles de la tarea
        Text(text = "Nombre: $nombre")
        Text(text = "Descripción: $descripcion")
        Text(text = "Fecha: $fecha")
        Text(text = "Coste: $coste")
        Text(text = "Prioridad: ${if (prioridad) "Alta" else "Baja"}")
        Spacer(modifier = Modifier.height(16.dp))
        // Botón para volver a la pantalla anterior
        Button(onClick = onBackClick) {
            Text("Volver")
        }
    }
}

// Función de vista previa para la pantalla de detalles de la tarea
@Preview(showBackground = true)
@Composable
fun DatosTareaScreenPreview() {
    Ejercicio3eTheme {
        // Vista previa de la pantalla de detalles de la tarea
        DatosTareaScreen(
            nombre = "Tarea 1",
            descripcion = "Descripción de la tarea 1",
            fecha = "01/01/2023",
            coste = 100.0,
            prioridad = true,
            onBackClick = {}
        )
    }
}