package com.example.workOut.data

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.workOut.viewModel.ExerciseViewModel
import java.util.Locale

// Exercise menu
@Entity(tableName = "menus")
data class Menu (
//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0,
    @PrimaryKey
    var name: String,
    var estimatedTime: Int = 0
)

// Details of each menu
@Entity(
    tableName = "exercises",
    foreignKeys = [ForeignKey(
        entity = Menu::class,
        parentColumns = ["name"],
        childColumns = ["menu"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(index = true)
    var menu: String, // the menu this exercise belongs to
    var name: String = "",
    var description: String = "",
    var type: ExerciseType = ExerciseType.COUNTER, // count or time (15 times of 30 seconds)
    var numSet: Int = 1, // num of set
    var countOrTime: Int = 1, // 15 times of 30 seconds
    var restTime: Int = 0 // rest time between each set
)

enum class ExerciseType {
    COUNTER, // e.g., jump 15 times
    TIMER, // e.g., hold for 30 seconds
}

fun timeStringGenerator(
    timeSec: Int,
    needsHour: Boolean = false
): String {
    val hour = timeSec / 60 / 60
    val minutes = timeSec / 60
    val seconds = timeSec % 60

    if (needsHour) {
        return String.format(Locale.US, "%02d:%02d:%02d", hour, minutes % 60, seconds)
    } else {
        return String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }
}

@Composable
fun ExerciseDisplayScreen(
    exercise: Exercise,
    onlyNameAndDescription: Boolean = false
) {
    Column {
        // Name
        Text(
            text = exercise.name,
             style = if (onlyNameAndDescription) MaterialTheme.typography.headlineLarge else MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(4.dp))

        // description
        Text(exercise.description, style = if (onlyNameAndDescription) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))

        if (onlyNameAndDescription) return

            // countOrTime
        Column {
            if (exercise.type == ExerciseType.COUNTER) {
                Text("一組 ${exercise.countOrTime} 次")
            } else if (exercise.type == ExerciseType.TIMER) {
                Text("一組 ${timeStringGenerator(exercise.countOrTime)}")
            }
            Text("休息 ${timeStringGenerator(exercise.restTime)}")
        }
        Spacer(Modifier.height(4.dp))

        // numSet
        Text("共 ${exercise.numSet} 組")
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
fun ExerciseInputScreen(
    viewModel: ExerciseViewModel,
    exercise: Exercise
) {
    // name
    OutlinedTextField(
        value = exercise.name,
        onValueChange = {
            viewModel.updateExercise(exercise.copy(name = it))
        },
        label = { Text("運動名稱") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))

    // description
    OutlinedTextField(
        value = exercise.description,
        onValueChange = {
            viewModel.updateExercise(exercise.copy(description = it))
        },
        label = { Text("詳細敘述（選填）") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))

    // type
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("計算類型")
        Spacer(modifier = Modifier.width(8.dp))
        DropdownMenu(
            options = listOf("計數", "計時"),
            selectedOption = exercise.type,
            onOptSelected = {
                if (it == "計數") {
                    viewModel.updateExercise(exercise.copy(type = ExerciseType.COUNTER))
                } else if (it == "計時") {
                    viewModel.updateExercise(exercise.copy(type = ExerciseType.TIMER))
                }
            }
        )
    }
    Spacer(modifier = Modifier.height(8.dp))

    // counts
    if (exercise.type == ExerciseType.COUNTER) {
        Text(
            text = "次數",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        ExerciseInputWithWheel(
            value = exercise.countOrTime,
            onValueChange = { value ->
                viewModel.updateExercise(exercise.copy(countOrTime = value))
            },
            range = (1..99).toList()
        )
    } else if (exercise.type == ExerciseType.TIMER) {
        Text(
            text = "持續時間",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        // Minute
        Row {
            ExerciseInputWithWheel(
                value = exercise.countOrTime / 60,
                onValueChange = { value ->
                    viewModel.updateExercise(exercise.copy(countOrTime = value * 60 + exercise.countOrTime % 60))
                },
                range = (0..99).toList()
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(":", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Seconds
            ExerciseInputWithWheel(
                value = exercise.countOrTime % 60,
                onValueChange = { value ->
                    viewModel.updateExercise(exercise.copy(countOrTime = exercise.countOrTime / 60 * 60 + value))
                },
                range = (0..59).toList()
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))

    // sets
    Text(
        text = "組數",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
    ExerciseInputWithWheel(
        value = exercise.numSet,
        onValueChange = { value ->
            viewModel.updateExercise(exercise.copy(numSet = value))
        },
        range = (1..99).toList()
    )

    // rest time
    Text(
        text = "休息時間",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
    // Minute
    Row {
        ExerciseInputWithWheel(
            value = exercise.restTime / 60,
            onValueChange = { value ->
                viewModel.updateExercise(exercise.copy(restTime = value * 60 + exercise.restTime % 60))
            },
            range = (0..99).toList()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(":", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))

        // Seconds
        ExerciseInputWithWheel(
            value = exercise.restTime % 60,
            onValueChange = { value ->
                viewModel.updateExercise(exercise.copy(restTime = exercise.restTime / 60 * 60 + value))
            },
            range = (0..59).toList()
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun DropdownMenu(
    options: List<String>,
    selectedOption: ExerciseType,
    onOptSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (selectedOption == ExerciseType.COUNTER) Text("計數")
            else if (selectedOption == ExerciseType.TIMER) Text("計時")
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Choose")
        }

        androidx.compose.material3.DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ExerciseInputWithWheel(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: List<Int>
) {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = range.indexOf(value)
    )
    val index = remember { mutableIntStateOf(range.indexOf(value)) }

    LaunchedEffect(value) {
        index.intValue = range.indexOf(value)
        if (index.intValue in range.indices) {
            listState.animateScrollToItem(index.intValue)
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .height(60.dp)
                .width(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.height(100.dp),
                flingBehavior = rememberSnapFlingBehavior(listState)
            ) {
                items(range) { item ->
                    Text(
                        text = item.toString(),
                        fontSize = 20.sp,
                        fontWeight = if (item == value) FontWeight.Bold else FontWeight.Normal,
                        color = if (item == value) Color.Black else Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onValueChange(item)
                            }
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}