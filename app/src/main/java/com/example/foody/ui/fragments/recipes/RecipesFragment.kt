package com.example.foody.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foody.R
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
class RecipesFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipes, container, false)

        view.recyclerview.showShimmer()

        return view
    }


}