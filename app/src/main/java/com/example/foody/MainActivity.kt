package com.example.foody

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * we can implement navController with two ways
         * 1- if you use fragment tag in xml you must implement the way 1
         * 2- if you use androidx.fragment.app.FragmentContainerView tag in xml you must implement the way 2
         * in this project I implemented the way 1
         */

        // way 1 - when using "fragment" tag in xml
        navController = findNavController(R.id.navHostFragment)

        // way 2 - when using "androidx.fragment.app.FragmentContainerView tag in xml"
        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        //navController = navHostFragment.navController

        // AppBarConfiguration actually pass our destination
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.recipesFragment,
                R.id.favoriteRecipesFragment,
                R.id.foodJokeFragment
            )
        )

        // bottomNavigationView is the navigation that access this view from our activity
        bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    // for navigating app
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}