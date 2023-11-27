import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mobila_applikationer_lab12.ui.viewmodels.Favorite
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class FavouritesRepo(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val FAVORITES_KEY = stringPreferencesKey("favorites")
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

    suspend fun saveFavorites(favorites: List<Favorite>) {
        val jsonString = gson.toJson(favorites)
        dataStore.edit { preferences ->
            preferences[FAVORITES_KEY] = jsonString
        }
    }
}