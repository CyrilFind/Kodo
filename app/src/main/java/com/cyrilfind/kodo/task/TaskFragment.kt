package com.cyrilfind.kodo.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.cyrilfind.kodo.databinding.TaskFragmentBinding


class TaskFragment : Fragment() {
    private lateinit var binding: TaskFragmentBinding
    private val args: TaskFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TaskFragmentBinding.inflate(inflater)
        val taskViewModelFactory = TaskViewModel.Factory(args.task)
        val viewModelProvider = ViewModelProviders.of(this, taskViewModelFactory)
        binding.viewModel = viewModelProvider.get(TaskViewModel::class.java)
        return binding.root
    }
}
