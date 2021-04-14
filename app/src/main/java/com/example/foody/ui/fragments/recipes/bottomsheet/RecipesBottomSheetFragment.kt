package com.example.foody.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foody.R
import com.example.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foody.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.fragment_recipes_bottom_sheet.view.*
import java.lang.Exception
import java.util.*

// instead of extend this class from Fragment() we use BottomSheetDialogFragment()
class RecipesBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipesViewModel

    // set default values
    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize our RecipesViewModel
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mView = inflater.inflate(R.layout.fragment_recipes_bottom_sheet, container, false)

        /**
         * when user open the bottom sheet
         * we must read from DataStore latest value first
         * So readMealAndDietType is a Flow and should convert to LiveData
         */
        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType

            updateChip(value.selectedMealTypeId,mView.mealType_chipGroup)
            updateChip(value.selectedDietTypeId,mView.dietType_chipGroup)

        })

        // for mealType chipGroup
        mView.mealType_chipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            // this variable will hold our selected chip
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().toLowerCase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId
        }

        // for dietType chipGroup
        mView.dietType_chipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            // this variable will hold our selected chip
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId

        }

        mView.apply_btn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )
            // if in the paranteces of the "actionRecipesBottomSheetFragmentToRecipesFragment" there is not require parameter please rebuild project.
            val action = RecipesBottomSheetFragmentDirections.actionRecipesBottomSheetFragmentToRecipesFragment(true)
            findNavController().navigate(action)
        }

        return mView
    }

     private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
         if(chipId != 0) {
             try {
                 chipGroup.findViewById<Chip>(chipId).isChecked = true
             } catch (e: Exception) {
                 Log.d("RecipesBottomSheet",e.message.toString())
             }
         }
     }


}