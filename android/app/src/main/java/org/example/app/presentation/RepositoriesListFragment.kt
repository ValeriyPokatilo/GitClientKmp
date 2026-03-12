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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.xl.gitclientkmp.viewModels.RepositoriesListViewModel
import kotlinx.coroutines.launch
import org.example.app.R
import org.example.app.databinding.FragmentRepositoriesListBinding
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
                            // TODO: - handleEmptyState()
                        }

                        RepositoriesListViewModel.State.Loading -> {
                            // TODO: - handleLoadingState()
                        }

                        is RepositoriesListViewModel.State.Loaded -> {
                             handleLoadedState(state)
                        }

                        is RepositoriesListViewModel.State.Error -> {
                            // TODO: - handleErrorState(state)
                        }
                    }
                }
            }
        }
    }

    private fun handleLoadedState(state: RepositoriesListViewModel.State.Loaded) {
        repoAdapter.submitList(state.repositories)
        binding.recyclerView.isVisible = true
        binding.progressIndicator.hide()
        // TODO: - binding.placeholderView.hide()
    }

    private fun bindActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collect { action ->
                    when (action) {
                        is RepositoriesListViewModel.Action.RouteToDetail -> {
                            // TODO: - navigate to details
                        }

                        RepositoriesListViewModel.Action.Logout -> {
                            // TODO: - navigate to auth
                        }
                    }
                }
            }
        }
    }
}
