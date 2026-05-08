package org.example.app.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import app.xl.gitclientkmp.Issue
import app.xl.gitclientkmp.IssueState
import dev.icerock.moko.units.UnitItem
import org.example.app.R
import org.example.app.databinding.IssueItemBinding
import org.example.app.utils.toShortDate

class IssueUnitItem(
    override val itemId: Long,
    private val issue: Issue,
    private val onClick: (Issue) -> Unit
) : UnitItem {

    @LayoutRes
    private val layoutId: Int = R.layout.issue_item
    override val viewType: Int get() = layoutId

    override fun bindViewHolder(viewHolder: RecyclerView.ViewHolder) {
        val holder: ViewHolder = viewHolder as? ViewHolder ?: return
        val context: Context = holder.itemView.context

        holder.issueTitle.text = issue.title
        holder.issueUpdatedAt.text = issue.updatedAt.toShortDate()

        when (issue.state) {
            IssueState.OPEN -> {
                holder.issueStatus.setTextColor(ContextCompat.getColor(context, R.color.light_green))
            }
            IssueState.CLOSED -> {
                holder.issueStatus.setTextColor(ContextCompat.getColor(context, R.color.error))
            }
        }
        holder.issueStatus.text = issue.state.displayText()

        holder.itemView.setOnClickListener {
            onClick(issue)
        }
    }

    override fun createViewHolder(
        parent: ViewGroup,
        lifecycleOwner: LifecycleOwner
    ): RecyclerView.ViewHolder {
        val binding: IssueItemBinding = IssueItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    class ViewHolder(
        val binding: IssueItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val issueTitle: TextView = binding.issueTitle
        val issueStatus: TextView = binding.issueStatus
        val issueUpdatedAt: TextView = binding.issueUpdatedAt
    }
}
