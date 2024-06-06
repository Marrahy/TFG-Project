package com.sergimarrahyarenas.bloodstats.viewmodel

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.sergimarrahyarenas.bloodstats.data.database.BloodStatsDatabase
import com.sergimarrahyarenas.bloodstats.data.database.entities.FavoriteEntity
import com.sergimarrahyarenas.bloodstats.data.database.entities.PreferencesEntity
import com.sergimarrahyarenas.bloodstats.data.database.entities.UserEntity
import com.sergimarrahyarenas.bloodstats.data.database.entities.UserFavoriteCrossRef
import com.sergimarrahyarenas.bloodstats.data.repository.FavoriteRepository
import com.sergimarrahyarenas.bloodstats.data.repository.PreferencesRepository
import com.sergimarrahyarenas.bloodstats.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(context: Context) : ViewModel() {
    private val database: BloodStatsDatabase = BloodStatsDatabase.getInstance(context)

    private val userDao = database.userDao()
    private val preferencesDao = database.preferencesDao()
    private val favoriteDao = database.favoriteDao()

    private val userRepository = UserRepository(userDao)
    private val preferencesRepository = PreferencesRepository(preferencesDao)
    private val favoriteRepository = FavoriteRepository(favoriteDao)

    private val _user = MutableLiveData<UserEntity?>()
    val user: LiveData<UserEntity?> = _user

    private val _userPreferences = MutableLiveData<PreferencesEntity?>()
    val userPreferences: LiveData<PreferencesEntity?> = _userPreferences

    private val _userFavorites = MutableLiveData<List<FavoriteEntity>>()
    val userFavorites: LiveData<List<FavoriteEntity>> = _userFavorites

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _onUserExistsError = MutableLiveData<Boolean>()

    /**
     * This function verify the user credentials
     *
     * @param userName User name from the user input
     * @param userPassword Password from the user input
     * @return true if user exists at the DB
     */
    suspend fun verifyUserCredentials(userName: String, userPassword: String): Boolean {
        val user = userRepository.getUserByName(userName)
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(user)
        }
        return user?.userPassword == userPassword
    }

    /**
     * This function checks if the user exists in the DB
     *
     * @param userName User name from the user input
     * @return true if there is a coincidence in DB
     */
    suspend fun checkIfUserExists(userName: String): Boolean {
        val user = userRepository.getUserByName(userName)
        return user == null
    }

    /**
     * This function creates a User in the DB and post the User to _user variable
     *
     * @param userEmail Email from the user if the sign in is from Google Sign In function
     * @param userName User name from the user input
     * @param userPassword Password from the user input
     * @param avatar Avatar selected by the user
     * @param onResult onResult it's function is to call the verifyUser function
     */
    fun createIfNotExists(
        userEmail: String?,
        userName: String,
        userPassword: String,
        avatar: Int,
        onResult: (UserEntity) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val existingUser = userEmail?.let { userRepository.getUserByEmail(it) }
                if (existingUser == null) {
                    val user = UserEntity(
                        userName = userName,
                        userPassword = userPassword,
                        userEmail = userEmail,
                        avatarId = avatar
                    )
                    database.withTransaction {
                        userRepository.insertUser(user)
                        val preferences = PreferencesEntity(userId = user.userUUID, theme = "light")
                        preferencesRepository.insertPreferences(preferences)
                    }

                    withContext(Dispatchers.Main) {
                        _user.postValue(user)
                        onResult(user)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onResult(existingUser)
                    }
                }
                user.value?.userUUID?.let { getUserWithPreferences(userUUID = it) }
                user.value?.let { getFavorites(it.userUUID) }
            } catch (e: SQLiteConstraintException) {
                _onUserExistsError.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                _onUserExistsError.postValue(true)
            }
        }
        _onUserExistsError.postValue(false)
    }

    /**
     * This function gets the preferences from the user and post it at _userPreferences variable
     *
     * @param userUUID ID from the user logged
     */
    fun getUserWithPreferences(userUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userWithPreferences = preferencesRepository.getPreferencesByUserUUID(userUUID)
            _userPreferences.postValue(userWithPreferences)
        }
    }

    /**
     * This function updates the user selected theme
     *
     * @param userUUID ID from the user logged
     * @param newTheme Selected theme by the user
     */
    fun updateUserTheme(userUUID: String, newTheme: String) {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.updateThemeByUserUUID(userUUID, newTheme)
        }
    }

    /**
     * This function post the user logged at _user variable
     *
     * @param userEntity User logged
     */
    fun saveUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(userEntity)
        }
    }

    /**
     * This function clear the _user variable when a user signs out or delete it's account
     *
     */
    fun clearUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(null)
            _userPreferences.postValue(null)
        }
    }

    /**
     * This function deletes the user entity logged
     *
     * @param userUUID ID from the user
     */
    fun deleteUser(userUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val preferences = userRepository.getUserWithPreferences(userUUID).preferences
                preferences?.let { preferencesRepository.deletePreferences(it) }

                val favorites = userRepository.getUserFavorites(userUUID)
                if (favorites.isNotEmpty()) {
                    favorites.forEach { favorite ->
                        userRepository.deleteUserFavoriteCrossRef(
                            UserFavoriteCrossRef(
                                userUUID,
                                favorite.favoriteUUID
                            )
                        )

                        favoriteRepository.deleteFavorite(favorite)
                    }
                }

                userRepository.deleteUser(userUUID)
                _user.postValue(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * This function checks if the character is favorite based in the user favorite list from the DB
     *
     * @param user User logged
     * @param characterName Character searched
     */
    fun checkIfFavorite(user: UserEntity, characterName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFavorite = userRepository.getUserFavorite(user.userUUID, characterName)
            _isFavorite.postValue(isFavorite?.characterName == characterName)
        }
    }

    /**
     * This function adds a character to the user favorite list at the DB
     *
     * @param userUUID ID from the user
     * @param characterName Character searched
     * @param characterRealmSlug Character realm
     * @param characterMythicRating Character mythic rating
     */
    fun addFavorite(userUUID: String, characterName: String, characterRealmSlug: String, characterMythicRating: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favorite = FavoriteEntity(
                    userId = userUUID,
                    characterName = characterName,
                    characterRealmSlug = characterRealmSlug,
                    characterMythicRating = characterMythicRating
                )
                database.withTransaction {
                    favoriteRepository.insertFavorite(favorite)
                    val userFavoriteCrossRef = UserFavoriteCrossRef(
                        userUUID = userUUID,
                        favoriteUUID = favorite.favoriteUUID
                    )
                    userRepository.insertUserFavoriteCrossRef(userFavoriteCrossRef)
                }
                _isFavorite.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * This function gets the user favorite list from the DB
     *
     * @param userUUID ID from the user
     */
    fun getFavorites(userUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _userFavorites.postValue(userRepository.getUserFavorites(userUUID))
        }
    }

    /**
     * This function deletes a favorite from the DB based in the userUUID and the characterName
     *
     * @param userUUID ID from the user
     * @param characterName Character searched
     */
    fun removeFavorite(userUUID: String, characterName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favorite = userRepository.getUserFavorite(userUUID, characterName)
                if (favorite != null) {
                    database.withTransaction {
                        userRepository.deleteUserFavoriteCrossRef(
                            UserFavoriteCrossRef(
                                userUUID = userUUID,
                                favoriteUUID = favorite.favoriteUUID
                            )
                        )
                        favoriteRepository.deleteFavorite(favorite)
                    }
                    _isFavorite.postValue(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}