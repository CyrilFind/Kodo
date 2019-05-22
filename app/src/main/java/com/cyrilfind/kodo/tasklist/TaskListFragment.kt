package com.cyrilfind.kodo.tasklist

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.cyrilfind.kodo.R
import com.cyrilfind.kodo.databinding.TasksListFragmentBinding
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.okButton

class TaskListFragment : Fragment() {
    private lateinit var binding: TasksListFragmentBinding
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TaskListViewModel::class.java)
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
        viewModel.tasksListLiveData.observe(this, Observer {
            binding.tasksRecyclerView.smoothScrollToPosition(0)
        })
        viewModel.reverseOrder = sharedPreferences.getBoolean("order", false)
        viewModel.refreshTasks()
        (activity as? AppCompatActivity)?.supportActionBar?.title = sharedPreferences.getString("title", "")
        return binding.root
    }

    private fun onClickFab(view: View) {
        view.isEnabled = false
        showAddItemDialog { text ->
            if (null != text) viewModel.addTaskToList(text)
            view.isEnabled = true
        }
    }

    private fun showAddItemDialog(onFinish: (String?) -> Unit) {
        val editText = EditText(context)
        editText.setText(sharedPreferences.getString("default_text", ""))
        editText.setSelection(editText.text.length)
        context?.alert(R.string.add_task_dialog_message, R.string.add_task_dialog_title) {
            customView = editText
            okButton { onFinish(editText.text.toString()) }
            cancelButton { onFinish(null) }
            onCancelled { onFinish(null) }
        }?.show()
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
            else -> super.onOptionsItemSelected(item)
        }
    }
}
