package com.github.recipes.models


import com.google.gson.annotations.SerializedName

data class FoodJoke(
    @SerializedName("text")
    val text: String // Do you sell hot dogs? Because you know how to make a wiener stand.
)