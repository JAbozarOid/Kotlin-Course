package com.github.recipes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.recipes.data.database.entities.FavoriteEntity
import com.github.recipes.data.database.entities.FoodJokeEntity
import com.github.recipes.data.database.entities.RecipesEntity

/**
 * exportSchema means to export the database schema into a folder, it's not mandatory
 * it's a good practice to have a history of your schema in your code base and you should commit the schema files into your control system
 * when it's set to false it is disabled
 * After adding FavoriteEntity::class to entities you must change the version to 2 but for now we uninstall the app
 */
@Database(
    entities = [RecipesEntity::class,FavoriteEntity::class,FoodJokeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipesDao() : RecipesDao


}