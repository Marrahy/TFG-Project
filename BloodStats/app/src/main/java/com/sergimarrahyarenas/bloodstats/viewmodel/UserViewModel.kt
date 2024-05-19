package com.sergimarrahyarenas.bloodstats.viewmodel

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.sergimarrahyarenas.bloodstats.database.BloodStatsDatabase
import com.sergimarrahyarenas.bloodstats.database.entities.PreferencesEntity
import com.sergimarrahyarenas.bloodstats.database.entities.UserEntity
import com.sergimarrahyarenas.bloodstats.database.entities.UserFavoriteCrossRef
import com.sergimarrahyarenas.bloodstats.database.pojos.UserWithFavorites
import com.sergimarrahyarenas.bloodstats.database.pojos.UserWithPreferences
import kotlinx.coroutines.Dispatchers
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

    private val _onUserExistsError = MutableLiveData<Boolean>()
    val onUserExistsError: LiveData<Boolean> = _onUserExistsError

    suspend fun verifyUserCredentials(userName: String, userPassword: String): Boolean {
        val user = database.userDao().getUserByName(userName)
        _user.postValue(user)
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
        onResult: (UserEntity) -> Unit
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
                        Log.d("user", "${user.userUUID} ${user.userName}")
                        _user.postValue(user)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onResult(existingUser)
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

    fun getUserWithFavorites(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userWithFavorites = database.userDao().getUserWithFavorites(userId)

            withContext(Dispatchers.Main) {
                _userWithFavorites.postValue(userWithFavorites)
            }
        }
    }

    fun deleteUser(userId: String) {
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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}