package com.cyrilfind.kodo.tasklist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyrilfind.kodo.databinding.ItemViewBinding
import com.cyrilfind.kodo.model.Task

class TaskListAdapter(
    private val tasksList: MutableList<Task>,
    private val onClickDelete: (Int) -> Unit,
    private val onClickCheckbox: (Int, Boolean, (Boolean) -> Unit) -> Unit
) : RecyclerView.Adapter<TaskListAdapter.TasksViewHolder>() {
    var onClickItem: (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemViewBinding.inflate(inflater)
        return TasksViewHolder(binding)
    }

    override fun getItemCount(): Int = tasksList.size

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) = holder.bind(tasksList[position])

    inner class TasksViewHolder(private val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.itemDeleteButton.setOnClickListener { onClickDelete(adapterPosition) }
            binding.itemCheckBox.setOnCheckedChangeListener(this::onCheckChange)
            binding.itemCardView.setOnClickListener { onClickItem(tasksList[adapterPosition]) }
        }

        private fun onCheckChange(buttonView: CompoundButton, isChecked: Boolean) {
            if (!buttonView.isPressed) return // ignore changes from code
            buttonView.isEnabled = false
            onClickCheckbox(adapterPosition, isChecked) { reallyChecked ->
                setChecked(reallyChecked)
                buttonView.isEnabled = true
            }
        }

        fun bind(task: Task) {
            binding.itemTextView.text = task.text
            setChecked(task.checked)
        }

        private fun setChecked(reallyChecked: Boolean) {
            binding.itemCheckBox.isChecked = reallyChecked
            binding.itemTextView.strikeThrough = reallyChecked
        }

        private var TextView.strikeThrough
            get() = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG > 0
            set(value) {
                paintFlags = if (value)
                    paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else
                    paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
    }
}
