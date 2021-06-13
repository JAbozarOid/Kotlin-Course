package com.example.foody.data.database

import androidx.room.*
import com.example.foody.data.database.entities.FavoriteEntity
import com.example.foody.data.database.entities.FoodJokeEntity
import com.example.foody.data.database.entities.RecipesEntity
import com.example.foody.models.FoodJoke
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

    // *** These below functions are for inserting,reading and updating favorite recipes inside our favorite_recipes_table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM favorite_recipes_table ORDER BY id ASC")
    fun readFavoriteRecipes() : Flow<List<FavoriteEntity>>

    @Delete
    suspend fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity)

    //delete all
    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavoriteRecipes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("SELECT * FROM food_joke_table ORDER BY id ASC")
    fun readFoodJoke() : Flow<List<FoodJokeEntity>>

}