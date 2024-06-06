package com.sergimarrahyarenas.bloodstats.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergimarrahyarenas.bloodstats.data.network.client.RetrofitApiClient
import com.sergimarrahyarenas.bloodstats.data.specializations.CharacterClassSpecialization
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characetermythickeystone.CharacterMythicKeystone
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterencounters.CharacterEncounters
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.CharacterEquipment
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.EquippedItem
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster.CharacterGuildRoster
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.charactermedia.CharacterMedia
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.CharacterProfileSummary
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.SpellTooltip
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterstatistics.CharacterStatistics
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.ItemData
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.ItemStats
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemmedia.ItemMedia
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.realm.RealmInfo
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.specializationmedia.SpecializationMedia
import com.sergimarrahyarenas.bloodstats.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class BlizzardViewModel : ViewModel() {
    //Retrofit instance
    private val retrofitService = RetrofitApiClient()

    //Request in process
    private val _isLoading = MutableLiveData(false)

    //Error at Api Response
    private val _responseError = MutableLiveData(false)

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

    private val _characterSpecializationMedia = MutableLiveData<SpecializationMedia?>()
    val characterSpecializationMedia: LiveData<SpecializationMedia?> = _characterSpecializationMedia

    //Item Api
    private val _itemData = MutableLiveData<ItemData?>()
    val itemData: LiveData<ItemData?> = _itemData

    private val _itemStats = MutableLiveData<List<ItemStats>>()

    //Realm Api
    private val _listOfRealms = MutableLiveData<List<RealmInfo>?>()
    val listOfRealms: LiveData<List<RealmInfo>?> = _listOfRealms

    //Entities Media Api
    private val _characterMedia = MutableLiveData<CharacterMedia?>()
    val characterMedia: LiveData<CharacterMedia?> = _characterMedia

    private val _membersMedia = MutableLiveData<List<CharacterMedia?>?>()

    private val _equippedItemsMedia = MutableLiveData<List<ItemMedia?>>()
    val equippedItemsMedia: LiveData<List<ItemMedia?>> = _equippedItemsMedia

    private val _itemMedia = MutableLiveData<ItemMedia?>()
    val itemMedia: LiveData<ItemMedia?> = _itemMedia

    //Device Language
    private val _deviceLanguage = MutableLiveData<String>()
    val deviceLanguage: LiveData<String> = _deviceLanguage

    /**
     * Initialize deviceLanguage to get the device language and
     * makes a request to Blizzard's API and gets the Access Token for the OAuth 1.0
     *
     */
    init {
        getLocaleDeviceLanguage()
        viewModelScope.launch(Dispatchers.IO) {
            _accessToken.postValue(
                retrofitService.getAccessTokenApiService()?.accessToken
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
                    retrofitService.getCharacterProfileSummary(
                        accessToken = accessToken.value!!,
                        characterName = characterName,
                        realmSlug = realmSlug
                    )
                )

                _characterEquipment.postValue(
                    retrofitService.getCharacterEquipment(
                        accessToken = accessToken.value!!,
                        characterName = characterName,
                        realmSlug = realmSlug,
                        locale = deviceLanguage.value!!
                    )
                )

                _characterMedia.postValue(
                    retrofitService.getCharacterMedia(
                        accessToken = accessToken.value!!,
                        characterName = characterName,
                        realmSlug = realmSlug,
                        locale = deviceLanguage.value!!
                    )
                )

                _characterEquipment.value?.equipped_items?.let { equippedItems ->
                    _equippedItems.postValue(equippedItems)
                    equippedItems.forEach { equippedItem ->
                        retrofitService.getItemDataById(
                            accessToken = accessToken.value!!,
                            itemId = equippedItem.item.id,
                            locale = deviceLanguage.value!!
                        )
                    }
                }

                val equippedItemsMediaList =
                    _characterEquipment.value?.equipped_items?.map { equippedItem ->
                        equippedItem.media.id.let {
                            retrofitService.getItemMedia(
                                accessToken = accessToken.value!!,
                                itemId = it,
                                locale = deviceLanguage.value!!
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

    /**
     * This function makes an API request to Blizzard's API and gets the character specialization
     *
     * @param characterName Name of the character searched
     * @param realmSlug Realm where the character is located
     */
    fun loadCharacterSpecialization(characterName: String, realmSlug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val characterSpecialization = retrofitService.getCharacterSpecialization(
                    accessToken = accessToken.value!!,
                    characterName = characterName,
                    realmSlug = realmSlug,
                    locale = deviceLanguage.value!!
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
                characterSpecialization?.specializations?.get(0)?.specialization?.id?.let {
                    loadSpecializationMedia(
                        it
                    )
                }

                _responseError.postValue(false)
            } catch (e: Exception) {
                e.printStackTrace()
                _responseError.postValue(true)
            }
        }
        _isLoading.postValue(false)
    }

    /**
     * This function makes an API request to Blizzard's API and gets all the character dungeon info
     *
     * @param characterName Name of the character searched
     * @param realmSlug Realm where the character is located
     */
    private fun getCharacterEncounters(characterName: String, realmSlug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val encounters = retrofitService.getCharacterEncounters(
                    accessToken = accessToken.value!!,
                    characterName = characterName,
                    realmSlug = realmSlug,
                    locale = deviceLanguage.value!!
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

    /**
     * This function makes an API request to Blizzard's API and gets mainly the Mythic Rating from the character searched
     *
     * @param characterName Name of the character searched
     * @param realmSlug Realm where the character is located
     */
    private fun getCharacterMythicKeystone(characterName: String, realmSlug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                _characterMythicKeystone.postValue(
                    retrofitService.getCharacterMythicKeystone(
                        accessToken = accessToken.value!!,
                        characterName = characterName,
                        realmSlug = realmSlug,
                        locale = deviceLanguage.value!!
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
     * This function makes an API request to Blizzard's API and gets the character stats
     *
     * @param characterName Name of the character searched
     * @param realmSlug Realm of the character searched
     */

    private fun getCharacterStatistics(characterName: String, realmSlug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                _characterStatistics.postValue(
                    retrofitService.getCharacterStatisticsSummary(
                        accessToken = accessToken.value!!,
                        characterName = characterName,
                        realmSlug = realmSlug,
                        locale = deviceLanguage.value!!
                    )
                )

                _responseError.postValue(false)
            } catch (e: Exception) {
                _responseError.postValue(true)
            }
            _isLoading.postValue(false)
        }
    }

    /**
     * This function makes and API request to Blizzard's API and gets all the character guild info
     *
     * @param guildName Name of the guild from the character searched
     * @param realm Realm where the character is located
     */
    private fun loadCharacterGuildRoster(guildName: String, realm: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _characterGuildRoster.postValue(
                retrofitService.getCharacterGuildRoster(
                    accessToken = accessToken.value!!,
                    characterName = guildName,
                    realmSlug = realm,
                    locale = deviceLanguage.value!!
                )
            )
        }
    }

    /**
     * This function makes an API request to Blizzard's API and gets all the members media
     *
     */
    private fun getMembersMedia() {
        viewModelScope.launch(Dispatchers.IO) {
            val listOfMembersMedia =
                _characterGuildRoster.value?.members?.map { member ->
                    member.character.name.let { name ->
                        member.character.realm.slug.let { realmSlug ->
                            retrofitService.getCharacterMedia(
                                accessToken = accessToken.value!!,
                                characterName = name,
                                realmSlug = realmSlug,
                                locale = deviceLanguage.value!!
                            )
                        }
                    }
                }
            _membersMedia.postValue(listOfMembersMedia)
        }
    }

    /**
     * This function makes an API request to Blizzard's API and gets all the items media
     *
     * @param itemId ID of the item
     */
    fun loadItemDataAndMedia(itemId: Int) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            _itemData.postValue(
                retrofitService.getItemDataById(
                    accessToken = accessToken.value!!,
                    itemId = itemId,
                    locale = deviceLanguage.value!!
                )
            )

            _itemMedia.postValue(
                retrofitService.getItemMedia(
                    accessToken = accessToken.value!!,
                    itemId = itemId,
                    locale = deviceLanguage.value!!
                )
            )
        }
        _isLoading.postValue(false)
    }

    private fun loadSpecializationMedia(specializationId: Int) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            _characterSpecializationMedia.postValue(
                retrofitService.getCharacterSpecializationMedia(
                    accessToken = accessToken.value!!,
                    specializationId = specializationId,
                    locale = deviceLanguage.value!!
                )
            )
        }
    }

    /**
     * This function makes an API request to Blizzard's API and gets a list of all EUW Realms
     *
     * @param accessToken Access Token needed to verify the client at Blizzard server
     */
    fun loadListOfEURealms(accessToken: String) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            var realms = retrofitService.getListOfEURealms(
                accessToken = accessToken,
                locale = deviceLanguage.value!!
            )
            realms = realms?.sortedBy { it.name }
            _listOfRealms.postValue(realms)
        }
        _isLoading.postValue(false)
    }

    /**
     * This function clear all the data from an item
     *
     */
    fun clearItemData() {
        viewModelScope.launch(Dispatchers.IO) {
            _itemData.postValue(null)
            _itemStats.postValue(emptyList())
        }
    }

    /**
     * This function clear all the user data
     *
     */
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

    /**
     * This function gets the device language
     *
     */
    private fun getLocaleDeviceLanguage() {
        val language = Locale.getDefault().language

        _deviceLanguage.postValue(
            when (language) {
                "es" -> Constants.LOCALE_ES
                "en" -> Constants.LOCALE_EN
                else -> Constants.LOCALE_ES
            }
        )
    }

    /**
     * This function post the main primary stat from the character searched based in the class and specialization
     *
     */
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
