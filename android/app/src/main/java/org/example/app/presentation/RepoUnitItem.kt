package org.example.app.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import app.xl.gitclientkmp.Repository
import dev.icerock.moko.units.UnitItem
import org.example.app.R
import org.example.app.databinding.RepositoryItemBinding

class RepoUnitItem(
    override val itemId: Long,
    private val repository: Repository,
    private val onClick: (Repository) -> Unit
) : UnitItem {

    @LayoutRes
    private val layoutId: Int = R.layout.repository_item
    override val viewType: Int get() = layoutId

    override fun bindViewHolder(viewHolder: RecyclerView.ViewHolder) {
        val holder: ViewHolder = viewHolder as? ViewHolder ?: return
        val context: Context = holder.itemView.context

        holder.repositoryName.text = repository.name
        holder.repositoryLanguage.text = repository.language.orEmpty()

        val color: Int = repository.languageColor
            ?: ContextCompat.getColor(context, R.color.white)
        holder.repositoryLanguage.setTextColor(color)

        holder.repositoryDescription.apply {
            text = repository.descriptionText
            isVisible = !repository.descriptionText.isNullOrBlank()
        }

        holder.itemView.setOnClickListener {
            onClick(repository)
        }
    }

    override fun createViewHolder(
        parent: ViewGroup,
        lifecycleOwner: LifecycleOwner
    ): RecyclerView.ViewHolder {
        val binding: RepositoryItemBinding = RepositoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    class ViewHolder(
        val binding: RepositoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val repositoryName: TextView = binding.repositoryName
        val repositoryLanguage: TextView = binding.repositoryLanguage
        val repositoryDescription: TextView = binding.repositoryDescription
    }
}
