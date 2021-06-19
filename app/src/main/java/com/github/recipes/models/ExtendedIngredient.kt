package com.github.recipes.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
//import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.Parcelize


// after migrating from kotlinx.android.synthetic to view binding, @Parcelize doesn't work so you must add  id 'kotlin-parcelize' plugin in gradle file
@Parcelize
data class ExtendedIngredient(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("consistency")
    val consistency: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("original")
    val original: String,
    @SerializedName("unit")
    val unit: String
) : Parcelable
