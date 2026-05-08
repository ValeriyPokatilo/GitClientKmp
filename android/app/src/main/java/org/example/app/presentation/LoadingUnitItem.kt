package org.example.app.presentation

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.units.viewbinding.VBTableUnitItem
import dev.icerock.moko.units.viewbinding.VBViewHolder
import org.example.app.R
import org.example.app.databinding.LoadingUnitBinding

class LoadingUnitItem : VBTableUnitItem<LoadingUnitBinding>() {
    override val layoutId: Int = R.layout.loading_unit

    override fun bindView(view: View): LoadingUnitBinding = LoadingUnitBinding.bind(view)

    override fun LoadingUnitBinding.bindData(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        viewHolder: VBViewHolder<LoadingUnitBinding>
    ) = Unit

    override val itemId: Long = TableUnitItem.NO_ID
}
