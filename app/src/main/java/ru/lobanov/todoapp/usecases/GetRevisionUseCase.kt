package ru.lobanov.todoapp.usecases

import ru.lobanov.todoapp.repository.RetrofitRepository
import ru.lobanov.todoapp.retrofitConnect.RetrofitConstants


class GetRevisionUseCase {

    private val retrofitRepository: RetrofitRepository

    init {
        retrofitRepository = RetrofitRepository()
    }

    suspend fun getRevision() {
        val response = retrofitRepository.getList()
        RetrofitConstants.REVISION = response.body()!!.revision
    }
}