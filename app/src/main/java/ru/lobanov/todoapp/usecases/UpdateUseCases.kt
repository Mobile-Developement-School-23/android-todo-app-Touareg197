package ru.lobanov.todoapp.usecases

import android.app.Application
import ru.lobanov.todoapp.database.TaskDatabase
import ru.lobanov.todoapp.database.TaskEntry
import ru.lobanov.todoapp.repository.DataBaseRepository
import ru.lobanov.todoapp.repository.RetrofitRepository
import ru.lobanov.todoapp.retrofitConnect.RetrofitConstants
import ru.lobanov.todoapp.retrofitConnect.model.Element
import ru.lobanov.todoapp.retrofitConnect.model.Todo


class UpdateUseCases(application: Application) {

    private val toDoDao = TaskDatabase.getDatabase(application).toDoDao()
    private val repository: DataBaseRepository
    private val retrofitRepository: RetrofitRepository
    private val getRevisionUseCase: GetRevisionUseCase

    init {
        getRevisionUseCase = GetRevisionUseCase()
        retrofitRepository = RetrofitRepository()
        repository = DataBaseRepository(toDoDao)
    }

    suspend fun update(taskEntry: TaskEntry) {
        repository.updateData(taskEntry)
    }

    suspend fun updateRetrofit(taskEntry: TaskEntry) {
        retrofitRepository.putItem(taskEntry.id, toElement(taskEntry))
        getRevisionUseCase.getRevision()
    }

    private fun toElement(todo: TaskEntry): Element {
        return Element(
            Todo(
                changed_at = todo.changed_at,
                created_at = todo.created_at,
                id = todo.id,
                text = todo.text,
                deadline = todo.deadline,
                done = todo.done,
                last_updated_by = todo.last_updated_by,
                importance = todo.importance,
                color = todo.color
            ),
            RetrofitConstants.REVISION.toString()
        )
    }
}