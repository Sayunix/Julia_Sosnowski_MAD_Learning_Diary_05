package com.example.tasksapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapplication.models.Task
import com.example.tasksapplication.models.getTasks
import com.example.tasksapplication.repositories.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository): ViewModel() {
    private val _tasks = MutableStateFlow(listOf<Task>())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    //Alle Tasks aus Datenbank auslesen
    init {
        viewModelScope.launch {
            repository.getAllTasks().collect { taskList ->
                if (!taskList.isNullOrEmpty()) {
                    _tasks.value = taskList

                }
            }

        }
    }


    suspend fun toggleDoneState(task: Task) {
        task.isDone = !task.isDone
        repository.update(task)
    }

    suspend fun addTask(task: Task){
        repository.add(task)
        }


    suspend fun deleteTask(task: Task) {
        repository.delete(task)

    }
}

