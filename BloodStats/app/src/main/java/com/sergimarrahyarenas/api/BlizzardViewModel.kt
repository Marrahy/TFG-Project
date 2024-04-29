package com.sergimarrahyarenas.api.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sergimarrahyarenas.api.TokenResponse
import com.sergimarrahyarenas.api.models.CharacterStatistics
import com.sergimarrahyarenas.api.network.NetworkManager
import com.sergimarrahyarenas.api.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class BlizzardViewModel(
    private val repository: Repository,
    private val networkManager: NetworkManager
): ViewModel() {

    private val clientId = "286817623f474bcface004dc20313828"
    private val clientSecret = "UCysV7ZsseanapD1lOfVg7FRTpCj2GXl"

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String> = _accessToken

    private val _characterStatistics = MutableLiveData<CharacterStatistics?>()
    val characterStatistics: LiveData<CharacterStatistics?> = _characterStatistics

    private val _entityName = MutableLiveData<String>()
    val entityName: LiveData<String> = _entityName

    private val _realm = MutableLiveData<String>()
    val realm: LiveData<String> = _realm


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _accessToken.postValue(
                networkManager.getAccessToken(clientId, clientSecret).accessToken
            )
        }
    }

    fun loadCharacterStatistics(name: String, realm: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val characterStatistics = accessToken.value?.let {
                repository.getCharacterStatistics(it, name, realm)
            }
            _characterStatistics.postValue(characterStatistics)
        }
    }

    fun loadEntityInformation(entityName: String) {

    }
}
