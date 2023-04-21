package com.example.tasksapplication.data

import androidx.room.*
import com.example.tasksapplication.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // CRUD = Create, Read, Update, Delete
    @Insert
    suspend fun add(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    //Flow ist eine Bibliothek in Kotlin, die es ermöglicht, asynchrone Datenströme (Streams) von
    //Daten zu verarbeiten und zu transformieren. Mit Flow können Sie auf Ereignisse reagieren,
    //die asynchron und unvorhersehbar auftreten können, wie z.B. Benutzerinteraktionen oder
    //Netzwerkantworten.
    @Query("SELECT * from task")
    fun readAll(): Flow<List<Task>>

    @Query("Select * from task where isDone = 1")
    fun readAllChecked(): Flow<List<Task>>

    @Query("Select * from task where id=:taskId")
    fun getTaskById(taskId: Int): Task

    @Query ("DELETE from Task")
    fun deleteAll()
}