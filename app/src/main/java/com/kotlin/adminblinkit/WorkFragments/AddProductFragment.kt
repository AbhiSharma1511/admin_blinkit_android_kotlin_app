package com.kotlin.adminblinkit.WorkFragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.kotlin.adminblinkit.Constants
import com.kotlin.adminblinkit.R
import com.kotlin.adminblinkit.adapter.AdapterSelectedImage
import com.kotlin.adminblinkit.databinding.FragmentAddProductBinding

class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private var imageUris : ArrayList<Uri> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        changeStatusBarColor()

        setAutoCompleteTextView()
        onImageSelectionClicked()

        return binding.root
    }

    private fun onImageSelectionClicked() {
        binding.btnSelectImages.setOnClickListener{
            registerForActivityResult(ActivityResultContracts.GetMultipleContents()){
                val fiveImage = listOf<Uri>()
                imageUris.clear()
                imageUris.addAll(fiveImage)

                binding.rvProductImages.adapter = AdapterSelectedImage(imageUris)

            }.launch("image/*")
        }
    }

    private fun setAutoCompleteTextView() {
        val units = ArrayAdapter(requireContext(), R.layout.item_view, Constants.allUnitOfProduct)
        val category = ArrayAdapter(requireContext(), R.layout.item_view, Constants.allProductCategory)
        val productType = ArrayAdapter(requireContext(), R.layout.item_view, Constants.allProductType)

        binding.apply {
            atvUnit.setAdapter(units)
            atvProductType.setAdapter(productType)
            atvProductCategory.setAdapter(category)
        }
    }

    private fun changeStatusBarColor() {
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.yellow)
            statusBarColor = statusBarColors
        }
    }

}