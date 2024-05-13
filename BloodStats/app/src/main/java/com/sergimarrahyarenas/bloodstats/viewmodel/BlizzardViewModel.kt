package com.sergimarrahyarenas.bloodstats.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergimarrahyarenas.bloodstats.api.blizzardmanagement.RetrofitApiClient
import com.sergimarrahyarenas.bloodstats.data.CharacterClassSpecialization
import com.sergimarrahyarenas.bloodstats.models.characterequipment.CharacterEquipment
import com.sergimarrahyarenas.bloodstats.models.characterequipment.EquippedItem
import com.sergimarrahyarenas.bloodstats.models.charactermedia.CharacterMedia
import com.sergimarrahyarenas.bloodstats.models.characterprofilesummary.CharacterProfileSummary
import com.sergimarrahyarenas.bloodstats.models.characterspecialization.CharacterSpecialization
import com.sergimarrahyarenas.bloodstats.models.characterstatistics.CharacterStatistics
import com.sergimarrahyarenas.bloodstats.models.itemdata.ItemData
import com.sergimarrahyarenas.bloodstats.models.itemdata.ItemStats
import com.sergimarrahyarenas.bloodstats.models.itemmedia.ItemMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlizzardViewModel: ViewModel() {

    private val accessTokenService = RetrofitApiClient()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseError = MutableLiveData(false)
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

    private val _characterMedia = MutableLiveData<CharacterMedia?>()
    val characterMedia: LiveData<CharacterMedia?> = _characterMedia

    private val _equippedItems = MutableLiveData<List<EquippedItem?>>()
    val equippedItems: LiveData<List<EquippedItem?>> = _equippedItems

    private val _itemData = MutableLiveData<ItemData?>()
    val itemData: LiveData<ItemData?> = _itemData

    private val _itemStats = MutableLiveData<List<ItemStats>>()
    val itemStats: LiveData<List<ItemStats>> = _itemStats

    private val _equippedItemsMedia = MutableLiveData<List<ItemMedia?>>()
    val equippedItemMedia: LiveData<List<ItemMedia?>> = _equippedItemsMedia

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

                _characterEquipment.value?.equipped_items?.let { equippedItems ->
                    _equippedItems.postValue(equippedItems)
                    equippedItems.forEach { equippedItem ->
                        accessTokenService.getItemDataById(
                            accessToken = accessToken.value!!,
                            itemId = equippedItem.item.id
                        )
                    }
                }

                equippedItems.value.let { equippedItemsMedia ->
                    equippedItemsMedia?.forEach { equippedItemMedia ->
                        equippedItemMedia?.item?.id?.let {
                            _equippedItemsMedia.postValue(
                                listOf(
                                    accessTokenService.getItemMedia(
                                        accessToken = accessToken.value!!,
                                        itemId = it
                                    )
                                )
                            )
                        }
                    }
                }

                getPrimaryAttribute()
            _responseError.postValue(false)
        } catch (e: Exception) {
            _responseError.postValue(true)
        }
            _isLoading.postValue(false)
        }
    }

    fun loadItemDataAndMedia(itemId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _itemData.postValue(
                accessTokenService.getItemDataById(
                    accessToken = accessToken.value!!,
                    itemId = itemId
                )
            )

            _itemMedia.postValue(
                accessTokenService.getItemMedia(
                    accessToken = accessToken.value!!,
                    itemId = itemId
                )
            )

            for (stat in itemData.value?.preview_item?.stats!!) {
                _itemStats.postValue(
                    listOf(
                        ItemStats(
                            name = stat.type.name,
                            display_string = stat.display.display_string,
                        )
                    )
                )
            }
        }
    }

    private fun getPrimaryAttribute() {
        val characterClass = _characterProfileSummary.value?.character_class?.name
        val characterSpec = _characterProfileSummary.value?.active_spec?.name

        val spec = CharacterClassSpecialization.Resources.getAttribute(CharacterClassSpecialization(characterClass, characterSpec))
        return _characterSpec.postValue(spec)
    }
}
