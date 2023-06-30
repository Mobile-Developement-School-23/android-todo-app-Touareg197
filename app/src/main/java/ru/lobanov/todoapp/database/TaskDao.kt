package ru.lobanov.todoapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface ToDoDao {

    @Insert
    fun insert(todo: TaskEntry)

    @Insert
    fun insertList(todoList: List<TaskEntry>)

    @Delete
    fun delete(todo: TaskEntry)

    @Update
    fun update(todo: TaskEntry)

    @Update
    fun updateList(todo: List<TaskEntry>)

    @Query("DELETE FROM tasks_table")
    fun deleteAll()

    @Query(
        "DELETE FROM tasks_table WHERE EXISTS ( SELECT 1 FROM tasks_table t2 " +
                "WHERE tasks_table.id = t2.id" +
                "  AND tasks_table.ids > t2.ids" +
                ")"
    )
    fun deletes()

    @Query("SELECT * FROM tasks_table ORDER BY created_at DESC")
    fun getAllTasks(): LiveData<List<TaskEntry>>

    @Query("SELECT * FROM tasks_table WHERE done == 0")
    fun getAllPriorityTasks(): LiveData<List<TaskEntry>>

    @Query("SELECT * FROM tasks_table WHERE done == 1")
    fun getAllDoneTasks(): LiveData<List<TaskEntry>>

    @Query("select * from tasks_table where text like :searchQuery order by created_at desc")
    fun searchDatabase(searchQuery: String): LiveData<List<TaskEntry>>

}