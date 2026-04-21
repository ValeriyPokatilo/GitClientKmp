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
import app.xl.gitclientkmp.viewModels.RepositoriesListViewModel
import dev.icerock.moko.units.adapter.UnitsRecyclerViewAdapter
import kotlinx.coroutines.launch
import org.example.app.R
import org.example.app.databinding.RepositoriesListFragmentBinding
import org.example.app.utils.toUnitItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoriesListFragment : Fragment() {

    private var _binding: RepositoriesListFragmentBinding? = null
    private val binding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val viewModel: RepositoriesListViewModel by viewModel()

    private lateinit var unitsAdapter: UnitsRecyclerViewAdapter

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
        _binding = RepositoriesListFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationBar()
        setupRecyclerView()
        bindToViewModel()

        viewModel.onStart()
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
        layoutManager = LinearLayoutManager(requireContext())

        unitsAdapter = UnitsRecyclerViewAdapter(viewLifecycleOwner)
        adapter = unitsAdapter

        addItemDecoration(divider)
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

    private fun renderState(state: RepositoriesListViewModel.State) {
        binding.progressIndicator.isVisible = state is RepositoriesListViewModel.State.Loading
        binding.recyclerView.isVisible = state is RepositoriesListViewModel.State.Loaded
        binding.placeholderView.isVisible =
            state !is RepositoriesListViewModel.State.Loaded && state !is RepositoriesListViewModel.State.Loading

        when {
            state is RepositoriesListViewModel.State.Empty -> {
                binding.placeholderView.showEmpty { viewModel.onRetryButtonPressed() }
            }
            state is RepositoriesListViewModel.State.Error -> {
                binding.placeholderView.showError(state.error) { viewModel.onRetryButtonPressed() }
            }
            else -> {
                binding.placeholderView.hide()
            }
        }

        if (state is RepositoriesListViewModel.State.Loaded) {
            val units = state.repositories.map { repo ->
                repo.toUnitItem { clickedRepo ->
                    viewModel.onRepositoryItemPressed(clickedRepo)
                }
            }
            unitsAdapter.units = units
        }
    }
    private fun handleAction(action: RepositoriesListViewModel.Action) {
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
