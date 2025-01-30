package com.example.workOut.ui.screen

import com.example.workOut.utils.CountDownTimer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.workOut.data.Exercise
import com.example.workOut.data.ExerciseDisplayScreen
import com.example.workOut.data.ExerciseType
import com.example.workOut.viewModel.TTSViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen (
    ttsViewModel: TTSViewModel,
    exercises: List<Exercise>,
    onFinish: () -> Unit
) {
    // TODO: where's the for loop??
    var currentIndex by remember { mutableIntStateOf(0) }
    val currentExercise = exercises.getOrNull(currentIndex)
    var isRest by remember { mutableStateOf(false) }
    var isPrepared by remember { mutableStateOf(false) }
    var numSet by remember { mutableIntStateOf(0) }

    // Finish
    if (currentExercise == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "你超棒！", style = MaterialTheme.typography.displayLarge)
            ttsViewModel.speak("你超棒")
            Button(onClick = onFinish) {
                Text("Home")
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentExercise.menu, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onFinish) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Home")
                    }
                }
            )
        }
    ) { paddingValue ->

        if (isRest) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Take a rest", style = MaterialTheme.typography.displayLarge)
                CountDownTimer(
                    ttsViewModel = ttsViewModel,
                    totalTime = currentExercise.restTime,
                    onFinish = {
                        isRest = false
                        isPrepared = false
                        numSet++
                        if (numSet == currentExercise.numSet) {
                            currentIndex++
                            numSet = 0
                        }
                    },
                    style = MaterialTheme.typography.displayLarge
                )
                // TODO: skip rest time
            }
        } else {
//            Spacer(Modifier.height(16.dp))
//            Spacer(Modifier.width(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValue),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                ExerciseDisplayScreen(currentExercise, onlyNameAndDescription = true)
                Spacer(modifier = Modifier.height(16.dp))
                Text("第 ${numSet + 1} 組")
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (currentExercise.type == ExerciseType.TIMER) {
                    if (isPrepared) {
                        Text("Go!", style = MaterialTheme.typography.displayLarge)
                        Spacer(Modifier.height(8.dp))
                        CountDownTimer(
                            ttsViewModel = ttsViewModel,
                            totalTime = currentExercise.countOrTime,
                            onFinish = { isRest = true },
                            style = MaterialTheme.typography.displayLarge
                        )
                    } else {
                        // Prepare time: 5 sec
                        ttsViewModel.speak("${currentExercise.name}, ${currentExercise.description}, ${currentExercise.countOrTime} 秒一組, 第${numSet+1}組")
                        Text("Ready...", style = MaterialTheme.typography.displayLarge)
                        Spacer(Modifier.height(8.dp))
                        CountDownTimer(
                            ttsViewModel = ttsViewModel,
                            totalTime = 5,
                            startSpeak = 3,
                            onFinish = { isPrepared = true },
                            style = MaterialTheme.typography.displayLarge
                        )
                    }
                } else {
                    ttsViewModel.speak("${currentExercise.name}, ${currentExercise.description}, ${currentExercise.countOrTime} 次一組, 第${numSet+1}組")
                    Text(
                        text = "${currentExercise.countOrTime} 次",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { isRest = true }) {
                        Text(
                            text = "Next",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
}