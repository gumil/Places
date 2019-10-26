package dev.gumil.places

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
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

    private val snackbar by lazy {
        Snackbar.make(container, getString(R.string.error_message), Snackbar.LENGTH_SHORT).apply {
            view.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.closed))
        }
    }

    private var selectedType = PlacesType.CAFE
        set(value) {
            field = value
            placesListView.isRefreshing = true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.menu_cafe)

        container.addView(placesListView)

        placesViewModel.onError = {
            snackbar.show()
        }

        placesListView.loadingListener = {
            when (it) {
                PlacesViewModel.State.Mode.REFRESH -> placesViewModel.refresh(
                    52.3760508,
                    4.8788894,
                    selectedType
                )
                PlacesViewModel.State.Mode.LOAD_MORE -> placesViewModel.loadMore(
                    52.3760508,
                    4.8788894,
                    selectedType
                )
            }
        }

        placesViewModel.state.observe(this, Observer {
            placesListView.render(it)
        })

        placesListView.isRefreshing = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_places, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        selectedType = when (item.itemId) {
            R.id.menu_cafe -> {
                PlacesType.CAFE
            }
            R.id.menu_bar -> {
                PlacesType.BAR
            }
            R.id.menu_restaurant -> {
                PlacesType.RESTAURANT
            }
            else -> return false
        }

        title = item.title
        return true
    }
}
