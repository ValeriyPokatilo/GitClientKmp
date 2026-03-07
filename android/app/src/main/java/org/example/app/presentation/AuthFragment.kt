package org.example.app.presentation

import android.app.AlertDialog
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
import app.xl.gitclientkmp.AuthViewModel
import app.xl.gitclientkmp.MR
import kotlinx.coroutines.launch
import org.example.app.R
import org.example.app.databinding.FragmentAuthBinding
import org.example.app.utils.showKeyboard
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        bindToViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindToViewModel() {
        bindInputs()

        binding.signInButton.setOnClickListener {
            viewModel.onSignButtonPressed()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

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

    private fun handleAction(action: AuthViewModel.Action) {
        when (action) {
            is AuthViewModel.Action.RouteToMain -> {
                findNavController().navigate(
                    R.id.action_authFragment_to_repositoriesListFragment
                )
            }

            is AuthViewModel.Action.ShowError -> {
                showErrorDialog(action.code, action.message)
            }

            is AuthViewModel.Action.FocusOnTokenField -> {
                binding.tokenInputEdit.requestFocus()
                binding.tokenInputEdit.showKeyboard()
            }
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

    private fun setupUI() {
        val context = requireContext()
        val placeholder = MR.strings.token_text_field_placeholder.getString(context)

        binding.signInButton.text = MR.strings.sign_in_button_title.getString(context)
        binding.tokenInputLayout.hint = placeholder
        binding.tokenInputEdit.hint = placeholder
    }

    private fun showErrorDialog(code: Int?, message: String?) {
        val errorMessageText = if (!message.isNullOrBlank()) {
            message
        } else {
            MR.strings.check_connection.getString(requireContext())
        }

        val fullMessage = buildString {
            append(errorMessageText)
            code?.let { append(" / $it") }
        }

        AlertDialog.Builder(requireContext())
            .setTitle(MR.strings.error.getString(requireContext()))
            .setMessage(fullMessage)
            .setPositiveButton(MR.strings.ok.getString(requireContext()), null)
            .show()
    }
}
