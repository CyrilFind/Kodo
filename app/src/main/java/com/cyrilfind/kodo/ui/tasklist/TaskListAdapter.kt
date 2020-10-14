package com.cyrilfind.kodo.ui.tasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyrilfind.kodo.databinding.TasksListItemBinding
import com.cyrilfind.kodo.model.Task
import com.cyrilfind.kodo.setTextOrGone
import com.cyrilfind.kodo.strikeThrough
import kotlin.properties.Delegates

class TaskListAdapter(var listener: Listener) :
    RecyclerView.Adapter<TaskListAdapter.TasksViewHolder>() {
    var tasksList: List<Task> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TasksListItemBinding.inflate(inflater, parent, false)
        return TasksViewHolder(binding)
    }

    override fun getItemCount(): Int = tasksList.size

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) =
        holder.bind(tasksList[position])

    inner class TasksViewHolder(private val binding: TasksListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            setChecked(false)
            binding.taskTitleTextView.text = task.title
            binding.taskDescriptionTextView.setTextOrGone(task.description)
            binding.itemCardView.setOnClickListener { listener.onClickItem(task) }
            binding.itemDeleteButton.setOnClickListener {
                listener.onClickDelete(
                    task,
                    adapterPosition
                )
            }
            binding.itemCheckBox.setOnCheckedChangeListener { _, isChecked ->
                setChecked(isChecked)
                listener.onClickCheckbox(task, isChecked)
            }
        }

        private fun setChecked(checked: Boolean) {
            binding.itemCheckBox.isChecked = checked
            binding.taskTitleTextView.strikeThrough = checked
        }
    }

    interface Listener {
        fun onClickDelete(task: Task, position: Int)
        fun onClickCheckbox(task: Task, isChecked: Boolean)
        fun onClickItem(task: Task)
    }
}
