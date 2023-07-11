package ru.lobanov.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ru.lobanov.todoapp.database.TaskEntry
import ru.lobanov.todoapp.usecases.DeleteUseCases
import ru.lobanov.todoapp.usecases.UpdateUseCases
import ru.lobanov.todoapp.util.NetworkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    private val updateUseCases: UpdateUseCases
    private val deleteUseCases: DeleteUseCases

    init {
        updateUseCases = UpdateUseCases(application)
        deleteUseCases = DeleteUseCases(application)
    }

    fun delete(taskEntry: TaskEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteUseCases.delete(taskEntry)
            if (NetworkUtil.getConnectivityStatus(context = getApplication())) {
                deleteUseCases.deleteRetrofit(taskEntry.id)
            }
        }
    }

    fun update(
        ids: Int,
        task_str: String,
        important: String,
        deadline: Long,
        createdAt: Long,
        s: String,
        dateNow: Long,
        b: Boolean,
        id: String,
        toString: String
    ) {
        val taskEntry = TaskEntry(
            ids,
            text = task_str,
            importance = important,
            deadline = deadline,
            created_at = createdAt,
            color = "",
            changed_at = dateNow,
            done = false,
            id = id,
            last_updated_by = toString
        )
        viewModelScope.launch(Dispatchers.IO) {
            updateUseCases.update(taskEntry)
            if (NetworkUtil.getConnectivityStatus(context = getApplication())) {
                updateUseCases.updateRetrofit(taskEntry)
            }
        }
    }

}