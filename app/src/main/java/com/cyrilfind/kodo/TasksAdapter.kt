package com.cyrilfind.kodo

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyrilfind.kodo.databinding.ItemViewBinding
import kotlinx.android.synthetic.main.item_view.view.*

class TasksAdapter(
    private val tasksList: List<Task>,
    private val onClickDelete: (Int) -> Unit,
    private val onClickCheckbox: (Int, Boolean, (Boolean) -> Unit) -> Unit
) : RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemViewBinding.inflate(inflater)
        return TasksViewHolder(itemView.root)
    }

    override fun getItemCount(): Int = tasksList.size

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) = holder.bind(tasksList[position])

    inner class TasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.itemDeleteButton.setOnClickListener { onClickDelete(adapterPosition) }
            itemView.itemCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if(!buttonView.isPressed) return@setOnCheckedChangeListener // ignore changes from code
                buttonView.isEnabled = false
                onClickCheckbox(adapterPosition, isChecked) { reallyChecked ->
                    setChecked(reallyChecked)
                    buttonView.isEnabled = true
                }
            }
        }

        private fun setChecked(reallyChecked: Boolean) {
            itemView.itemCheckBox.isChecked = reallyChecked
            itemView.itemTextView.strikeThrough = reallyChecked
        }

        fun bind(task: Task) {
            itemView.itemTextView.text = task.text
            setChecked(task.checked)
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
