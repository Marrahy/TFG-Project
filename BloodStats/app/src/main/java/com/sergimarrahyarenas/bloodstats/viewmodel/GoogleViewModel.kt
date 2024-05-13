package com.sergimarrahyarenas.bloodstats.viewmodel

import androidx.lifecycle.ViewModel
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.SignInResult
import com.sergimarrahyarenas.bloodstats.api.googlemanagement.sign_in.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GoogleViewModel: ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }
}