package com.example.foody.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foody.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// instead of extend this class from Fragment() we use BottomSheetDialogFragment()
class RecipesBottomSheetFragment : BottomSheetDialogFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes_bottom_sheet, container, false)
    }


}