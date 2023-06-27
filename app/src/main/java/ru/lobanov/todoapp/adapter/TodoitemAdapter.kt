package ru.lobanov.todoapp.adapter

import android.app.Activity
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.lobanov.todoapp.R
import ru.lobanov.todoapp.model.TodoItemsRepository
import ru.lobanov.todoapp.model.TodoItem
import java.text.SimpleDateFormat
import java.util.Date

class TodoItemAdapter : RecyclerView.Adapter<TodoItemAdapter.ViewHolder>() {

    var todoItemsRepository = TodoItemsRepository()
    var todoItemsRepositoryVisible = TodoItemsRepository()
    var visibility = false
    lateinit var activity: Activity

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
        val checked: ImageView
        val importance: ImageView
        val dataTv: TextView

        init {
            title = itemView.findViewById(R.id.title)
            checked = itemView.findViewById(R.id.checked)
            importance = itemView.findViewById(R.id.important)
            dataTv = itemView.findViewById(R.id.dateTV)
        }

        fun bind(i: Int) {
            if (visibility) {
                setAllProperties(todoItemsRepository, i)
            } else {
                setAllProperties(todoItemsRepositoryVisible, i)
            }
        }

        /**
         * Установка всех ресурсов по соответствующим параметрам
         */
        fun setAllProperties(todoItemsRepository: TodoItemsRepository, i: Int) {
            title.text = todoItemsRepository[i].title
            title.setTextColor(getColor(activity, R.color.black))
            dataTv.text = todoItemsRepository[i].deadline
            if (todoItemsRepository[i].done) {
                checked.setImageResource(R.drawable.ic_checked)
                title.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                title.setTextColor(getColor(activity, R.color.label_light_tertiary))
            } else {
                checked.setImageResource(R.drawable.ic_unchecked)
                title.paintFlags = title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                title.setTextColor(getColor(activity, R.color.black))
            }
            if (todoItemsRepository[i].importance == "important") {
                importance.setImageResource(R.drawable.ic_high)

            }
            if (todoItemsRepository[i].importance == "low")
                importance.setImageResource(R.drawable.ic_low)
            if (todoItemsRepository[i].importance == "basic") {
                importance.setImageResource(R.drawable.rectangle)

            }
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            /** Проверка на "дедлайн" **/
            if (todoItemsRepository[i].deadline != "") {
                val firstDate: Date = sdf.parse(todoItemsRepository[i].deadline) as Date
                val secondDate: Date = sdf.parse(todoItemsRepository[i].created_at) as Date
                if (!todoItemsRepository[i].done) {
                    val cmp = firstDate.compareTo(secondDate)
                    when {
                        cmp > 0 -> {
                            checked.setImageResource(R.drawable.ic_unchecked)
                        }
                        cmp < 0 -> {
                            checked.setImageResource(R.drawable.ic_uncheckedred)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ViewHolder(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
        holder.itemView.setOnClickListener { view ->
            /** Передача данных другому фрагменту и переход к нему **/
            val bundle = Bundle()
            bundle.putParcelable("MyArgChange", todoItemsRepository)
            if (visibility) {
                bundle.putInt("id", position)
            } else {
                bundle.putInt(
                    "id",
                    todoItemsRepository.indexOf(todoItemsRepositoryVisible[position])
                )
            }
            bundle.putBoolean("new", false)
            view.findNavController().navigate(R.id.ToDoDetailsFragment, bundle)
        }
    }

    override fun getItemCount() = todoItemsRepositoryVisible.size

    fun submitRepository(todoItemsRepository: TodoItemsRepository) {
        val old = todoItemsRepositoryVisible
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            TodoItemDiffCallBack(old, todoItemsRepository)
        )
        todoItemsRepositoryVisible = todoItemsRepository
        diffResult.dispatchUpdatesTo(this)
    }

    class TodoItemDiffCallBack(
        var old: TodoItemsRepository,
        var new: TodoItemsRepository
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = old.size

        override fun getNewListSize() = new.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldProduct: TodoItem = old[oldItemPosition];
            val newProduct: TodoItem = new[newItemPosition];
            return oldProduct.id == newProduct.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition] == new[newItemPosition]
        }
    }

}