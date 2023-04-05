package com.example.tasksapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapplication.models.Task
import com.example.tasksapplication.models.getTasks
import com.example.tasksapplication.repositories.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository): ViewModel() {
    private val _tasks = MutableStateFlow(listOf<Task>())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        // read all tasks from repo
        viewModelScope.launch { // start a coroutine
            repository.getAllTasks().collect{taskList ->    // collect the flow
                if(!taskList.isNullOrEmpty()) {
                    _tasks.value = taskList
                }
            }
        }
    }

    suspend fun toggleDoneState(task: Task) {
        repository.update(task)
    }

    suspend fun addTask(task: Task){
        repository.add(task)
    }

    suspend fun deleteTask(task: Task) {
        repository.delete(task)
    }
}