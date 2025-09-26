package co.netguru.baby.monitor.client.common.view

import androidx.recyclerview.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindView(item: T)
}
