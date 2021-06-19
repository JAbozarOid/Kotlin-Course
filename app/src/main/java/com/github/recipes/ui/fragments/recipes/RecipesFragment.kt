package com.github.recipes.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*

import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.recipes.viewmodels.MainViewModel
import com.github.recipes.R
import com.github.recipes.adapters.RecipesAdapter
import com.github.recipes.databinding.FragmentRecipesBinding
import com.github.recipes.util.NetworkListener
import com.github.recipes.util.NetworkResult
import com.github.recipes.util.observeOnce
import com.github.recipes.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * - RecipesFragment Class depend to MainViewModel class
 * - MainViewModel class depend to Repository class
 * - Repository class depend in two classes, one LocalDataSource(Room) class and second one RemoteDataSource(Retrofit)
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
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {

    // we should bind our MainViewModel with the exact variable  which we have created in fragment_recipes.xml
    // when we have converted our fragment_recipes.xml layout to binding layout the FragmentRecipesBinding is automatically created.
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    /**
     * late init :
     * late init means late initialization.
     * If you do not want to initialize a variable in the constructor instead you want to initialize it later on and if you can guarantee the initialization before using it, then declare that variable with late init keyword.
     * It will not allocate memory until initialized.
     */
    // private lateinit var mView: View //*** after we converted fragment_recipes.xml to binding layout we replace mView with _binding

    /**
     * lazy :
     * It means lazy initialization.
     * Your variable will not be initialized unless you use that variable in your code.
     * It will be initialized only once after that we always use the same value.
     */
    private val mAdapter by lazy { RecipesAdapter() }

    private lateinit var mainViewModel: MainViewModel

    private lateinit var recipesViewModel: RecipesViewModel

    // *** when user press the apply button on bottomSheet and we want to close the dialog and back to RecipesFragment
    // if the RecipesFragmentArgs doesn't exist please rebuild project
    private val args by navArgs<RecipesFragmentArgs>()

    // create a variable for listen to network state
    private lateinit var networkListener: NetworkListener


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
        //mView = inflater.inflate(R.layout.fragment_recipes, container, false)
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        // active menu and override method onCreateOptionMenu below the method setupRecyclerView() for having search menu
        setHasOptionsMenu(true)

        setupRecyclerView()

        // set the latest value from DataStore to backOnline
        recipesViewModel.readBackOnline.observe(viewLifecycleOwner, {
            recipesViewModel.backOnline = it
        })


        // *** check network available or not
        lifecycleScope.launch {
            // collect is a suspend function and must be run in Coroutine
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect { status ->
                Log.d("NetworkListener", status.toString())
                recipesViewModel.networkStatus = status
                recipesViewModel.showNetworkStatus()

                //* whenever we open up our application, we are going to request a new data every time
                //** instead of requesting a new data, every time we want to read our database and only if database is empty then we're going to call this a request API data
                //*** so we remove this "requestApiData" function and read data from our database
                //requestApiData()
                readDatabase()
            }
        }


        // when user click on the floating action button open the bottom sheet
        binding.recipesFab.setOnClickListener {
            // if the internet is available open the bottom sheet
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheetFragment)

            } else {
                // toast a message that there is no internet connection
                recipesViewModel.showNetworkStatus()
            }
        }

        //return mView
        return binding.root
    }

    // we create two function
    // the first one for showing and second one for hiding shimmer effect on our recycler view
    private fun showShimmerEffect() {
        //mView.recyclerview.showShimmer()
        binding.recyclerview.showShimmer()
    }

    private fun hideShimmerEffect() {
        //mView.recyclerview.hideShimmer()
        binding.recyclerview.hideShimmer()
    }

    private fun setupRecyclerView() {
        //mView.recyclerview.adapter = mAdapter
        binding.recyclerview.adapter = mAdapter
        //mView.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    // this is one of the two callbacks of the SearchView.OnQueryTextListener
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchApiData(query)
        }
        return true
    }

    // this is another callback of the SearchView.OnQueryTextListener
    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    // because the "readRecipes" in mainViewModel is not a suspend function we can put observer inside of lifecycle scope
    // *** for call the observer only once, we replace our extension function "observeOnce" with default observe
    private fun readDatabase() {
        // <<< *** this readRecipes.observe replace with below readRecipes.observeOnce
        /*lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { database ->
                // if database is not empty read from our database, it means we have some data in our database
                if (database.isNotEmpty()) {
                    Log.d("RecipesFragment", "readDatabase called!")
                    // we have one row in our database and each time we have new data it just replace with first row
                    // one row means first index[0]
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            })
        }*/
        // this readRecipes.observe replace with below readRecipes.observeOnce *** >>>

        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, { database ->
                // if database is not empty read from our database, it means we have some data in our database
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d("RecipesFragment", "readDatabase called!")
                    // we have one row in our database and each time we have new data it just replace with first row
                    // one row means first index[0]
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            })
        }

    }

    private fun requestApiData() {
        Log.d("RecipesFragment", "requestApiData called!")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
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

    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipe = response.data
                    foodRecipe?.let {
                        mAdapter.setData(it)
                    }
                }
            }

        })
    }

    // when request API data and occurred error instead of showing the empty list, show the previous state of the list
    // because the "readRecipes" in mainViewModel is not a suspend function we can put observer inside of lifecycle scope
    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
            })
        }

    }

    // we are going to avoid the memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}