package dev.gumil.places

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dev.gumil.places.data.PlacesType
import dev.gumil.places.presentation.PlacesViewModel
import dev.gumil.places.presentation.list.PlacesListView

class MainActivity : AppCompatActivity() {

    private val component by lazy {
        ActivityComponentImpl((application as PlacesApp).appComponent)
    }

    private val viewModelFactory by lazy {
        component.viewModelFactory
    }

    private val placesViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[PlacesViewModel::class.java]
    }

    private val container by lazy {
        findViewById<ViewGroup>(R.id.container_main)
    }

    private val placesListView by lazy {
        PlacesListView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        container.addView(placesListView)

        placesListView.loadingListener = {
            when (it) {
                PlacesViewModel.State.Mode.REFRESH -> placesViewModel.refresh(
                    52.3760508,
                    4.8788894,
                    PlacesType.BAR
                )
                PlacesViewModel.State.Mode.LOAD_MORE -> placesViewModel.loadMore(
                    52.3760508,
                    4.8788894,
                    PlacesType.BAR
                )
            }
        }

        placesViewModel.state.observe(this, Observer {
            placesListView.render(it)
        })

        placesViewModel.refresh(52.3760508, 4.8788894, PlacesType.BAR)
    }
}
