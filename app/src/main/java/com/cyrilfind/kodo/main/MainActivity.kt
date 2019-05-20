package com.cyrilfind.kodo.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cyrilfind.kodo.R
import com.cyrilfind.kodo.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.okButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainContent.tasksRecyclerView.adapter = viewModel.recyclerAdapter
        binding.fab.setOnClickListener(this::onClickFab)
        binding.mainContent.swipeRefresh.setOnRefreshListener(viewModel::refreshTasks)
        binding.mainContent.swipeRefresh.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent
        )
        viewModel.tasksListLiveData.observe(this, Observer {
            binding.mainContent.tasksRecyclerView.smoothScrollToPosition(0)
        })
        viewModel.isRefreshing.observe(this, Observer {
            binding.mainContent.swipeRefresh.isRefreshing = it
        })

        setSupportActionBar(toolbar)
        viewModel.refreshTasks()
    }

    private fun onClickFab(view: View) {
        view.isEnabled = false
        showAddItemDialog { text ->
            if (null != text) viewModel.addTaskToList(text)
            view.isEnabled = true
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_refresh -> {
                viewModel.refreshTasks()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}