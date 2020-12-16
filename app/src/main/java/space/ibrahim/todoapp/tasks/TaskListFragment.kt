package space.ibrahim.todoapp.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import space.ibrahim.todoapp.databinding.FragmentTaskListBinding

@AndroidEntryPoint
class TaskListFragment : Fragment() {

    private val viewModel: TaskListViewModel by viewModels()
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initView()
        viewModel.checkIsUserLoggedIn()
        viewModel.getTasks()

    }

    private fun initView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = TaskAdapter(
            doOnTaskChecked = { task, position, isChecked ->
                viewModel.itemChecked(task, position, isChecked)
            },
            doOnItemClicked = { task ->
                viewModel.itemClicked(task)
            }
        )

        binding.addTask.setOnClickListener {
            findNavController().navigate(TaskListFragmentDirections.actionTaskListFragmentToCreateTaskFragment())
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getTasks()
        }
    }

    private fun initObservers() {
        viewModel.uiState.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    is TaskListViewModel.UiState.TasksLoadedState -> {
                        binding.swipeRefresh.isRefreshing = false
                        (binding.recyclerView.adapter as TaskAdapter).insertTasks(it.tasks)
                    }
                    is TaskListViewModel.UiState.TaskUpdatedState -> {
                        (binding.recyclerView.adapter as TaskAdapter).updateTask(
                            it.task,
                            it.position
                        )

                    }
                    is TaskListViewModel.UiState.NavigateToTaskState -> {//TODO: Open edit fragment
                    }
                    TaskListViewModel.UiState.UnauthorizedState
                    -> {
                        findNavController().navigate(TaskListFragmentDirections.actionTaskListFragmentToRegistrationFragment2())
                    }
                    is TaskListViewModel.UiState.ErrorState -> {
                        binding.swipeRefresh.isRefreshing = false
                        Snackbar.make(binding.root, it.message, Snackbar.LENGTH_LONG).show()
                    }
                    TaskListViewModel.UiState.LoadingState -> {
                        binding.swipeRefresh.isRefreshing = true
                    }
                }
            }
        })
    }

}