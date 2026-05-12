package org.example.app.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.xl.gitclientkmp.Issue
import app.xl.gitclientkmp.IssueState
import app.xl.gitclientkmp.domain.error.ErrorModel
import app.xl.gitclientkmp.presentation.IssueInfoViewModel
import io.noties.markwon.Markwon
import org.example.app.R
import org.example.app.databinding.IssueInfoFragmentBinding
import org.example.app.model.PlaceholderState
import org.example.app.utils.MarkwonFactory
import org.example.app.utils.collectIn
import org.example.app.utils.toShortDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class IssueInfoFragment : Fragment() {

    private var _binding: IssueInfoFragmentBinding? = null
    private val binding: IssueInfoFragmentBinding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val args: IssueInfoFragmentArgs by navArgs()

    private val viewModel: IssueInfoViewModel by viewModel {
        parametersOf(args.owner, args.repositoryName, args.issueNumber)
    }

    private var markwon: Markwon? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = IssueInfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationBar()
        setupUI(context = view.context)
        localize()
        bindToViewModel()

        viewModel.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupNavigationBar() {
        binding.toolbar.title = getString(R.string.issue_number) + args.issueNumber.toString()

        binding.toolbar.setNavigationIcon(
            R.drawable.ic_back
        )

        binding.toolbar.setNavigationOnClickListener {
            viewModel.onBackButtonPressed()
        }
    }

    private fun setupUI(context: Context) {
        markwon = MarkwonFactory.createMarkwon(context = context)

        binding.retryButton.setButtonClickListener {
            viewModel.onRetryButtonPressed()
        }
    }

    private fun localize() {
        binding.retryButton.setTitle(title = getString(R.string.retry))
    }

    private fun bindToViewModel() {
        viewModel.state.collectIn(owner = viewLifecycleOwner, action = ::renderState)
        viewModel.action.collectIn(owner = viewLifecycleOwner, action = ::handleAction)
    }

    private fun renderState(state: IssueInfoViewModel.State) {
        val isLoading: Boolean = state == IssueInfoViewModel.State.Loading
        val isError: Boolean = state is IssueInfoViewModel.State.Error
        val isLoaded: Boolean = state is IssueInfoViewModel.State.Loaded

        binding.detailsProgressIndicator.isVisible = isLoading
        binding.scrollView.isVisible = isLoaded
        binding.retryButton.isVisible = isError
        binding.placeholderView.isVisible = isError

        if (isError) {
            showError(error = state.error)
        }

        if (!isLoaded) {
            return
        }

        val issue: Issue = state.issue
        val context: Context = requireContext()

        val (textColor, backgroundColor) = when (issue.state) {
            IssueState.OPEN -> {
                ContextCompat.getColor(context, R.color.light_green) to
                    ContextCompat.getColorStateList(context, R.color.light_green_20)
            }

            IssueState.CLOSED -> {
                ContextCompat.getColor(context, R.color.error) to
                    ContextCompat.getColorStateList(context, R.color.error_20)
            }
        }

        binding.issueStatus.apply {
            text = issue.state.displayText()
            setTextColor(textColor)
            chipBackgroundColor = backgroundColor
        }
        binding.issueDate.text = issue.updatedAt.toShortDate()
        binding.issueTitle.text = issue.title
        binding.description.text = getString(R.string.description)

        markwon?.setMarkdown(binding.issueBody, issue.body)
    }

    private fun handleAction(action: IssueInfoViewModel.Action) {
        when (action) {
            IssueInfoViewModel.Action.RouteToBack -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun showError(error: ErrorModel) {
        binding.placeholderView.render(
            state = PlaceholderState.Error(error = error)
        )
    }
}
