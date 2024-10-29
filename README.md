los demas commits anteriores a implementar firebase estan en el repositorio de examen1Eventos, cuando marrcas una tarea como favorita si vuelves a la activity anterior y vuelves a la lista de tareas aparece en pendientes aunque se haya añadido a la de hechas, pero lo demas funciona bien

https://github.com/ffernandoss/ejercicio3e.git

# Ejercicio 3 - Gestión de Tareas con Firebase y Jetpack Compose

Este proyecto es una aplicación de Android para gestionar tareas utilizando Firebase Firestore y Jetpack Compose. La aplicación permite añadir, eliminar, marcar como hechas y marcar como favoritas las tareas. Además, se asegura de que no se puedan añadir dos tareas con el mismo nombre.

## Estructura del Proyecto

El proyecto está compuesto por las siguientes actividades y archivos principales:

- `Ejercicio3MainActivity.kt`: Actividad principal que proporciona la estructura básica de la pantalla y navega a `TareasEjercicio3Activity`.
- `TareasEjercicio3Activity.kt`: Actividad para gestionar las tareas, incluyendo la adición de nuevas tareas.
- `MostrarTareasActivity.kt`: Actividad para mostrar la lista de tareas, con opciones para marcar como hechas, eliminar y marcar como favoritas.
- `DatosActivity.kt`: Actividad para mostrar los detalles de una tarea seleccionada.
- `Tarea.kt`: Clase de datos que representa una tarea.

## Funcionalidades

### Ejercicio3MainActivity

- **Descripción**: Actividad principal que proporciona una estructura básica de la pantalla y navega a `TareasEjercicio3Activity`.
- **Componentes**:
  - `Exercise3Screen`: Función composable que muestra un botón para navegar a `TareasEjercicio3Activity`.

### TareasEjercicio3Activity

- **Descripción**: Actividad para gestionar las tareas, incluyendo la adición de nuevas tareas.
- **Componentes**:
  - `TareasEjercicio3Screen`: Función composable que permite añadir nuevas tareas y mostrar la lista de tareas.
  - `addTaskToFirestore`: Función para añadir una tarea a Firebase Firestore.

### MostrarTareasActivity

- **Descripción**: Actividad para mostrar la lista de tareas, con opciones para marcar como hechas, eliminar y marcar como favoritas.
- **Componentes**:
  - `MostrarTareasScreen`: Función composable que muestra la lista de tareas y proporciona opciones para interactuar con ellas.
  - `deleteTask`: Función para eliminar una tarea de Firebase Firestore.

### DatosActivity

- **Descripción**: Actividad para mostrar los detalles de una tarea seleccionada.
- **Componentes**:
  - `DatosTareaScreen`: Función composable que muestra los detalles de una tarea.


## Uso

1. **Añadir Tareas**: En `TareasEjercicio3Activity`, completa los campos de la tarea y haz clic en "Añadir tarea".
2. **Mostrar Tareas**: Haz clic en "Mostrar tareas" para navegar a `MostrarTareasActivity` y ver la lista de tareas.
3. **Marcar como Hecha**: En `MostrarTareasActivity`, selecciona una tarea y haz clic en "Marcar hecha".
4. **Eliminar Tareas**: En `MostrarTareasActivity`, selecciona una tarea y haz clic en "Borrar".
5. **Marcar como Favorita**: En `MostrarTareasActivity`, selecciona una tarea y haz clic en "Marcar favorita".
6. **Ver Detalles de la Tarea**: En `MostrarTareasActivity`, selecciona una tarea y haz clic en "Mostrar datos" para navegar a `DatosActivity`.
