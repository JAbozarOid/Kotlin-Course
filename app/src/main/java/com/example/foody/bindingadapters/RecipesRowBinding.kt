package com.example.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.example.foody.R

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
            }
        }
    }
}