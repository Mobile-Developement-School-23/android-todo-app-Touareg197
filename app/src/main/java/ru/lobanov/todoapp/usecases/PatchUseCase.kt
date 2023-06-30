package ru.lobanov.todoapp.usecases

import ru.lobanov.todoapp.database.TaskEntry
import ru.lobanov.todoapp.repository.RetrofitRepository
import ru.lobanov.todoapp.retrofitConnect.RetrofitConstants
import ru.lobanov.todoapp.retrofitConnect.model.Todo
import ru.lobanov.todoapp.retrofitConnect.model.TodoList


class PatchUseCase {

    private val retrofitRepository: RetrofitRepository
    private val getRevisionUseCase: GetRevisionUseCase

    init {
        getRevisionUseCase = GetRevisionUseCase()
        retrofitRepository = RetrofitRepository()
    }

    suspend fun patchList(list: List<TaskEntry>) {
        retrofitRepository.patchList(toTodoList(list))
        getRevisionUseCase.getRevision()
    }

    private fun toTodoList(getAllTasks: List<TaskEntry>): TodoList {
        val arr: ArrayList<Todo> = arrayListOf()
        for (element in getAllTasks) {
            arr.add(toTodo(element))
        }
        return TodoList(arr, RetrofitConstants.REVISION)
    }

    private fun toTodo(todo: TaskEntry): Todo {
        return Todo(
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
}