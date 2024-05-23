package com.sergimarrahyarenas.bloodstats.preferences

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtils {

    private const val PREFS_NAME = "bloodstats-prefs"
    private const val USER_UUID_KEY = "user_uuid"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserUUID(context: Context, userUUID: String) {
        val editor = getPreferences(context).edit()
        editor.putString(USER_UUID_KEY, userUUID)
        editor.apply()
    }

    fun getUserUUID(context: Context): String? {
        return getPreferences(context).getString(USER_UUID_KEY, null)
    }

    fun clearUserUUID(context: Context) {
        val editor = getPreferences(context).edit()
        editor.remove(USER_UUID_KEY)
        editor.apply()
    }
}