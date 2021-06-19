package com.github.recipes.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.recipes.models.FoodRecipe
import com.github.recipes.util.Constants.Companion.RECIPES_TABLE

/**
 * this entity class or our table will contain only one row offer for the recipe
 */
@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(var foodRecipe: FoodRecipe) {

    // autoGenerate = false means when a new data fetch from our API, we are going to replace all the data from our database table with new data
    // we fetch a new data it means when our app reads our database, it will fetch the newest data which we have in our database
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}