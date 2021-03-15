package com.example.foody.util

import androidx.recyclerview.widget.DiffUtil
import com.example.foody.models.Result

/**
 * this way of implementation of DiffUtil run in main thread
 * for implementation that run on the background thread see AsyncListDiffer
 */
class RecipesDiffUtil(private val oldList: List<Result>, private val newList: List<Result>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        /**a
         * === operator means referential equality operators
         * Referential equality :
         * Referential equality is checked by the === operation and its negated counterpart !==. a === b evaluates to true if and only if a and b point to the same object.
         * For values represented by primitive types at runtime (for example, Int ), the === equality check is equivalent to the == check.
         */
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}