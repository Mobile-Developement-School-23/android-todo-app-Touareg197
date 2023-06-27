package ru.lobanov.todoapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.lobanov.todoapp.R
import ru.lobanov.todoapp.model.TodoItemsRepository
import ru.lobanov.todoapp.adapter.TodoItemAdapter
import ru.lobanov.todoapp.model.TodoItem
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

class ToDoListFragment : Fragment() {

    lateinit var visibilityBtn: ImageButton
    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var collapsedMenu: Menu

    var dessertAdapter: TodoItemAdapter = TodoItemAdapter()

    var todoItemsRepository = TodoItemsRepository()
    var todoItemsRepositoryVisible = TodoItemsRepository()

    private var appBarExpanded = true
    private var visibility = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        requireActivity().setTheme(R.style.Theme_ToDoApplication)
        return inflater.inflate(R.layout.fragment_to_do_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val fab: FloatingActionButton = view.findViewById(R.id.fab)

        visibilityBtn = view.findViewById(R.id.visibilityBtn)

        activity?.invalidateOptionsMenu()

        val toolbar: Toolbar = view.findViewById(R.id.anim_toolbar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        appBarLayout = view.findViewById(R.id.appbar)

        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar)
        val dateTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        val todoItemsRepositoryFromNew = arguments?.getParcelable<TodoItemsRepository>("Change")

        val todoItemsRepository = TodoItemsRepository()
        /** Если запускается первый раз то рандом
         * иначе используются значения
         * данные нам при запуске **/
        if (todoItemsRepositoryFromNew == null) {
            var todoItem = TodoItem(
                id = "1",
                "blablabla",
                "important",
                done = false,
                deadline = "22 июля 2022",
                created_at = "22 июля 2022"
            )
            var todoItem2 = TodoItem(
                id = "1",
                "dasdasdasd",
                "low",
                done = true,
                deadline = "22 июля 2022",
                created_at = "22 июля 2022"
            )
            val todoItem3 = TodoItem(
                id = "0",
                "большой текст большой текстм большой текст большой т",
                "basic",
                deadline = "27/07/2022",
                done = false,
                created_at = dateTime
            )

            todoItemsRepository.add(todoItem3)

            for (i in 0 until 30) {
                var importance = "basic"
                var done = false
                if ((0..8).random() == 1) {
                    importance = "important"
                } else
                    if ((0..6).random() == 1) {
                        importance = "low"
                    }
                if ((0..3).random() == 1) {
                    done = true
                }
                var deadline = ""
                if (i % 3 == 0) {
                    deadline = "27/09/2022"
                }
                todoItemsRepository.add(
                    TodoItem(
                        id = (i + 1).toString(),
                        "blablabla",
                        importance = importance,
                        done = done,
                        deadline = deadline,
                        created_at = dateTime
                    )
                )
            }
            initialize(todoItemsRepository)
        } else {
            initialize(todoItemsRepositoryFromNew)
        }

        /** Кнопка для добавления новго дела **/
        fab.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("MyArgChange", dessertAdapter.todoItemsRepository)
            bundle.putBoolean("new", true)
            findNavController().navigate(R.id.ToDoDetailsFragment, bundle)
        }
        /** Кнопка для изменение папраметра видеть/не видеть
         * выполненные значения **/
        visibilityBtn.setOnClickListener {
            setVisibility(todoItemsRepository, todoItemsRepositoryVisible)
        }

        /** Обработка скрола appBarа **/
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            //  Vertical offset == 0 indicates appBar is fully expanded.
            if (abs(verticalOffset) > 200) {
                appBarExpanded = false
                activity?.invalidateOptionsMenu()
            } else {
                appBarExpanded = true
                activity?.invalidateOptionsMenu()
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (!appBarExpanded || collapsedMenu.size() != 0) {
            //collapsed
            collapsingToolbar.expandedTitleMarginStart = 16

            collapsingToolbar.title = "Мои дела"
            collapsingToolbar.setCollapsedTitleTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.black
                )
            )
            collapsingToolbar.setExpandedTitleColor(
                ContextCompat.getColor(
                    requireActivity(),
                    com.google.android.material.R.color.mtrl_btn_transparent_bg_color
                )
            )

            if (!visibility) {
                collapsedMenu.add("visibility")
                    .setIcon(R.drawable.ic_baseline_visibility_24)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            } else {
                collapsedMenu.add("visibility")
                    .setIcon(R.drawable.ic_baseline_visibility_off_24)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            }

        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        collapsedMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title === "visibility") {
            setVisibility(todoItemsRepository, todoItemsRepositoryVisible)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setVisibility(
        todoItemsRepository: TodoItemsRepository,
        todoItemsRepositoryVisible: TodoItemsRepository
    ) {
        if (!visibility) {
            visibilityBtn.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        } else {
            visibilityBtn.setImageResource(R.drawable.ic_baseline_visibility_24)
        }
        visibility = !visibility
        dessertAdapter.visibility = visibility
        if (visibility) {
            val productDiffUtilCallback =
                TodoItemAdapter.TodoItemDiffCallBack(
                    dessertAdapter.todoItemsRepositoryVisible,
                    todoItemsRepository
                )
            val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)
            dessertAdapter.submitRepository(todoItemsRepository)
            productDiffResult.dispatchUpdatesTo(dessertAdapter)
        } else {
            val productDiffUtilCallback =
                TodoItemAdapter.TodoItemDiffCallBack(
                    dessertAdapter.todoItemsRepositoryVisible,
                    todoItemsRepositoryVisible
                )
            val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)
            dessertAdapter.submitRepository(todoItemsRepositoryVisible)
            productDiffResult.dispatchUpdatesTo(dessertAdapter)
        }
    }

    fun initialize(todoItemsRepositoryFromNew: TodoItemsRepository) {
        /**
         * Список только с невыполненными делами
         */
        todoItemsRepositoryVisible.clear()
        for (i in 0 until todoItemsRepositoryFromNew.size) {
            if (!todoItemsRepositoryFromNew[i].done) {
                todoItemsRepositoryVisible.add(todoItemsRepositoryFromNew[i])
            }
        }

        /**
         * Изменение текста колличества выполненных дел
         */
        requireView().findViewById<TextView?>(R.id.performed).text = "Выполнено - " +
                (todoItemsRepositoryFromNew.size - todoItemsRepositoryVisible.size).toString()

        /**
         * Инициализация RecyclerView
         */
        val recyclerView: RecyclerView = requireView().findViewById(R.id.scrollableview)
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = dessertAdapter;
        todoItemsRepositoryFromNew.reversed()
        dessertAdapter.todoItemsRepository = todoItemsRepositoryFromNew
        dessertAdapter.todoItemsRepositoryVisible = todoItemsRepositoryVisible
        dessertAdapter.activity = requireActivity()
        dessertAdapter.notifyDataSetChanged()
        todoItemsRepository = dessertAdapter.todoItemsRepository
    }
}