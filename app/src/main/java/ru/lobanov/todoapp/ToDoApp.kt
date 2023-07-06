package ru.lobanov.todoapp

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Загатовка на будущее класса ToDoListApp,
 * где можно отслеживать жизненный цикл единственной в приложении Activity
 */
class ToDoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

        })
    }

}