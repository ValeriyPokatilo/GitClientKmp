package org.example.app.presentation

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.xl.gitclientkmp.License
import app.xl.gitclientkmp.RepositoryDetails
import app.xl.gitclientkmp.domain.error.ErrorModel
import app.xl.gitclientkmp.domain.extensions.toDisplayUrl
import app.xl.gitclientkmp.presentation.RepositoryInfoViewModel
import io.noties.markwon.Markwon
import org.example.app.R
import org.example.app.databinding.DetailInfoFragmentBinding
import org.example.app.model.PlaceholderState
import org.example.app.utils.MarkwonFactory
import org.example.app.utils.collectIn
import org.example.app.utils.openUrl
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailInfoFragment : Fragment() {

    private var _binding: DetailInfoFragmentBinding? = null
    private val binding: DetailInfoFragmentBinding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val args: DetailInfoFragmentArgs by navArgs()

    private val viewModel: RepositoryInfoViewModel by viewModel {
        parametersOf(args.owner, args.repositoryName, args.branch)
    }
    private val repositoryName: String
        get() = args.repositoryName

    private var markwon: Markwon? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailInfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationBar()
        setupUI(context = view.context)
        bindToViewModel()

        viewModel.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupNavigationBar() {
        binding.toolbar.title = repositoryName

        binding.toolbar.setNavigationIcon(
            R.drawable.ic_back
        )

        binding.toolbar.setNavigationOnClickListener {
            viewModel.onBackButtonPressed()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_logout -> {
                    viewModel.onLogoutPressed()
                    true
                }

                else -> false
            }
        }
    }

    private fun setupUI(context: Context) {
        markwon = MarkwonFactory.createMarkwon(context)

        binding.retryButton.setTitle(getString(R.string.retry))
        binding.retryButton.setButtonClickListener {
            viewModel.onRetryButtonPressed()
        }
    }

    private fun bindToViewModel() {
        viewModel.state.collectIn(viewLifecycleOwner, action = ::renderState)
        viewModel.action.collectIn(viewLifecycleOwner, action = ::handleAction)
    }

    private fun renderState(state: RepositoryInfoViewModel.State) {
        binding.detailsProgressIndicator.isVisible = state == RepositoryInfoViewModel.State.Loading

        binding.scrollView.isVisible = state is RepositoryInfoViewModel.State.Loaded

        binding.placeholderView.isVisible =
            state != RepositoryInfoViewModel.State.Loading && state !is RepositoryInfoViewModel.State.Loaded

        binding.retryButton.isVisible = state is RepositoryInfoViewModel.State.Error

        if (state is RepositoryInfoViewModel.State.Error) {
            showError(state.error)
        }

        if (state is RepositoryInfoViewModel.State.Loaded) {
            setupDetails(state.githubRepo)
            handleReadmeState(state.readmeState)
        }
    }

    private fun handleAction(action: RepositoryInfoViewModel.Action) {
        when (action) {
            RepositoryInfoViewModel.Action.Logout -> {
                navigateToAuth()
            }

            RepositoryInfoViewModel.Action.RouteToBack -> {
                navigateToList()
            }

            RepositoryInfoViewModel.Action.RouteToIssues -> {
                navigateToIssues()
            }
        }
    }

    private fun handleReadmeState(readmeState: RepositoryInfoViewModel.ReadmeState) {
        when (readmeState) {
            RepositoryInfoViewModel.ReadmeState.Loading -> {
                renderReadmeLoading()
            }

            is RepositoryInfoViewModel.ReadmeState.Loaded -> {
                val markdown: String? = readmeState.markdown?.takeIf { it.isNotEmpty() }
                if (markdown == null) {
                    renderReadmeEmpty()
                } else {
                    renderReadmeMarkdown(markdown = markdown)
                }
            }

            RepositoryInfoViewModel.ReadmeState.Empty -> {
                renderReadmeEmpty()
            }

            is RepositoryInfoViewModel.ReadmeState.Error -> {
                renderReadmeError(error = readmeState.error)
            }
        }
    }

    private fun renderReadmeLoading() {
        binding.readmeProgressIndicator.isVisible = true

        binding.readmeTextView.text = ""
        binding.readmeTextView.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
    }

    private fun renderReadmeEmpty() {
        binding.readmeProgressIndicator.isVisible = false

        binding.readmeTextView.text = getString(R.string.no_readme_md)
        binding.readmeTextView.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.white_70)
        )
    }

    private fun renderReadmeMarkdown(markdown: String) {
        binding.readmeProgressIndicator.isVisible = false

        binding.readmeTextView.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
        markwon?.setMarkdown(binding.readmeTextView, markdown)
    }

    private fun renderReadmeError(error: ErrorModel) {
        binding.readmeProgressIndicator.isVisible = false

        binding.readmeTextView.text = ""
        binding.readmeTextView.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        )

        showError(error)
    }

    private fun setupDetails(details: RepositoryDetails) {
        setupRepositoryLink(url = details.url)
        setupLicense(license = details.license)
        setupCounters(details = details)
        setupIssues(issuesCount = details.openIssuesCount)
    }

    private fun setupRepositoryLink(url: String) {
        binding.linkTextView.text = url.toDisplayUrl()
        binding.linkTextView.movementMethod = LinkMovementMethod.getInstance()
        binding.linkTextView.setOnClickListener {
            openUrl(url)
        }
    }

    private fun setupLicense(license: License?) {
        license?.let {
            binding.licenselink.text = license.name
            binding.licenselink.movementMethod = LinkMovementMethod.getInstance()
            val url: String? = it.url
            if (!url.isNullOrBlank()) {
                binding.licenselink.setOnClickListener {
                    openUrl(url = url)
                }
            }
        }
    }

    private fun setupCounters(details: RepositoryDetails) {
        binding.starsCounter.text = details.stargazersCount.toString()
        binding.forksCounter.text = details.forksCount.toString()
        binding.watchersCounter.text = details.subscribersCount.toString()
    }

    private fun setupIssues(issuesCount: Int) {
        binding.issuelink.paintFlags =
            binding.issuesCounter.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.issuelink.setOnClickListener {
            viewModel.onViewIssuesPressed()
        }
        binding.issuesCounter.text = issuesCount.toString()
    }

    private fun showError(error: ErrorModel) {
        binding.placeholderView.render(
            state = PlaceholderState.Error(error = error)
        )
    }

    private fun navigateToAuth() {
        findNavController().navigate(resId = R.id.action_global_authFragment)
    }

    private fun navigateToList() {
        findNavController().popBackStack()
    }

    private fun navigateToIssues() {
        val action: NavDirections = DetailInfoFragmentDirections
            .actionDetailInfoFragmentToIssuesListFragment(
                owner = args.owner,
                repositoryName = args.repositoryName
            )

        findNavController().navigate(directions = action)
    }
}
