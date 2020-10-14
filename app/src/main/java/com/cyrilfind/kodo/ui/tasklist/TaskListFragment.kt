package com.cyrilfind.kodo.ui.tasklist

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.cyrilfind.kodo.Constants.ORDER_PREF_KEY
import com.cyrilfind.kodo.Constants.TITLE_PREF_KEY
import com.cyrilfind.kodo.R
import com.cyrilfind.kodo.databinding.TasksListFragmentBinding
import com.cyrilfind.kodo.model.Task
import kotlinx.coroutines.launch
import java.util.*

class TaskListFragment : Fragment(), TaskListAdapter.Listener {
    private lateinit var binding: TasksListFragmentBinding
    private val adapter = TaskListAdapter(this)
    private val viewModel: TaskListViewModel by viewModels()

    private val sharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = TasksListFragmentBinding.inflate(layoutInflater)
        val savedList =
            savedInstanceState?.getParcelableArrayList<Task>("tasks").orEmpty()
        setupAdapter(savedList)
        setupFab()
        setHasOptionsMenu(true)
        setupSwipeRefresh()
        (activity as? AppCompatActivity)?.supportActionBar?.title = sharedPreferences.getString(TITLE_PREF_KEY, "")
        viewModel.reverseOrder = sharedPreferences.getBoolean(ORDER_PREF_KEY, false)
        return binding.root
    }

    private fun setupAdapter(list: List<Task>) {
        binding.tasksRecyclerView.adapter = adapter
        adapter.tasksList = list
        viewModel.tasksListLiveData.observe(this, Observer {
            if (it == null) {
                findNavController().navigate(R.id.action_tasksListFragment_to_loginFragment)
            } else {
                adapter.tasksList = it
            }
        })
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            it.isEnabled = false
            showAddItemDialog { text ->
                viewModel.add(Task(title = text))
                it.isEnabled = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshTasks()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                viewModel.refreshTasks()
                true
            }
            R.id.action_toggle_completed -> {
                onActionToggleCompleted(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setupSwipeRefresh() {
        binding.swipeRefresh.apply {
            setOnRefreshListener { refresh() }
            setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorAccent
            )
        }
    }

    private fun refresh() {
        lifecycleScope.launch {
            binding.swipeRefresh.isRefreshing = true
            viewModel.refreshTasks()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("tasks", ArrayList(adapter.tasksList))
    }

    override fun onClickDelete(task: Task, position: Int) {
        viewModel.delete(task)
    }

    override fun onClickCheckbox(task: Task, isChecked: Boolean) {
//        task.checked = isChecked
        viewModel.updateChecked(task)
    }

    override fun onClickItem(task: Task) {
        goToTaskDetail(task)
    }

    private fun goToTaskDetail(task: Task) {
        findNavController().navigate(TaskListFragmentDirections.openTask(task))
    }

    private fun showAddItemDialog(onFinish: (String?) -> Unit) {
        val editText = EditText(context)
        editText.setText(sharedPreferences.getString("default_text", ""))
        editText.setSelection(editText.text.length)
        with(AlertDialog.Builder(requireContext())) {
            setView(editText)
            setTitle(R.string.add_task_dialog_title)
            setMessage(R.string.add_task_dialog_message)
            setPositiveButton(android.R.string.ok) { _, _ -> onFinish(editText.text.toString()) }
            setNegativeButton(android.R.string.cancel) { _, _ -> onFinish(null) }
            setOnCancelListener { onFinish(null) }
            show()
        }
    }

    private fun onActionToggleCompleted(item: MenuItem) {
        toggleMenu(item)
        toggleTaskList()
    }

    private fun toggleTaskList() {
        viewModel.showCompleted = !viewModel.showCompleted
        viewModel.refreshTasks()
    }

    private fun toggleMenu(item: MenuItem) {
        if (viewModel.showCompleted) {
            item.setIcon(R.drawable.ic_radio_button_unchecked_white_24dp)
            item.title = getText(R.string.action_show_completed)
        } else {
            item.setIcon(R.drawable.ic_check_circle_white_24dp)
            item.title = getText(R.string.action_hide_completed)
        }
    }
}
