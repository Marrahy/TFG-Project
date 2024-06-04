package com.sergimarrahyarenas.bloodstats.model.google.signinstate

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
