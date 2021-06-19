package com.github.recipes.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.github.recipes.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.github.recipes.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.github.recipes.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.github.recipes.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.github.recipes.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.github.recipes.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.github.recipes.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.github.recipes.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

// we use this annotation because later we are going to use this DataStoreRepository inside RecipesViewModel
@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    /**
     * DataStore
     * The main difference between DataStore and Share Preferences is that DataStore is running on background thread and not on main thread
     * In DataStore we need to define our preferences keys
     */
    // with this DataStore we are going to save the selected items when user select in bottom sheet
    // when user open the bottom sheet again, we want to show the user the before state
    // we are going to save the name of the chip and id of that chip
    private object PreferenceKeys {
        // *** for meal type we are going to store two values
        val selectedMealType = preferencesKey<String>(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = preferencesKey<Int>(PREFERENCES_MEAL_TYPE_ID)

        // *** for diet type we are going to store two values
        val selectedDietType = preferencesKey<String>(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = preferencesKey<Int>(PREFERENCES_DIET_TYPE_ID)

        // if the device of the user has internet connection back again after it has no internet connection in the previous state
        val backOnline = preferencesKey<Boolean>(PREFERENCES_BACK_ONLINE)
    }

    // all data store under this name
    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCES_NAME
    )

    // function for saving key value pairs
    // edit function is a kotlin coroutine and run in background thread so we need suspend function
    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        dataStore.edit { preferences ->
            // key = value
            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }


    /**
     * Kotlin operators and symbols
     * ?: this operator means that if "preferences[PreferenceKeys.selectedMealType]" has a value put inside selectedMealType and if it doesn't value put DEFAULT_MEAL_TYPE inside
     */
    val readMealAndDiet: Flow<MealAndDietType> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }
        .map { preferences ->
            // if no value save in these values put the value inside it or if has values use it
            val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0
            MealAndDietType(
                // the value of the above values map to MealAndDietType data class
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    // create a function for saving the state of the internet connection when it's online
    suspend fun saveBackOnline(backOnline: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.backOnline] = backOnline
        }
    }

    // read the last state of the internet connection online
    val readBackOnline: Flow<Boolean> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }
        .map { preferences ->
            val backOnline = preferences[PreferenceKeys.backOnline] ?: false
            backOnline
        }
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)

