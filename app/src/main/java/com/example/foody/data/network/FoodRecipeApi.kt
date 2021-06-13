package com.example.foody.data.network

import com.example.foody.models.FoodJoke
import com.example.foody.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipeApi {

    @GET("/recipes/complexSearch")
    // this function will return FoodRecipe model
    // we use suspend keyword, because our function will use kotlin coroutine and this function will run on a background thread instead of a main thread, so that's a huge bonus
    suspend fun getRecipes(
        // @QueryMap retrofit annotation will let us create something like a hash map to add all our queries at once
        // @QueryMap let us to pass multiple queries inside our get request
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipe>

    @GET("/recipes/complexSearch")
    // GET request for searching API
    suspend fun searchRecipes(
        @QueryMap searchQuery: Map<String, String>
    ): Response<FoodRecipe>

    @GET("/food/jokes/random")
    suspend fun getFoodJoke(
        @Query("apiKey") apiKey: String
    ) : Response<FoodJoke>
}