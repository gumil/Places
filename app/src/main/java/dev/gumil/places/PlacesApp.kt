package dev.gumil.places

import android.app.Application

internal class PlacesApp : Application() {

    val appComponent: AppComponent by lazy {
        AppComponentImpl(this)
    }
}
