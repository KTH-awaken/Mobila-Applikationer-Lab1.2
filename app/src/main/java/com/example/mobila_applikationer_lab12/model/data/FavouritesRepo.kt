import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mobila_applikationer_lab12.ui.viewmodels.Favorite
import com.example.mobila_applikationer_lab12.ui.viewmodels.HomeData
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class FavouritesRepo(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val FAVORITES_KEY = stringPreferencesKey("favorites")
        private val HOMEDATA_KEY = stringPreferencesKey("homeData")
        private const val TAG = "FavouritesRepo"
    }

    private val gson = Gson()

    val favorites: Flow<List<Favorite>> = dataStore.data
        .catch { e ->
            if (e is IOException) {
                Log.e(TAG, "Error reading preferences", e)
                emit(emptyPreferences())
            } else {
                throw e
            }
        }
        .map { preferences ->
            val jsonString = preferences[FAVORITES_KEY] ?: return@map emptyList()
            gson.fromJson(jsonString, object : TypeToken<List<Favorite>>() {}.type)
        }

    val homeData: Flow<HomeData> = dataStore.data
        .catch { e ->
            if (e is IOException) {
                Log.e(TAG, "Error reading preferences", e)
                emit(emptyPreferences())
            } else {
                throw e
            }
        }
        .map { preferences ->
            val jsonString = preferences[HOMEDATA_KEY]
            if (jsonString != null) {
                gson.fromJson(jsonString, HomeData::class.java)
            } else {
                // Return a default or empty HomeData
                HomeData(weekly = emptyList(), hourly = emptyList())
            }
        }


    suspend fun saveFavorites(favorites: List<Favorite>) {
        val jsonString = gson.toJson(favorites)
        dataStore.edit { preferences ->
            preferences[FAVORITES_KEY] = jsonString
        }
    }

    suspend fun saveHomeData(homeData: HomeData) {
        val jsonString = gson.toJson(homeData)
        dataStore.edit { preferences ->
            preferences[HOMEDATA_KEY] = jsonString
        }
    }
}
