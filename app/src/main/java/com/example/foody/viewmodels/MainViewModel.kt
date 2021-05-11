package com.example.foody.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.foody.data.Repository
import com.example.foody.data.database.entities.RecipesEntity
import com.example.foody.models.FoodRecipe
import com.example.foody.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

/**
 * inject repository class in view model class
 */
class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
) :
    AndroidViewModel(application) {

    /** ROOM DATABASE */
    // asLiveData cast kotlin flow to LiveData, "asLiveData" is in lifecycle-extensions external library"
    // this readRecipes is not a suspend function
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()

    /**
     * There are two types of variables – mutable and immutable
     * mutable : the value of the mutable variable can be changed.
     * immutable : An immutable variable is one whose value cannot be changed, also known as unchangeable or read-only variable
     */
    /** RETROFIT */
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchedRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    /**
     * Launch Function
     * The launch will not block the main thread,
     * but on the other hand, the execution of the rest part of the code will not wait for the launch result since launch is not a suspend call
     * When to Use Launch?
     * Launch can be used at places where users do not want to use the returned result,
     * which is later used in performing some other work.
     * For example, It can be used at places involving tasks like update or changing a color,
     * as in this case returned information would be of no use.
     */
    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        /**
         * this suspend function must be called inside of a suspend function or a CoroutineScope
         */
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                //*** if response is not null, we want to cache our response into our database
                val foodRecipe = recipesResponse.value!!.data
                if (foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchedRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                searchedRecipesResponse.value = handleFoodRecipesResponse(response)

            } catch (e: Exception) {
                searchedRecipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            searchedRecipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }


    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        // first convert food recipe model to recipe entity and then insert into database
        // whenever we fetch a new data from our API, we're going to cache that data immediately
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    // we use "Dispatchers.IO" because we are running a database work
    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    /**
     * Kotlin operators and symbols
     * 1- !! : asserts that an expression is non-null, force the null with !!
     * 2- -> :
     *      a) separates the parameters and body of a lambda expression
     *      b) separates the parameters and return type declaration in a function type
     *      c) separates the condition and body of a when expression branch
     */
    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipe = response.body()
                return NetworkResult.Success(foodRecipe!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    /**
     * In Kotlin, "when" replaces the switch operator of other languages like Java.
     * A certain block of code needs to be executed when some condition is fulfilled.
     * The argument of when expression compares with all the branches one by one until some match is found.
     * After the first match found, it reaches to end of the when block and execute the code next to when block.
     * Unlike switch case in java or any other programming language, we do not require break statement at the end of each case.
     * In Kotlin, "when" can be used in two ways:
     * 1- when as a statement
     * 2- when as an expression
     */
// create a function for checking internet
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}