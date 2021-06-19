package com.github.recipes.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.github.recipes.R
import com.github.recipes.databinding.FragmentOverviewBinding
import com.github.recipes.databinding.FragmentRecipesBottomSheetBinding
import com.github.recipes.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.github.recipes.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.github.recipes.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
//import kotlinx.android.synthetic.main.fragment_recipes_bottom_sheet.view.*
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

    // after migrating from kotlinx.android.synthetic to view binding, first comment kotlinx.android.synthetic import and then create two below variables
    private var _binding: FragmentRecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize our RecipesViewModel
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //val mView = inflater.inflate(R.layout.fragment_recipes_bottom_sheet, container, false)
        _binding = FragmentRecipesBottomSheetBinding.inflate(inflater, container, false)


        /**
         * when user open the bottom sheet
         * we must read from DataStore latest value first
         * So readMealAndDietType is a Flow and should convert to LiveData
         */
        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType

            //updateChip(value.selectedMealTypeId,mView.mealType_chipGroup)
            updateChip(value.selectedMealTypeId,binding.mealTypeChipGroup)

            //updateChip(value.selectedDietTypeId,mView.dietType_chipGroup)
            updateChip(value.selectedDietTypeId,binding.dietTypeChipGroup)

        })

        // for mealType chipGroup
        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            // this variable will hold our selected chip
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId
        }

        // for dietType chipGroup
        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            // this variable will hold our selected chip
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId

        }

       binding.applyBtn.setOnClickListener {
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

        //return mView
        return binding.root
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

    // we are going to avoid the memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}