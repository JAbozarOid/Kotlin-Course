package com.github.recipes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.github.recipes.R
import com.github.recipes.databinding.IngredientsRowLayoutBinding
import com.github.recipes.models.ExtendedIngredient
import com.github.recipes.util.Constants.Companion.BASE_IMAGE_URL
import com.github.recipes.util.RecipesDiffUtil
import java.util.*

//import kotlinx.android.synthetic.main.ingredients_row_layout.view.*

/**
 * this class must extend from RecyclerView.Adapter
 */
class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()


    //class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class MyViewHolder(val binding: IngredientsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        /*return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ingredients_row_layout, parent, false)
        )*/

        return MyViewHolder(
            IngredientsRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.ingredientsImageView.load(BASE_IMAGE_URL + ingredientsList[position].image) {
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        //holder.itemView.ingredient_name.text = ingredientsList[position].name.capitalize()
        holder.binding.ingredientName.text = ingredientsList[position].name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }

        //holder.itemView.ingredient_amount.text = ingredientsList[position].amount.toString()
        holder.binding.ingredientAmount.text = ingredientsList[position].amount.toString()

        //holder.itemView.ingredient_unit.text = ingredientsList[position].unit
        holder.binding.ingredientUnit.text = ingredientsList[position].unit

        //holder.itemView.ingredient_consistency.text = ingredientsList[position].consistency
        holder.binding.ingredientConsistency.text = ingredientsList[position].consistency

        //holder.itemView.ingredient_original.text = ingredientsList[position].original
        holder.binding.ingredientOriginal.text = ingredientsList[position].original

    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(newIngredients: List<ExtendedIngredient>) {

        val ingredientsDiffUtil = RecipesDiffUtil(ingredientsList, newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)

    }
}

