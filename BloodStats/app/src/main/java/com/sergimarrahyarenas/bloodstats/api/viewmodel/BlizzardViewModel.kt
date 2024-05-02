package com.sergimarrahyarenas.bloodstats.api.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergimarrahyarenas.bloodstats.api.RetrofitApiClient
import com.sergimarrahyarenas.bloodstats.api.models.characterdata.CharacterData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlizzardViewModel: ViewModel() {

    private val accessTokenService = RetrofitApiClient()

    private val _accessToken = MutableLiveData<String?>()
    val accessToken: LiveData<String?> = _accessToken

    private val _characterData = MutableLiveData<CharacterData?>()
    val characterData: LiveData<CharacterData?> = _characterData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _accessToken.postValue(
                accessTokenService.getAccessTokenApiService()?.accessToken
            )
        }
    }

    fun loadCharacterData(name: String, realm: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _characterData.postValue(
                accessTokenService.getCharacterData(
                    accessToken = accessToken.value!!,
                    name = name,
                    realm = realm
                )
            )
        }
    }
}
