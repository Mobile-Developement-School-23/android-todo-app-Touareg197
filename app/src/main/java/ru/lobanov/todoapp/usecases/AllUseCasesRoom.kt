package ru.lobanov.todoapp.usecases

import android.app.Application
import androidx.lifecycle.LiveData
import ru.lobanov.todoapp.database.TaskEntry


class AllUseCasesRoom(application: Application) {

    private val deleteRepeatUseCase: DeleteRepeatUseCase
    private val insertUseCases: InsertUseCases
    private val updateUseCases: UpdateUseCases
    private val deleteUseCases: DeleteUseCases
    private val getListUseCases: GetListUsecases

    init {
        getListUseCases = GetListUsecases(application)
        deleteUseCases = DeleteUseCases(application)
        updateUseCases = UpdateUseCases(application)
        insertUseCases = InsertUseCases(application)
        deleteRepeatUseCase = DeleteRepeatUseCase(application)
    }

    fun getAllPriorityTask(): LiveData<List<TaskEntry>> {
        return getListUseCases.getAllPriorityTask()
    }

    fun getAllDone(): LiveData<List<TaskEntry>> {
        return getListUseCases.getDoneList()
    }

    fun getList(): LiveData<List<TaskEntry>> {
        return getListUseCases.getList()
    }

    suspend fun deleteRepeat() {
        deleteRepeatUseCase.deleteRepeat()
    }

    suspend fun insert(taskEntry: TaskEntry) {
        insertUseCases.insert(taskEntry = taskEntry)
    }

    suspend fun update(taskEntry: TaskEntry) {
        updateUseCases.update(taskEntry = taskEntry)
    }

    suspend fun delete(taskEntry: TaskEntry) {
        deleteUseCases.delete(taskEntry)
    }

}