package ru.lobanov.todoapp.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.lobanov.todoapp.database.TaskDatabase
import ru.lobanov.todoapp.database.TaskEntry
import ru.lobanov.todoapp.repository.DataBaseRepository
import ru.lobanov.todoapp.repository.RetrofitRepository
import ru.lobanov.todoapp.retrofit.RetrofitConstants
import ru.lobanov.todoapp.retrofit.model.Todo
import ru.lobanov.todoapp.retrofit.model.TodoList


class MyWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    var repository: RetrofitRepository
    private val toDoDao = TaskDatabase.getDatabase(context).toDoDao()
    private val repositoryBD: DataBaseRepository

    init {
        repositoryBD = DataBaseRepository(toDoDao)
        repository = RetrofitRepository()
    }

    override suspend fun doWork(): Result {
        return try {
            try {
                Log.d("MyWorker", "Run work manager")
                doTheTask()
                Result.success()
            } catch (e: Exception) {
                Log.d("MyWorker", "exception in doWork ${e.message}")
                Result.failure()
            }
        } catch (e: Exception) {
            Log.d("MyWorker", "exception in doWork ${e.message}")
            Result.failure()
        }
    }

    private suspend fun doTheTask() {
        val response = repository.getList()
        RetrofitConstants.REVISION = response.body()!!.revision
        repositoryBD.insertList(toTaskEntryList(response.body()))
        repositoryBD.deletes()
    }

    private fun toTaskEntry(todo: Todo): TaskEntry {
        return TaskEntry(
            ids = 0,
            changed_at = todo.changed_at,
            created_at = todo.created_at,
            id = todo.id,
            text = todo.text,
            deadline = todo.deadline,
            done = todo.done,
            last_updated_by = todo.last_updated_by,
            importance = todo.importance,
            color = todo.color
        )
    }

    private fun toTaskEntryList(todoList: TodoList?): List<TaskEntry> {
        val list: MutableList<TaskEntry> = mutableListOf()
        if (todoList != null) {
            for (i in 0 until todoList.list.size) {
                list.add(toTaskEntry(todoList.list[i]))
            }
        }
        return list
    }
}