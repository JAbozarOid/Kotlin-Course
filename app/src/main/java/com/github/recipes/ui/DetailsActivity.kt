package com.github.recipes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.github.recipes.R
import com.github.recipes.adapters.PagerAdapter
//import com.github.recipes.adapters.PagerAdapter
import com.github.recipes.data.database.entities.FavoriteEntity
import com.github.recipes.databinding.ActivityDetailsBinding
import com.github.recipes.databinding.ActivityMainBinding
import com.github.recipes.ui.fragments.ingredinets.IngredientFragment
import com.github.recipes.ui.fragments.instructions.InstructionsFragment
import com.github.recipes.ui.fragments.overview.OverviewFragment
import com.github.recipes.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.activity_details.*
import java.lang.Exception

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    // after migrating from kotlinx.android.synthetic to view binding, first comment kotlinx.android.synthetic import and then create two below variables
    private lateinit var binding: ActivityDetailsBinding

    private val args by navArgs<DetailsActivityArgs>()

    // for inserting favorite recipe inside our main view model
    // this is lazy initialization
    private val mainViewModel: MainViewModel by viewModels()

    private var recipeSaved = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_details)
        setContentView(binding.root)

        //setSupportActionBar(toolbar)
        setSupportActionBar(binding.toolbar)

        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // initialize pager adapter here and add fragments that we want to show in view pager, so we add fragments in specific order
        // before initialize pager adapter we need to create bundle object, so we need to get the values or the data from our safe, which we have passed from our recipes fragment to
        // our detailed activity
        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientFragment())
        fragments.add(InstructionsFragment())

        // add titles for fragments
        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable("recipeBundle", args.result)

        // initialize pager adapter
        /*val pagerAdapter = PagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )*/
        // *** after changing FragmentPagerAdapter the above code is not working, so we add the below codes
        val pagerAdapter = PagerAdapter(
            resultBundle,
            fragments,
            this
        )

        // set the pager adapter to view pager in details activity
        //viewPager.adapter = pagerAdapter
        //binding.viewPager.adapter = pagerAdapter
        // *** after changing FragmentPagerAdapter the above code is not working, so we add the below codes
        binding.viewPager2.apply {
            adapter = pagerAdapter
        }

        // set each fragments to each related fragments
        //tabLayout.setupWithViewPager(viewPager)

        //binding.tabLayout.setupWithViewPager(binding.viewPager2)
        // *** after migrating from viewPager to viewPager2 the above line of code doesn't work, so we add the code below
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)

        // when this screen is loaded we want to check if this recipe is saved or not, if it was saved the color of star icon should be yellow
        val menuItem = menu?.findItem(R.id.save_to_favorite_menu)
        checkSavedRecipes(menuItem!!)
        return true
    }

    // when user click on the back button on the toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        // when user click on the star icon we want to save in favorite_recipes_table
        else if (item.itemId == R.id.save_to_favorite_menu && !recipeSaved) {
            saveToFavorites(item)
        } else if (item.itemId == R.id.save_to_favorite_menu && recipeSaved) {
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this, { favoriteEntity ->
            try {
                for (savedRecipe in favoriteEntity) {
                    if (savedRecipe.result.id == args.result.id) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                    } else {
                        // if ids are not equal which means that recipe is not saved
                        changeMenuItemColor(menuItem, R.color.white)
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        })
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoriteEntity = FavoriteEntity(0, args.result)
        mainViewModel.insertFavoriteRecipes(favoriteEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe Saved.")

        recipeSaved = true
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoriteEntity = FavoriteEntity(
            savedRecipeId,
            args.result
        )
        mainViewModel.deleteFavoriteRecipes(favoriteEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from Favorites")
        recipeSaved = false
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}.show()
    }


}