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
import app.xl.gitclientkmp.MR
import app.xl.gitclientkmp.domain.error.ErrorModel
import app.xl.gitclientkmp.viewModel.AuthViewModel
import kotlinx.coroutines.launch
import org.example.app.R
import org.example.app.databinding.AuthFragmentBinding
import org.example.app.extensions.showKeyboard
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

    private fun bindToViewModel() {
        binding.signInButton.setOnClickListener {
            viewModel.onSignButtonPressed()
        }

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
        when (state) {
            AuthViewModel.State.Idle -> {
                binding.tokenInputLayout.error = null
                binding.signInButton.isEnabled = true
                binding.signInButton.setTextColor(Color.WHITE)
                binding.progressIndicator.hide()
            }

            AuthViewModel.State.Loading -> {
                binding.signInButton.isEnabled = false
                binding.signInButton.setTextColor(Color.TRANSPARENT)
                binding.progressIndicator.show()
            }

            is AuthViewModel.State.InvalidInput -> {
                binding.signInButton.isEnabled = false
                binding.tokenInputLayout.error =
                    MR.strings.invalid_token_reason.getString(requireContext())
            }
        }
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

    private fun setupUI() {
        val context = requireContext()
        val placeholder = MR.strings.token_text_field_placeholder.getString(context)

        binding.signInButton.text = MR.strings.sign_in_button_title.getString(context).uppercase()
        binding.tokenInputLayout.hint = placeholder
        binding.tokenInputEdit.hint = placeholder
    }

    private fun showErrorDialog(model: ErrorModel) {
        val title = MR.strings.error.getString(requireContext())
        val code = model.title.toString(requireContext())
        val message = model.message.toString(requireContext())
        val fullMessage = "$code / $message"
        val buttonTitle = MR.strings.ok.getString(requireContext())

        showErrorAlertDialog(title = title, message = fullMessage, buttonTitle = buttonTitle)
    }
}
