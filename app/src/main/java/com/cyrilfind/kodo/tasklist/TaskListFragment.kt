package com.cyrilfind.kodo.tasklist

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.cyrilfind.kodo.R
import com.cyrilfind.kodo.databinding.TasksListFragmentBinding
import com.cyrilfind.kodo.model.Task
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.okButton

class TaskListFragment : Fragment(), TaskNavigator {
    private lateinit var binding: TasksListFragmentBinding
    private val viewModel by lazy {
        val taskListViewModelFactory = TaskListViewModel.Factory(this)
        val viewModelProvider = ViewModelProviders.of(this, taskListViewModelFactory)
        viewModelProvider.get(TaskListViewModel::class.java)
    }

    private val sharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        binding = TasksListFragmentBinding.inflate(layoutInflater)
        binding.tasksRecyclerView.adapter = viewModel.recyclerAdapter
        binding.fab.setOnClickListener(this::onClickFab)
        binding.swipeRefresh.setOnRefreshListener(viewModel::refreshTasks)
        binding.swipeRefresh.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent
        )
        viewModel.isRefreshing.observe(this, Observer {
            binding.swipeRefresh.isRefreshing = it
        })
        viewModel.shouldScrollPosition.observe(this, Observer {
            binding.tasksRecyclerView.smoothScrollToPosition(it)
        })
        viewModel.reverseOrder = sharedPreferences.getBoolean("order", false)
        viewModel.refreshTasks()
        (activity as? AppCompatActivity)?.supportActionBar?.title = sharedPreferences.getString("title", "")
        return binding.root
    }

    override fun goToTaskDetail(task: Task) {
        findNavController().navigate(TaskListFragmentDirections.openTask(task))
    }

    private fun onClickFab(view: View) {
        view.isEnabled = false
        showAddItemDialog { text ->
            if (null != text) viewModel.addTaskToList(text)
            view.isEnabled = true
        }
    }

    private fun showAddItemDialog(onFinish: (String?) -> Unit) {
        val editText = setupDialogEditText()
        context?.alert(R.string.add_task_dialog_message, R.string.add_task_dialog_title) {
            customView = editText
            okButton { onFinish(editText.text.toString()) }
            cancelButton { onFinish(null) }
            onCancelled { onFinish(null) }
        }?.show()
    }

    private fun setupDialogEditText(): EditText {
        val editText = EditText(context)
        editText.setText(sharedPreferences.getString("default_text", ""))
        editText.setSelection(editText.text.length)
        return editText
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
}
