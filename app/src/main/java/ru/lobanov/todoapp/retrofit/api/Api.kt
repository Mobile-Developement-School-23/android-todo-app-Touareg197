package ru.lobanov.todoapp.retrofit.api

import ru.lobanov.todoapp.retrofit.model.TodoList
import ru.lobanov.todoapp.retrofit.model.Element
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.PATCH
import retrofit2.http.Query


interface Api {
    @GET("list")
    suspend fun getList(
    ): Response<TodoList>

    @GET("list")
    suspend fun getItem(
        @Header("X-Last-Known-Revision") revision: String,
        @Query("id") id: String,
    ): Response<Element>

    @DELETE("list/{id}")
    suspend fun deleteItem(
        @Header("X-Last-Known-Revision") revision: String,
        @Path("id") id: String
    ): Response<Element>

    @PUT("list/{id}")
    suspend fun putItem(
        @Header("X-Last-Known-Revision") revision: String,
        @Path("id") id: String,
        @Body elementModel: Element
    ): Response<Element>

    @POST("list")
    suspend fun postElement(
        @Header("X-Last-Known-Revision") revision: String,
        @Body elementModel: Element
    ): Response<Element>

    @PATCH("list")
    suspend fun patchElement(
        @Header("X-Last-Known-Revision") revision: String,
        @Body elementModel: TodoList
    ): Response<TodoList>

}