package org.example.app.presentation

import android.os.Bundle
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.xl.gitclientkmp.MR
import app.xl.gitclientkmp.domain.entity.AppError
import app.xl.gitclientkmp.viewModels.RepositoriesListViewModel
import kotlinx.coroutines.launch
import org.example.app.R
import org.example.app.databinding.FragmentRepositoriesListBinding
import org.example.app.entity.PlaceholderModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoriesListFragment : Fragment() {

    private var _binding: FragmentRepositoriesListBinding? = null
    private val binding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val viewModel: RepositoriesListViewModel by viewModel()

    private lateinit var repoAdapter: RepoAdapter

    private val divider by lazy {
        DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
            ContextCompat.getDrawable(requireContext(), R.drawable.divider)?.let {
                setDrawable(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepositoriesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationBar()
        setupRecyclerView()
        bindToViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupNavigationBar() {
        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_logout) {
                viewModel.onLogoutButtonPressed()
                true
            } else {
                false
            }
        }
    }

    private fun setupRecyclerView() = with(binding.recyclerView) {
        repoAdapter = RepoAdapter { repository ->
            viewModel.onRepositoryItemPressed(repository)
        }
        adapter = repoAdapter
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(divider)
        setHasFixedSize(true)
    }

    private fun bindToViewModel() {
        bindState()
        bindActions()
    }

    private fun bindState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        RepositoriesListViewModel.State.Empty -> {
                            handleEmptyState()
                        }

                        RepositoriesListViewModel.State.Loading -> {
                            handleLoadingState()
                        }

                        is RepositoriesListViewModel.State.Loaded -> {
                            handleLoadedState(state)
                        }

                        is RepositoriesListViewModel.State.Error -> {
                            handleErrorState(state)
                        }
                    }
                }
            }
        }
    }

    private fun handleEmptyState() {
        binding.recyclerView.isVisible = false
        binding.progressIndicator.hide()
        binding.placeholderView.show(
            PlaceholderModel(
                iconRes = R.drawable.ic_empty,
                title = MR.strings.repositories_empty_title.getString(requireContext()),
                titleColorRes = R.color.blue,
                message = MR.strings.repositories_empty_message.getString(requireContext()),
                buttonTitle = MR.strings.refresh.getString(requireContext()),
                buttonAction = {
                    viewModel.onRetryButtonPressed()
                }
            )
        )
    }

    private fun handleLoadedState(state: RepositoriesListViewModel.State.Loaded) {
        repoAdapter.submitList(state.repositories)
        binding.recyclerView.isVisible = true
        binding.progressIndicator.hide()
        binding.placeholderView.hide()
    }

    private fun handleLoadingState() {
        binding.recyclerView.isVisible = false
        binding.progressIndicator.show()
        binding.placeholderView.hide()
    }

    private fun handleErrorState(state: RepositoriesListViewModel.State.Error) {
        binding.recyclerView.isVisible = false
        binding.progressIndicator.hide()

        when (state.error) {
            is AppError.Http -> {
                val error = state.error as AppError.Http
                binding.placeholderView.show(
                    model = PlaceholderModel(
                        iconRes = R.drawable.ic_error,
                        title = error.code.toString(),
                        titleColorRes = R.color.error,
                        message = error.message.toString(),
                        buttonTitle = MR.strings.retry.getString(requireContext()),
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
                        title = MR.strings.repositories_connection_error_title.getString(requireContext()),
                        titleColorRes = R.color.error,
                        message = MR.strings.repositories_connection_error_message.getString(requireContext()),
                        buttonTitle = MR.strings.retry.getString(requireContext()),
                        buttonAction = {
                            viewModel.onRetryButtonPressed()
                        }
                    )
                )
            }
        }
    }

    private fun bindActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collect { action ->
                    when (action) {
                        is RepositoriesListViewModel.Action.RouteToDetail -> {
                            navigateToDetails(
                                owner = action.owner,
                                repositoryName = action.repositoryName,
                                branch = action.branch
                            )
                        }

                        RepositoriesListViewModel.Action.Logout -> {
                            navigateToAuth()
                        }
                    }
                }
            }
        }
    }

    private fun navigateToAuth() {
        findNavController().navigate(R.id.action_global_authFragment)
    }

    private fun navigateToDetails(owner: String, repositoryName: String, branch: String) {
        val action = RepositoriesListFragmentDirections
            .actionRepositoriesListFragmentToDetailInfoFragment(
                owner = owner,
                repositoryName = repositoryName,
                branch = branch
            )

        findNavController().navigate(action)
    }
}
