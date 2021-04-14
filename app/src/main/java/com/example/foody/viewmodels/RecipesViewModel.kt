package com.example.foody.viewmodels

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.foody.data.DataStoreRepository
import com.example.foody.util.Constants.Companion.API_KEY
import com.example.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foody.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.foody.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.foody.util.Constants.Companion.QUERY_API_KEY
import com.example.foody.util.Constants.Companion.QUERY_DIET
import com.example.foody.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.foody.util.Constants.Companion.QUERY_NUMBER
import com.example.foody.util.Constants.Companion.QUERY_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// RecipesViewModel share between RecipesBottomSheetFragment class and RecipesFragment

// with this annotation, I have injected DataStoreRepository inside our recipe's viewModel
class RecipesViewModel @ViewModelInject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    // we are going to change these values
    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    val readMealAndDietType = dataStoreRepository.readMealAndDiet

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        // collect data from readMealAndDiet Flow
        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                // get data and save in the variables mealType and dietType
                // get the value from DataStore directly
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }

        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        // *** with this way we are going to get the newest values inside our apply queries
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        // *** with this way we are going to get the newest values inside our apply queries
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    // we are going to save the state of variables when user open select from bottom sheet
    // when for the second time user back to the bottom sheet we want to save the  before search
    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }
}