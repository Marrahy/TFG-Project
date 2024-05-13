package com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
