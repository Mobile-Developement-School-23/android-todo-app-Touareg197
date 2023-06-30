package ru.lobanov.todoapp.util

import android.content.Context
import android.net.ConnectivityManager


internal object NetworkUtil {

    fun getConnectivityStatus(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null
    }

}