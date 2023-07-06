package ru.lobanov.todoapp.repository

import androidx.lifecycle.LiveData
import ru.lobanov.todoapp.database.ToDoDao
import ru.lobanov.todoapp.database.TaskEntry


class DataBaseRepository(val toDoDao: ToDoDao) {

    suspend fun insert(todo: TaskEntry) = toDoDao.insert(todo)

    suspend fun insertList(todoList: List<TaskEntry>) = toDoDao.insertList(todoList)

    suspend fun updateData(todo: TaskEntry) = toDoDao.update(todo)

    suspend fun updateDataList(todoList: List<TaskEntry>) = toDoDao.updateList(todoList)

    suspend fun deleteItem(todo: TaskEntry) = toDoDao.delete(todo)

    suspend fun deleteAll() = toDoDao.deleteAll()

    fun deletes() = toDoDao.deletes()

    fun getAllTasks(): LiveData<List<TaskEntry>> = toDoDao.getAllTasks()

    fun getAllPriorityTasks(): LiveData<List<TaskEntry>> = toDoDao.getAllPriorityTasks()

    fun getAllDone(): LiveData<List<TaskEntry>> = toDoDao.getAllDoneTasks()

    fun searchDatabase(searchQuery: String): LiveData<List<TaskEntry>> =
        toDoDao.searchDatabase(searchQuery)

}