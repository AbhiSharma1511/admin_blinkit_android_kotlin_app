package com.kotlin.adminblinkit.viewModels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.kotlin.adminblinkit.Utils
import com.kotlin.userblinkit.models.Admin
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {

    private val _verificationId = MutableStateFlow<String?>(null)
    private val _otpSent = MutableStateFlow(false)
    val otpSent = _otpSent

    private val _isSignedInSuccessfully = MutableStateFlow(false)
    val isSignedInSuccessfully = _isSignedInSuccessfully

    private val _isACurrentUser = MutableStateFlow(false)
    val isACurrentUser = _isACurrentUser

    init {
        Utils.getAuthInstance().currentUser?.let {
            _isACurrentUser.value = true
        }
    }

    fun sendOtp(userNumber: String, activity: Activity) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: Verification completed successfully.")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e(TAG, "onVerificationFailed: Verification failed.", e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                Log.d(TAG, "onCodeSent: OTP code sent successfully.")
                _verificationId.value = verificationId
                _otpSent.value = true
            }
        }
        val options = PhoneAuthOptions.newBuilder(Utils.getAuthInstance())
            .setPhoneNumber(userNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(otp: String, userNumber: String, admin: Admin) {
        val credential = PhoneAuthProvider.getCredential(_verificationId.value.toString(), otp)
        Utils.getAuthInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                admin.uid = Utils.getCurrentUserId()
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithPhoneAuthCredential: Admin signed in successfully.")

                    admin.uid?.let {
                        FirebaseDatabase.getInstance().getReference("Admin").child("AdminInfo").child(
                            admin.uid!!
                        ).setValue(admin)
                    }
                    _isSignedInSuccessfully.value = true
                } else {
                    Log.e(TAG, "signInWithPhoneAuthCredential: Sign-in failed.", task.exception)
                }
            }
    }

    companion object {
        private const val TAG = "AuthViewModel"
    }
}