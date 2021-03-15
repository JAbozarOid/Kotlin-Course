package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.databinding.RecipesRowLayoutBinding
import com.example.foody.models.FoodRecipe
import com.example.foody.models.Result
import com.example.foody.util.RecipesDiffUtil

/**
 * this class must extend from RecyclerView.Adapter
 */
class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipes = emptyList<Result>()

    // we must pass binding of our view to this class, RecipesRowLayoutBinding is the automatic generating class
    class MyViewHolder(private val binding: RecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {
            // binding.result means the "result" variable that created in recipes_row_layout.xml
            binding.result = result

            /**
             * executePendingBindings() method
             * this function will basically update out layout whenever there is a change inside our data
             * this must be run on UI thread
             */
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // in this variable we're going to store a current item from our recycler view
        // we are using this position parameter to get dynamically the position of our row
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    // use this function for set data in here as a parameter
    /**
     * to call this function from our recipes fragment and we pass to a new data to this function every time
     * store that new data inside recipe empty list
     */
    fun setData(newData: FoodRecipe) {

        // we use diffutil instead of notifyDataSetChanged()
        val recipesDiffUtil = RecipesDiffUtil(recipes, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData.results
        diffUtilResult.dispatchUpdatesTo(this)

        // we need to tell recycler view to update the values when receive a new data
        // this notifyDataSetChanged() method updating the whole list all over again without even checking if our new list of the recipes actually contains some recipes from our shor list
        /**
         * For this problem we use DiffUtil
         * this library compare all the list of the recipes, and it will update only those recipes or views which are new
         * this is a huge bonous for application performance
         */
        //notifyDataSetChanged()
    }
}