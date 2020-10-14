package com.cyrilfind.kodo.ui.taskform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cyrilfind.kodo.databinding.TaskFormFragmentBinding
import com.cyrilfind.kodo.hideKeyboard
import kotlinx.coroutines.launch


class TaskFormFragment : Fragment() {
    private lateinit var binding: TaskFormFragmentBinding
    private val args: TaskFormFragmentArgs by navArgs()
    private val viewModelFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            TaskFormViewModel(args.task) as T
    }
    private val viewModel: TaskFormViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TaskFormFragmentBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.taskDeleteButton.setOnClickListener {
            lifecycleScope.launch {
                binding.viewModel?.deleteTask()
                findNavController().popBackStack()
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.hideKeyboard()
    }
}
