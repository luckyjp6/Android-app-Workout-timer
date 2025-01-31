package com.example.workOut.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.workOut.data.timeStringGenerator
import com.example.workOut.viewModel.ExerciseViewModel
import com.example.workOut.R
import com.example.workOut.viewModel.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuListScreen(
    viewModel: ExerciseViewModel,
    settingViewModel: SettingViewModel,
    onMenuSelected: (menu: String) -> Unit,
    onAddMenuClicked: () -> Unit
) {
    val menus by viewModel.allMenus.collectAsState()
    val isDarkTheme by settingViewModel.isDarkTheme.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Menu List",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                ) },
                actions = {
                    IconButton(onClick = { settingViewModel.changeIsDarkTheme() }) {
                        if (isDarkTheme) {
                            val painter: Painter = painterResource(id = R.drawable.sun)
                            Icon(painter = painter, contentDescription = "Light",
                                 tint = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            val painter: Painter = painterResource(id = R.drawable.moon)
                            Icon(painter = painter, contentDescription = "Dark",
                                tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddMenuClicked() },
                contentColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Menu",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    ) { paddingValues ->
        LazyColumn (
            modifier = Modifier.padding(paddingValues)
        ) {
            items(menus) { menu ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onMenuSelected(menu.name) }
                        .padding(10.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = menu.name,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Total: ${
                                timeStringGenerator(
                                    menu.estimatedTime,
                                    needsHour = true
                                )
                            }",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}