// MostrarTareasActivity.kt
package com.example.ejercicio3e

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.example.ejercicio3e.ui.theme.Ejercicio3eTheme

class MostrarTareasActivity : ComponentActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var showDoneTasks by remember { mutableStateOf(false) }
            var selectedTask by remember { mutableStateOf<Tarea?>(null) }
            var listaTareas by remember { mutableStateOf(listOf<Tarea>()) }

            // Carga inicial de tareas desde Firestore
            LaunchedEffect(Unit) {
                db.collection("tareas").get()
                    .addOnSuccessListener { result ->
                        listaTareas = result.map { document -> document.toObject<Tarea>() }
                    }
            }

            Ejercicio3eTheme {
                MostrarTareasScreen(
                    listaTareas = listaTareas,
                    showDoneTasks = showDoneTasks,
                    selectedTask = selectedTask,
                    onBackClick = { finish() },
                    onTaskClick = { tarea -> selectedTask = tarea },
                    onToggleShowDoneTasks = { showDoneTasks = !showDoneTasks },
                    onMarkDone = { tarea ->
                        val updatedTarea = tarea.copy(hecha = true)
                        db.collection("tareas").document(tarea.nombre)
                            .set(updatedTarea)
                            .addOnSuccessListener {
                                listaTareas = listaTareas.map { if (it.nombre == tarea.nombre) updatedTarea else it }
                            }
                    },
                    onShowData = { tarea ->
                        val intent = Intent(this, DatosActivity::class.java).apply {
                            putExtra("NOMBRE_TAREA", tarea.nombre)
                            putExtra("DESCRIPCION_TAREA", tarea.descripcion)
                            putExtra("FECHA_TAREA", tarea.fecha)
                            putExtra("COSTE_TAREA", tarea.coste)
                            putExtra("PRIORIDAD_TAREA", tarea.prioridad)
                        }
                        startActivity(intent)
                    },
                    onDeleteTask = { tarea ->
                        deleteTask(tarea) { success ->
                            if (success) {
                                listaTareas = listaTareas.filter { it.nombre != tarea.nombre }
                            }
                        }
                    },
                    onMarkFavorite = { tarea ->
                        val updatedTarea = tarea.copy(favorita = true)
                        db.collection("tareas").document(tarea.nombre)
                            .set(updatedTarea)
                            .addOnSuccessListener {
                                listaTareas = listaTareas.filter { it.nombre != tarea.nombre }
                            }
                    }
                )
            }
        }
    }

    private fun deleteTask(tarea: Tarea, onComplete: (Boolean) -> Unit) {
        Log.d("MostrarTareasActivity", "Attempting to delete task: ${tarea.nombre}")
        db.collection("tareas")
            .whereEqualTo("nombre", tarea.nombre)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("tareas").document(document.id).delete()
                        .addOnSuccessListener {
                            Log.d("MostrarTareasActivity", "Successfully deleted task: ${tarea.nombre}")
                            onComplete(true)
                        }
                        .addOnFailureListener { e ->
                            Log.e("MostrarTareasActivity", "Error deleting task: ${tarea.nombre}", e)
                            onComplete(false)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("MostrarTareasActivity", "Error finding task: ${tarea.nombre}", e)
                onComplete(false)
            }
    }
}

@Composable
fun MostrarTareasScreen(
    listaTareas: List<Tarea>,
    showDoneTasks: Boolean,
    selectedTask: Tarea?,
    onBackClick: () -> Unit,
    onTaskClick: (Tarea) -> Unit,
    onToggleShowDoneTasks: () -> Unit,
    onMarkDone: (Tarea) -> Unit,
    onShowData: (Tarea) -> Unit,
    onDeleteTask: (Tarea) -> Unit,
    onMarkFavorite: (Tarea) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Button(onClick = onBackClick) {
            Text("Volver")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onToggleShowDoneTasks) {
            Text("Mostrar/ocultar tareas hechas")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(listaTareas) { tarea ->
                if (showDoneTasks || !tarea.hecha) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTaskClick(tarea) }
                            .padding(8.dp)
                            .background(if (tarea.hecha) Color.Gray else Color.Transparent)
                    ) {
                        Text(text = tarea.nombre)
                        if (selectedTask == tarea) {
                            Row {
                                Button(onClick = { onMarkDone(tarea) }) {
                                    Text("Marcar hecha")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { onShowData(tarea) }) {
                                    Text("Mostrar datos")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { onDeleteTask(tarea) }) {
                                    Text("Borrar")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { onMarkFavorite(tarea) }) {
                                    Text("Marcar favorita")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
