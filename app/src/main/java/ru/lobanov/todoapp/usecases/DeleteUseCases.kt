package ru.lobanov.todoapp.usecases

import android.app.Application
import ru.lobanov.todoapp.database.TaskDatabase
import ru.lobanov.todoapp.database.TaskEntry
import ru.lobanov.todoapp.repository.DataBaseRepository
import ru.lobanov.todoapp.repository.RetrofitRepository
import javax.inject.Inject


class DeleteUseCases @Inject constructor(
    private val application: Application
) {

    private val toDoDao = TaskDatabase.getDatabase(application).toDoDao()
    private val repository: DataBaseRepository
    private val retrofitRepository: RetrofitRepository
    private val getRevisionUseCase: GetRevisionUseCase

    init {
        retrofitRepository = RetrofitRepository()
        repository = DataBaseRepository(toDoDao)
        getRevisionUseCase = GetRevisionUseCase()
    }

    suspend fun delete(taskEntry: TaskEntry) {
        repository.deleteItem(taskEntry)
    }

    suspend fun deleteRetrofit(taskEntry: String) {
        var response = retrofitRepository.deleteItem(taskEntry)
        getRevisionUseCase.getRevision()
    }

}