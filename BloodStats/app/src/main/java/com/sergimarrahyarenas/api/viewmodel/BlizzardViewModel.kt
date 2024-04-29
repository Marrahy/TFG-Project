package com.sergimarrahyarenas.api.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergimarrahyarenas.api.BlizzardApiService
import com.sergimarrahyarenas.api.RetrofitService
import com.sergimarrahyarenas.api.models.bossdata.BossData
import com.sergimarrahyarenas.api.models.characterdata.CharacterData
import com.sergimarrahyarenas.api.models.characterstatistics.CharacterStatistics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlizzardViewModel: ViewModel() {
//    private val networkManager = NetworkManager()
//    private val repository = Repository(networkManager)

    private val accessTokenBlizzardApiService = RetrofitService.accessTokenRetrofit.create(BlizzardApiService::class.java)
    private val characterBlizzardService = RetrofitService.blizzardApiProfileRequestRetrofit.create(BlizzardApiService::class.java)
    private val gameDataBlizzardService = RetrofitService.blizzardApiGameDataRequestRetrofit.create(BlizzardApiService::class.java)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String> = _accessToken

    private val _characterData = MutableLiveData<CharacterData?>()
    val characterData: LiveData<CharacterData?> = _characterData

    private val _bossData = MutableLiveData<BossData?>()
    val bossData: LiveData<BossData?> = _bossData

    private val _entityName = MutableLiveData<String>()
    val entityName: LiveData<String> = _entityName

    private val _realm = MutableLiveData<String>()
    val realm: LiveData<String> = _realm


    init {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun loadCharacterData(name: String, realm: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val characterData = accessToken.value!!.let {
                characterBlizzardService.getCharacterData(name = name, realm = realm, accessToken = it).body()
            }
            _characterData.postValue(characterData)
        }
    }

    fun loadBossData(name: String) {
        val blizzardApiService = RetrofitService.accessTokenRetrofit.create(BlizzardApiService::class.java)
        viewModelScope.launch(Dispatchers.IO) {
            val bossData = accessToken.value!!.let {
                blizzardApiService.getBossData(name = name, accessToken = it).body()
            }
            _bossData.postValue(bossData)
        }
    }
}
