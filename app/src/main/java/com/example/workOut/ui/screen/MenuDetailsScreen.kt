package com.example.workOut.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.workOut.data.ExerciseDisplayScreen
import com.example.workOut.data.ExerciseInputScreen
import com.example.workOut.data.timeStringGenerator
import com.example.workOut.viewModel.ExerciseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDetailsScreen(
    viewModel: ExerciseViewModel,
    menu: String,
    onBack: () -> Unit,
    onStart: () -> Unit
) {
    viewModel.loadExercises(menu)
    val exerciseItems by viewModel.exercises.collectAsState()
    val estimatedTime by viewModel.estimatedTime.collectAsState()
    var menuName by remember { mutableStateOf(menu) }
    var isEditMenu by remember { mutableStateOf(false) }
    var newMenuName by remember { mutableStateOf(menuName) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu details", style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Home",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                actions = {
                    Text(timeStringGenerator(estimatedTime),
                        color = MaterialTheme.colorScheme.onPrimary)
                    IconButton(onClick = onStart) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Start",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    Text("Total: ${timeStringGenerator(estimatedTime)}", style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primaryContainer)
                },
                floatingActionButton = {
                    Row {
                        FloatingActionButton(
                            onClick = {
                                viewModel.deleteMenu(menuName = menuName)
                                onBack()
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove menu",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        FloatingActionButton(
                            onClick = { viewModel.addNewExercise(menuName = menuName) }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Exercise",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
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
//                .verticalScroll((rememberScrollState()))
        ) {
            // Menu name
            if (isEditMenu) {
                item {
                    OutlinedTextField(
                        value = newMenuName,
                        onValueChange = { newMenuName = it },
                        label = { Text("Menu Name") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.displaySmall
                    )
                    Button(
                        onClick = {
                            viewModel.updateMenuName(menuName, newMenuName)
                            menuName = newMenuName
                            isEditMenu = false
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Save")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = menuName,
                            style = MaterialTheme.typography.displaySmall,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { isEditMenu = true }) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Exercises
            items(exerciseItems) { exercise ->
                var isEditing by remember { mutableStateOf(false) }
                var oldExercise by remember { mutableStateOf(exercise) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    if (isEditing) {
                        ExerciseInputScreen(
                            viewModel = viewModel,
                            exercise = exercise
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    viewModel.updateExercise(oldExercise)
                                    isEditing = false
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Cancel")
                            }
                            Button(
                                onClick = {
                                    viewModel.saveExercise(exercise)
                                    isEditing = false
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Save")
                            }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            ExerciseDisplayScreen(exercise = exercise)
                            IconButton(
                                onClick = {
                                    isEditing = true
                                    oldExercise = exercise
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null)
                            }
                        }
                    }
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
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}