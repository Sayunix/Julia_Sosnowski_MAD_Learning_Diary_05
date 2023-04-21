package com.example.tasksapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasksapplication.data.TaskDatabase
import com.example.tasksapplication.models.Task
import com.example.tasksapplication.models.getTasks
import com.example.tasksapplication.repositories.TaskRepository
import com.example.tasksapplication.ui.theme.TasksApplicationTheme
import com.example.tasksapplication.utils.InjectorUtils
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TasksApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val viewModel: TaskViewModel = viewModel(factory = InjectorUtils.provideTaskViewModelFactory(
                        LocalContext.current))

                    val tasksState = viewModel.tasks.collectAsState()
                    val coroutineScope = rememberCoroutineScope()

                    Column {
                        AddTask(
                            onAddClick = {task ->
                                coroutineScope.launch {//damit es nicht auf dem Main-Thread läuft
                                    viewModel.addTask(task)
                                }
                            }
                        )
                        TaskList(
                            tasks = tasksState.value,
                            onTaskChecked = {task ->
                                coroutineScope.launch {
                                    viewModel.toggleDoneState(task)
                                }
                                            },
                            onTaskDone = {task ->
                                coroutineScope.launch {
                                viewModel.deleteTask(task)

                                }
                            }
                        )


                }
            }
        }
    }
}

@Composable
fun AddTask(
    onAddClick: (Task) -> Unit = {}
) {
    var label by remember {
        mutableStateOf("")
    }

    Row {
        OutlinedTextField(
            value = label,
            onValueChange = {label = it}
        )
        
        Button(onClick = {
            onAddClick(Task(label = label))
            label = ""
        }) {
            Text(text = "Add")
        }
    }
}

@Composable
fun TaskList(
    tasks: List<Task> = remember { getTasks() },
    onTaskChecked: (Task) -> Unit = {},
    onTaskDone: (Task) -> Unit = {}
){

    LazyColumn{
        items(items = tasks){ task ->
            TaskItem(
                taskName = task.label,
                checked = task.isDone,
                onCheckedChange = { onTaskChecked(task) },
                onClose = { onTaskDone(task) })
        }
    }
}

@Composable
fun TaskItem(
    taskName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isChecked = remember {
        mutableStateOf(checked)

    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            text = taskName
        )
        Checkbox(
            checked = isChecked.value,
            onCheckedChange = {
                isChecked.value = !isChecked.value
                onCheckedChange(it)
            }
        )
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }
    }
}
}