package org.example.app.presentation

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.xl.gitclientkmp.presentation.CreateIssueViewModel
import io.noties.markwon.Markwon
import org.example.app.R
import org.example.app.databinding.CreateIssueFragmentBinding
import org.example.app.utils.MarkwonFactory
import org.example.app.utils.bindField
import org.example.app.utils.collectIn
import org.example.app.utils.showErrorAlertDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.InputStream

class CreateIssueFragment : Fragment() {

    private var _binding: CreateIssueFragmentBinding? = null
    private val binding: CreateIssueFragmentBinding
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    private val args: CreateIssueFragmentArgs by navArgs()

    private val viewModel: CreateIssueViewModel by viewModel {
        parametersOf(args.owner, args.repositoryName)
    }

    private val pickImagesLauncher = registerForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        if (uris.isNotEmpty()) {
            val resolver: ContentResolver = requireContext().contentResolver
            val bytesList: List<ByteArray> = uris.map { uri ->
                resolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                uriToBytes(uri)
            }

            viewModel.onFilesSelected(files = bytesList)
        }
    }

    private var markwon: Markwon? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreateIssueFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI(context = view.context)
        setupNavigationBar()
        bindToViewModel()
        bindInputs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI(context: Context) {
        markwon = MarkwonFactory.createMarkwon(context)

        binding.submitButton.text = getString(R.string.submit_new_issue_button_title)

        val titlePlaceholder: String = getString(R.string.title)
        binding.titleInputLayout.hint = titlePlaceholder

        val bodyPlaceholder: String = getString(R.string.description)
        binding.bodyInputEdit.hint = bodyPlaceholder

        binding.submitButton.setOnClickListener {
            viewModel.onSubmitButtonPressed()
        }

        binding.attachFiles.setOnClickListener {
            viewModel.onAttachPressed()
        }

        binding.arrowIcon.setOnClickListener {
            viewModel.onArrowPressed()
        }
    }

    private fun setupNavigationBar() {
        binding.toolbar.title = getString(R.string.new_issue)

        binding.toolbar.setNavigationIcon(
            R.drawable.ic_back
        )

        binding.toolbar.setNavigationOnClickListener {
            viewModel.onBackButtonPressed()
        }
    }

    private fun bindInputs() {
        binding.titleInputLayout.bindField(
            lifecycleOwner = viewLifecycleOwner,
            formField = viewModel.title
        )

        binding.bodyInputLayout.bindField(
            lifecycleOwner = viewLifecycleOwner,
            formField = viewModel.body
        )
    }

    private fun bindToViewModel() {
        viewModel.state.collectIn(owner = viewLifecycleOwner, action = ::renderState)
        viewModel.action.collectIn(owner = viewLifecycleOwner, action = ::handleAction)
    }

    private fun renderState(state: CreateIssueViewModel.State) {
        val isLoading: Boolean = state.isLoading

        binding.submitButton.isEnabled = !isLoading
        binding.submitButton.setTextColor(
            if (isLoading) {
                Color.TRANSPARENT
            } else {
                requireContext().getColor(R.color.white)
            }
        )

        if (isLoading) {
            binding.progressIndicator.show()
        } else {
            binding.progressIndicator.hide()
        }

        renderAttachments(state = state)
    }

    private fun renderAttachments(state: CreateIssueViewModel.State) {
        val isUploading: Boolean = state.totalCount > 0 && state.uploadingCount < state.totalCount

        binding.attachFiles.text = when {
            isUploading -> getString(
                R.string.upload_files,
                state.uploadingCount,
                state.totalCount
            )
            else -> getString(R.string.attach_files)
        }

        binding.attachmentsContainer.isVisible = state.isExpanded
        binding.arrowIcon.rotation =
            if (state.isExpanded) ROTATION_EXPANDED else ROTATION_COLLAPSED
        binding.attachedCounter.text = state.uploadingCount.toString()

        renderUploadedUrls(urls = state.uploadedUrls)
    }

    private fun renderUploadedUrls(urls: List<String>) {
        val container: TextView = binding.attachmentsContainer
        val markdown: String = urls.joinToString(separator = "\n\n") {
            "![image]($it)"
        }

        markwon?.setMarkdown(container, markdown)
    }

    private fun handleAction(action: CreateIssueViewModel.Action) {
        when (action) {
            is CreateIssueViewModel.Action.ShowError -> {
                showErrorAlertDialog(errorModel = action.error)
            }

            CreateIssueViewModel.Action.RouteBack -> {
                navigateBack()
            }

            CreateIssueViewModel.Action.OpenImagePicker -> {
                openImagePicker()
            }
        }
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }

    private fun openImagePicker() {
        pickImagesLauncher.launch(
            input = PickVisualMediaRequest(
                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }

    private fun uriToBytes(uri: Uri): ByteArray {
        val inputStream: InputStream = requireContext().contentResolver.openInputStream(uri)
            ?: error("Cannot open stream")

        return inputStream.use { it.readBytes() }
    }

    private companion object {
        const val ROTATION_EXPANDED = 180f
        const val ROTATION_COLLAPSED = 0f
    }
}
