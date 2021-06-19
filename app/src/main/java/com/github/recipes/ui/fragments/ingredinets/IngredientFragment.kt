package com.github.recipes.ui.fragments.ingredinets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.recipes.R
import com.github.recipes.adapters.IngredientsAdapter
import com.github.recipes.databinding.FragmentIngredientBinding
import com.github.recipes.models.Result
import com.github.recipes.util.Constants.Companion.RECIPE_RESULT_KEY

//import kotlinx.android.synthetic.main.fragment_ingredient.view.*

class IngredientFragment : Fragment() {

    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    // after migrating from kotlinx.android.synthetic to view binding, first comment kotlinx.android.synthetic import and then create two below variables
    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_ingredient, container, false)
        _binding = FragmentIngredientBinding.inflate(inflater, container, false)

        // get arguments from bundle which bundle hold the result which means recipe
        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        //setupRecyclerView(view)
        setupRecyclerView()

        myBundle?.extendedIngredients?.let { mAdapter.setData(it) }

        //return view
        return binding.root
    }

    // after migrating from KotlinX to view binding remove (view: View) from function parameter
    private fun setupRecyclerView() {
        //view.ingredients_recyclerview.adapter = mAdapter
        binding.ingredientsRecyclerview.adapter = mAdapter

        //view.ingredients_recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.ingredientsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // avoid memory leaks
        _binding = null
    }
}