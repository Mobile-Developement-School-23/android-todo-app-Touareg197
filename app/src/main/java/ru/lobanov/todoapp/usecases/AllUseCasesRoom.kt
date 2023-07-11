package ru.lobanov.todoapp.usecases

import android.app.Application
import androidx.lifecycle.LiveData
import ru.lobanov.todoapp.database.TaskEntry
import javax.inject.Inject


class AllUseCasesRoom @Inject constructor(
    private val application: Application
) {

    private val deleteRepeatUseCase: DeleteRepeatUseCase
    private val insertUseCases: InsertUseCases
    private val updateUseCases: UpdateUseCases
    private val deleteUseCases: DeleteUseCases
    private val getListUseCases: GetListUseCases

    init {
        getListUseCases = GetListUseCases(application)
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