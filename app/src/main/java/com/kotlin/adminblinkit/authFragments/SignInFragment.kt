package com.kotlin.adminblinkit.authFragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.kotlin.adminblinkit.R
import com.kotlin.adminblinkit.Utils
import com.kotlin.adminblinkit.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(layoutInflater)
        changeStatusBarColor()
        getUserNumber()
        onContinueButtonClick()
        return binding.root
    }
    private fun onContinueButtonClick() {
        binding.btnContinue.setOnClickListener {
            val number = binding.edtUserNumber.text.toString()

            if(number.isEmpty() || number.length!=10){
                Utils.showToast(requireContext(),"Please enter a valid phone number")
            }else{
                val bundle = Bundle()
                bundle.putString("phoneNumber", number)
                findNavController().navigate(R.id.action_signInFragment_to_OTPFragment, bundle)
            }
        }
    }

    private fun getUserNumber() {
        binding.edtUserNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val len = number?.length

                if (len==10){
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.green))
                }else{
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grayish_blue))
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun changeStatusBarColor() {
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.yellow)
            statusBarColor = statusBarColors
        }
    }

}