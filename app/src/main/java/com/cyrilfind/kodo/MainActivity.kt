package com.cyrilfind.kodo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.cyrilfind.kodo.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.okButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var tasksList = mutableListOf<Task>()
    private val todoRepository = TodoRepository()
    private val recyclerAdapter
        get() = binding.mainContent.tasksRecyclerView.adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainContent.tasksRecyclerView.adapter = TasksAdapter(tasksList, this::onClickDelete, this::onClickCheckbox)
        binding.fab.setOnClickListener(this::onClickFab)
        binding.mainContent.swiperefresh.setOnRefreshListener(this::refreshTasks)

        setSupportActionBar(toolbar)
        refreshTasks()
    }

    private fun onClickDelete(position: Int) {
        lifecycleScope.launch {
            val task = tasksList[position]
            if (todoRepository.deleteTask(task)) {
                tasksList.removeAt(position)
                recyclerAdapter?.notifyItemRemoved(position)
            }
        }
    }

    private fun onClickCheckbox(position: Int, checked: Boolean, callback: (Boolean) -> Unit) {
        lifecycleScope.launch {
            val task = tasksList[position]
            task.checked = todoRepository.checkTask(task, checked)
            callback(task.checked)
        }
    }

    private fun onClickFab(view: View) {
        view.isEnabled = false
        showAddItemDialog { text ->
            if (null != text) addTaskToList(text)
            view.isEnabled = true
        }
    }

    private fun addTaskToList(text: String) {
        lifecycleScope.launch {
            todoRepository.createTask(text)?.let { task ->
                tasksList.add(0, task)
                recyclerAdapter?.notifyItemInserted(0)
                binding.mainContent.tasksRecyclerView.smoothScrollToPosition(0)
            }
        }
    }

    private fun showAddItemDialog(onFinish: (String?) -> Unit) {
        val editText = EditText(this)
        alert(R.string.add_task_dialog_message, R.string.add_task_dialog_title) {
            customView = editText
            okButton { onFinish(editText.text.toString()) }
            cancelButton { onFinish(null) }
            onCancelled { onFinish(null) }
        }.show()
    }

    private fun refreshTasks() {
        lifecycleScope.launch {
            todoRepository.getTasks()?.let { tasks ->
                tasksList.clear()
                tasksList.addAll(tasks)
                recyclerAdapter?.notifyDataSetChanged()
            }
            binding.mainContent.swiperefresh.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_refresh -> {
                refreshTasks()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}