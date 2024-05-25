package com.haker.hakerweather.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SavedPreference {
    const val EMAIL = "email"
    const val USERNAME = "username"
    const val HAS_LOGIN = "has_login"

    private  fun getSharedPreference(ctx: Context?): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    private fun  editor(context: Context, const:String, string: String){
        getSharedPreference(
            context
        )?.edit()?.putString(const,string)?.apply()
    }

    fun getEmail(context: Context)= getSharedPreference(
        context
    )?.getString(EMAIL,"")

    fun setEmail(context: Context, email: String){
        editor(
            context,
            EMAIL,
            email
        )
    }

    fun setUsername(context: Context, username:String){
        editor(
            context,
            USERNAME,
            username
        )
    }

    fun getUsername(context: Context) = getSharedPreference(
        context
    )?.getString(USERNAME,"")

    fun getHasLogin(context: Context)= getSharedPreference(
        context
    )?.getString(HAS_LOGIN,"")

    fun setHasLogin(context: Context, hasLogin: String){
        editor(
            context,
            HAS_LOGIN,
            hasLogin
        )
    }
}