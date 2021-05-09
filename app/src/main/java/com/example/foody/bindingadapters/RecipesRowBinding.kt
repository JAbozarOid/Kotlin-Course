package com.example.foody.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.foody.R
import com.example.foody.models.Result
import com.example.foody.ui.fragments.recipes.RecipesFragmentDirections
import org.jsoup.Jsoup
import java.lang.Exception

class RecipesRowBinding {

    /**
     * we can access the functions inside this companion object elsewhere
     */
    companion object {
        // this function is used for converting integer to string
        // likes and clocks in recipes_row_layout.xml are integer in a textview
        /**
         * the value in the parameter of BindingAdapter is used as a property in xml in heart_textView
         */
        @BindingAdapter("setNumberOfLikes")
        /**
         * @JvmStatic :
         * with this annotation we tell to the compiler to make our function static
         * we can actually access this function elsewhere in our project
         */
        @JvmStatic
        fun setNumberOfLikes(textView: TextView, likes: Int) {
            textView.text = likes.toString()
        }

        @BindingAdapter("setNumberOfMinutes")
        @JvmStatic
        fun setNumberOfMinutes(textView: TextView, minutes: Int) {
            textView.text = minutes.toString()
        }

        // for leaf, we must change both imageView and textView
        // in this function we use View, because we must change both imageView and textView
        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, vegan: Boolean) {
            if (vegan) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }

                    is ImageView -> {
                        view.setColorFilter(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        //when user click in one of the rows in the recipes fragment, we want to pass data to Details Activity
        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeClickListener(recipeRowLayout: ConstraintLayout, result: Result) {
            // if we don't see the action of "actionRecipesFragmentToDetailsActivity" we must rebuild our project
            recipeRowLayout.setOnClickListener {
                try {
                    val action =
                        RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
                    recipeRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("onRecipeClickListener", e.toString())
                }
            }
        }

        // create a function for parsing html tags
        // in recipes row layout and details activity the descriptions has html tags
        // for solving this problem we must use Jsoup Library, which it is a Java Html Parser
        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView, description: String?) {
            if (description != null) {
                // with this line of code we parse html tags
                val desc = Jsoup.parse(description).text()
                textView.text = desc
            }
        }
    }


}