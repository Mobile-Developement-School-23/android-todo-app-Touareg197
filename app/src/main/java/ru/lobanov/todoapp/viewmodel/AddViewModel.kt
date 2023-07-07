package ru.lobanov.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ru.lobanov.todoapp.database.TaskEntry
import ru.lobanov.todoapp.usecases.GetListUseCases
import ru.lobanov.todoapp.usecases.InsertUseCases
import ru.lobanov.todoapp.util.NetworkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AddViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    private val insertUseCases: InsertUseCases
    private val getListUsecases: GetListUseCases
    var getAllTasks: LiveData<List<TaskEntry>>

    init {
        getListUsecases = GetListUseCases(application)
        insertUseCases = InsertUseCases(application)
        getAllTasks = getListUsecases.getList()
    }

    fun insert(
        title_str: String,
        priority: String,
        dates: Long,
        dateNow: Long,
        s: String,
        dateNow1: Long,
        b: Boolean,
        toString: String,
        toString1: String
    ) {
        val taskEntry = TaskEntry(
            0,
            text = title_str,
            importance = priority,
            deadline = dates,
            created_at = dateNow,
            color = "",
            changed_at = dateNow1,
            done = false,
            id = toString,
            last_updated_by = toString1
        )
        viewModelScope.launch(Dispatchers.IO) {
            insertUseCases.insert(taskEntry)
            if (NetworkUtil.getConnectivityStatus(context = getApplication())) {
                insertUseCases.insertRetrofit(taskEntry)
            }
        }
    }

    fun getList() {
        if (NetworkUtil.getConnectivityStatus(context = getApplication())) {
            viewModelScope.launch(Dispatchers.IO) {
                getListUsecases.getListRetrofit()
            }
        }
    }

}