package org.example.app.presentation

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.xl.gitclientkmp.MR
import app.xl.gitclientkmp.domain.entity.AppError
import app.xl.gitclientkmp.domain.entity.License
import app.xl.gitclientkmp.domain.entity.RepositoryDetails
import app.xl.gitclientkmp.viewModels.RepositoryInfoViewModel
import kotlinx.coroutines.launch
import org.example.app.R
import org.example.app.databinding.FragmentDetailInfoBinding
import org.example.app.entity.PlaceholderModel
import org.example.app.utils.MarkwonFactory
import org.example.app.utils.openUrl
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailInfoFragment : Fragment() {

    private var _binding: FragmentDetailInfoBinding? = null
    private val binding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val args: DetailInfoFragmentArgs by navArgs()

    private val viewModel: RepositoryInfoViewModel by viewModel {
        parametersOf(args.owner, args.repositoryName, args.branch)
    }
    private val repositoryName: String
        get() = args.repositoryName

    private val markwon by lazy {
        MarkwonFactory.createMarkwon(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationBar()
        bindToViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindToViewModel() {
        bindActions()
        bindState()
    }

    private fun bindActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collect { action ->
                    when (action) {
                        RepositoryInfoViewModel.Action.Logout -> {
                            navigateToAuth()
                        }
                        RepositoryInfoViewModel.Action.RouteBack -> {
                            navigateToList()
                        }
                    }
                }
            }
        }
    }

    private fun bindState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        RepositoryInfoViewModel.State.Loading -> {
                            binding.detailsProgressIndicator.show()
                            binding.scrollView.isVisible = false
                            binding.placeholderView.hide()
                        }
                        is RepositoryInfoViewModel.State.Loaded -> {
                            binding.detailsProgressIndicator.hide()
                            binding.scrollView.isVisible = true
                            binding.placeholderView.hide()

                            setupDetails(state.githubRepo)
                            handleReadmeState(state.readmeState)
                        }
                        is RepositoryInfoViewModel.State.Error -> {
                            binding.detailsProgressIndicator.hide()
                            binding.scrollView.isVisible = false
                            showError(state.error)
                        }
                    }
                }
            }
        }
    }

    private fun handleReadmeState(readmeState: RepositoryInfoViewModel.ReadmeState) {
        val readmeTextView = binding.readmeTextView
        when (readmeState) {
            RepositoryInfoViewModel.ReadmeState.Loading -> {
                binding.readmeProgressIndicator.show()
            }

            is RepositoryInfoViewModel.ReadmeState.Loaded -> {
                binding.readmeProgressIndicator.hide()
                val markdown = readmeState.markdown
                if (!markdown.isNullOrEmpty()) {
                    markwon.setMarkdown(readmeTextView, markdown)
                }
            }

            RepositoryInfoViewModel.ReadmeState.Empty -> {
                binding.readmeProgressIndicator.hide()
                readmeTextView.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.white_70)
                )
                readmeTextView.text = MR.strings.no_readme_md.getString(
                    requireContext()
                )
            }

            is RepositoryInfoViewModel.ReadmeState.Error -> {
                binding.readmeProgressIndicator.hide()
                showError(readmeState.error)
            }
        }
    }

    private fun setupDetails(details: RepositoryDetails) {
        setupRepositoryLink(details.url)
        setupLicense(details.license)
        setupCounters(details)
    }

    private fun setupRepositoryLink(url: String) {
        val displayUrl = url.removePrefix("https://").removePrefix("http://")
        binding.linkTextView.text = displayUrl
        binding.linkTextView.movementMethod = LinkMovementMethod.getInstance()
        binding.linkTextView.setOnClickListener {
            openUrl(url)
        }
    }

    private fun setupLicense(license: License?) {
        license?.let {
            binding.licenselink.text = license.name
            binding.licenselink.movementMethod = LinkMovementMethod.getInstance()
            val url = it.url
            if (!url.isNullOrBlank()) {
                binding.licenselink.setOnClickListener {
                    openUrl(url)
                }
            }
        }
    }

    private fun setupCounters(details: RepositoryDetails) {
        binding.starsCounter.text = details.stargazersCount.toString()
        binding.forksCounter.text = details.forksCount.toString()
        binding.watchersCounter.text = details.subscribersCount.toString()
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
                } else -> false
            }
        }
    }

    private fun showError(error: AppError) {
        when (error) {
            is AppError.Http -> {
                binding.placeholderView.show(
                    model = PlaceholderModel(
                        iconRes = R.drawable.ic_error,
                        title = error.code.toString(),
                        titleColorRes = R.color.error,
                        message = error.message.toString(),
                        buttonTitle = MR.strings.retry.getString(
                            requireContext()
                        ),
                        buttonAction = {
                            viewModel.onRetryButtonPressed()
                        }
                    )
                )
            }

            is AppError.Network -> {
                binding.placeholderView.show(
                    model = PlaceholderModel(
                        iconRes = R.drawable.ic_not_connected,
                        title = MR.strings.repositories_connection_error_title.getString(
                            requireContext()
                        ),
                        titleColorRes = R.color.error,
                        message = MR.strings.repositories_connection_error_message.getString(
                            requireContext()
                        ),
                        buttonTitle = MR.strings.retry.getString(
                            requireContext()
                        ),
                        buttonAction = {
                            viewModel.onRetryButtonPressed()
                        }
                    )
                )
            }
        }
    }

    private fun navigateToAuth() {
        findNavController().navigate(R.id.action_global_authFragment)
    }

    private fun navigateToList() {
        findNavController().popBackStack()
    }
}
