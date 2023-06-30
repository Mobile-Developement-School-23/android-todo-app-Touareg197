package ru.lobanov.todoapp.repository

import retrofit2.Response
import ru.lobanov.todoapp.retrofitConnect.RetrofitConstants
import ru.lobanov.todoapp.retrofitConnect.api.RetrofitInstance.api
import ru.lobanov.todoapp.retrofitConnect.model.Element
import ru.lobanov.todoapp.retrofitConnect.model.TodoList


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