package com.example.foody.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    // we define two queries
    // 1- one for inserting data to recipe's entity
    // 2- second one for reading the data from our recipes entity class

    // "OnConflictStrategy.REPLACE" means when we fetch a new data from our API, we want to replace the old one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    // this function reading our recipes from our database table
    /**
     * kotlin flow
     * instead of using live data we can use flow.
     * we use flow in this dao and inside our repository, but when we reach our view model, then we are going to convert this flow to a live data
     * for importing flow use kotlin coroutine
     */
    // Query annotation use for custom queries
    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes() : Flow<List<RecipesEntity>>
}