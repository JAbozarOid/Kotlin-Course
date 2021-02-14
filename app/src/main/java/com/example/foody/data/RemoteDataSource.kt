package com.example.foody.data

import com.example.foody.data.network.FoodRecipeApi
import com.example.foody.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

/**
 * - this class wil basically request the data from our API,
 * and before that, we're going to inject this for the Recipes API service(interface) inside our remote data source
 * - we can actually request some new data from this remote data source class
 */
class RemoteDataSource @Inject constructor(
    private val foodRecipeApi: FoodRecipeApi
) {
    // we use suspend keyword because getRecipes function is a suspend function
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipeApi.getRecipes(queries)
    }
}