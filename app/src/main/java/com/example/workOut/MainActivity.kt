package com.example.workOut


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workOut.ui.screen.AddEditMenuScreen
import com.example.workOut.ui.screen.MenuDetailsScreen
import com.example.workOut.ui.screen.MenuListScreen
import com.example.workOut.ui.screen.TrainingScreen
import com.example.workOut.ui.theme.DynamicThemeSetup
import com.example.workOut.viewModel.ExerciseViewModel
import com.example.workOut.viewModel.SettingViewModel
import com.example.workOut.viewModel.TTSViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val exerciseViewModel: ExerciseViewModel = hiltViewModel()
                    val ttsViewModel: TTSViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))[TTSViewModel::class.java]
                    val settingViewModel = SettingViewModel()

                    settingViewModel.setIsDarkTheme(isSystemInDarkTheme())
                    DynamicThemeSetup(settingViewModel) {
                        WorkOut(exerciseViewModel, ttsViewModel, settingViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun WorkOut(
    exerciseViewModel: ExerciseViewModel,
    ttsViewModel: TTSViewModel,
    settingViewModel: SettingViewModel
) {
    var menu by remember { mutableStateOf("") }
    var currentScreen by remember { mutableStateOf("menuList") }

    when (currentScreen) {
        "menuList" -> MenuListScreen(
            viewModel = exerciseViewModel,
            settingViewModel = settingViewModel,
            onMenuSelected = { selectedMenu ->
                menu = selectedMenu
                currentScreen = "menuDetails"
            },
            onAddMenuClicked = { currentScreen = "addEditMenu" }
        )
        "addEditMenu" -> AddEditMenuScreen(
            viewModel = exerciseViewModel,
            onBack = { currentScreen = "menuList" },
            onSave = { currentScreen = "menuList" }
        )
        "menuDetails" -> MenuDetailsScreen(
            viewModel = exerciseViewModel,
            menu = menu,
            onStart = { currentScreen = "training" },
            onBack = { currentScreen = "menuList" }
        )
        "training" -> TrainingScreen(
            ttsViewModel = ttsViewModel,
            exercises = exerciseViewModel.exercises.collectAsState().value,
            onFinish = { currentScreen = "menuDetails" }
        )
    }
}