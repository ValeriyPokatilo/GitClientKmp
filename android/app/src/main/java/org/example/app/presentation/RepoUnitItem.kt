package org.example.app.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import app.xl.gitclientkmp.domain.entity.Repository
import dev.icerock.moko.units.UnitItem
import org.example.app.R

class RepoUnitItem(
    override val itemId: Long,
    private val repository: Repository,
    private val onClick: (Repository) -> Unit
) : UnitItem {

    @LayoutRes
    private val layoutId: Int = R.layout.repository_item
    override val viewType: Int get() = layoutId

    override fun bindViewHolder(viewHolder: RecyclerView.ViewHolder) {
        viewHolder as ViewHolder

        viewHolder.repositoryName.text = repository.name
        viewHolder.repositoryLanguage.text = repository.language
        viewHolder.repositoryLanguage.setTextColor(
            repository.languageColor ?: R.color.white
        )

        viewHolder.repositoryDescription.apply {
            text = repository.description
            isVisible = !repository.description.isNullOrBlank()
        }

        viewHolder.itemView.setOnClickListener {
            onClick(repository)
        }
    }

    override fun createViewHolder(
        parent: ViewGroup,
        lifecycleOwner: LifecycleOwner
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val repositoryName: TextView = view.findViewById(R.id.repositoryName)
        val repositoryLanguage: TextView = view.findViewById(R.id.repositoryLanguage)
        val repositoryDescription: TextView = view.findViewById(R.id.repositoryDescription)
    }
}
