package org.example.app.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import app.xl.gitclientkmp.domain.error.ErrorModel
import app.xl.gitclientkmp.viewModel.AuthViewModel
import kotlinx.coroutines.launch
import org.example.app.R
import org.example.app.databinding.AuthFragmentBinding
import org.example.app.utils.showKeyboard
import org.example.app.utils.showErrorAlertDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : Fragment() {

    private var _binding: AuthFragmentBinding? = null
    private val binding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        bindToViewModel()
        bindInputs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        binding.tokenInputLayout.hint = getString(R.string.token_text_field_placeholder)

        binding.signInButton.text = getString(R.string.sign_in_button_title)
        binding.signInButton.setOnClickListener {
            viewModel.onSignButtonPressed()
        }
    }

    private fun bindToViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state ->
                        renderState(state)
                    }
                }
                launch {
                    viewModel.action.collect { action ->
                        handleAction(action)
                    }
                }
            }
        }
    }

    private fun bindInputs() {
        binding.tokenInputEdit.doAfterTextChanged {
            viewModel.onTokenChanged(it?.toString().orEmpty())
        }
    }

    private fun renderState(state: AuthViewModel.State) {
        val isLoading = state == AuthViewModel.State.Loading
        val isInvalid = state is AuthViewModel.State.InvalidInput

        binding.progressIndicator.visibility =
            if (isLoading) View.VISIBLE else View.GONE

        binding.signInButton.isEnabled = !isLoading && !isInvalid

        binding.signInButton.setTextColor(
            if (isLoading) Color.TRANSPARENT else Color.WHITE
        )

        binding.tokenInputLayout.error =
            if (isInvalid) getString(R.string.invalid_token_reason) else null
    }

    private fun handleAction(action: AuthViewModel.Action) {
        when (action) {
            is AuthViewModel.Action.RouteToMain -> {
                findNavController().navigate(
                    R.id.action_authFragment_to_repositoriesListFragment
                )
            }

            is AuthViewModel.Action.ShowError -> {
                showErrorDialog(action.error)
            }

            is AuthViewModel.Action.FocusOnTokenField -> {
                binding.tokenInputEdit.requestFocus()
                binding.tokenInputEdit.showKeyboard()
            }
        }
    }

    private fun showErrorDialog(model: ErrorModel) {
        val message = model.title.toString(requireContext())
        val postfix = model.message.toString(requireContext())
        val fullMessage = "$message\n$postfix"

        showErrorAlertDialog(
            title = getString(R.string.error),
            message = fullMessage,
            buttonTitle = getString(R.string.ok)
        )
    }
}
