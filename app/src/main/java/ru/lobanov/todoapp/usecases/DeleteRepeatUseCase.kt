package ru.lobanov.todoapp.usecases

import android.app.Application
import ru.lobanov.todoapp.database.TaskDatabase
import ru.lobanov.todoapp.repository.DataBaseRepository
import javax.inject.Inject


class DeleteRepeatUseCase @Inject constructor(
    private val application: Application
) {

    private val toDoDao = TaskDatabase.getDatabase(application).toDoDao()
    private val repository: DataBaseRepository

    init {
        repository = DataBaseRepository(toDoDao)
    }

    fun deleteRepeat() = repository.deletes()

}