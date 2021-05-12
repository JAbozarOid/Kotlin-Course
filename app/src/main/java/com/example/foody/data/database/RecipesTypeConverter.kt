package com.example.foody.data.database

import androidx.room.TypeConverter
import com.example.foody.models.FoodRecipe
import com.example.foody.models.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * we can not store complex objects in our database directly,
 * we need to convert them to acceptable types and then we can store them in a database
 * so we need TypeConverter
 * A TypeConverter convert the recipe object into a json or string when we are "writing" to our database,
 * and convert back from that string or json to recipe object when we are "reading" from our database
 */
class RecipesTypeConverter {

    // in the future we replace this gson to kotlinx serialization
    var gson = Gson()

    // first function convert the recipe to string => writing in database
    // for converting object model to string we need to use gson library => this means serialize our object to json format or string
    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe) : String {
        return gson.toJson(foodRecipe)
    }

    // the second function which convert string back to for the recipe => reading from database
    @TypeConverter
    fun stringToFoodRecipe(data: String) : FoodRecipe {
        val listType = object : TypeToken<FoodRecipe>(){}.type
        return gson.fromJson(data,listType)

    }

    // *** these two below functions are for saving favorite recipe(FavoriteFragment) inside our database
    @TypeConverter
    fun resultToString(result: Result) : String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data: String) : Result {
        val listType = object : TypeToken<Result>(){}.type
        return gson.fromJson(data,listType)

    }
}