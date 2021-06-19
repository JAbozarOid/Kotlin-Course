package com.github.recipes.data

import com.github.recipes.data.network.FoodRecipeApi
import com.github.recipes.models.FoodJoke
import com.github.recipes.models.FoodRecipe
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

    // search recipe API
    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe> {
        return foodRecipeApi.searchRecipes(searchQuery)
    }

    suspend fun getFoodJoke(apiKey: String) : Response<FoodJoke> {
        return foodRecipeApi.getFoodJoke(apiKey)
    }
}