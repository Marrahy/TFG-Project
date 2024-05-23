package com.sergimarrahyarenas.bloodstats.viewmodel

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.sergimarrahyarenas.bloodstats.database.BloodStatsDatabase
import com.sergimarrahyarenas.bloodstats.database.entities.FavoriteEntity
import com.sergimarrahyarenas.bloodstats.database.entities.PreferencesEntity
import com.sergimarrahyarenas.bloodstats.database.entities.UserEntity
import com.sergimarrahyarenas.bloodstats.database.entities.UserFavoriteCrossRef
import com.sergimarrahyarenas.bloodstats.database.pojos.UserWithFavorites
import com.sergimarrahyarenas.bloodstats.database.pojos.UserWithPreferences
import com.sergimarrahyarenas.bloodstats.preferences.SharedPreferencesUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(context: Context) : ViewModel() {
    private val database: BloodStatsDatabase = BloodStatsDatabase.getInstance(context)
    private val userDao = database.userDao()

    private val _userWithPreferences = MutableLiveData<UserWithPreferences?>()
    val userWithPreferences: LiveData<UserWithPreferences?> = _userWithPreferences

    private val _userWithFavorites = MutableLiveData<UserWithFavorites?>()
    val userWithFavorites: LiveData<UserWithFavorites?> = _userWithFavorites

    private val _user = MutableLiveData<UserEntity?>()
    val user: LiveData<UserEntity?> = _user

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _onUserExistsError = MutableLiveData<Boolean>()
    val onUserExistsError: LiveData<Boolean> = _onUserExistsError

    init {
        val userUUID = SharedPreferencesUtils.getUserUUID(context)
        viewModelScope.launch(Dispatchers.IO) {
            userUUID?.let {
                _user.postValue(userDao.getUserByUUID(it))
            }
        }
    }

    suspend fun verifyUserCredentials(userName: String, userPassword: String, context: Context): Boolean {
        val user = database.userDao().getUserByName(userName)
        _user.postValue(user)
        user?.let {
            SharedPreferencesUtils.saveUserUUID(context, it.userUUID)
        }
        return user?.userPassword == userPassword
    }

    suspend fun checkIfUserExists(userName: String): Boolean {
        val user = database.userDao().getUserByName(userName)
        return user == null
    }

    fun createIfNotExists(
        userEmail: String?,
        userName: String,
        userPassword: String,
        context: Context,
        onResult: (UserEntity) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val existingUser = userEmail?.let { database.userDao().getUserByEmail(it) }
                if (existingUser == null) {
                    val user = UserEntity(
                        userName = userName,
                        userPassword = userPassword,
                        userEmail = userEmail
                    )
                    database.withTransaction {
                        userDao.insertUser(user)
                        val preferences = PreferencesEntity(userId = user.userUUID, theme = "light")
                        userDao.insertPreferences(preferences)
                    }

                    withContext(Dispatchers.Main) {
                        onResult(user)
                        SharedPreferencesUtils.saveUserUUID(context, user.userUUID)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onResult(existingUser)
                        SharedPreferencesUtils.saveUserUUID(context, existingUser.userUUID)
                    }
                }
            } catch (e: SQLiteConstraintException) {
                withContext(Dispatchers.Main) {
                    _onUserExistsError.postValue(true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _onUserExistsError.postValue(true)
                }
            }
        }
        _onUserExistsError.postValue(false)
    }

    fun getUserWithPreferences(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userWithPreferences = database.userDao().getUserWithPreferences(userId)
            _userWithPreferences.postValue(userWithPreferences)
        }
    }

    private fun getUserWithFavorites(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userWithFavorites = database.userDao().getUserWithFavorites(userId)

            withContext(Dispatchers.Main) {
                _userWithFavorites.postValue(userWithFavorites)
            }
        }
    }

    fun deleteUser(userId: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val preferences = database.userDao().getUserWithPreferences(userId)?.preferences
                preferences?.let { database.userDao().deletePreferences(it) }

                val favorites = database.userDao().getUserWithFavorites(userId)?.favorites
                favorites?.forEach { favorite ->
                    database.userDao().deleteUserFavoriteCrossRef(
                        UserFavoriteCrossRef(
                            userId,
                            favorite.favoriteUUID
                        )
                    )
                    database.userDao().deleteFavorite(favorite)
                }

                database.userDao().deleteUserById(userId)
                SharedPreferencesUtils.clearUserUUID(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun checkIfFavorite(characterName: String, characterRealmSlug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val favorite = database.userDao().getFavoriteByCharacterNameAndRealm(characterName = characterName, characterRealmSlug = characterRealmSlug)
            _isFavorite.postValue(favorite != null)
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
                    userDao.insertFavorite(favorite)
                    val userFavoriteCrossRef = UserFavoriteCrossRef(
                        userUUID = userUUID,
                        favoriteUUID = favorite.favoriteUUID
                    )
                    userDao.insertUserFavoriteCrossRef(userFavoriteCrossRef)
                }
                _isFavorite.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeFavorite(userUUID: String, characterName: String, characterRealmSlug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favorite = database.userDao().getFavoriteByCharacterNameAndRealm(characterName, characterRealmSlug)
                favorite?.let {
                    database.withTransaction {
                        userDao.deleteUserFavoriteCrossRef(UserFavoriteCrossRef(userUUID = userUUID, favoriteUUID = it.favoriteUUID))
                        userDao.deleteFavorite(it)
                    }
                    _isFavorite.postValue(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}