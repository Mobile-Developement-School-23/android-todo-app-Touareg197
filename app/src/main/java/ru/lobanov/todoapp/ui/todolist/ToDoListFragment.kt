package ru.lobanov.todoapp.ui.todolist

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.lobanov.todoapp.R
import ru.lobanov.todoapp.databinding.FragmentToDoListBinding
import ru.lobanov.todoapp.util.NetworkUtil
import ru.lobanov.todoapp.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ToDoListFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: ToDoListAdapter
    lateinit var binding: FragmentToDoListBinding
    private lateinit var collapsedMenu: Menu
    private var appBarExpanded = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.setTheme(R.style.Theme_ToDoApplication);

        binding = FragmentToDoListBinding.inflate(inflater)
        start()
        setObserveAndVM()
        setAdapter()
        fabClick()
        swiped()
        colapsingSettings()
        setActionBarProperties()
        appbarExpanded()
        setHasOptionsMenu(true)
        visibilityClick()
        hideKeyboard(requireActivity())

        viewModel.getList()
        return binding.root
    }

    private fun start() {
        if (!NetworkUtil.getConnectivityStatus(context = requireActivity())) {
            Snackbar.make(
                binding.root, getString(R.string.no_internet), Snackbar.LENGTH_LONG
            ).apply {
                setAction(getString(R.string.ok)) {}
                show()
            }
        }
    }

    private fun visibilityClick() {
        binding.visibilityBtn.setOnClickListener {
            changeVisibility()
        }
    }

    private fun changeVisibility() {
        viewModel.visibility = !viewModel.visibility
        if (!viewModel.visibility) {
            setNoVisibility()
            viewModel.getAllPriorityTask.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        } else {
            viewModel.getAllTasks.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
            setVisibility()
        }
    }

    private fun setVisibility() =
        binding.visibilityBtn.setImageResource(R.drawable.ic_baseline_visibility_off_24)

    private fun setNoVisibility() =
        binding.visibilityBtn.setImageResource(R.drawable.ic_baseline_visibility_24)

    private fun changeMenuVisibility() {
        if (viewModel.visibility) {
            collapsedMenu.add("visibility")
                .setIcon(R.drawable.ic_baseline_visibility_off_24)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

        } else {
            collapsedMenu.add("visibility")
                .setIcon(R.drawable.ic_baseline_visibility_24)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
    }

    private fun setActionBarProperties() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.animToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(false);
    }

    private fun appbarExpanded() {
        binding.appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (Math.abs(verticalOffset) > 200) {
                appBarExpanded = false
                activity?.invalidateOptionsMenu()
            } else {
                appBarExpanded = true
                activity?.invalidateOptionsMenu()
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (!appBarExpanded || collapsedMenu.size() != 0) {
            changeMenuVisibility()
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
            changeVisibility()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun colapsingSettings() {
        binding.collapsingToolbar.setCollapsedTitleTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.black
            )
        )
        binding.collapsingToolbar.setExpandedTitleColor(
            ContextCompat.getColor(
                requireActivity(),
                com.google.android.material.R.color.mtrl_btn_transparent_bg_color
            )
        )
        binding.collapsingToolbar.expandedTitleMarginStart = 16
        binding.collapsingToolbar.title = getString(R.string.my_todos)
    }

    private fun setObserveAndVM() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.getAllDoneTasks.observe(viewLifecycleOwner) {
            binding.executed.text = String.format(getString(R.string.executed), it.size)
        }
        viewModel.getAllTasks
        viewModel.getAllTasks.observe(viewLifecycleOwner) {
            viewModel.deleteRepeat()
            viewModel.patchList(it)
        }

        viewModel.getAllPriorityTask.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            viewModel.deleteRepeat()
            viewModel.patchList(it)
        }
    }

    private fun setAdapter() {
        adapter = ToDoListAdapter(TaskClickListener { taskEntry ->
            findNavController().navigate(
                ToDoListFragmentDirections.actionToDoListFragmentToModifyToDoFragment(
                    taskEntry
                )
            )
        },
            DoneClickListener { taskEntry ->
                viewModel.update(taskEntry)
                adapter.notifyDataSetChanged()
            }
        )
        binding.recyclerView.adapter = adapter
    }

    private fun fabClick() {
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_toDoListFragment_to_createToDoFragment)
        }
    }

    private fun swiped() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val taskEntry = adapter.currentList[position]
                viewModel.delete(taskEntry)
                Snackbar.make(binding.root, "Удалено", Snackbar.LENGTH_LONG).apply {
                    setAction("Отменить") {
                        viewModel.insert(taskEntry)
                    }
                    show()
                }
            }
        }).attachToRecyclerView(binding.recyclerView)
    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = activity.currentFocus
        currentFocusedView.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

}