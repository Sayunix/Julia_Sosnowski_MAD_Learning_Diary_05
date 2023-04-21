package com.example.tasksapplication.utils

import android.content.Context
import com.example.tasksapplication.TaskViewModelFactory
import com.example.tasksapplication.data.TaskDatabase
import com.example.tasksapplication.repositories.TaskRepository



object InjectorUtils {

    private fun getTaskRepository(context: Context): TaskRepository {
        return TaskRepository(TaskDatabase.getDatabase(context).taskDao())
    }

    fun provideTaskViewModelFactory(context: Context): TaskViewModelFactory {
        //Um Boilerplaiting zu vermeiden, kann auch library dagger/hilt verwendet werden, macht aber mehr Sinn in größeren Apps
        // val db = TaskDatabase.getDatabase(context) //LocalContext wenn  wir in einem composable wären
        val repository = getTaskRepository(context)
        return TaskViewModelFactory(repository)


    }
}