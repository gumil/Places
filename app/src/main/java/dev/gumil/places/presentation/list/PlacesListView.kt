package dev.gumil.places.presentation.list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.gumil.places.R
import dev.gumil.places.domain.Place
import dev.gumil.places.presentation.PlacesViewModel

internal class PlacesListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    var isRefreshing
        get() = swipeRefreshLayout.isRefreshing
        set(value) {
            swipeRefreshLayout.isRefreshing = value
            loadingListener(PlacesViewModel.State.Mode.REFRESH)
        }

    var loadingListener: (mode: PlacesViewModel.State.Mode) -> Unit = {}

    private val swipeRefreshLayout by lazy {
        findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
    }

    private val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView)
    }

    private val emptyView by lazy {
        findViewById<View>(R.id.text_empty_places)
    }

    private val adapter = ItemAdapter(PlaceViewItem()).apply {
        footerItem = FooterViewItem()
    }

    private var isLoading = true

    init {
        View.inflate(context, R.layout.view_places_list, this)

        initializeList()
    }

    fun render(state: PlacesViewModel.State) {
        showList(state.list)

        when (state.loadingMode) {
            PlacesViewModel.State.Mode.REFRESH -> {
                adapter.list = state.list
                swipeRefreshLayout.isRefreshing = false
            }
            PlacesViewModel.State.Mode.LOAD_MORE -> TODO()
        }
    }

    private fun initializeList() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && !isLoading) {
                    val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                    val visibleItemCount = recyclerView.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItem = layoutManager.findFirstVisibleItemPositions(null).first()

                    totalItemCount - visibleItemCount <= firstVisibleItem + VISIBLE_THRESHOLD
                    loadingListener(PlacesViewModel.State.Mode.LOAD_MORE)
                    isLoading = true
                }
            }
        })

        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            loadingListener(PlacesViewModel.State.Mode.REFRESH)
        }
    }

    private fun showList(list: List<Place>) {
        swipeRefreshLayout.isVisible = list.isNotEmpty()
        emptyView.isVisible = list.isEmpty()
    }

    companion object {
        private const val VISIBLE_THRESHOLD = 2
    }
}

