package com.cyrilfind.kodo.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cyrilfind.kodo.databinding.TaskFragmentBinding
import kotlinx.coroutines.launch


class TaskFragment : Fragment() {
    private lateinit var binding: TaskFragmentBinding
    private val args: TaskFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TaskFragmentBinding.inflate(inflater)
        val taskViewModelFactory = TaskViewModel.Factory(args.task)
        val viewModelProvider = ViewModelProviders.of(this, taskViewModelFactory)
        binding.viewModel = viewModelProvider.get(TaskViewModel::class.java)
        binding.viewModel?.dateStringLiveData?.observe(this, Observer {
            binding.taskDateTextView.setText(it) // hack to sync both dateTime views
        })
        binding.viewModel?.dateLiveData?.observe(this, Observer {
            if (it != null)
                binding.taskDateCalendarView.date = it
        })
        binding.taskDeleteButton.setOnClickListener {
            lifecycleScope.launch {
                binding.viewModel?.deleteTask()
                findNavController().popBackStack()
            }
        }
        return binding.root
    }
}
