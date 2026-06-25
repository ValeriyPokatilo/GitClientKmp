package org.example.app.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.xl.gitclientkmp.domain.error.ErrorModel
import app.xl.gitclientkmp.presentation.AuthViewModel
import org.example.app.R
import org.example.app.databinding.AuthFragmentBinding
import org.example.app.utils.collectIn
import org.example.app.utils.showErrorAlertDialog
import org.example.app.utils.showKeyboard
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : Fragment() {

    private var _binding: AuthFragmentBinding? = null
    private val binding: AuthFragmentBinding
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
        localize()
        bindToViewModel()
        bindInputs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        binding.signInButton.setOnClickListener {
            viewModel.onSignButtonPressed()
        }
    }

    private fun localize() {
        binding.tokenInputLayout.hint = getString(R.string.token_text_field_placeholder)

        binding.signInButton.text = getString(R.string.sign_in_button_title)
    }

    private fun bindToViewModel() {
        viewModel.state.collectIn(owner = viewLifecycleOwner, action = ::renderState)
        viewModel.action.collectIn(owner = viewLifecycleOwner, action = ::handleAction)
    }

    private fun bindInputs() {
        binding.tokenInputEdit.doAfterTextChanged {
            viewModel.onTokenChanged(text = it?.toString().orEmpty())
        }
    }

    private fun renderState(state: AuthViewModel.State) {
        val isLoading: Boolean = state == AuthViewModel.State.Loading
        val isInvalid: Boolean = state is AuthViewModel.State.InvalidInput

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
                    resId = R.id.action_authFragment_to_repositoriesListFragment
                )
            }

            is AuthViewModel.Action.ShowError -> {
                showErrorDialog(model = action.error)
            }

            is AuthViewModel.Action.FocusOnTokenField -> {
                binding.tokenInputEdit.requestFocus()
                binding.tokenInputEdit.showKeyboard()
            }
        }
    }

    private fun showErrorDialog(model: ErrorModel) {
        showErrorAlertDialog(
            title = getString(R.string.error),
            message = model.alertMessage
                ?.toString(requireContext())
                .orEmpty(),
            buttonTitle = getString(R.string.ok)
        )
    }
}
