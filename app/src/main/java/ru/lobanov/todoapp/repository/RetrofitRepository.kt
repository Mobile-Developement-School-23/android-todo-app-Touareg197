package ru.lobanov.todoapp.repository

import retrofit2.Response
import ru.lobanov.todoapp.retrofit.RetrofitConstants
import ru.lobanov.todoapp.retrofit.api.RetrofitInstance.api
import ru.lobanov.todoapp.retrofit.model.Element
import ru.lobanov.todoapp.retrofit.model.TodoList


class RetrofitRepository {

    suspend fun getList(): Response<TodoList> {
        return api.getList()
    }

    suspend fun getItem(id: String): Response<Element> {
        return api.getItem(RetrofitConstants.REVISION.toString(), id)
    }

    suspend fun deleteItem(id: String): Response<Element> {
        return api.deleteItem(RetrofitConstants.REVISION.toString(), id)
    }

    suspend fun putItem(id: String, element: Element): Response<Element> {
        return api.putItem(RetrofitConstants.REVISION.toString(), id, element)
    }

    suspend fun postItem(Item: Element): Response<Element> {
        return api.postElement(RetrofitConstants.REVISION.toString(), Item)
    }

    suspend fun patchList(Item: TodoList): Response<TodoList> {
        return api.patchElement(RetrofitConstants.REVISION.toString(), Item)
    }

}