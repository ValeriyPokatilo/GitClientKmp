package org.example.app.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.xl.gitclientkmp.Issue
import app.xl.gitclientkmp.domain.error.ErrorModel
import app.xl.gitclientkmp.presentation.IssuesListViewModel
import dev.icerock.moko.mvvm.ResourceState
import dev.icerock.moko.mvvm.utils.bindNotNull
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.units.adapter.UnitsRecyclerViewAdapter
import org.example.app.R
import org.example.app.databinding.IssuesListFragmentBinding
import org.example.app.model.PlaceholderState
import org.example.app.utils.collectIn
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.apply
import kotlin.collections.map
import kotlin.collections.orEmpty
import kotlin.let

class IssuesListFragment : Fragment() {

    private var _binding: IssuesListFragmentBinding? = null
    private val binding: IssuesListFragmentBinding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val args: IssuesListFragmentArgs by navArgs()

    private val viewModel: IssuesListViewModel by viewModel {
        parametersOf(args.owner, args.repositoryName)
    }

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
        _binding = IssuesListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationBar()
        setupUI()
        setupRecyclerView()
        bindToViewModel()
        bindPaging()

        viewModel.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupNavigationBar() {
        binding.toolbar.title = getString(R.string.issues)

        binding.toolbar.setNavigationIcon(
            R.drawable.ic_back
        )

        binding.toolbar.setNavigationOnClickListener {
            viewModel.onBackButtonPressed()
        }
    }

    private fun setupUI() {
        binding.createIssueButton.setPrimaryStyle()
        binding.createIssueButton.setTitle(title = getString(R.string.new_issue_button_title))
        binding.createIssueButton.setButtonClickListener {
            viewModel.onCreateIssuePressed()
        }
        binding.retryButton.setSecondaryStyle()
        binding.retryButton.setButtonClickListener {
            viewModel.onRetryButtonPressed()
        }
    }

    private fun setupRecyclerView() = with(receiver = binding.recyclerView) {
        layoutManager = LinearLayoutManager(requireContext())

        unitsAdapter = UnitsRecyclerViewAdapter(lifecycleOwner = viewLifecycleOwner)
        adapter = unitsAdapter

        addItemDecoration(divider)
    }

    private fun bindToViewModel() {
        viewModel.state.bindNotNull(lifecycleOwner = this) {
                state: ResourceState<List<IssuesListViewModel.UiItem>, ErrorModel> ->
            renderState(state = state)
        }
        viewModel.action.collectIn(owner = viewLifecycleOwner, action = ::handleAction)
    }

    private fun bindPaging() {
        binding.recyclerView.addOnChildAttachStateChangeListener(
            object : RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewDetachedFromWindow(view: View) = Unit

                override fun onChildViewAttachedToWindow(view: View) {
                    val position: Int = binding.recyclerView.getChildAdapterPosition(view)
                    val count: Int = unitsAdapter?.itemCount ?: 0

                    if (position != count - 1) return

                    viewModel.onReachEnd()
                }
            }
        )
    }

    private fun renderState(
        state: ResourceState<List<IssuesListViewModel.UiItem>, ErrorModel>
    ) {
        val data: List<IssuesListViewModel.UiItem> = state.dataValue().orEmpty()

        val units: List<UnitItem> = data.map { issue ->
            issue.toUnitItem { clickedIssue ->
                viewModel.onIssueItemPressed(clickedIssue)
            }
        }
        unitsAdapter?.units = units

        binding.recyclerView.isVisible = state.isSuccess()
        binding.issuesProgressIndicator.isVisible = state.isLoading()

        binding.placeholderView.isVisible =
            state is ResourceState.Empty || state is ResourceState.Failed

        when (state) {
            is ResourceState.Empty -> {
                binding.placeholderView.render(
                    state = PlaceholderState.Empty(
                        title = getString(
                            requireContext(),
                            R.string.issues_empty_title
                        ),
                        message = getString(
                            requireContext(),
                            R.string.issues_empty_message
                        )
                    )
                )
                binding.retryButton.setTitle(title = getString(R.string.refresh))
            }

            is ResourceState.Failed -> {
                binding.placeholderView.render(
                    state = PlaceholderState.Error(error = state.error)
                )
                binding.retryButton.setTitle(title = getString(R.string.retry))
            }

            else -> {
                binding.placeholderView.render(state = PlaceholderState.Hidden)
            }
        }

        binding.retryButton.isVisible =
            state is ResourceState.Empty || state is ResourceState.Failed

        binding.createIssueButton.isVisible =
            state is ResourceState.Success || state is ResourceState.Empty
    }

    private fun handleAction(action: IssuesListViewModel.Action) {
        when (action) {
            IssuesListViewModel.Action.RouteToBack -> {
                navigateToDetails()
            }

            is IssuesListViewModel.Action.RouteToCreate -> {
                navigateToCreateIssue()
            }

            is IssuesListViewModel.Action.RouteToDetail -> {
                navigateToDetails(
                    owner = action.owner,
                    repositoryName = action.repositoryName,
                    issueNumber = action.issueNumber
                )
            }
        }
    }

    private fun navigateToDetails() {
        findNavController().popBackStack()
    }

    private fun navigateToCreateIssue() {
        val action: NavDirections = IssuesListFragmentDirections
            .actionIssuesListFragmentToCreateIssueFragment(
                owner = args.owner,
                repositoryName = args.repositoryName
            )
        findNavController().navigate(directions = action)
    }

    private fun navigateToDetails(owner: String, repositoryName: String, issueNumber: Int) {
        val action: NavDirections = IssuesListFragmentDirections
            .actionIssuesListFragmentToIssueInfoFragment(
                owner = owner,
                repositoryName = repositoryName,
                issueNumber = issueNumber
            )
        findNavController().navigate(directions = action)
    }

    private fun IssuesListViewModel.UiItem.toUnitItem(
        onClick: (Issue) -> Unit
    ): UnitItem {
        return when (this) {
            is IssuesListViewModel.UiItem.IssueItem -> IssueUnitItem(
                itemId = issue.id,
                issue = issue,
                onClick = onClick
            ) as UnitItem

            IssuesListViewModel.UiItem.LoaderItem -> LoadingUnitItem() as UnitItem
        }
    }
}
