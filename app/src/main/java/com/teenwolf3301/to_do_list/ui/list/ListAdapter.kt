package com.teenwolf3301.to_do_list.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teenwolf3301.to_do_list.APP_ACTIVITY
import com.teenwolf3301.to_do_list.R
import com.teenwolf3301.to_do_list.data.Priority
import com.teenwolf3301.to_do_list.data.Task
import com.teenwolf3301.to_do_list.databinding.ListItemBinding
import com.teenwolf3301.to_do_list.ui.list.ListAdapter.TaskViewHolder

class ListAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Task, TaskViewHolder>(DiffCallback()) {

    inner class TaskViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }
                cbCompleted.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClick(task, cbCompleted.isChecked)
                    }
                }
            }
        }

        fun bind(task: Task) {
            binding.apply {
                cbCompleted.isChecked = task.isCompleted
                tvItemName.text = task.name
                tvItemCategory.text = task.category
                when (task.priority) {
                    Priority.LOW -> icPriority.backgroundTintList =
                        ContextCompat.getColorStateList(APP_ACTIVITY, R.color.google_green)
                    Priority.MEDIUM -> icPriority.backgroundTintList =
                        ContextCompat.getColorStateList(APP_ACTIVITY, R.color.google_yellow)
                    Priority.HIGH -> icPriority.backgroundTintList =
                        ContextCompat.getColorStateList(APP_ACTIVITY, R.color.google_red)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(task: Task)
        fun onCheckBoxClick(task: Task, isChecked: Boolean)
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}