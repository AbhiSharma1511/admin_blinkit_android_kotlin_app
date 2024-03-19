package com.kotlin.adminblinkit.authFragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kotlin.adminblinkit.R
import com.kotlin.adminblinkit.Utils
import com.kotlin.adminblinkit.activity.AdminMainActivity
import com.kotlin.adminblinkit.databinding.FragmentSplashBinding
import com.kotlin.adminblinkit.viewModels.AuthViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private lateinit var binding : FragmentSplashBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentSplashBinding.inflate(layoutInflater)
        changeStatusBarColor()
        Handler(Looper.getMainLooper()).postDelayed({
//            loggedInUser()

            lifecycleScope.launch {
                viewModel.isACurrentUser.collectLatest {
                    if(it){
                        startActivity(Intent(requireActivity(), AdminMainActivity::class.java))
                        requireActivity().finish()
                    }
                    else{
                        findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
                    }
                }
            }
        },3000)
        return binding.root
    }

    private fun changeStatusBarColor() {
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.yellow)
            statusBarColor = statusBarColors
        }
    }

    private fun loggedInUser(){
        val currentUser = Utils.getAuthInstance().currentUser
        if(currentUser!=null){
            startActivity(Intent(requireActivity(), AdminMainActivity::class.java))
            requireActivity().finish()
        }
    }
}