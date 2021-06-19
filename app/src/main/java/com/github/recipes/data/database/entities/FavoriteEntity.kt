package com.github.recipes.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.recipes.models.Result
import com.github.recipes.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoriteEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)