package com.example.tasksapplication.repositories

import com.example.tasksapplication.data.TaskDao
import com.example.tasksapplication.models.Task


//Viewmodel nutzt Repo um mit Datenbank zu kommunizieren
class TaskRepository(private val taskDao: TaskDao) {

    suspend fun add(task: Task) = taskDao.add(task)

    suspend fun delete(task: Task) = taskDao.delete(task)

    suspend fun update (task: Task) = taskDao.update(task)

    fun getAllTasks() = taskDao.readAll()
}