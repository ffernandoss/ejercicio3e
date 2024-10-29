// TareasEjercicio3Activity.kt
package com.example.ejercicio3e

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import com.example.ejercicio3e.ui.theme.Ejercicio3eTheme

// Clase de datos para representar una tarea
data class Tarea(
    val nombre: String,
    val descripcion: String,
    val fecha: String,
    val coste: Double,
    val prioridad: Boolean,
    val hecha: Boolean = false,
    val favorita: Boolean = false
)

// Actividad principal para gestionar las tareas del ejercicio 3
class TareasEjercicio3Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Ejercicio3eTheme {
                TareasEjercicio3Screen(onShowTasksClick = { tareas ->
                    val intent = Intent(this, MostrarTareasActivity::class.java)
                    intent.putStringArrayListExtra("NOMBRES_TAREAS", ArrayList(tareas.map { it.nombre }))
                    startActivity(intent)
                })
            }
        }
    }
}

// Función composable para la pantalla de gestión de tareas
@Composable
fun TareasEjercicio3Screen(modifier: Modifier = Modifier, onShowTasksClick: (List<Tarea>) -> Unit) {
    // Variables de estado para los campos de entrada y la lista de tareas
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var coste by remember { mutableStateOf("") }
    var prioridad by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val listaTareas = remember { mutableStateOf(loadTareas(context)) }
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    // Mostrar un Toast si showToast es verdadero
    if (showToast) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            showToast = false
        }
    }

    // Función para verificar si un valor es numérico
    fun isNumeric(value: String): Boolean {
        return value.toDoubleOrNull() != null
    }

    // Configuración del DatePickerDialog para seleccionar la fecha
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            fecha = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Estructura de la interfaz de usuario
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Campo de texto para el nombre de la tarea
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Campo de texto para la descripción de la tarea
        TextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Campo de texto para la fecha de la tarea
        TextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Select date")
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Campo de texto para el coste de la tarea
        TextField(
            value = coste,
            onValueChange = { coste = it },
            label = { Text("Coste") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Switch para indicar si la tarea tiene prioridad
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Prioridad")
            Switch(
                checked = prioridad,
                onCheckedChange = { prioridad = it }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Botón para añadir una nueva tarea
        Button(onClick = {
            if (nombre.isBlank() || descripcion.isBlank() || fecha.isBlank() || coste.isBlank() || !isNumeric(coste)) {
                toastMessage = "Todos los campos son obligatorios y el coste debe ser numérico"
                showToast = true
            } else if (listaTareas.value.any { it.nombre == nombre }) {
                toastMessage = "Ya existe una tarea con ese nombre"
                showToast = true
            } else {
                val nuevaTarea = Tarea(nombre, descripcion, fecha, coste.toDouble(), prioridad)
                listaTareas.value = listaTareas.value + nuevaTarea
                saveTareas(context, listaTareas.value)
                nombre = ""
                descripcion = ""
                fecha = ""
                coste = ""
                prioridad = false
                toastMessage = "Tarea añadida"
                showToast = true
            }
        }) {
            Text("Añadir tarea")
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Botón para mostrar la lista de tareas
        Button(onClick = { onShowTasksClick(listaTareas.value) }) {
            Text("Mostrar tareas")
        }
    }
}

// Función para guardar la lista de tareas en SharedPreferences
fun saveTareas(context: android.content.Context, listaTareas: List<Tarea>) {
    val sharedPreferences = context.getSharedPreferences("TareasPrefs", android.content.Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val gson = Gson()
    val json = gson.toJson(listaTareas)
    editor.putString("TareasList", json)
    editor.apply()
}

// Función para cargar la lista de tareas desde SharedPreferences
fun loadTareas(context: android.content.Context): List<Tarea> {
    val sharedPreferences = context.getSharedPreferences("TareasPrefs", android.content.Context.MODE_PRIVATE)
    val gson = Gson()
    val json = sharedPreferences.getString("TareasList", null)
    val type = object : TypeToken<List<Tarea>>() {}.type
    return if (json != null) gson.fromJson(json, type) else listOf()
}

// Función de vista previa para la pantalla de gestión de tareas
@Preview(showBackground = true)
@Composable
fun TareasEjercicio3ScreenPreview() {
    Ejercicio3eTheme() {
        TareasEjercicio3Screen(onShowTasksClick = {})
    }
}