package com.example.workOut.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.workOut.data.timeStringGenerator
import com.example.workOut.viewModel.TTSViewModel
import kotlinx.coroutines.delay

@Composable
fun CountDownTimer (
    ttsViewModel: TTSViewModel,
    startSpeak: Int = 4,
    totalTime: Int,
    onFinish: () -> Unit,
    style: TextStyle
) {
    var timeLeft by remember { mutableIntStateOf(totalTime) }
    var isPaused by remember { mutableStateOf(false) }

    LaunchedEffect(timeLeft, isPaused) {
        if (!isPaused && timeLeft > 0) {
            delay(1000L)
            timeLeft--

            if (timeLeft in 1..startSpeak) ttsViewModel.speak(timeLeft.toString())
        }
        if (timeLeft == 0) {
            onFinish()
        }
    }
    Text(timeStringGenerator(timeLeft), style = style)
    Spacer(Modifier.height(8.dp))
    Row {
        Button(onClick = { isPaused = !isPaused }) {
            Text(
                text = if (isPaused) "Continue" else "Pause",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Button(onClick = { timeLeft = 0 }) {
            Text(
                text = "Skip",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}