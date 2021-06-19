package com.github.recipes.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.recipes.models.FoodJoke
import com.github.recipes.util.Constants.Companion.FOOD_JOKE_TABLE

@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity(
    // because foodJoke is a string type and we can store it in room database directly
    @Embedded
    var foodJoke: FoodJoke
) {
    // this autoGenerate is equal to false because we have only one row and that row will be update any time we request a new data from api
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}