package com.sergimarrahyarenas.bloodstats.api.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergimarrahyarenas.bloodstats.api.RetrofitApiClient
import com.sergimarrahyarenas.bloodstats.models.characterdata.CharacterData
import com.sergimarrahyarenas.bloodstats.models.charactermedia.CharacterMedia
import com.sergimarrahyarenas.bloodstats.models.itemdata.ItemData
import com.sergimarrahyarenas.bloodstats.models.itemmedia.ItemMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlizzardViewModel: ViewModel() {

    private val accessTokenService = RetrofitApiClient()

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseError = MutableLiveData<Boolean>(false)
    val responseError: LiveData<Boolean> = _responseError

    private val _accessToken = MutableLiveData<String?>()
    val accessToken: LiveData<String?> = _accessToken

    private val _characterData = MutableLiveData<CharacterData?>()
    val characterData: LiveData<CharacterData?> = _characterData

    private val _characterMedia = MutableLiveData<CharacterMedia?>()
    val characterMedia: LiveData<CharacterMedia?> = _characterMedia

    private val _itemData = MutableLiveData<List<ItemData?>>()
    val itemData: LiveData<List<ItemData?>> = _itemData

    private val _itemMedia = MutableLiveData<ItemMedia?>()
    val itemMedia: LiveData<ItemMedia?> = _itemMedia

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _accessToken.postValue(
                accessTokenService.getAccessTokenApiService()?.accessToken
            )
        }
    }

    fun loadCharacterDataAndMedia(name: String, realm: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                _characterData.postValue(
                    accessTokenService.getCharacterData(
                        accessToken = accessToken.value!!,
                        name = name,
                        realm = realm
                    )
                )

                _characterMedia.postValue(
                    accessTokenService.getCharacterMedia(
                        accessToken = accessToken.value!!,
                        name = name,
                        realm = realm
                    )
                )
                _isLoading.postValue(false)
                _responseError.postValue(false)
            } catch (e: Exception) {
                _responseError.postValue(true)
            }
        }
    }

    fun loadItemDataAndMedia(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _itemData.postValue(
                    listOf(
                        accessTokenService.getItemData(
                            accessToken = accessToken.value!!,
                            name = name
                        )
                    )
                )

                _itemMedia.postValue(
                    itemData.value?.get(0)?.results?.get(0)?.data?.media?.id?.let {
                        accessTokenService.getItemMedia(
                            accessToken = accessToken.value!!,
                            itemId = it
                        )
                    }
                )
                _responseError.postValue(false)
            } catch (e: Exception) {
                _responseError.postValue(true)
            }
        }
    }

//    fun getItemNames(json: String): List<String> {
//        val itemsName = mutableListOf<String>()
//        val jsonObject = JSONObject(json)
//        val resultsArray = jsonObject.getJSONArray("results")
//
//        for (i in 0 until resultsArray.length()) {
//            val itemObject = resultsArray.getJSONObject(i)
//            val dataObject = itemObject.getJSONObject("data")
//            val itemName = dataObject.getJSONObject("name").optString("en_US")
//            if (itemName.isNotEmpty()) {
//                _gearData.postValue(
//                    itemsName.add(itemName)
//                )
//            }
//        }
//        return itemsName
//    }
}
