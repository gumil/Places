package dev.gumil.places

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import dev.gumil.places.data.PlacesType
import dev.gumil.places.presentation.LocationObserver
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

    private val textPermissionNotGranted by lazy {
        findViewById<TextView>(R.id.text_permission_not_granted)
    }

    private val placesListView by lazy {
        PlacesListView(this)
    }

    private val snackbar by lazy {
        Snackbar.make(container, getString(R.string.error_message), Snackbar.LENGTH_SHORT).apply {
            view.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.closed))
        }
    }

    private val permissionsHelper by lazy {
        PermissionsHelper(this, {
            loadLocation(false)
        }, {
            loadLocation(true)
        })
    }

    private val prefs by lazy {
        getPreferences(Context.MODE_PRIVATE)
    }

    private lateinit var location: Location

    private var selectedType = PlacesType.CAFE
        set(value) {
            field = value
            placesListView.refresh()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.menu_cafe)

        location = Location("initial").apply {
            latitude = prefs.getFloat(PREF_LAT, DEFAULT_LATITUDE).toDouble()
            longitude = prefs.getFloat(PREF_LNG, DEFAULT_LONGITUDE).toDouble()
        }

        initializeViews()

        initializeViewModel()

        permissionsHelper.checkPermissions()

        lifecycle.addObserver(LocationObserver(this) {
            location = it
            placesListView.refresh()
            prefs.edit()
                .putFloat(PREF_LAT, it.latitude.toFloat())
                .putFloat(PREF_LNG, it.longitude.toFloat())
                .apply()
        })
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsHelper.onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun loadLocation(useDefault: Boolean) {
        textPermissionNotGranted.isVisible = useDefault
        if (useDefault || !isGpsEnabled()) {
            placesListView.refresh()
        }
    }

    private fun initializeViews() {
        container.addView(placesListView)

        placesListView.loadingListener = {
            when (it) {
                PlacesViewModel.State.Mode.REFRESH -> placesViewModel.refresh(
                    location.latitude,
                    location.longitude,
                    selectedType
                )
                PlacesViewModel.State.Mode.LOAD_MORE -> placesViewModel.loadMore(
                    location.latitude,
                    location.longitude,
                    selectedType
                )
            }
        }

        textPermissionNotGranted.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionsHelper.requestPermission()
            }
        }
    }

    private fun initializeViewModel() {
        placesViewModel.onError = {
            snackbar.show()
        }

        placesViewModel.state.observe(this, Observer {
            placesListView.render(it)
        })
    }

    private fun isGpsEnabled(): Boolean {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    companion object {
        private const val PREF_LAT = "latitude"
        private const val PREF_LNG = "longitude"

        private const val DEFAULT_LATITUDE = 52.3760508f
        private const val DEFAULT_LONGITUDE = 4.8788894f
    }
}
