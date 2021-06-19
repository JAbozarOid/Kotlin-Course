package com.github.recipes.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * This class is used for handling viewpager in DetailsActivity class
 * This class has 4 parameters
 * 1- Bundle : is used to parse data from our details activity to our fragments
 * BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT : this indicates that only the current fragment will be in the resume state and all other fragments are kept it started
 */
class PagerAdapter(
    private val resultBundle: Bundle,
    private val fragments: ArrayList<Fragment>,
    private val title: ArrayList<String>,
    private val fm: FragmentManager
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        // it return 3, because we add three fragments
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        // to pass all the result =from our recipe to those fragments
        fragments[position].arguments = resultBundle
        return fragments[position]
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}