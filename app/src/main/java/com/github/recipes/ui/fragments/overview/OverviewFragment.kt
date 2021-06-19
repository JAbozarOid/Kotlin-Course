package com.github.recipes.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.github.recipes.R
import com.github.recipes.databinding.FragmentInstructionsBinding
import com.github.recipes.databinding.FragmentOverviewBinding
import com.github.recipes.models.Result
import com.github.recipes.util.Constants.Companion.RECIPE_RESULT_KEY
//import kotlinx.android.synthetic.main.fragment_overview.view.*
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {

    // after migrating from kotlinx.android.synthetic to view binding, first comment kotlinx.android.synthetic import and then create two below variables
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_overview, container, false)
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        // get arguments from bundle which bundle hold the result which means recipe
        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        //view.main_imageView.load(myBundle?.image)
        binding.mainImageView.load(myBundle?.image)

        //view.title_textView.text = myBundle?.title
        binding.titleTextView.text = myBundle?.title

        //view.likes_textView.text = myBundle?.aggregateLikes.toString()
        binding.likesTextView.text = myBundle?.aggregateLikes.toString()

        //view.time_textView.text = myBundle?.readyInMinutes.toString()
        binding.timeTextView.text = myBundle?.readyInMinutes.toString()

        // view.summary_textView.text = myBundle?.summary
        //*** instead of using the above line of code use the below code for removing html tags
        myBundle?.summary.let {
            val summary = Jsoup.parse(it).text()
            //view.summary_textView.text = summary
            binding.summaryTextView.text = summary
        }


        if (myBundle?.vegetarian == true) {
            binding.vegetarianImageView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.vegetarianTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }
        if (myBundle?.vegan == true) {
            binding.veganImageView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.veganTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }
        if (myBundle?.glutenFree == true) {
            binding.glutenFreeImageView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.glutenFreeTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }
        if (myBundle?.dairyFree == true) {
            binding.diaryFreeImageView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.diaryFreeTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }
        if (myBundle?.veryHealthy == true) {
            binding.healthyImageView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.healthyTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }
        if (myBundle?.cheap == true) {
            binding.cheapImageView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.cheapTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }

        //return view
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // avoid memory leaks
        _binding = null
    }

}