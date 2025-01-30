package com.example.workOut.ui.screen

import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.workOut.data.ExerciseInputScreen
import com.example.workOut.data.Menu
import com.example.workOut.data.timeStringGenerator
import com.example.workOut.viewModel.ExerciseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMenuScreen (
    viewModel: ExerciseViewModel,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var currentAddScreen by remember { mutableStateOf("MenuName") }
    var menuName by remember { mutableStateOf("") }

    when (currentAddScreen) {
        "MenuName" -> {
            AddMenuNameScreen(
                viewModel = viewModel,
                onNext = { name ->
                    menuName = name
                    currentAddScreen = "Exercises"
                },
                onBack = {
                    onBack()
                }
            )
        }
        "Exercises" -> {
            AddExerciseScreen(
                menuName = menuName,
                viewModel = viewModel,
                onBack = {
                    currentAddScreen = "MenuName"
                    viewModel.deleteMenu(menuName = menuName)
                 },
                onSave = { onSave() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMenuNameScreen(
    viewModel: ExerciseViewModel,
    onNext: (String) -> Unit,
    onBack: () -> Unit
) {
    var menuName by remember { mutableStateOf("") }
    var isMenuNameValid by remember { mutableStateOf(false) }

    LaunchedEffect(menuName) {
        viewModel.isMenuNameExists(menuName, { isExists ->
            isMenuNameValid = (!isExists) && menuName.isNotBlank()
        })
    }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Set Menu Name") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cancel")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (!isMenuNameValid) {
                                Toast.makeText(
                                    context,
                                    "Menu name cannot be empty",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@IconButton
                            }
                            viewModel.insertMenu( Menu(name = menuName) )
                            onNext(menuName)
                        }
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Next")
                    }
                }
            )
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .padding(16.dp)
        ) {
            Text(
                text = "Menu Name",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = menuName,
                onValueChange = {
//                    if (isMenuNameValid) {
                        menuName = it.toString()
//                    }
                },
                label = { Text("Enter menu name") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (!isMenuNameValid) {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Close, contentDescription = "")
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (!isMenuNameValid) {
                if (menuName.isBlank()) {
                    Text(
                        text = "Menu name cannot be empty.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    Text(
                        text = "Menu name already exists.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseScreen(
    menuName: String,
    viewModel: ExerciseViewModel,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    viewModel.loadExercises(menuName = menuName, isNew = true)
    val exerciseItems by viewModel.exercises.collectAsState()
    val estimatedTime by viewModel.estimatedTime.collectAsState()

    val context = LocalContext.current

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(menuName) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (exerciseItems.any { it.name.isBlank() || it.countOrTime <= 0 }) {
                                Toast.makeText(
                                    context,
                                    "Exercise name or count/time cannot be blank or invalid.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@IconButton
                            }
                            viewModel.saveExercises()
                            onSave()
                        }
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    Text("Total: ${timeStringGenerator(estimatedTime)}", style = MaterialTheme.typography.bodyLarge)
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = { viewModel.addNewExercise(menuName = menuName) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Exercise")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
//                .verticalScroll(rememberScrollState())
        ) {
            item {
                Text(
                    text = "運動項目",
                    style = MaterialTheme.typography.titleMedium
                )
            }
//            exerciseItems.forEach { exercise ->
            items(exerciseItems) { exercise ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    ExerciseInputScreen(
                        viewModel = viewModel,
                        exercise = exercise,
                    )
                    // Remove button
                    Button(
                        onClick = {
                            viewModel.deleteExercise(exercise)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Remove", color = MaterialTheme.colorScheme.onError)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

        }
    }
}