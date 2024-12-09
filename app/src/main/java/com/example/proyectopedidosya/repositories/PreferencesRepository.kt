package com.example.proyectopedidosya.repositories

import android.content.Context

object PreferencesRepository {
    private const val PREF_NAME = "proyecto-final"
    private const val TOKEN_KEY = "token"

    fun saveToken(token: String, context: Context) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun getToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(TOKEN_KEY, null)
    }

    fun clearToken(context: Context) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove(TOKEN_KEY)
            apply()
        }
    }

    fun isUserLogged(context: Context): Boolean {
        return getToken(context) != null
    }
}