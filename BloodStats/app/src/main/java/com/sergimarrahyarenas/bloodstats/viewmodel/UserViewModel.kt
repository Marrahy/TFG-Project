package com.sergimarrahyarenas.bloodstats.viewmodel

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.sergimarrahyarenas.bloodstats.R
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
    val onUserExistsError: LiveData<Boolean> = _onUserExistsError

    suspend fun verifyUserCredentials(userName: String, userPassword: String): Boolean {
        val user = userRepository.getUserByName(userName)
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(user)
        }
        return user?.userPassword == userPassword
    }

    suspend fun checkIfUserExists(userName: String): Boolean {
        val user = userRepository.getUserByName(userName)
        return user == null
    }

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

    fun getUserWithPreferences(userUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userWithPreferences = preferencesRepository.getPreferencesByUserUUID(userUUID)
            _userPreferences.postValue(userWithPreferences)
        }
    }

    fun updateUserTheme(userUUID: String, newTheme: String) {
        Log.d("updateUserTheme", newTheme)
        Log.d("updateUserTheme", userUUID)
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.updateThemeByUserUUID(userUUID, newTheme)
        }
    }

    fun saveUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(userEntity)
        }
    }

    fun clearUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(null)
            _userPreferences.postValue(null)
        }
    }

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

    fun checkIfFavorite(user: UserEntity, characterName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFavorite = userRepository.getUserFavorite(user.userUUID, characterName)
            _isFavorite.postValue(isFavorite?.characterName == characterName)
        }
    }

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

    fun getFavorites(userUUID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _userFavorites.postValue(userRepository.getUserFavorites(userUUID))
        }
    }

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