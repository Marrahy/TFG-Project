package com.sergimarrahyarenas.bloodstats.model.signinstate

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
