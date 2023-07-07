package ru.lobanov.todoapp.usecases

import android.app.Application
import ru.lobanov.todoapp.database.TaskEntry
import javax.inject.Inject


class AllUseCasesRetrofit @Inject constructor(
    private val application: Application
) {

    private val getListUseCases: GetListUseCases
    private val getRevisionUseCase: GetRevisionUseCase
    private val deleteRepeatUseCase: DeleteRepeatUseCase
    private val insertUseCases: InsertUseCases
    private val updateUseCases: UpdateUseCases
    private val patchUseCase: PatchUseCase
    private val deleteUseCases: DeleteUseCases

    init {
        deleteUseCases = DeleteUseCases(application)
        patchUseCase = PatchUseCase()
        updateUseCases = UpdateUseCases(application)
        insertUseCases = InsertUseCases(application)
        deleteRepeatUseCase = DeleteRepeatUseCase(application)
        getRevisionUseCase = GetRevisionUseCase()
        getListUseCases = GetListUseCases(application)
    }

    suspend fun getList() {
        getListUseCases.getListRetrofit()
    }

    suspend fun getRevision() {
        getRevisionUseCase.getRevision()
    }

    suspend fun insert(taskEntry: TaskEntry) {
        insertUseCases.insertRetrofit(taskEntry = taskEntry)
    }

    suspend fun update(taskEntry: TaskEntry) {
        updateUseCases.updateRetrofit(taskEntry = taskEntry)
    }

    suspend fun delete(taskEntry: String) {
        deleteUseCases.deleteRetrofit(taskEntry)
    }

    suspend fun patch(list: List<TaskEntry>) {
        patchUseCase.patchList(list)
    }
}