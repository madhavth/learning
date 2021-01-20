package com.madhavth.firebaselearning

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.IntegerRes
import com.madhavth.firebaselearning.Widgets.INTERNET_IP
import com.madhavth.firebaselearning.Widgets.LOCAL_IP

const val SHARED_PREFERENCE = "mySharedPreference"
const val KEY_WORLD_STATS ="key_world_stats"
const val KEY_NEPAL_STATS ="key_nepal_stats"

class AppPrefrence(context: Context) {
    private val preferences = context.getSharedPreferences(SHARED_PREFERENCE,0)
    private val editor = preferences.edit()

    var worldStats: String
        get() {
            return preferences.getString(KEY_WORLD_STATS, "connect to the internet")?: "connect to the internet"
        }
    set(value){
        editor.putString(KEY_WORLD_STATS, value).apply()
    }

    var nepalStats: String
    get(){
        return preferences.getString(KEY_NEPAL_STATS, "connect to the internet")?: "connect to the internet"
    }

    set(value)
    {
        editor.putString(KEY_NEPAL_STATS, value).apply()
    }


    var lastIpAddress: String
    get() = preferences.getString("ip_address","connect to the internet") ?: "connect to the internet"
    set(value) {
        editor.putString("ip_address", value).apply()
    }


    var ipMode: Int
        get() = preferences.getInt("ip_address_mode", 0) ?: 0
    set(value){
        editor.putInt("ip_address_mode",value).apply()
    }

    fun toggleIpMode()
    {
        if(ipMode == LOCAL_IP)
            ipMode = INTERNET_IP
        else
            ipMode = LOCAL_IP
    }
}