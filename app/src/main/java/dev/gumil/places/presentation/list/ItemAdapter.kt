package dev.gumil.places.presentation.list

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

internal class ItemAdapter<M>(
    private val defaultItem: ViewItem<M>,
    private val prefetchDistance: Int = 2
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var footerItem: ViewItem<*>? = null

    private var _footerItem: ViewItem<*>? = null

    var list: List<M>
        get() = _list
        set(value) {
            _list = value.toMutableList()
            currentListSize = 0
            notifyDataSetChanged()
        }

    private var _list: MutableList<M> = mutableListOf()

    private var currentListSize = 0

    var onPrefetch: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        object : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                viewType,
                parent,
                false
            )
        ) {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < _list.size) {
            defaultItem.bind(holder.itemView, _list[position])
        }

        if (_list.size > currentListSize && position == (_list.size - prefetchDistance)) {
            currentListSize = _list.size
            Handler().post {
                onPrefetch?.invoke()
            }
        }
    }

    override fun getItemCount(): Int = _list.size + (_footerItem?.let { 1 } ?: 0)

    override fun getItemViewType(position: Int): Int {
        return if (position == _list.size) {
            _footerItem?.layout ?: 0
        } else {
            defaultItem.layout
        }
    }

    fun showFooter() {
        _footerItem = footerItem
        notifyItemInserted(currentListSize + 1)
    }

    fun addItems(items: List<M>) {
        _footerItem = null
        _list.addAll(items)
        notifyItemChanged(currentListSize)
        notifyItemRangeInserted(currentListSize + 1, currentListSize + items.size)
    }
}
