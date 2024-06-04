package com.sergimarrahyarenas.bloodstats.viewmodel

import androidx.lifecycle.ViewModel
import com.sergimarrahyarenas.bloodstats.model.google.signinresult.SignInResult
import com.sergimarrahyarenas.bloodstats.model.google.signinstate.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GoogleViewModel: ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    /**
     * This function updates the state based in the sign in result
     *
     * @param result Result from the sign in intent
     */
    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    /**
     * This function reset the state of the user logged
     *
     */
    fun resetState() {
        _state.update { SignInState() }
    }
}