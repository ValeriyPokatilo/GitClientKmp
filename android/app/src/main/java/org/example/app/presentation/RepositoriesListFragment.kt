package org.example.app.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.xl.gitclientkmp.presentation.RepositoriesListViewModel
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.units.adapter.UnitsRecyclerViewAdapter
import org.example.app.R
import org.example.app.databinding.RepositoriesListFragmentBinding
import org.example.app.model.PlaceholderState
import org.example.app.utils.collectIn
import org.example.app.utils.toUnitItem
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoriesListFragment : Fragment() {

    private var _binding: RepositoriesListFragmentBinding? = null
    private val binding: RepositoriesListFragmentBinding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val viewModel: RepositoriesListViewModel by viewModel()

    private var unitsAdapter: UnitsRecyclerViewAdapter? = null

    private val divider: DividerItemDecoration by lazy {
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
        setupUI()
        setupNavigationBar()
        setupRecyclerView()
        bindToViewModel()

        viewModel.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        binding.retryButton.setPrimaryStyle()
        binding.retryButton.setButtonClickListener {
            viewModel.onRetryButtonPressed()
        }
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

    private fun setupRecyclerView() = with(receiver = binding.recyclerView) {
        layoutManager = LinearLayoutManager(requireContext())

        unitsAdapter = UnitsRecyclerViewAdapter(lifecycleOwner = viewLifecycleOwner)
        adapter = unitsAdapter

        addItemDecoration(divider)
    }

    private fun bindToViewModel() {
        viewModel.state.collectIn(owner = viewLifecycleOwner, action = ::renderState)
        viewModel.action.collectIn(owner = viewLifecycleOwner, action = ::handleAction)
    }

    private fun renderState(state: RepositoriesListViewModel.State) {
        binding.progressIndicator.isVisible = state is RepositoriesListViewModel.State.Loading
        binding.recyclerView.isVisible = state is RepositoriesListViewModel.State.Loaded

        when (state) {
            is RepositoriesListViewModel.State.Empty -> {
                binding.placeholderView.render(
                    state = PlaceholderState.Empty(
                        title = ContextCompat.getString(
                            requireContext(),
                            R.string.repositories_empty_title
                        ),
                        message = ContextCompat.getString(
                            requireContext(),
                            R.string.repositories_empty_message
                        )
                    )
                )
                binding.retryButton.setTitle(title = getString(R.string.refresh))
            }

            is RepositoriesListViewModel.State.Error -> {
                binding.placeholderView.render(
                    state = PlaceholderState.Error(error = state.error)
                )
                binding.retryButton.setTitle(title = getString(R.string.retry))
            }
            else -> {
                binding.placeholderView.render(PlaceholderState.Hidden)
            }
        }

        binding.placeholderView.isVisible =
            state !is RepositoriesListViewModel.State.Loaded &&
            state !is RepositoriesListViewModel.State.Loading
        binding.retryButton.isVisible =
            state is RepositoriesListViewModel.State.Empty ||
            state is RepositoriesListViewModel.State.Error

        if (state is RepositoriesListViewModel.State.Loaded) {
            val units: List<UnitItem> = state.repositories.map { repo ->
                repo.toUnitItem { clickedRepo ->
                    viewModel.onRepositoryItemPressed(repository = clickedRepo)
                }
            }
            unitsAdapter?.units = units
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
        findNavController().navigate(resId = R.id.action_global_authFragment)
    }

    private fun navigateToDetails(owner: String, repositoryName: String, branch: String) {
        val action: NavDirections = RepositoriesListFragmentDirections
            .actionRepositoriesListFragmentToDetailInfoFragment(
                owner = owner,
                repositoryName = repositoryName,
                branch = branch
            )

        findNavController().navigate(directions = action)
    }
}
