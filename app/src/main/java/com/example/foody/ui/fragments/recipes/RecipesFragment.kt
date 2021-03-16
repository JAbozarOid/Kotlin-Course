package com.example.foody.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.viewmodels.MainViewModel
import com.example.foody.R
import com.example.foody.adapters.RecipesAdapter
import com.example.foody.util.Constants.Companion.API_KEY
import com.example.foody.util.NetworkResult
import com.example.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*

/**
 * - RecipesFragment Class depend to MainViewModel class
 * - MainViewModel class depend to Repository class
 * - Repository class depend in two classes, one LocalDataSource(Room) class and second one RemoteDataSurce(Retrofit)
 * - there is two different cases in which we are going to use a remote data source
 * 1- Database is empty
 * 2- Explicitly Request new data from our particular API
 * - our application will always request the data from our local database and display that in recyclerview and only what we need
 * - we are going to request the data from our remote data source,
 * that way we are going to save a lot of bandwidth because we're not going to request data all the time from our API
 * - Instead, we are going to use the offline cache to store new data from our API to our local database, and then after that, always retrieve that data from a local database,
 * and only when we need we're going to request a new data from our API
 */

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    /**
     * lateinit :
     * lateinit means late initialization.
     * If you do not want to initialize a variable in the constructor instead you want to initialize it later on and if you can guarantee the initialization before using it, then declare that variable with lateinit keyword.
     * It will not allocate memory until initialized.
     */
    private lateinit var mView: View

    /**
     * lazy :
     * It means lazy initialization.
     * Your variable will not be initialized unless you use that variable in your code.
     * It will be initialized only once after that we always use the same value.
     */
    private val mAdapter by lazy { RecipesAdapter() }

    private lateinit var mainViewModel: MainViewModel

    private lateinit var recipesViewModel: RecipesViewModel

    // this method is called before on create view
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_recipes, container, false)

        setupRecyclerView()

        requestApiData()

        return mView
    }

    // we create two function
    // the first one for showing and second one for hiding shimmer effect on our recycler view
    private fun showShimmerEffect() {
        mView.recyclerview.showShimmer()
    }

    private fun hideShimmerEffect() {
        mView.recyclerview.hideShimmer()
    }

    private fun setupRecyclerView() {
        mView.recyclerview.adapter = mAdapter
        mView.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
                /**
                 * let func
                 * takes the object it is invoked upon as the parameter and returns the result of the lambda expression.
                 * Kotlin let is a scoping function wherein the variables declared inside the expression cannot be used outside.
                 * it keyword contains the copy of the property inside let.
                 */
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
            }

        })
    }



}