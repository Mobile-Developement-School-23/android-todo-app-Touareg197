package ru.lobanov.todoapp.retrofit.model

import kotlin.collections.ArrayList


data class TodoList(
    val list: ArrayList<Todo>,
    val revision: Int
) 
