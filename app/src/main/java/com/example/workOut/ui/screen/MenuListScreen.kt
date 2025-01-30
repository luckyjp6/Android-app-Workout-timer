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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.workOut.data.timeStringGenerator
import com.example.workOut.viewModel.ExerciseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuListScreen(
    viewModel: ExerciseViewModel,
    onMenuSelected: (menu: String) -> Unit,
    onAddmenuClicked: () -> Unit
) {
    val menus by viewModel.allMenus.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Menu List",
                    style = MaterialTheme.typography.headlineLarge
                ) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddmenuClicked() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Menu")
            }
        }
    ) { paddingValues ->
        LazyColumn (
            modifier = Modifier.padding(paddingValues)
        ) {
            items(menus) { menu ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onMenuSelected(menu.name) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = menu.name,
                        style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Total: ${timeStringGenerator(menu.estimatedTime, needsHour = true)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}