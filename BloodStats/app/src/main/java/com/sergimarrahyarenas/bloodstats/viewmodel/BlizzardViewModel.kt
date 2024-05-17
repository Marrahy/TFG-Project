package com.sergimarrahyarenas.bloodstats.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergimarrahyarenas.bloodstats.api.blizzardmanagement.RetrofitApiClient
import com.sergimarrahyarenas.bloodstats.data.CharacterClassSpecialization
import com.sergimarrahyarenas.bloodstats.models.characterequipment.CharacterEquipment
import com.sergimarrahyarenas.bloodstats.models.characterequipment.EquippedItem
import com.sergimarrahyarenas.bloodstats.models.characterguildroster.CharacterGuildRoster
import com.sergimarrahyarenas.bloodstats.models.charactermedia.CharacterMedia
import com.sergimarrahyarenas.bloodstats.models.characterprofilesummary.CharacterProfileSummary
import com.sergimarrahyarenas.bloodstats.models.characterstatistics.CharacterStatistics
import com.sergimarrahyarenas.bloodstats.models.itemdata.ItemData
import com.sergimarrahyarenas.bloodstats.models.itemdata.ItemStats
import com.sergimarrahyarenas.bloodstats.models.itemmedia.ItemMedia
import com.sergimarrahyarenas.bloodstats.models.realm.RealmInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlizzardViewModel : ViewModel() {

    //Retrofit instance
    private val accessTokenService = RetrofitApiClient()

    //Api is getting consumed
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    //Error at Api Response
    private val _responseError = MutableLiveData(false)
    val responseError: LiveData<Boolean> = _responseError

    //Token
    private val _accessToken = MutableLiveData<String?>()
    val accessToken: LiveData<String?> = _accessToken

    //Character Api
    private val _characterProfileSummary = MutableLiveData<CharacterProfileSummary?>()
    val characterProfileSummary: LiveData<CharacterProfileSummary?> = _characterProfileSummary

    private val _characterEquipment = MutableLiveData<CharacterEquipment?>()

    private val _characterStatistics = MutableLiveData<CharacterStatistics?>()
    val characterStatistics: LiveData<CharacterStatistics?> = _characterStatistics

    private val _characterSpecialization = MutableLiveData<String?>()
    val characterSpecialization: LiveData<String?> = _characterSpecialization

    private val _characterGuildRoster = MutableLiveData<CharacterGuildRoster?>()
    val characterGuildRoster: LiveData<CharacterGuildRoster?> = _characterGuildRoster

    private val _equippedItems = MutableLiveData<List<EquippedItem?>>()
    val equippedItems: LiveData<List<EquippedItem?>> = _equippedItems

    //Item Api
    private val _itemData = MutableLiveData<ItemData?>()
    val itemData: LiveData<ItemData?> = _itemData

    private val _itemStats = MutableLiveData<List<ItemStats>>()
    val itemStats: LiveData<List<ItemStats>> = _itemStats

    //Realm Api
    private val _listOfRealms = MutableLiveData<List<RealmInfo>?>()
    val listOfRealms: LiveData<List<RealmInfo>?> = _listOfRealms

    //Entities Media Api
    private val _characterMedia = MutableLiveData<CharacterMedia?>()
    val characterMedia: LiveData<CharacterMedia?> = _characterMedia

    private val _membersMedia = MutableLiveData<List<CharacterMedia?>?>()
    val membersMedia: LiveData<List<CharacterMedia?>?> = _membersMedia

    private val _equippedItemsMedia = MutableLiveData<List<ItemMedia?>>()
    val equippedItemsMedia: LiveData<List<ItemMedia?>> = _equippedItemsMedia

    private val _itemMedia = MutableLiveData<ItemMedia?>()
    val itemMedia: LiveData<ItemMedia?> = _itemMedia


    /**
     * This function makes a request to Blizzard's API and gets the Access Token for the OAuth 1.0
     *
     */
    fun getAccessToken() {
        viewModelScope.launch(Dispatchers.IO) {
            _accessToken.postValue(
                accessTokenService.getAccessTokenApiService()?.accessToken
            )
        }
    }

    /**
     * This function makes various retrofit request to Blizzard's API and gets the ProfileSummary, CharacterMedia,
     * CharacterGuild, Character primary stat, CharacterEquipment and the CharacterMedia
     *
     * @param name Name of the searched character
     * @param realm Realm of the searched character
     */
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
                        realmSlug = realm
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

                val equippedItemsMediaList =
                    _characterEquipment.value?.equipped_items?.map { equippedItem ->
                        equippedItem.media.id.let {
                            accessTokenService.getItemMedia(
                                accessToken = accessToken.value!!,
                                itemId = it
                            )
                        }
                    }
                _equippedItemsMedia.postValue(equippedItemsMediaList!!)

                characterProfileSummary.value?.name?.let {
                    loadCharacterStatistics(
                        it,
                        characterProfileSummary.value!!.realm.slug
                    )
                }
                characterProfileSummary.value?.guild?.let {
                    loadCharacterGuildRoster(
                        it.name,
                        characterProfileSummary.value!!.realm.slug
                    )
                }

                //Function that gets the character primary stat
                getPrimaryAttribute()
                loadMembersMedia()
                _responseError.postValue(false)
            } catch (e: Exception) {
                _responseError.postValue(true)
            }
            _isLoading.postValue(false)
        }
    }

    /**
     * This function makes a request to Blizzard's API to get the character stats
     *
     * @param name Name of the character searched
     * @param realm Realm of the character searched
     */

    private fun loadCharacterStatistics(name: String, realm: String) {
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

    fun loadCharacterGuildRoster(guildName: String, realm: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _characterGuildRoster.postValue(
                accessTokenService.getCharacterGuildRoster(
                    accessToken = accessToken.value!!,
                    name = guildName,
                    realm = realm
                )
            )
        }
    }

    fun loadMembersMedia() {
        viewModelScope.launch(Dispatchers.IO) {
            val listOfMembersMedia =
                _characterGuildRoster.value?.members?.map { member ->
                    member.character.name.let { name ->
                        member.character.realm.slug.let { realmSlug ->
                            accessTokenService.getCharacterMedia(
                                accessToken = accessToken.value!!,
                                name = name,
                                realmSlug = realmSlug
                            )
                        }
                    }
                }
            _membersMedia.postValue(listOfMembersMedia)
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

    fun loadListOfEURealms(accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var realms = accessTokenService.getListOfEURealms(
                accessToken = accessToken
            )
            realms = realms?.sortedBy { it.name }
            _listOfRealms.postValue(realms)
        }
    }

    private fun getPrimaryAttribute() {
        val characterClass = _characterProfileSummary.value?.character_class?.name
        val characterSpec = _characterProfileSummary.value?.active_spec?.name

        val spec = CharacterClassSpecialization.Resources.getAttribute(
            CharacterClassSpecialization(
                characterClass,
                characterSpec
            )
        )
        return _characterSpecialization.postValue(spec)
    }
}
