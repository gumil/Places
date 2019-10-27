package dev.gumil.places.presentation.list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.gumil.places.R
import dev.gumil.places.domain.Place
import dev.gumil.places.presentation.PlacesViewModel

internal class PlacesListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    var loadingListener: (mode: PlacesViewModel.State.Mode) -> Unit = {}

    private val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView)
    }

    private val adapter = ItemAdapter(PlaceViewItem()).apply {
        footerItem = FooterViewItem()
        onPrefetch = {
            showFooter()
            loadingListener(PlacesViewModel.State.Mode.LOAD_MORE)
        }
    }

    private var isLoading = true

    init {
        View.inflate(context, R.layout.view_places_list, this)

        initializeList()
    }

    fun render(state: PlacesViewModel.State) {
        when (state.loadingMode) {
            PlacesViewModel.State.Mode.REFRESH -> {
                adapter.list = state.list
                isRefreshing = false
                isLoading = false
                showList(state.list)
            }
            PlacesViewModel.State.Mode.LOAD_MORE -> {
                adapter.addItems(state.list)
                isLoading = false
            }
        }
    }

    fun refresh() {
        isRefreshing = true
        loadingListener(PlacesViewModel.State.Mode.REFRESH)
    }

    private fun initializeList() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        setOnRefreshListener {
            loadingListener(PlacesViewModel.State.Mode.REFRESH)
        }
        isRefreshing = true
    }

    private fun showList(list: List<Place>) {
        if (list.isEmpty()) {
            recyclerView.adapter = SingleItemAdapter()
            return
        }
        recyclerView.adapter = adapter
    }
}
