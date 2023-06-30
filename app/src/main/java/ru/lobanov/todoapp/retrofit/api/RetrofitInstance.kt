package ru.lobanov.todoapp.retrofit.api

import ru.lobanov.todoapp.retrofit.RetrofitConstants.Companion.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {

    private val headerInterceptor: Interceptor = Interceptor { chain ->
        var request: Request = chain.request()

        request = request
            .newBuilder()
            .addHeader("Authorization", "Bearer erodent")
            .build()

        val response: Response = chain.proceed(request)
        response
    }

    private val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttp: OkHttpClient.Builder = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(loggingInterceptor)

    private val builder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build())
    }

    private val retrofit: Retrofit = builder.build()

    val api: Api by lazy {
        retrofit.create(Api::class.java)
    }

}