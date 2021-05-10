package com.example.foody.util

class Constants {

    companion object {
        const val BASE_URL = "https://api.spoonacular.com"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
        const val API_KEY = "68a4b0c964ba407591ed2f724aadbe66"

        // API Query Keys
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"
        const val QUERY_SEARCH = "query"

        // Room Database
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"

        // Bottom Sheet and Preferences
        const val DEFAULT_RECIPES_NUMBER = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"
        // Create Constants for Relevant DataStore
        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"
        // Name of the Preference that all value will be Stored
        const val PREFERENCES_NAME = "foody_preferences"

        // constant for when the device of the user has internet connection again
        const val PREFERENCES_BACK_ONLINE = "backOnline"

    }
}