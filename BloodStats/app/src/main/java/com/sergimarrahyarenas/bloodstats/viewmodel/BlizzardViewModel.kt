package com.sergimarrahyarenas.bloodstats.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergimarrahyarenas.bloodstats.data.network.client.RetrofitApiClient
import com.sergimarrahyarenas.bloodstats.data.CharacterClassSpecialization
import com.sergimarrahyarenas.bloodstats.model.characetermythickeystone.CharacterMythicKeystone
import com.sergimarrahyarenas.bloodstats.model.characterencounters.CharacterEncounters
import com.sergimarrahyarenas.bloodstats.model.characterequipment.CharacterEquipment
import com.sergimarrahyarenas.bloodstats.model.characterequipment.EquippedItem
import com.sergimarrahyarenas.bloodstats.model.characterguildroster.CharacterGuildRoster
import com.sergimarrahyarenas.bloodstats.model.charactermedia.CharacterMedia
import com.sergimarrahyarenas.bloodstats.model.characterprofilesummary.CharacterProfileSummary
import com.sergimarrahyarenas.bloodstats.model.characterspecialization.SpellTooltip
import com.sergimarrahyarenas.bloodstats.model.characterstatistics.CharacterStatistics
import com.sergimarrahyarenas.bloodstats.model.itemdata.ItemData
import com.sergimarrahyarenas.bloodstats.model.itemdata.ItemStats
import com.sergimarrahyarenas.bloodstats.model.itemmedia.ItemMedia
import com.sergimarrahyarenas.bloodstats.model.realm.RealmInfo
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

    private val _characterPrimaryStat = MutableLiveData<Int?>()
    val characterPrimaryStat: LiveData<Int?> = _characterPrimaryStat

    private val _characterActiveSpecialization = MutableLiveData<String?>()
    val characterActiveSpecialization: LiveData<String?> = _characterActiveSpecialization

    private val _listOfClassSpells = MutableLiveData<List<SpellTooltip?>>()
    val listOfClassSpells: LiveData<List<SpellTooltip?>> = _listOfClassSpells

    private val _listOfSpecSpells = MutableLiveData<List<SpellTooltip?>>()
    val listOfSpecSpells: LiveData<List<SpellTooltip?>> = _listOfSpecSpells

    private val _characterGuildRoster = MutableLiveData<CharacterGuildRoster?>()
    val characterGuildRoster: LiveData<CharacterGuildRoster?> = _characterGuildRoster

    private val _equippedItems = MutableLiveData<List<EquippedItem?>>()
    val equippedItems: LiveData<List<EquippedItem?>> = _equippedItems

    private val _characterEncounters = MutableLiveData<CharacterEncounters?>()
    val characterEncounters: LiveData<CharacterEncounters?> = _characterEncounters

    private val _characterMythicKeystone = MutableLiveData<CharacterMythicKeystone?>()
    val characterMythicKeystoneProfile: LiveData<CharacterMythicKeystone?> = _characterMythicKeystone

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
     * @param characterName Name of the searched character
     * @param realmSlug Realm of the searched character
     */
    fun loadCharacterProfileSummaryEquipmentMedia(characterName: String, realmSlug: String) {
        clearCharacterData()
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                _characterProfileSummary.postValue(
                    accessTokenService.getCharacterProfileSummary(
                        accessToken = accessToken.value!!,
                        characterName = characterName,
                        realmSlug = realmSlug
                    )
                )

                _characterEquipment.postValue(
                    accessTokenService.getCharacterEquipment(
                        accessToken = accessToken.value!!,
                        characterName = characterName,
                        realmSlug = realmSlug
                    )
                )

                _characterMedia.postValue(
                    accessTokenService.getCharacterMedia(
                        accessToken = accessToken.value!!,
                        characterName = characterName,
                        realmSlug = realmSlug
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
                    getCharacterStatistics(
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

                getPrimaryAttribute()
                getMembersMedia()
                getCharacterEncounters(characterName = characterName, realmSlug = realmSlug)
                getCharacterMythicKeystone(characterName = characterName, realmSlug = realmSlug)
            } catch (e: Exception) {
                _responseError.postValue(true)
            }
            _isLoading.postValue(false)
            _responseError.postValue(false)
        }
    }

    fun loadCharacterSpecialization(characterName: String, realmSlug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val characterSpecialization = accessTokenService.getCharacterSpecialization(
                    accessToken = accessToken.value!!,
                    characterName = characterName,
                    realmSlug = realmSlug
                )

                _characterActiveSpecialization.postValue(characterSpecialization?.active_specialization?.name)

                val characterClassSpells = mutableListOf<SpellTooltip>()
                val characterSpecSpells = mutableListOf<SpellTooltip>()

                characterSpecialization?.specializations?.let { specialization ->
                        specialization.map { talents ->
                            talents.loadouts[0].selected_class_talents.map { classSpells ->
                                classSpells.tooltip.spell_tooltip.let { characterClassSpells.add(it) }
                            }
                            talents.loadouts[0].selected_spec_talents.map { specSpells ->
                                specSpells.tooltip.spell_tooltip.let { characterSpecSpells.add(it) }
                            }
                        }
                    }

                _listOfClassSpells.postValue(characterClassSpells)
                _listOfSpecSpells.postValue(characterSpecSpells)

                _responseError.postValue(false)
            } catch (e: Exception) {
                e.printStackTrace()
                _responseError.postValue(true)
            }
        }
        _isLoading.postValue(false)
    }

    private fun getCharacterEncounters(characterName: String, realmSlug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val encounters = accessTokenService.getCharacterEncounters(
                    accessToken = accessToken.value!!,
                    characterName = characterName,
                    realmSlug = realmSlug
                )

                _characterEncounters.postValue(encounters)
                _responseError.postValue(false)
            } catch (e: Exception) {
                e.printStackTrace()
                _responseError.postValue(true)
            }
        }
        _isLoading.postValue(false)
    }

    private fun getCharacterMythicKeystone(characterName: String, realmSlug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                _characterMythicKeystone.postValue(
                    accessTokenService.getCharacterMythicKeystone(
                        accessToken = accessToken.value!!,
                        characterName = characterName,
                        realmSlug = realmSlug
                    )
                )

                _responseError.postValue(false)
            } catch (e: Exception) {
                e.printStackTrace()
                _responseError.postValue(true)
            }
        }
        _isLoading.postValue(false)
    }

    /**
     * This function makes a request to Blizzard's API to get the character stats
     *
     * @param characterName Name of the character searched
     * @param realmSlug Realm of the character searched
     */

    private fun getCharacterStatistics(characterName: String, realmSlug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                _characterStatistics.postValue(
                    accessTokenService.getCharacterStatisticsSummary(
                        accessToken = accessToken.value!!,
                        characterName = characterName,
                        realmSlug = realmSlug
                    )
                )

                _responseError.postValue(false)
            } catch (e: Exception) {
                _responseError.postValue(true)
            }
            _isLoading.postValue(false)
        }
    }

    private fun loadCharacterGuildRoster(guildName: String, realm: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _characterGuildRoster.postValue(
                accessTokenService.getCharacterGuildRoster(
                    accessToken = accessToken.value!!,
                    characterName = guildName,
                    realmSlug = realm
                )
            )
        }
    }

    private fun getMembersMedia() {
        viewModelScope.launch(Dispatchers.IO) {
            val listOfMembersMedia =
                _characterGuildRoster.value?.members?.map { member ->
                    member.character.name.let { name ->
                        member.character.realm.slug.let { realmSlug ->
                            accessTokenService.getCharacterMedia(
                                accessToken = accessToken.value!!,
                                characterName = name,
                                realmSlug = realmSlug
                            )
                        }
                    }
                }
            _membersMedia.postValue(listOfMembersMedia)
        }
    }

    fun loadItemDataAndMedia(itemId: Int) {
        _isLoading.postValue(true)
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
        }
        _isLoading.postValue(false)
    }

    fun loadListOfEURealms(accessToken: String) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            var realms = accessTokenService.getListOfEURealms(
                accessToken = accessToken
            )
            realms = realms?.sortedBy { it.name }
            _listOfRealms.postValue(realms)
        }
        _isLoading.postValue(false)
    }

    fun clearItemData() {
        viewModelScope.launch(Dispatchers.IO) {
            _itemData.postValue(null)
            _itemStats.postValue(emptyList())
        }
    }

    fun clearCharacterData() {
        _characterProfileSummary.postValue(null)
        _characterEquipment.postValue(null)
        _characterStatistics.postValue(null)
        _characterPrimaryStat.postValue(null)
        _characterActiveSpecialization.postValue(null)
        _listOfClassSpells.postValue(emptyList())
        _listOfSpecSpells.postValue(emptyList())
        _characterGuildRoster.postValue(null)
        _equippedItems.postValue(emptyList())
        _characterEncounters.postValue(null)
        _itemData.postValue(null)
        _itemStats.postValue(emptyList())
        _characterMedia.postValue(null)
        _membersMedia.postValue(emptyList())
        _equippedItemsMedia.postValue(emptyList())
        _itemMedia.postValue(null)
        _responseError.postValue(false)
        _isLoading.postValue(false)
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
        return _characterPrimaryStat.postValue(spec)
    }
}
