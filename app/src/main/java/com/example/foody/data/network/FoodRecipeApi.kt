package com.example.foody.data.network

import com.example.foody.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodRecipeApi {

    @GET("/recipes/complexSearch")
    // this function will return FoodRecipe model
    // we use suspend keyword, because our function will use kotlin coroutine and this function will run on a background thread instead of a main thread, so that's a huge bonus
    suspend fun getRecipes(
        // @QueryMap retrofit annotation will let us create something like a hash map to add all our queries at once
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipe>
}