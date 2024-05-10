package com.sergimarrahyarenas.bloodstats.api.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergimarrahyarenas.bloodstats.api.RetrofitApiClient
import com.sergimarrahyarenas.bloodstats.data.Character
import com.sergimarrahyarenas.bloodstats.models.characterequipment.CharacterEquipment
import com.sergimarrahyarenas.bloodstats.models.charactermedia.CharacterMedia
import com.sergimarrahyarenas.bloodstats.models.characterprofilesummary.CharacterProfileSummary
import com.sergimarrahyarenas.bloodstats.models.characterspecialization.CharacterSpecialization
import com.sergimarrahyarenas.bloodstats.models.characterstatistics.CharacterStatistics
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

    private val _characterProfileSummary = MutableLiveData<CharacterProfileSummary?>()
    val characterProfileSummary: LiveData<CharacterProfileSummary?> = _characterProfileSummary

    private val _characterEquipment = MutableLiveData<CharacterEquipment?>()
    val characterEquipment: LiveData<CharacterEquipment?> = _characterEquipment

    private val _characterStatistics = MutableLiveData<CharacterStatistics?>()
    val characterStatistics: LiveData<CharacterStatistics?> = _characterStatistics

    private val _characterSpecialization = MutableLiveData<CharacterSpecialization?>()
    val characterSpecialization: LiveData<CharacterSpecialization?> = _characterSpecialization

    private val _characterSpec = MutableLiveData<String?>()
    val characterSpec: LiveData<String?> = _characterSpec

    private val _itemData = MutableLiveData<ItemData?>()
    val itemData: LiveData<ItemData?> = _itemData

    private val _characterMedia = MutableLiveData<CharacterMedia?>()
    val characterMedia: LiveData<CharacterMedia?> = _characterMedia

    private val _characterEquipmentMedia = MutableLiveData<String?>()
    val characterEquipmentMedia: LiveData<String?> = _characterEquipmentMedia

    private val _itemMedia = MutableLiveData<ItemMedia?>()
    val itemMedia: LiveData<ItemMedia?> = _itemMedia

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _accessToken.postValue(
                accessTokenService.getAccessTokenApiService()?.accessToken
            )
        }
    }

    fun loadCharacterStatistics(name: String, realm: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                _characterStatistics.postValue(
                    accessTokenService.getCharacterStatisticsSummary(
                        accessToken = accessToken.value!!,
                        name = name,
                        realm = realm
                    )
                )

                _responseError.postValue(false)
            } catch (e: Exception) {
                _responseError.postValue(true)
            }
            _isLoading.postValue(false)
        }
    }

    fun loadCharacterProfileSummaryEquipmentMedia(name: String, realm: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                _characterProfileSummary.postValue(
                    accessTokenService.getCharacterProfileSummary(
                        accessToken = accessToken.value!!,
                        name = name,
                        realm = realm
                    )
                )

                _characterEquipment.postValue(
                    accessTokenService.getCharacterEquipment(
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

//                _characterEquipmentMedia.postValue(
//                    accessTokenService.getItemMedia(
//                        accessToken = accessToken.value!!,
//                        itemId =
//                    )
//                )


                getPrimaryAttribute()
            _responseError.postValue(false)
        } catch (e: Exception) {
            _responseError.postValue(true)
        }
            _isLoading.postValue(false)
        }
    }

    private fun getPrimaryAttribute() {
        val characterClass = _characterProfileSummary.value?.character_class?.name
        val characterSpec = _characterProfileSummary.value?.active_spec?.name

        val spec = Character.Resources.getAttribute(Character(characterClass, characterSpec))
        return _characterSpec.postValue(spec)
    }

    fun loadItemDataAndMedia(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _itemData.postValue(
                    accessTokenService.getItemData(
                        accessToken = accessToken.value!!,
                        name = name
                    )
                )

                _responseError.postValue(false)
            } catch (e: Exception) {
                _responseError.postValue(true)
            }
        }
    }
}
