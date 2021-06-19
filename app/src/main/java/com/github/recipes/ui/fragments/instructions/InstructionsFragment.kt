package com.github.recipes.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.github.recipes.R
import com.github.recipes.models.Result
import com.github.recipes.util.Constants
import kotlinx.android.synthetic.main.fragment_instructions.view.*

class InstructionsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_instructions, container, false)

        // get arguments from bundle which bundle hold the result which means recipe
        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        view.instruction_webView.webViewClient = object : WebViewClient() {}

        // because myBundle.sourceUrl is nullable and the method "loadUrl" accept only String we must first use this operator !!.
        val websiteUrl: String = myBundle!!.sourceUrl
        view.instruction_webView.loadUrl(websiteUrl)

        return view
    }


}