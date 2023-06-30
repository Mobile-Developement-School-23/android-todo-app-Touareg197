package ru.lobanov.todoapp.usecases

import android.app.Application
import androidx.lifecycle.LiveData
import ru.lobanov.todoapp.database.TaskDatabase
import ru.lobanov.todoapp.database.TaskEntry
import ru.lobanov.todoapp.repository.DataBaseRepository
import ru.lobanov.todoapp.repository.RetrofitRepository
import ru.lobanov.todoapp.retrofit.RetrofitConstants
import ru.lobanov.todoapp.retrofit.model.Todo
import ru.lobanov.todoapp.retrofit.model.TodoList


class GetListUsecases(application: Application) {

    private val toDoDao = TaskDatabase.getDatabase(application).toDoDao()
    private val repository: DataBaseRepository
    private val retrofitRepository: RetrofitRepository

    init {
        retrofitRepository = RetrofitRepository()
        repository = DataBaseRepository(toDoDao)
    }

    fun getDoneList(): LiveData<List<TaskEntry>> {
        return repository.getAllDone()
    }

    fun getList(): LiveData<List<TaskEntry>> {
        return repository.getAllTasks()
    }

    fun getAllPriorityTask(): LiveData<List<TaskEntry>> {
        return repository.getAllPriorityTasks()
    }

    suspend fun getListRetrofit() {
        val response = retrofitRepository.getList()
        RetrofitConstants.REVISION = response.body()!!.revision
        repository.insertList(toTaskEntryList(response.body()))
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