package com.kotlin.adminblinkit.authFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kotlin.adminblinkit.R
import com.kotlin.adminblinkit.Utils
import com.kotlin.adminblinkit.activity.AdminMainActivity
import com.kotlin.adminblinkit.databinding.FragmentOTPBinding
import com.kotlin.adminblinkit.viewModels.AuthViewModel
import com.kotlin.userblinkit.models.Users
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class OTPFragment : Fragment() {

    private lateinit var binding: FragmentOTPBinding
    private val viewModel : AuthViewModel by viewModels()
    private lateinit var userNumber: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentOTPBinding.inflate(layoutInflater)

        getUserNumber()
        sendOTP()
        onLoginButtonClick()
        onBackBtnClick()

        return binding.root
    }

    private fun onLoginButtonClick() {
        binding.btnContinue.setOnClickListener {
            Utils.showDialog(requireContext(),"Signing you...")
            val otp = binding.otpNumber.text.toString()
            if(otp.length<6){
                Utils.showToast(requireContext(),"Please enter right otp")
            }else{
                verify(otp)
            }
        }
    }

    private fun verify(otp: String) {
        val uid = Utils.getCurrentUserId()
        val user = Users(uid, userPhoneNumber = userNumber, userAddress = null)
        viewModel.signInWithPhoneAuthCredential(otp, userNumber, user)

        // Observe the sign-in status using collectLatest to avoid processing outdated events
        lifecycleScope.launch {
            viewModel.isSignedInSuccessfully.collectLatest { signedIn ->
                if (signedIn) {
                    Utils.hideDialog()
                    binding.otpNumber.text?.clear()
                    Utils.showToast(requireContext(), "Logged In")
                    startActivity(Intent(requireActivity(), AdminMainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

    private fun sendOTP() {
        Utils.showDialog(requireContext(), "Sending OTP...")
        viewModel.sendOtp(userNumber, requireActivity())

        // Observe the OTP sending status
        lifecycleScope.launch {
            viewModel.otpSent.collectLatest { otpSent ->
                if (otpSent) {
                    Utils.hideDialog()
                    Utils.showToast(requireContext(), "OTP sent...")
                }
            }
        }
    }
    private fun onBackBtnClick() {
        binding.tbOtpFragment.setNavigationOnClickListener{
            findNavController().navigate(R.id.action_OTPFragment_to_signInFragment)
        }
    }

    private fun getUserNumber() {
        val bundle = arguments
        userNumber = bundle?.getString("phoneNumber").toString()

        binding.tvOtpPhoneNumber.text = userNumber
        userNumber = "+91$userNumber"

    }


}