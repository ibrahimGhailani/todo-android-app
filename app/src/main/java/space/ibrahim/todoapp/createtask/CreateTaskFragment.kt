package space.ibrahim.todoapp.createtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import space.ibrahim.todoapp.databinding.FragmentCreateTaskBinding

@AndroidEntryPoint
class CreateTaskFragment : BottomSheetDialogFragment() {

    private val viewModel: CreateTaskViewModel by viewModels()
    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initObservers()
        initView()
    }

    private fun initView() {
        binding.createButton.setOnClickListener {
            viewModel.createTask(
                binding.title.editText?.text.toString(),
                binding.content.editText?.text.toString()
            )
        }
    }

    private fun initObservers() {
        viewModel.uiState.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                when (it) {
                    CreateTaskViewModel.UiState.UnauthorizedState -> TODO("Navigate to login")
                    is CreateTaskViewModel.UiState.ErrorState -> {
                        Snackbar.make(binding.root, it.message, Snackbar.LENGTH_LONG).show()
                    }
                    CreateTaskViewModel.UiState.TaskCreated -> {
                        findNavController().navigateUp()
                        Snackbar.make(binding.root, "Task created", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}