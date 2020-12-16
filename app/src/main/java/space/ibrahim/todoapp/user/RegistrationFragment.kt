package space.ibrahim.todoapp.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import space.ibrahim.todoapp.databinding.FragmentRegistrationBinding

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private val viewModel: RegistrationViewModel by viewModels()
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initView()
    }

    private fun initObservers() {
        viewModel.uiState.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    is RegistrationViewModel.UiState.ErrorState -> {
                        Snackbar.make(binding.root, it.message, Snackbar.LENGTH_LONG).show()
                    }
                    is RegistrationViewModel.UiState.RegistrationSucceededState -> {
                        findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragment2ToTaskListFragment())
                    }
                }
            }

        })
    }

    private fun initView() {
        binding.registerButton.setOnClickListener {
            viewModel.register(
                username = binding.username.editText?.text.toString(),
                password = binding.password.editText?.text.toString()
            )
        }

        binding.loginButton.setOnClickListener {
            viewModel.register(
                username = binding.username.editText?.text.toString(),
                password = binding.password.editText?.text.toString(),
                alreadyRegistered = true
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}