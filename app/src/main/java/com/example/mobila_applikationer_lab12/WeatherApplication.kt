package com.example.mobila_applikationer_lab12

import FavouritesRepo
import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private val FAVORITES_PREFERENCES_NAME = "favorites_preferences"

val Context.favoritesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = FAVORITES_PREFERENCES_NAME
)



class WeatherApplication : Application() {
    lateinit var favouritesRepo: FavouritesRepo
        private set
    override fun onCreate() {
        super.onCreate()
        favouritesRepo = FavouritesRepo(applicationContext.favoritesDataStore)
    }
}
